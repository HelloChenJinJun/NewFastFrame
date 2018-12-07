package com.example.commonlibrary.bean;

/**
 * 项目名称:    zhuayu_android
 * 创建人:      陈锦军
 * 创建时间:    2018/10/1     11:46
 */
public class BaseBean<D> {

    @Override
    public String toString() {
        return "BaseBean{" +
                "data=" + data +
                ", type=" + type +
                ", code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }

    /**
     * code : 200
     * desc : 成功
     * data : null
     */

    private D data;

    private String extraInfo;

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    //        用于表示当前的请求所属
    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    private int code;
    private String desc;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
