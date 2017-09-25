package com.example.news.mvp.news.othernew.special;

import com.example.commonlibrary.mvp.view.IView;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/25      22:51
 * QQ:             1981367757
 */

public interface ISpecialNewsView<T> extends IView<T> {
    public void updateBanner(String url);
}
