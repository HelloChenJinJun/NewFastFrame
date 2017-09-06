package com.example.commonlibrary.skin.attr;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonlibrary.skin.SkinManager;
import com.example.commonlibrary.utils.CommonLogger;

/**
 * Created by COOTEK on 2017/9/3.
 */

public abstract class SkinAttr {
    public static final String RESOURCE_TYPE_NAME_COLOR = "color";
    public static final String RESOURCE_TYPE_NAME_DRAWABLE = "drawable";
    public static final String RESOURCE_TYPE_NAME_MIPMAP = "mipmap";


    /**
     * 属性名：textColor
     */
    private String attrName;
    /**
     * 资源id
     */
    private int resId;
    /**
     * 资源名：tv_name
     */
    private String resName;
    /**
     * 资源类型名  color
     */
    private String typeName;


    public abstract void apply(View view);

    protected boolean isColorType() {
        return getTypeName().equals(RESOURCE_TYPE_NAME_COLOR);
    }

    protected boolean isDrawableType() {
        return getTypeName().equals(RESOURCE_TYPE_NAME_DRAWABLE);
    }


    public SkinAttr applyBackgroundColor(View view) {
        view.setBackgroundColor(SkinManager.getInstance().getColor(getResId()));
        return this;
    }


    public SkinAttr applyBackgroundDrawable(View view) {
        view.setBackground(SkinManager.getInstance().getDrawable(getResId()));
        return this;
    }


    public SkinAttr applyImageDrawable(View view) {
        ((ImageView) view).setImageDrawable(SkinManager.getInstance().getDrawable(getResId()));
        return this;
    }


    public SkinAttr applyTextColor(View view) {
        ((TextView) view).setTextColor(SkinManager.getInstance().getColorStateList(getResId()));
        return this;
    }


    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


    @Override
    public SkinAttr clone() {
        SkinAttr o = null;
        try {
            o = (SkinAttr) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    public void update(String attrName, int resId, Context context) {
        setResId(resId);
        setAttrName(attrName);
        setTypeName(context.getResources().getResourceTypeName(resId));
        setResName(context.getResources().getResourceName(resId));
        CommonLogger.e(toString());
    }


    @Override
    public String toString() {
        return "SkinAttr{" +
                "attrName='" + attrName + '\'' +
                ", resId=" + resId +
                ", resName='" + resName + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
