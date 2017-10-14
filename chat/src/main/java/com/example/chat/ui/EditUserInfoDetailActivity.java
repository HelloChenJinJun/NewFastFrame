package com.example.chat.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserManager;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.LogUtil;
import com.example.chat.util.TimeUtil;
import com.example.chat.view.AutoEditText;
import com.example.chat.view.CustomDatePickerDialog;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.Calendar;
import java.util.Date;

import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.OnAddressSelectedListener;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/26      23:47
 * QQ:             1981367757
 */
public class EditUserInfoDetailActivity extends SlideBaseActivity implements View.OnClickListener, OnAddressSelectedListener {
        private AutoEditText mAutoEditText;
        private String from;
        private ImageView male;
        private ImageView female;
        private ImageView other;
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
        protected void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

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
                from = getIntent().getStringExtra("from");
                groupId = getIntent().getStringExtra("groupId");
                content = getIntent().getStringExtra("message");
                switch (from) {
                        case "nick":
                        case "groupNick":
                        case "phone":
                        case "email":
                        case "signature":
                        case "groupDescription":
                        case "groupNotification":
                        case "groupName":
                                return R.layout.activity_edit_user_detail;
                        case "gender":
                                return R.layout.activity_edit_user_detail_gender;
                        case "address":
                                return R.layout.activity_edit_user_detail_address;
                        default:
                                return R.layout.activity_edit_user_detail_birth;
                }
        }


        @Override
        protected void initView() {
                if (from != null) {
                        switch (from) {
                                case "nick":
                                case "groupNick":
                                case "phone":
                                case "email":
                                case "signature":
                                case "groupDescription":
                                case "groupNotification":
                                case "groupName":
                                        initNormalView();
                                        break;
                                case "gender":
                                        initGenderView();
                                        break;
                                case "address":
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
                RelativeLayout femaleLayout = (RelativeLayout) findViewById(R.id.rl_edit_user_info_detail_female);
                RelativeLayout maleLayout = (RelativeLayout) findViewById(R.id.rl_edit_user_info_detail_male);
                RelativeLayout otherLayout = (RelativeLayout) findViewById(R.id.rl_edit_user_info_detail_other);
                female = (ImageView) findViewById(R.id.iv_edit_user_info_detail_female);
                male = (ImageView) findViewById(R.id.iv_edit_user_info_detail_male);
                other = (ImageView) findViewById(R.id.iv_edit_user_info_detail_other);
                femaleLayout.setOnClickListener(this);
                otherLayout.setOnClickListener(this);
                maleLayout.setOnClickListener(this);
                if (content != null) {
                        if (content.equals("男")) {
                                updateGenderChecked(0);
                        } else if (content.equals("女")) {
                                updateGenderChecked(1);
                        } else {
                                updateGenderChecked(3);
                        }
                }
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
                toolBarOption.setRightListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (!CommonUtils.isNetWorkAvailable()) {
                                        LogUtil.e("网络连接失败");
                                        return;
                                }
                                final Intent intent = new Intent();
                                switch (from) {
                                        case "nick":
                                        case "phone":
                                        case "email":
                                        case "signature":
                                        case "groupNick":
                                        case "groupDescription":
                                        case "groupNotification":
                                        case "groupName":
                                                if (mAutoEditText.getText() != null && !mAutoEditText.getText().toString().trim().equals("")) {
                                                        if (content != null && content.equals(mAutoEditText.getText().toString().trim())) {
                                                                LogUtil.e("没有修改");
                                                        } else {
                                                                content = mAutoEditText.getText().toString().trim();
                                                                if (from.equals("phone") && !CommonUtils.isPhone(content)) {
                                                                        LogUtil.e("输入的手机号码格式不对，请重新输入");
                                                                        return;
                                                                }
                                                                if (from.equals("email") && !CommonUtils.isEmail(content)) {
                                                                        LogUtil.e("输入的邮箱号码格式不对，请重新输入");
                                                                        return;
                                                                }
                                                                intent.putExtra("message", content);
                                                                setResult(Activity.RESULT_OK, intent);
                                                        }
                                                } else {
                                                        ToastUtils.showShortToast("内容不能为空");
                                                        mAutoEditText.startShakeAnimation();
                                                        return;
                                                }
                                                break;
                                        case "gender":
                                                if (content != null && content.equals(currentGender)) {
                                                        LogUtil.e("当前的性别未修改");
                                                } else {
                                                        content = currentGender;
                                                        LogUtil.e("当前的性别改变拉");
                                                        intent.putExtra("message", content);
                                                        setResult(Activity.RESULT_OK, intent);
                                                }
                                                break;
                                        case "address":
                                                if (content != null && content.equals(currentSelectedAddress)) {
                                                        LogUtil.e("现在的地址并没有改变");
                                                } else {
                                                        content = currentSelectedAddress;
                                                        intent.putExtra("message", content);
                                                        setResult(Activity.RESULT_OK, intent);
                                                }
                                                break;

                                        default:
                                                String time = TimeUtil.getDateFormalFromString(currentYear, currentMonth, currentDay);
                                                if (content != null && content.equals(time)) {
                                                        LogUtil.e("现在时间并没有改变");
                                                } else {
                                                        content = time;
                                                        intent.putExtra("message", content);
                                                        setResult(Activity.RESULT_OK, intent);
                                                }
                                                break;
                                }
                                if (intent.getStringExtra("message") != null) {
                                        showLoadDialog("正在修改........");
                                        if (groupId == null) {
                                                UserManager.getInstance().updateUserInfo(from, content, new UpdateListener() {
                                                        @Override
                                                        public void onSuccess() {
                                                                dismissLoadDialog();
                                                                ToastUtils.showShortToast("修改用户信息成功");
                                                                finish();
                                                        }

                                                        @Override
                                                        public void onFailure(int i, String s) {
                                                                dismissLoadDialog();
                                                                LogUtil.e("更新用户数据失败" + s + i);
                                                                setResult(Activity.RESULT_CANCELED);
                                                                finish();
                                                        }
                                                });
                                        } else {
                                                MsgManager.getInstance().updateGroupMessage(groupId, from, content, new UpdateListener() {
                                                        @Override
                                                        public void onSuccess() {
                                                                dismissLoadDialog();
                                                                LogUtil.e("更新群资料成功");
                                                                ToastUtils.showShortToast("更新群资料成功");
                                                                finish();
                                                        }

                                                        @Override
                                                        public void onFailure(int i, String s) {
                                                                dismissLoadDialog();
                                                                LogUtil.e("更新群资料失败" + s + i);
                                                                ToastUtils.showShortToast("更新群资料失败" + s);
                                                                setResult(Activity.RESULT_CANCELED);
                                                                finish();
                                                        }
                                                });
                                        }
                                }
                        }
                });
                setToolBar(toolBarOption);
        }

        private String getTitle(String from) {
                switch (from) {
                        case "nick":
                                return "昵称";
                        case "avatar":
                                return "头像";
                        case "gender":
                                return "性别";
                        case "birth":
                                return "生日";
                        case "email":
                                return "邮箱";
                        case "phone":
                                return "手机号码";
                        case "groupNick":
                                return "群名片";
                        case "groupDescription":
                                return "群描述";
                        case "groupNotification":
                                return "群通知";
                        case "groupName":
                                return "群名";
                        case "signature":
                                return "签名";
                        case "address":
                                return "地址";
                        default:
                                return "未知类型";
                }

        }

        @Override
        public void onClick(View v) {
                int i = v.getId();
                if (i == R.id.rl_edit_user_info_detail_female) {
                        updateGenderChecked(1);

                } else if (i == R.id.rl_edit_user_info_detail_male) {
                        updateGenderChecked(0);

                } else if (i == R.id.rl_edit_user_info_detail_other) {
                        updateGenderChecked(2);

                } else if (i == R.id.rl_edit_user_info_detail_birth) {
                        openDatePicker();

                } else if (i == R.id.rl_edit_user_info_detail_address) {
                        showBottomDialog();
                }
        }

        private BottomDialog mBottomDialog;

        private void showBottomDialog() {
                if (mBottomDialog == null) {
                        mBottomDialog = new BottomDialog(this);
                        mBottomDialog.setOnAddressSelectedListener(this);
                        mBottomDialog.show();
                }else {
                        mBottomDialog.show();
                }
        }

        private void openDatePicker() {
                CustomDatePickerDialog dialog = new CustomDatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                currentYear = year;
                                currentMonth = month;
                                currentDay = dayOfMonth;
                                updateDateChanged();
                        }
                }, currentYear, currentMonth, currentDay);
                dialog.show();
        }

        private void updateDateChanged() {
                birth.setText(TimeUtil.getDateFormalFromString(currentYear, currentMonth, currentDay));

        }

        private void updateGenderChecked(int position) {

                if (position == 0) {
                        currentGender = "男";
                        male.setImageResource(R.mipmap.nim_contact_checkbox_checked_green);
                        female.setImageResource(R.mipmap.nim_contact_checkbox_unchecked);
                        other.setImageResource(R.mipmap.nim_contact_checkbox_unchecked);
                } else if (position == 1) {
                        currentGender = "女";
                        male.setImageResource(R.mipmap.nim_contact_checkbox_unchecked);
                        female.setImageResource(R.mipmap.nim_contact_checkbox_checked_green);
                        other.setImageResource(R.mipmap.nim_contact_checkbox_unchecked);
                } else {
                        currentGender = "其他";
                        male.setImageResource(R.mipmap.nim_contact_checkbox_unchecked);
                        female.setImageResource(R.mipmap.nim_contact_checkbox_unchecked);
                        other.setImageResource(R.mipmap.nim_contact_checkbox_checked_green);
                }
        }

        @Override
        public void onAddressSelected(Province province, City city, County county, Street street) {
                if (mBottomDialog.isShowing()) {
                        mBottomDialog.dismiss();
                }
                StringBuilder stringBuilder=new StringBuilder();
                stringBuilder.append(province.name).append("-").append(city.name).append("-").append(county.name);
                if (street != null) {
                        stringBuilder.append("-").append(street.name);
                }
                currentSelectedAddress=stringBuilder.toString();
                address.setText(currentSelectedAddress);
        }


        @Override
        public void finish() {
                super.finish();
                if (mBottomDialog != null && mBottomDialog.isShowing()) {
                        mBottomDialog.dismiss();
                }
        }

        @Override
        public void updateData(Object o) {

        }
}
