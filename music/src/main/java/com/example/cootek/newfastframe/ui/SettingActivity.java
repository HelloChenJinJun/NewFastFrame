package com.example.cootek.newfastframe.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.example.commonlibrary.cusotomview.RoundAngleImageView;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.skin.SkinManager;
import com.example.commonlibrary.skin.theme.ThemeUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.cootek.newfastframe.R;

/**
 * Created by COOTEK on 2017/9/6.
 */

public class SettingActivity extends MusicBaseActivity implements View.OnClickListener, ColorChooserDialog.ColorCallback, CompoundButton.OnCheckedChangeListener {
    private CheckBox checkBox;
    private SharedPreferences sharedPreferences;
    private RoundAngleImageView color;
    private int themeColor;

    @Override
    public void updateData(Object o) {

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
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        checkBox = (CheckBox) findViewById(R.id.cb_activity_setting_night);
        checkBox.setOnCheckedChangeListener(this);
        color = (RoundAngleImageView) findViewById(R.id.riv_activity_setting_color);
        RelativeLayout colorPickerContainer = (RelativeLayout) findViewById(R.id.rl_activity_setting_color_picker_container);
        RelativeLayout nightContainer = (RelativeLayout) findViewById(R.id.rl_activity_setting_night_container);
        colorPickerContainer.setOnClickListener(this);
        nightContainer.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        sharedPreferences = getSharedPreferences(ThemeUtil.NAME, Context.MODE_PRIVATE);
        themeColor = sharedPreferences.getInt(ThemeUtil.THEME_COLOR, Color.parseColor("#FF2F3A4C"));
        color.setBackgroundColor(themeColor);
        checkBox.setBackgroundColor(themeColor);
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setTitle("设置");
        setToolBar(toolBarOption);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_activity_setting_night_container) {


        } else {
            new ColorChooserDialog.Builder(this, R.string.primary_color)
                    .preselect(themeColor)
                    .backButton(R.string.color_chooser_back)
                    .doneButton(R.string.color_chooser_done)
                    .cancelButton(R.string.color_chooser_cancel)
                    .customButton(R.string.color_chooser_custom)
                    .preselect(themeColor)
                    .presetsButton(R.string.color_chooser_preset)
                    .show();
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        sharedPreferences.edit().putInt(ThemeUtil.THEME_COLOR, selectedColor).putBoolean(ThemeUtil.IS_THEME, true).apply();
        SkinManager.getInstance().refreshSkin();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ToastUtils.showShortToast("暂时用皮肤插件来实现");
//        if (isChecked) {
//            sharedPreferences.edit().putBoolean("isNight", true).apply();
//            setTheme(R.style.CustomTheme_Night);
//            RxBusManager.getInstance().post(new ThemeEvent(true));
//        } else {
//            sharedPreferences.edit().putBoolean("isNight", false).apply();
//            RxBusManager.getInstance().post(new ThemeEvent(false));
//        }
    }
}
