package com.snew.video.bean;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/12     17:11
 */
public class QQVideoTabListBean {

    /**
     * index : [{"default_value":17,"display_name":"排序","name":"sort","option":[{"display_name":"最新","value":"19"},{"display_name":"最热","value":"17"},{"display_name":"好评","value":"16"}]},{"default_value":-1,"display_name":"按类型","name":"itype","option":[{"display_name":"全部","value":"-1"},{"display_name":"独播","value":"26"},{"display_name":"院线","value":"25"},{"display_name":"动作","value":"0"},{"display_name":"冒险","value":"1"},{"display_name":"喜剧","value":"3"},{"display_name":"爱情","value":"2"},{"display_name":"战争","value":"5"},{"display_name":"恐怖","value":"6"},{"display_name":"犯罪","value":"7"},{"display_name":"悬疑","value":"8"},{"display_name":"惊悚","value":"9"},{"display_name":"武侠","value":"10"},{"display_name":"科幻","value":"4"},{"display_name":"音乐","value":"19"},{"display_name":"歌舞","value":"20"},{"display_name":"动画","value":"16"},{"display_name":"奇幻","value":"17"},{"display_name":"家庭","value":"18"},{"display_name":"剧情","value":"15"},{"display_name":"伦理","value":"14"},{"display_name":"记录","value":"22"},{"display_name":"历史","value":"13"},{"display_name":"传记","value":"24"}]},{"default_value":0,"display_name":"按年代","name":"iyear","option":[{"display_name":"全部","value":"-1"},{"display_name":"2018","value":"2018"},{"display_name":"2017","value":"2017"},{"display_name":"2016","value":"2016"},{"display_name":"2015","value":"2015"},{"display_name":"2014","value":"2014"},{"display_name":"2013","value":"2013"},{"display_name":"2012","value":"2012"},{"display_name":"2011","value":"2011"},{"display_name":"2010","value":"2010"},{"display_name":"2009","value":"2009"},{"display_name":"2008","value":"2008"},{"display_name":"2007","value":"2007"},{"display_name":"2006","value":"2006"},{"display_name":"2005","value":"2005"},{"display_name":"2004","value":"2004"},{"display_name":"2003","value":"2003"},{"display_name":"2002","value":"2002"},{"display_name":"2001","value":"2001"},{"display_name":"2000","value":"2000"},{"display_name":"90年代","value":"1990"},{"display_name":"80年代","value":"1980"},{"display_name":"其他","value":"9999"}]},{"default_value":0,"display_name":"按地区","name":"iarea","option":[{"display_name":"全部","value":"-1"},{"display_name":"欧美","value":"1"},{"display_name":"华语","value":"2"},{"display_name":"日韩","value":"3"},{"display_name":"泰国","value":"4"},{"display_name":"印度","value":"5"},{"display_name":"其他","value":"6"}]},{"default_value":-1,"display_name":"按奖项","name":"iawards","option":[{"display_name":"全部","value":"-1"},{"display_name":"奥斯卡","value":"0"},{"display_name":"高分小众","value":"1"},{"display_name":"电影节","value":"2"},{"display_name":"系列","value":"3"}]}]
     * msg :
     * ret : 0
     */

    private String msg;
    private int ret;
    private List<IndexBean> index;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public List<IndexBean> getIndex() {
        return index;
    }

    public void setIndex(List<IndexBean> index) {
        this.index = index;
    }

    public static class IndexBean {
        /**
         * default_value : 17
         * display_name : 排序
         * name : sort
         * option : [{"display_name":"最新","value":"19"},{"display_name":"最热","value":"17"},{"display_name":"好评","value":"16"}]
         */

        private int default_value;
        private String display_name;
        private String name;
        private List<OptionBean> option;

        public int getDefault_value() {
            return default_value;
        }

        public void setDefault_value(int default_value) {
            this.default_value = default_value;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<OptionBean> getOption() {
            return option;
        }

        public void setOption(List<OptionBean> option) {
            this.option = option;
        }

        public static class OptionBean {
            /**
             * display_name : 最新
             * value : 19
             */

            private String display_name;
            private String value;

            public String getDisplay_name() {
                return display_name;
            }

            public void setDisplay_name(String display_name) {
                this.display_name = display_name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
