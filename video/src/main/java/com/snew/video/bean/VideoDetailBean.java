package com.snew.video.bean;

import java.util.List;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/11/27     16:01
 */
public class VideoDetailBean {

    /**
     * retCode : 200
     * retDesc : 成功
     * data : {"text":"哇塞！出神入化，太漂亮了！","video":{"link":[{"url":"http://v11-tt.ixigua.com/786d84417f17e5d41a324fafec6409e4/5bff6b31/video/m/220135f8030571e4a2e947980e69f7bbae21145449000010a510df708b/?rc=Z2c3OzhlaTZmaGUzaGhoOEApQHRAbzUzNTU6MzUzMzU1NDU0NDVvQGgzdSlAZjN1KWRzcmd5a3VyZ3lybHh3ZjUzQDEyL18vMWA1MzE2MWA1Li5zLW8jbyMtMC4tMzItLi0zLjYuNS06I28jOmEtcSM6YHZpXGJmK2BeYmYrXnFsOiMuL14%3D","buttonText":"极速(360p)视频地址1","type":"极速(360p)"},{"url":"http://v7.pstatp.com/a605c3544f402c6bbb37e25b040324d6/5bff6b31/video/m/220135f8030571e4a2e947980e69f7bbae21145449000010a510df708b/?rc=Z2c3OzhlaTZmaGUzaGhoOEApQHRAbzUzNTU6MzUzMzU1NDU0NDVvQGgzdSlAZjN1KWRzcmd5a3VyZ3lybHh3ZjUzQDEyL18vMWA1MzE2MWA1Li5zLW8jbyMtMC4tMzItLi0zLjYuNS06I28jOmEtcSM6YHZpXGJmK2BeYmYrXnFsOiMuL14%3D","buttonText":"极速(360p)视频地址2","type":"极速(360p)"}],"download":[{"url":"http://v11-tt.ixigua.com/786d84417f17e5d41a324fafec6409e4/5bff6b31/video/m/220135f8030571e4a2e947980e69f7bbae21145449000010a510df708b/?rc=Z2c3OzhlaTZmaGUzaGhoOEApQHRAbzUzNTU6MzUzMzU1NDU0NDVvQGgzdSlAZjN1KWRzcmd5a3VyZ3lybHh3ZjUzQDEyL18vMWA1MzE2MWA1Li5zLW8jbyMtMC4tMzItLi0zLjYuNS06I28jOmEtcSM6YHZpXGJmK2BeYmYrXnFsOiMuL14%3D","buttonText":"极速(360p)视频地址1","type":"极速(360p)"}]}}
     * succ : true
     */

    private int retCode;
    private String retDesc;
    private DataBean data;
    private boolean succ;

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getRetDesc() {
        return retDesc;
    }

    public void setRetDesc(String retDesc) {
        this.retDesc = retDesc;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isSucc() {
        return succ;
    }

    public void setSucc(boolean succ) {
        this.succ = succ;
    }

    public static class DataBean {
        /**
         * text : 哇塞！出神入化，太漂亮了！
         * video : {"link":[{"url":"http://v11-tt.ixigua.com/786d84417f17e5d41a324fafec6409e4/5bff6b31/video/m/220135f8030571e4a2e947980e69f7bbae21145449000010a510df708b/?rc=Z2c3OzhlaTZmaGUzaGhoOEApQHRAbzUzNTU6MzUzMzU1NDU0NDVvQGgzdSlAZjN1KWRzcmd5a3VyZ3lybHh3ZjUzQDEyL18vMWA1MzE2MWA1Li5zLW8jbyMtMC4tMzItLi0zLjYuNS06I28jOmEtcSM6YHZpXGJmK2BeYmYrXnFsOiMuL14%3D","buttonText":"极速(360p)视频地址1","type":"极速(360p)"},{"url":"http://v7.pstatp.com/a605c3544f402c6bbb37e25b040324d6/5bff6b31/video/m/220135f8030571e4a2e947980e69f7bbae21145449000010a510df708b/?rc=Z2c3OzhlaTZmaGUzaGhoOEApQHRAbzUzNTU6MzUzMzU1NDU0NDVvQGgzdSlAZjN1KWRzcmd5a3VyZ3lybHh3ZjUzQDEyL18vMWA1MzE2MWA1Li5zLW8jbyMtMC4tMzItLi0zLjYuNS06I28jOmEtcSM6YHZpXGJmK2BeYmYrXnFsOiMuL14%3D","buttonText":"极速(360p)视频地址2","type":"极速(360p)"}],"download":[{"url":"http://v11-tt.ixigua.com/786d84417f17e5d41a324fafec6409e4/5bff6b31/video/m/220135f8030571e4a2e947980e69f7bbae21145449000010a510df708b/?rc=Z2c3OzhlaTZmaGUzaGhoOEApQHRAbzUzNTU6MzUzMzU1NDU0NDVvQGgzdSlAZjN1KWRzcmd5a3VyZ3lybHh3ZjUzQDEyL18vMWA1MzE2MWA1Li5zLW8jbyMtMC4tMzItLi0zLjYuNS06I28jOmEtcSM6YHZpXGJmK2BeYmYrXnFsOiMuL14%3D","buttonText":"极速(360p)视频地址1","type":"极速(360p)"}]}
         */

        private String text;
        private VideoBean video;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public VideoBean getVideo() {
            return video;
        }

        public void setVideo(VideoBean video) {
            this.video = video;
        }

        public static class VideoBean {
            private List<LinkBean> link;
            private List<DownloadBean> download;

            public List<LinkBean> getLink() {
                return link;
            }

            public void setLink(List<LinkBean> link) {
                this.link = link;
            }

            public List<DownloadBean> getDownload() {
                return download;
            }

            public void setDownload(List<DownloadBean> download) {
                this.download = download;
            }

            public static class LinkBean {
                /**
                 * url : http://v11-tt.ixigua.com/786d84417f17e5d41a324fafec6409e4/5bff6b31/video/m/220135f8030571e4a2e947980e69f7bbae21145449000010a510df708b/?rc=Z2c3OzhlaTZmaGUzaGhoOEApQHRAbzUzNTU6MzUzMzU1NDU0NDVvQGgzdSlAZjN1KWRzcmd5a3VyZ3lybHh3ZjUzQDEyL18vMWA1MzE2MWA1Li5zLW8jbyMtMC4tMzItLi0zLjYuNS06I28jOmEtcSM6YHZpXGJmK2BeYmYrXnFsOiMuL14%3D
                 * buttonText : 极速(360p)视频地址1
                 * type : 极速(360p)
                 */

                private String url;
                private String buttonText;
                private String type;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getButtonText() {
                    return buttonText;
                }

                public void setButtonText(String buttonText) {
                    this.buttonText = buttonText;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }

            public static class DownloadBean {
                /**
                 * url : http://v11-tt.ixigua.com/786d84417f17e5d41a324fafec6409e4/5bff6b31/video/m/220135f8030571e4a2e947980e69f7bbae21145449000010a510df708b/?rc=Z2c3OzhlaTZmaGUzaGhoOEApQHRAbzUzNTU6MzUzMzU1NDU0NDVvQGgzdSlAZjN1KWRzcmd5a3VyZ3lybHh3ZjUzQDEyL18vMWA1MzE2MWA1Li5zLW8jbyMtMC4tMzItLi0zLjYuNS06I28jOmEtcSM6YHZpXGJmK2BeYmYrXnFsOiMuL14%3D
                 * buttonText : 极速(360p)视频地址1
                 * type : 极速(360p)
                 */

                private String url;
                private String buttonText;
                private String type;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getButtonText() {
                    return buttonText;
                }

                public void setButtonText(String buttonText) {
                    this.buttonText = buttonText;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }
        }
    }
}
