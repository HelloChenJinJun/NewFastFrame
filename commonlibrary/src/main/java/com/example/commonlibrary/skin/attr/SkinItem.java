package com.example.commonlibrary.skin.attr;

import android.view.View;
import com.example.commonlibrary.skin.SkinAttr;

import java.util.List;

/**
 * Created by COOTEK on 2017/9/4.
 */

public class SkinItem {
    private View view;
    private List<SkinAttr> skinAttrs;


    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public List<SkinAttr> getSkinAttrs() {
        return skinAttrs;
    }

    public void setSkinAttrs(List<SkinAttr> skinAttrs) {
        this.skinAttrs = skinAttrs;
    }


    public void apply() {
        if (view != null) {
            for (SkinAttr skinAttr :
                    skinAttrs) {
                skinAttr.apply(view);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null && obj instanceof SkinItem && ((SkinItem) obj).getView().equals(getView()));
    }
}
