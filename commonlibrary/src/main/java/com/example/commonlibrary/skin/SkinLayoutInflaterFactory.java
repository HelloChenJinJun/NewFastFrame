package com.example.commonlibrary.skin;

import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.example.commonlibrary.skin.attr.BackgroundAttr;
import com.example.commonlibrary.skin.attr.SkinItem;
import com.example.commonlibrary.skin.attr.SrcAttr;
import com.example.commonlibrary.skin.attr.TextColorAttr;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.SkinUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by COOTEK on 2017/9/3.
 */

public class SkinLayoutInflaterFactory implements LayoutInflaterFactory {

    private static final String STYLE = "style";
    private static final String TEXT_COLOR = "textColor";
    private static final String BACKGROUND = "background";
    private static final String SRC = "src";
    private AppCompatActivity appCompatActivity;
    private static List<String> supportAttrsName = new ArrayList<>();
    private Map<View, SkinItem> viewSkinItemMap = new HashMap<>();

    static {
        supportAttrsName.add(TEXT_COLOR);
        supportAttrsName.add(BACKGROUND);
        supportAttrsName.add(SRC);
    }


    public static List<String> getSupportAttrsName() {
        return supportAttrsName;
    }

    public SkinLayoutInflaterFactory(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
//        获取是否应用换肤操作


        boolean isEnable = attrs.getAttributeBooleanValue(SkinUtil.NAME_PLACE, "enable", false);
        CommonLogger.e("1这里" + isEnable);
        View view = appCompatActivity.getDelegate().createView(parent, name, context, attrs);
        if (view == null) {
            view = ViewProducer.createViewFromTag(context, name, attrs);
        }
        return applySkin(context, view, attrs);
    }

    private View applySkin(Context context, View view, AttributeSet attrs) {
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
            try {
                if (attrName.equals(STYLE)) {
                    String styleName = attrValue.substring(attrValue.indexOf("/") + 1);
                    int styleId = context.getResources().getIdentifier(styleName, STYLE, context.getPackageName());
                    int[] styleAttrs = new int[]{android.R.attr.textColor, android.R.attr.background
                    };
                    TypedArray typedArray = context.getTheme().obtainStyledAttributes(styleId, styleAttrs);
                    int textColorResId = typedArray.getResourceId(0, 0);
                    int backgroundResId = typedArray.getResourceId(1, 0);
                    if (textColorResId != 0) {
                        createSkinFromAttrName(TEXT_COLOR, textColorResId, view).apply(view);
                    }
                    if (backgroundResId != 0) {
                        createSkinFromAttrName(BACKGROUND, backgroundResId, view).apply(view);
                    }
                } else if (supportAttrsName.contains(attrName) && attrValue.startsWith("@")) {
                    //                只有引用类型才可以换肤
                    CommonLogger.e("value" + attrValue + " attrName" + attrName);
                    int id = Integer.parseInt(attrValue.substring(1));
                    if (id != 0) {
                        createSkinFromAttrName(attrName, id, view);
                    }
                }
            } catch (Resources.NotFoundException | NumberFormatException e) {
                e.printStackTrace();
                CommonLogger.e("哈哈" + e.getMessage() + e.getCause().toString());
            }
        }
        return view;
    }


    public void applyAllViewSkin() {
        if (viewSkinItemMap != null && viewSkinItemMap.size() > 0) {
            CommonLogger.e("这里了吗" + viewSkinItemMap.values().size());
            for (SkinItem skinItem :
                    viewSkinItemMap.values()) {
                skinItem.apply();
            }
        }
    }


    public SkinAttr createSkinFromAttrName(String attrName, int resId, View view) {
        SkinAttr skinAttr = null;
        switch (attrName) {
            case BACKGROUND:
                skinAttr = new BackgroundAttr();
                break;
            case SRC:
                skinAttr = new SrcAttr();
                break;
            case TEXT_COLOR:
                skinAttr = new TextColorAttr();
                break;
            default:
                break;
        }
        skinAttr.update(attrName, resId, view.getContext());
        if (viewSkinItemMap.get(view) == null) {
            SkinItem skinItem = new SkinItem();
            List<SkinAttr> list = new ArrayList<>();
            list.add(skinAttr);
            skinItem.setView(view);
            skinItem.setSkinAttrs(list);
            viewSkinItemMap.put(view, skinItem);
        } else {
            viewSkinItemMap.get(view).getSkinAttrs().add(skinAttr);
        }
//        CommonLogger.e(skinAttr.toString());
        return skinAttr;
    }


}
