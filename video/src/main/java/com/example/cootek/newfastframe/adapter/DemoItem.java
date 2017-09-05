package com.example.cootek.newfastframe.adapter;

import java.util.List;

/**
 * Created by COOTEK on 2017/9/5.
 */

public class DemoItem {

    /**
     * is_ios : false
     * timestamp : 1504594604
     * err_msg : OK
     * result : [{"total_price":188,"gift_price":188,"nick_name":"adrianna","image":"http://cootek-dialer-download.oss-cn-hangzhou.aliyuncs.com/voice_chat_room/gift/candy.png","gift_num":1,"create_time":"2017-08-03 15:20"},{"total_price":188,"gift_price":188,"nick_name":"adrianna","image":"http://cootek-dialer-download.oss-cn-hangzhou.aliyuncs.com/voice_chat_room/gift/candy.png","gift_num":1,"create_time":"2017-08-03 10:50"},{"total_price":188,"gift_price":188,"nick_name":"adrianna","image":"http://cootek-dialer-download.oss-cn-hangzhou.aliyuncs.com/voice_chat_room/gift/candy.png","gift_num":1,"create_time":"2017-08-03 10:49"}]
     * req_id : 0
     * result_code : 2000
     */

    private boolean is_ios;
    private String timestamp;
    private String err_msg;
    private int req_id;
    private int result_code;
    private List<ResultBean> result;

    public boolean isIs_ios() {
        return is_ios;
    }

    public void setIs_ios(boolean is_ios) {
        this.is_ios = is_ios;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public int getReq_id() {
        return req_id;
    }

    public void setReq_id(int req_id) {
        this.req_id = req_id;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * total_price : 188
         * gift_price : 188
         * nick_name : adrianna
         * image : http://cootek-dialer-download.oss-cn-hangzhou.aliyuncs.com/voice_chat_room/gift/candy.png
         * gift_num : 1
         * create_time : 2017-08-03 15:20
         */

        private int total_price;
        private int gift_price;
        private String nick_name;
        private String image;
        private int gift_num;
        private String create_time;

        public int getTotal_price() {
            return total_price;
        }

        public void setTotal_price(int total_price) {
            this.total_price = total_price;
        }

        public int getGift_price() {
            return gift_price;
        }

        public void setGift_price(int gift_price) {
            this.gift_price = gift_price;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getGift_num() {
            return gift_num;
        }

        public void setGift_num(int gift_num) {
            this.gift_num = gift_num;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
