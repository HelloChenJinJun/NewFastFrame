package com.example.chat.bean;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/11     14:53
 * QQ:         1981367757
 */

public class NotifyPostResult {

    /**
     * appKey : da01a9bd74d83c20b64936b0831918ea
     * tableName : PublicPostBean
     * objectId :
     * action : updateTable
     * data : {"author":"5b085e6465","content":"{\"content\":\"好吧v财产分割给\",\"imageList\":null,\"location\":\"不显示\",\"shareContent\":{\"address\":null,\"avatar\":\"http://bmob-cdn-16009.b0.upaiyun.com/2017/12/29/c84e370f4092749380c234e1d76d4d0e.jpg\",\"commentCount\":0,\"createAt\":\"2018-01-11 14:54:16\",\"likeCount\":0,\"nick\":\"你好帅\",\"pid\":\"2b2c11ca49\",\"postDataBean\":{\"content\":\"你那边不vv吃\",\"imageList\":[\"http://bmob-cdn-16009.b0.upaiyun.com/2018/01/11/756fea365d0346db9c43fc1e93f209af.jpg\",\"http://bmob-cdn-16009.b0.upaiyun.com/2018/01/11/a3a4f7f855354dcbac4fd8147077720a.jpg\"],\"location\":\"不显示\",\"shareContent\":null,\"shareType\":0,\"voiceUrl\":null},\"sex\":false,\"shareCount\":0,\"uid\":\"5b085e6465\"},\"shareType\":10,\"voiceUrl\":null}","createdAt":"2018-01-11 14:54:58","msgType":11,"objectId":"b8001000e8","updatedAt":"2018-01-11 14:54:58"}
     */

    private String appKey;
    private String tableName;
    private String objectId;
    private String action;
    private DataBean data;

    @Override
    public String toString() {
        return "NotifyPostResult{" +
                "appKey='" + appKey + '\'' +
                ", tableName='" + tableName + '\'' +
                ", objectId='" + objectId + '\'' +
                ", action='" + action + '\'' +
                ", data=" + data +
                '}';
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * author : 5b085e6465
         * content : {"content":"好吧v财产分割给","imageList":null,"location":"不显示","shareContent":{"address":null,"avatar":"http://bmob-cdn-16009.b0.upaiyun.com/2017/12/29/c84e370f4092749380c234e1d76d4d0e.jpg","commentCount":0,"createAt":"2018-01-11 14:54:16","likeCount":0,"nick":"你好帅","pid":"2b2c11ca49","postDataBean":{"content":"你那边不vv吃","imageList":["http://bmob-cdn-16009.b0.upaiyun.com/2018/01/11/756fea365d0346db9c43fc1e93f209af.jpg","http://bmob-cdn-16009.b0.upaiyun.com/2018/01/11/a3a4f7f855354dcbac4fd8147077720a.jpg"],"location":"不显示","shareContent":null,"shareType":0,"voiceUrl":null},"sex":false,"shareCount":0,"uid":"5b085e6465"},"shareType":10,"voiceUrl":null}
         * createdAt : 2018-01-11 14:54:58
         * msgType : 11
         * objectId : b8001000e8
         * updatedAt : 2018-01-11 14:54:58
         */

        private String author;
        private NewDataBean content;
        private String createdAt;
        private int msgType;
        private String objectId;
        private String updatedAt;


        @Override
        public String toString() {
            return "DataBean{" +
                    "author='" + author + '\'' +
                    ", content=" + content +
                    ", createdAt='" + createdAt + '\'' +
                    ", msgType=" + msgType +
                    ", objectId='" + objectId + '\'' +
                    ", updatedAt='" + updatedAt + '\'' +
                    '}';
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public NewDataBean getContent() {
            return content;
        }

        public void setContent(NewDataBean content) {
            this.content = content;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public int getMsgType() {
            return msgType;
        }

        public void setMsgType(int msgType) {
            this.msgType = msgType;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }




        public static class NewDataBean{

            @Override
            public String toString() {
                return "NewDataBean{" +
                        "content='" + content + '\'' +
                        ", imageList=" + imageList +
                        ", location='" + location + '\'' +
                        ", shareContent=" + shareContent +
                        ", shareType=" + shareType +
                        ", voiceUrl=" + voiceUrl +
                        '}';
            }

            /**
             * content : 好吧v财产分割给
             * imageList : null
             * location : 不显示
             * shareContent : {"address":null,"avatar":"http://bmob-cdn-16009.b0.upaiyun.com/2017/12/29/c84e370f4092749380c234e1d76d4d0e.jpg","commentCount":0,"createAt":"2018-01-11 14:54:16","likeCount":0,"nick":"你好帅","pid":"2b2c11ca49","postDataBean":{"content":"你那边不vv吃","imageList":["http://bmob-cdn-16009.b0.upaiyun.com/2018/01/11/756fea365d0346db9c43fc1e93f209af.jpg","http://bmob-cdn-16009.b0.upaiyun.com/2018/01/11/a3a4f7f855354dcbac4fd8147077720a.jpg"],"location":"不显示","shareContent":null,"shareType":0,"voiceUrl":null},"sex":false,"shareCount":0,"uid":"5b085e6465"}
             * shareType : 10
             * voiceUrl : null
             */





            private String content;
            private Object imageList;
            private String location;
            private ShareContentBean shareContent;
            private int shareType;
            private Object voiceUrl;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public Object getImageList() {
                return imageList;
            }

            public void setImageList(Object imageList) {
                this.imageList = imageList;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public ShareContentBean getShareContent() {
                return shareContent;
            }

            public void setShareContent(ShareContentBean shareContent) {
                this.shareContent = shareContent;
            }

            public int getShareType() {
                return shareType;
            }

            public void setShareType(int shareType) {
                this.shareType = shareType;
            }

            public Object getVoiceUrl() {
                return voiceUrl;
            }

            public void setVoiceUrl(Object voiceUrl) {
                this.voiceUrl = voiceUrl;
            }

            public static class ShareContentBean {
                @Override
                public String toString() {
                    return "ShareContentBean{" +
                            "address=" + address +
                            ", avatar='" + avatar + '\'' +
                            ", commentCount=" + commentCount +
                            ", createAt='" + createAt + '\'' +
                            ", likeCount=" + likeCount +
                            ", nick='" + nick + '\'' +
                            ", pid='" + pid + '\'' +
                            ", postDataBean=" + postDataBean +
                            ", sex=" + sex +
                            ", shareCount=" + shareCount +
                            ", uid='" + uid + '\'' +
                            '}';
                }

                /**
                 * address : null
                 * avatar : http://bmob-cdn-16009.b0.upaiyun.com/2017/12/29/c84e370f4092749380c234e1d76d4d0e.jpg
                 * commentCount : 0
                 * createAt : 2018-01-11 14:54:16
                 * likeCount : 0
                 * nick : 你好帅
                 * pid : 2b2c11ca49
                 * postDataBean : {"content":"你那边不vv吃","imageList":["http://bmob-cdn-16009.b0.upaiyun.com/2018/01/11/756fea365d0346db9c43fc1e93f209af.jpg","http://bmob-cdn-16009.b0.upaiyun.com/2018/01/11/a3a4f7f855354dcbac4fd8147077720a.jpg"],"location":"不显示","shareContent":null,"shareType":0,"voiceUrl":null}
                 * sex : false
                 * shareCount : 0
                 * uid : 5b085e6465
                 */




                private Object address;
                private String avatar;
                private int commentCount;
                private String createAt;
                private int likeCount;
                private String nick;
                private String pid;
                private PostDataBeanBean postDataBean;
                private boolean sex;
                private int shareCount;
                private String uid;

                public Object getAddress() {
                    return address;
                }

                public void setAddress(Object address) {
                    this.address = address;
                }

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }

                public int getCommentCount() {
                    return commentCount;
                }

                public void setCommentCount(int commentCount) {
                    this.commentCount = commentCount;
                }

                public String getCreateAt() {
                    return createAt;
                }

                public void setCreateAt(String createAt) {
                    this.createAt = createAt;
                }

                public int getLikeCount() {
                    return likeCount;
                }

                public void setLikeCount(int likeCount) {
                    this.likeCount = likeCount;
                }

                public String getNick() {
                    return nick;
                }

                public void setNick(String nick) {
                    this.nick = nick;
                }

                public String getPid() {
                    return pid;
                }

                public void setPid(String pid) {
                    this.pid = pid;
                }

                public PostDataBeanBean getPostDataBean() {
                    return postDataBean;
                }

                public void setPostDataBean(PostDataBeanBean postDataBean) {
                    this.postDataBean = postDataBean;
                }

                public boolean isSex() {
                    return sex;
                }

                public void setSex(boolean sex) {
                    this.sex = sex;
                }

                public int getShareCount() {
                    return shareCount;
                }

                public void setShareCount(int shareCount) {
                    this.shareCount = shareCount;
                }

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

                public static class PostDataBeanBean {
                    /**
                     * content : 你那边不vv吃
                     * imageList : ["http://bmob-cdn-16009.b0.upaiyun.com/2018/01/11/756fea365d0346db9c43fc1e93f209af.jpg","http://bmob-cdn-16009.b0.upaiyun.com/2018/01/11/a3a4f7f855354dcbac4fd8147077720a.jpg"]
                     * location : 不显示
                     * shareContent : null
                     * shareType : 0
                     * voiceUrl : null
                     */

                    private String content;
                    private String location;
                    private Object shareContent;
                    private int shareType;
                    private Object voiceUrl;
                    private List<String> imageList;
                    


                    @Override
                    public String toString() {
                        return "PostDataBeanBean{" +
                                "content='" + content + '\'' +
                                ", location='" + location + '\'' +
                                ", shareContent=" + shareContent +
                                ", shareType=" + shareType +
                                ", voiceUrl=" + voiceUrl +
                                ", imageList=" + imageList +
                                '}';
                    }

                    public String getContent() {
                        return content;
                    }

                    public void setContent(String content) {
                        this.content = content;
                    }

                    public String getLocation() {
                        return location;
                    }

                    public void setLocation(String location) {
                        this.location = location;
                    }

                    public Object getShareContent() {
                        return shareContent;
                    }

                    public void setShareContent(Object shareContent) {
                        this.shareContent = shareContent;
                    }

                    public int getShareType() {
                        return shareType;
                    }

                    public void setShareType(int shareType) {
                        this.shareType = shareType;
                    }

                    public Object getVoiceUrl() {
                        return voiceUrl;
                    }

                    public void setVoiceUrl(Object voiceUrl) {
                        this.voiceUrl = voiceUrl;
                    }

                    public List<String> getImageList() {
                        return imageList;
                    }

                    public void setImageList(List<String> imageList) {
                        this.imageList = imageList;
                    }
                }
            }
        }
    }
}
