package com.example.chat.mvp.editInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.base.Constant;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.adressList.AddressListActivity;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.LogUtil;
import com.example.chat.util.TimeUtil;
import com.example.chat.view.AutoEditText;
import com.example.chat.view.CustomDatePickerDialog;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.Calendar;
import java.util.Date;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/26      23:47
 * QQ:             1981367757
 */
public class EditUserInfoDetailActivity extends SlideBaseActivity implements View.OnClickListener {
        private AutoEditText mAutoEditText;
        private String from;
        private TextView address;
        private int currentDay = 4;
        private int currentMonth = 9;
        private int currentYear = 1995;
        private TextView birth;
        private String content;
        private String currentGender = "男";
        private String groupId;
        private String currentSelectedAddress;



        @Override
        protected boolean isNeedHeadLayout() {
                return true;
        }

        @Override
        protected boolean isNeedEmptyLayout() {
                return false;
        }

        @Override
        protected int getContentLayout() {
                from = getIntent().getStringExtra(Constant.FROM);
                groupId = getIntent().getStringExtra(Constant.GROUP_ID);
                content = getIntent().getStringExtra(Constant.DATA);
                switch (from) {
                        case Constant.NICK:
                        case Constant.PHONE:
                        case Constant.EMAIL:
                        case Constant.SIGNATURE:
                        case Constant.GROUP_DESCRIPTION:
                        case Constant.GROUP_NOTIFICATION:
                        case Constant.GROUP_NAME:
                                return R.layout.activity_edit_user_detail;
                        case Constant.GENDER:
                                return R.layout.activity_edit_user_detail_gender;
                        case Constant.ADDRESS:
                                return R.layout.activity_edit_user_detail_address;
                        default:
                                return R.layout.activity_edit_user_detail_birth;
                }
        }


        @Override
        protected void initView() {
                if (from != null) {
                        switch (from) {
                                case Constant.NICK:
                                case Constant.PHONE:
                                case Constant.EMAIL:
                                case Constant.SIGNATURE:
                                case Constant.GROUP_DESCRIPTION:
                                case Constant.GROUP_NOTIFICATION:
                                case Constant.GROUP_NAME:
                                        initNormalView();
                                        break;
                                case Constant.GENDER:
                                        initGenderView();
                                        break;
                                case Constant.ADDRESS:
                                        initAddressView();
                                        break;
                                default:
                                        initBirthView();
                                        break;
                        }
                }
        }

        private void initAddressView() {
                findViewById(R.id.rl_edit_user_info_detail_address).setOnClickListener(this);
                address = (TextView) findViewById(R.id.tv_edit_user_info_detail_address);
                if (content != null) {
                        address.setText(content);
                } else {
                        address.setText("未填写地址");
                }
        }

        private void initBirthView() {
                RelativeLayout birthLayout = (RelativeLayout) findViewById(R.id.rl_edit_user_info_detail_birth);
                birth = (TextView) findViewById(R.id.tv_edit_user_info_detail_birth);
                birthLayout.setOnClickListener(this);
                if (content != null) {
                        birth.setText(content);
                        Date date = TimeUtil.getDateFormalFromString(content);
                        if (date != null) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                currentDay = calendar.get(Calendar.DATE);
                                currentMonth = calendar.get(Calendar.MONTH);
                                currentYear = calendar.get(Calendar.YEAR);
                        }
                }

        }

        private void initGenderView() {
                RadioGroup radioGroup= (RadioGroup) findViewById(R.id.rg_edit_user_detail_gender_container);
                if (content != null) {
                        switch (content) {
                                case "男":
                                        radioGroup.check(R.id.rb_edit_user_detail_gender_male);
                                        break;
                                case "女":
                                        radioGroup.check(R.id.rb_edit_user_detail_gender_female);
                                        break;

                        }
                }
                radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                        if (checkedId == R.id.rb_edit_user_detail_gender_male) {
                                currentGender="男";
                        }else {
                                currentGender="女";
                        }
                });
        }

        private void initNormalView() {
                mAutoEditText = (AutoEditText) findViewById(R.id.aet_edit_user_info_detail);
                mAutoEditText.setText(content);
        }

        @Override
        public void initData() {
                ToolBarOption toolBarOption = new ToolBarOption();
                toolBarOption.setTitle("编辑" + getTitle(from));
                toolBarOption.setRightResId(R.drawable.ic_file_upload_blue_grey_900_24dp);
                toolBarOption.setRightListener(v -> {
                        if (!AppUtil.isNetworkAvailable()) {
                                LogUtil.e("网络连接失败");
                                return;
                        }
                        final Intent intent = new Intent();
                        switch (from) {
                                case Constant.NICK:
                                case Constant.PHONE:
                                case Constant.EMAIL:
                                case Constant.SIGNATURE:
                                case Constant.GROUP_DESCRIPTION:
                                case Constant.GROUP_NOTIFICATION:
                                case Constant.GROUP_NAME:
                                        if (mAutoEditText.getText() != null && !mAutoEditText.getText().toString().trim().equals("")) {
                                                if (content != null && content.equals(mAutoEditText.getText().toString().trim())) {
                                                        LogUtil.e("没有修改");
                                                        ToastUtils.showShortToast("没有修改");
                                                } else {
                                                        String temp=mAutoEditText.getText().toString().trim();
                                                        if (from.equals(Constant.PHONE) && !CommonUtils.isPhone(temp)) {
                                                                ToastUtils.showShortToast("输入的手机号码格式不对，请重新输入");
                                                                LogUtil.e("输入的手机号码格式不对，请重新输入");
                                                                return;
                                                        }
                                                        if (from.equals(Constant.EMAIL) && !AppUtil.isEmail(temp)) {
                                                                LogUtil.e("输入的邮箱号码格式不对，请重新输入");
                                                                LogUtil.e("输入的邮箱号码格式不对，请重新输入");
                                                                return;
                                                        }
                                                        intent.putExtra(Constant.DATA, temp);
                                                        setResult(Activity.RESULT_OK, intent);
                                                }
                                        } else {
                                                ToastUtils.showShortToast("内容不能为空");
                                                mAutoEditText.startShakeAnimation();
                                                return;
                                        }
                                        break;
                                case Constant.GENDER:
                                        if (content != null && content.equals(currentGender)) {
                                                LogUtil.e("当前的性别未修改");
                                        } else {
                                                intent.putExtra(Constant.DATA, currentGender);
                                                setResult(Activity.RESULT_OK, intent);
                                        }
                                        break;
                                case Constant.ADDRESS:
                                        if (content != null && content.equals(currentSelectedAddress)) {
                                                LogUtil.e("现在的地址并没有改变");
                                        } else {
                                                intent.putExtra(Constant.DATA, currentSelectedAddress);
                                                setResult(Activity.RESULT_OK, intent);
                                        }
                                        break;

                                default:
                                        String time = TimeUtil.getDateFormalFromString(currentYear, currentMonth, currentDay);
                                        if (content != null && content.equals(time)) {
                                                LogUtil.e("现在时间并没有改变");
                                        } else {
                                                intent.putExtra(Constant.DATA, time);
                                                setResult(Activity.RESULT_OK, intent);
                                        }
                                        break;
                        }
                        String data=intent.getStringExtra(Constant.DATA);
                        if (data != null) {
                                showLoadDialog("正在修改........");
                                if (groupId == null) {
                                        UserManager.getInstance().updateUserInfo(from, data, new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                        dismissLoadDialog();
                                                        if (e == null) {
                                                                ToastUtils.showShortToast("修改用户信息成功");
                                                        }else {
                                                                LogUtil.e("更新用户数据失败" +e.toString());
                                                                setResult(Activity.RESULT_CANCELED);
                                                        }
                                                        finish();
                                                }

                                        });
                                } else {
                                        MsgManager.getInstance().updateGroupMessage(groupId, from, data, new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                        dismissLoadDialog();
                                                        if (e == null) {
                                                                LogUtil.e("更新群资料成功");
                                                                ToastUtils.showShortToast("更新群资料成功");
                                                        }else {
                                                                LogUtil.e("更新群资料失败" +e.toString());
                                                                ToastUtils.showShortToast("更新群资料失败" + e.toString());
                                                                setResult(Activity.RESULT_CANCELED);
                                                        }
                                                        finish();
                                                }

                                        });
                                }
                        }
                });
                setToolBar(toolBarOption);
        }

        private String getTitle(String from) {
                switch (from) {
                        case Constant.NICK:
                                return "昵称";
                        case Constant.AVATAR:
                                return "头像";
                        case Constant.GENDER:
                                return "性别";
                        case Constant.BIRTHDAY:
                                return "生日";
                        case Constant.EMAIL:
                                return "邮箱";
                        case Constant.PHONE:
                                return "手机号码";
                        case Constant.GROUP_DESCRIPTION:
                                return "群介绍";
                        case Constant.GROUP_NOTIFICATION:
                                return "群通知";
                        case Constant.GROUP_NAME:
                                return "群名";
                        case Constant.SIGNATURE:
                                return "签名";
                        case Constant.ADDRESS:
                                return "地址";
                        default:
                                return "未知类型";
                }

        }

        @Override
        public void onClick(View v) {
                int i = v.getId();
               if (i == R.id.rl_edit_user_info_detail_birth) {
                        openDatePicker();

                } else if (i == R.id.rl_edit_user_info_detail_address) {
                        AddressListActivity.start(this, null, Constant.REQUEST_CODE_NORMAL);
                }
        }



        private void openDatePicker() {
                CustomDatePickerDialog dialog = new CustomDatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                        currentYear = year;
                        currentMonth = month;
                        currentDay = dayOfMonth;
                        updateDateChanged();
                }, currentYear, currentMonth, currentDay);
                dialog.show();
        }

        private void updateDateChanged() {
                birth.setText(TimeUtil.getDateFormalFromString(currentYear, currentMonth, currentDay));

        }



        @Override
        public void updateData(Object o) {

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == Activity.RESULT_OK) {
                        if (requestCode == Constant.REQUEST_CODE_NORMAL) {
                                String address=data.getStringExtra(Constant.CONTENT);
                                this.address.setText(address);
                                currentSelectedAddress=address;
                        }
                }
        }

        public static void start(Activity activity, String from, String data, int requestCode) {
                start(activity,null,from,data,requestCode);
        }

        public static void start(Activity activity, String groupId, String from, String data, int requestCode) {
                Intent intent=new Intent(activity,EditUserInfoDetailActivity.class);
                intent.putExtra(Constant.FROM,from);
                intent.putExtra(Constant.DATA,data);
                intent.putExtra(Constant.GROUP_ID,groupId);
                activity.startActivityForResult(intent,requestCode);
        }
}
