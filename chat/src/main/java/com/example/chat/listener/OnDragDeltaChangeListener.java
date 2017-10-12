package com.example.chat.listener;

import android.view.View;

/**
 * 项目名称:    FilpperLayoutDemo
 * 创建人:        陈锦军
 * 创建时间:    2016/11/4      14:08
 * QQ:             1981367757
 */
public interface OnDragDeltaChangeListener {
        void onDrag(View view, float delta);

        void onCloseMenu();

        void onOpenMenu();
}
