package com.example.news.mvp.cardinfo;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.news.NewsApplication;
import com.example.news.R;
import com.example.news.bean.BankAccountItem;
import com.example.news.bean.CardPayResultBean;
import com.example.news.bean.CardPersonInfoBean;
import com.example.news.dagger.cardinfo.CardInfoModule;
import com.example.news.dagger.cardinfo.DaggerCardInfoComponent;
import com.example.news.mvp.cardlogin.CardLoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/19      23:19
 * QQ:             1981367757
 */

public class CardInfoActivity extends BaseActivity<Object, CardInfoPresenter> implements View.OnClickListener {
    private TextView name, sex, college, unit, certType, certNumber, uid, start, end, bankNumber, status, money, cardType, phoneNumber;
    private Button pay;
    private EditText input;

    @Override
    public void updateData(Object o) {
        if (o instanceof BankAccountItem) {
            BankAccountItem bankAccountItem = (BankAccountItem) o;
            float money = (float) (Integer.valueOf(bankAccountItem.getCard().get(0)
                    .getDb_balance()) / 100.0);
            this.money.setText("余额：" + money + "元");
            status.setText("冻结状态" + bankAccountItem.getCard().get(0)
                    .getAcc_status());
            certNumber.setText("身份证号：" + bankAccountItem.getCard().get(0)
                    .getCert());
            bankNumber.setText("银行卡号:" + bankAccountItem.getCard().get(0)
                    .getBankacc());
            phoneNumber.setText("手机号码:" + bankAccountItem.getCard().get(0)
                    .getPhone());
            start.setText("进校日期" + bankAccountItem.getCard().get(0)
                    .getCreatedate());
            end.setText("过期日期" + bankAccountItem.getCard().get(0)
                    .getExpdate());
            uid.setText("学号:" + bankAccountItem.getCard().get(0)
                    .getSno());
        } else if (o instanceof CardPersonInfoBean) {
            CardPersonInfoBean cardPersonInfoBean = (CardPersonInfoBean) o;
            name.setText(cardPersonInfoBean.getName());
            sex.setText(cardPersonInfoBean.getSex());
            cardType.setText(cardPersonInfoBean.getCertType());
            unit.setText(cardPersonInfoBean.getUnit());
            college.setText(cardPersonInfoBean.getCollege());
        } else if (o instanceof CardPayResultBean) {
            CardPayResultBean cardPayResultBean = (CardPayResultBean) o;
            if (cardPayResultBean.isIsSucceed()) {
                ToastUtils.showShortToast("充值成功");
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(cardPayResultBean.getMsg());
                    jsonObject = jsonObject.getJSONObject("transfer");
                    ToastUtils.showShortToast("充值失败" + jsonObject.getString("errmsg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    CommonLogger.e("充值json转化失败" + e.getMessage());
                }
            }
        }else {
           CardLoginActivity.start(this,"cookie过期");
            finish();
        }
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_card_info;
    }

    @Override
    protected void initView() {
        pay = (Button) findViewById(R.id.btn_activity_card_info_pay);
        name = (TextView) findViewById(R.id.tv_activity_card_info_name);
        college = (TextView) findViewById(R.id.tv_activity_card_info_college);
        sex = (TextView) findViewById(R.id.tv_activity_card_info_sex);
        cardType = (TextView) findViewById(R.id.tv_activity_card_info_card_type);
        unit = (TextView) findViewById(R.id.tv_activity_card_info_unit);
        uid = (TextView) findViewById(R.id.tv_activity_card_info_uid);
        start = (TextView) findViewById(R.id.tv_activity_card_info_start_time);
        end = (TextView) findViewById(R.id.tv_activity_card_info_end_time);
        money = (TextView) findViewById(R.id.tv_activity_card_info_money);
        phoneNumber = (TextView) findViewById(R.id.tv_activity_card_info_phone);
        certNumber = (TextView) findViewById(R.id.tv_activity_card_info_card_number);
        bankNumber = (TextView) findViewById(R.id.tv_activity_card_info_bank_number);
        certType = (TextView) findViewById(R.id.tv_activity_card_info_cert_type);
        status = (TextView) findViewById(R.id.tv_activity_card_info_status);
        input = (EditText) findViewById(R.id.et_activity_card_info_input);
        pay.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        DaggerCardInfoComponent.builder().cardInfoModule(new CardInfoModule(this))
                .newsComponent(NewsApplication.getNewsComponent())
                .build().inject(this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenter.getPersonCardInfo();
            }

        });
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(input.getText().toString().trim())) {
            ToastUtils.showShortToast("金额不能为空");
        } else {
            showLoadDialog("正在充值............");
            presenter.pay(input.getText().toString().trim());
        }
    }


    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        super.showError(errorMsg, listener);
        dismissLoadDialog();
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        dismissLoadDialog();
    }

    public static void start(Context context) {
        Intent intent=new Intent(context,CardInfoActivity.class);
        context.startActivity(intent);
    }
}
