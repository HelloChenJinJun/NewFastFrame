package com.example.commonlibrary.router;

import android.content.Context;

import java.util.Map;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/9     21:44
 * QQ:         1981367757
 */

public class RouterRequest {
    private String provideName;
    private String actionName;
    private Map<String,Object> paramMap;
    private Context context;
    private boolean isFinish;
    private Object object;


    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    private RouterRequest(Builder builder) {
        setProvideName(builder.provideName);
        setActionName(builder.actionName);
        setParamMap(builder.paramMap);
        setContext(builder.context);
        setFinish(builder.isFinish);
        setObject(builder.object);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getProvideName() {
        return provideName;
    }

    public void setProvideName(String provideName) {
        this.provideName = provideName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public static final class Builder {
        private String provideName;
        private String actionName;
        private Map<String, Object> paramMap;
        private Context context;
        public boolean isFinish=false;
        public Object object;

        public Builder() {
        }

        public Builder provideName(String val) {
            provideName = val;
            return this;
        }


        public Builder object(Object object){
            this.object=object;
            return this;
        }


        public Builder isFinish(boolean isFinish){
            this.isFinish=isFinish;
            return this;
        }

        public Builder actionName(String val) {
            actionName = val;
            return this;
        }

        public Builder paramMap(Map<String, Object> val) {
            paramMap = val;
            return this;
        }


        public Builder context(Context context){
            this.context=context;
            return this;
        }

        public RouterRequest build() {
            return new RouterRequest(this);
        }
    }
}
