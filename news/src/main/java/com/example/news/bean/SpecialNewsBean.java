package com.example.news.bean;

import com.example.commonlibrary.baseadapter.baseitem.MultipleItem;
import com.example.news.util.NewsUtil;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/25      18:44
 * QQ:             1981367757
 */

public class SpecialNewsBean implements MultipleItem {
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_PHOTO_SET = 2;
    private int type;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public SpecialNewsBean(RawSpecialNewsBean.TopicsEntity.DocsEntity bean) {
        this.bean = bean;
        if (NewsUtil.PHOTO_SET.equals(bean.getSkipType())) {
            type = TYPE_PHOTO_SET;
        } else {
            type = TYPE_NORMAL;
        }
    }


    public SpecialNewsBean(String title) {
        this.title = title;
        type = TYPE_HEADER;
    }

    private RawSpecialNewsBean.TopicsEntity.DocsEntity bean;
    private String title;

    public RawSpecialNewsBean.TopicsEntity.DocsEntity getBean() {
        return bean;
    }

    public void setBean(RawSpecialNewsBean.TopicsEntity.DocsEntity bean) {
        this.bean = bean;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemViewType() {
        return type;
    }
}
