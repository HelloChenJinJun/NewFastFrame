package com.example.cootek.newfastframe.bean;

import java.util.List;

/**
 * Created by COOTEK on 2017/8/15.
 */

public class SearchMusicBean {

    /**
     * song : [{"bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","weight":"5590","songname":"Hello","songid":"544169425","has_mv":"0","yyr_artist":"0","resource_type_ext":"0","artistname":"Adele","info":"","resource_provider":"1","control":"0000000000","encrypted_songid":"8408206F5DD008597ADBDB"},{"bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","weight":"1480","songname":"Hello 梦想","songid":"550228589","has_mv":"0","yyr_artist":"0","resource_type_ext":"0","artistname":"MERA","info":"","resource_provider":"1","control":"0000000000","encrypted_songid":"520820CBD26C08598294C6"},{"bitrate_fee":"{\"0\":\"129|-1\",\"1\":\"-1|-1\"}","weight":"820","songname":"Hello","songid":"14672450","has_mv":"0","yyr_artist":"0","resource_type_ext":"2","artistname":"王杰","info":"","resource_provider":"1","control":"0000000000","encrypted_songid":"5606dfe2420958414a1eL"},{"bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","weight":"380","songname":"地球上如此热烈的最后一晚,","songid":"275377896","has_mv":"0","yyr_artist":"0","resource_type_ext":"0","artistname":"HELLO sea","info":"","resource_provider":"1","control":"0000000000","encrypted_songid":"90081069eee8095818b66cL"},{"bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","weight":"310","songname":"Hello 大叔","songid":"533385904","has_mv":"0","yyr_artist":"0","resource_type_ext":"0","artistname":"李晓杰","info":"","resource_provider":"1","control":"0000000000","encrypted_songid":"15081FCAD0A80858A3D9D0"},{"bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","weight":"310","songname":"Hello","songid":"432679","has_mv":"0","yyr_artist":"0","resource_type_ext":"0","artistname":"阿杜","info":"","resource_provider":"1","control":"0000000000","encrypted_songid":"76081D5F59FA0859438377"},{"bitrate_fee":"{\"0\":\"129|-1\",\"1\":\"-1|-1\"}","weight":"220","songname":"Oh God","songid":"132091302","has_mv":"0","yyr_artist":"0","resource_type_ext":"0","artistname":"Hello Saferide","info":"","resource_provider":"1","control":"0000000000","encrypted_songid":"57077df8da60958481d53L"},{"bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","weight":"170","songname":"Hello,Again~昔からある場所~","songid":"26227616","has_mv":"0","yyr_artist":"0","resource_type_ext":"0","artistname":"My Little Lover","info":"","resource_provider":"1","control":"0000000000","encrypted_songid":"410719033a00956ef8d61L"},{"bitrate_fee":"{\"0\":\"129|-1\",\"1\":\"-1|-1\"}","weight":"170","songname":"Hello","songid":"341317","has_mv":"0","yyr_artist":"0","resource_type_ext":"0","artistname":"Luther Vandross","info":"","resource_provider":"1","control":"0000000000","encrypted_songid":"640553545095848d95bL"},{"bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","weight":"150","songname":"催眠曲 (With 김현민)","songid":"275377900","has_mv":"0","yyr_artist":"0","resource_type_ext":"0","artistname":"HELLO sea","info":"","resource_provider":"1","control":"0000000000","encrypted_songid":"94081069eeec095818b66dL"}]
     * album : [{"albumname":"Hello","weight":"5590","artistname":"Adele","resource_type_ext":"0","artistpic":"http://qukufile2.qianqian.com/data2/pic/22755c0d4921906703ac5f1aab6cb197/544169422/544169422.jpg@s_1,w_40,h_40","albumid":"544169422"},{"albumname":" Hello 梦想","weight":"1480","artistname":"MERA","resource_type_ext":"0","artistpic":"http://qukufile2.qianqian.com/data2/pic/97145d775292456746fcb4aa7f4769fd/550228180/550228180.jpg@s_1,w_40,h_40","albumid":"550228587"},{"albumname":"Hello!","weight":"1170","artistname":"王杰","resource_type_ext":"0","artistpic":"http://qukufile2.qianqian.com/data2/pic/e90888ec2dcd22f8902181e0eb118c87/288536543/288536543.jpeg@s_1,w_40,h_40","albumid":"184808"},{"albumname":"Hello 梁心颐","weight":"910","artistname":"梁心颐","resource_type_ext":"0","artistpic":"http://qukufile2.qianqian.com/data2/pic/1e978afd01ec57e1a3ed4e7541985480/262015032/262015032.jpg@s_0,w_40","albumid":"3451242"},{"albumname":"我分开海和他","weight":"530","artistname":"HELLO sea","resource_type_ext":"0","artistpic":"http://qukufile2.qianqian.com/data2/pic/fa0c613bcabaf904f7a92ab4a4fb4d0d/288066249/288066249.jpg@s_0,w_40","albumid":"275377879"},{"albumname":"Hello 大叔","weight":"480","artistname":"李晓杰","resource_type_ext":"0","artistpic":"http://qukufile2.qianqian.com/data2/pic/93947d1886cfe0446a87ef9a028b41f6/533383839/533383839.jpg@s_1,w_40,h_40","albumid":"533383839"},{"albumname":"Dad Told Me","weight":"220","artistname":"Hello Saferide","resource_type_ext":"0","artistpic":"http://qukufile2.qianqian.com/data2/pic/132091227/132091227.jpg@s_1,w_40,h_40","albumid":"132091224"},{"albumname":"Hello Baby","weight":"180","artistname":"左佳","resource_type_ext":"0","artistpic":"http://qukufile2.qianqian.com/data2/pic/238666007/238666007.jpg@s_0,w_40","albumid":"238666376"},{"albumname":"The Fox, The Hunter and Hello Saferide","weight":"160","artistname":"Hello Saferide","resource_type_ext":"0","artistpic":"http://qukufile2.qianqian.com/data2/pic/122972834/122972834.jpg@s_1,w_40,h_40","albumid":"122972833"},{"albumname":"Hello Neptune","weight":"50","artistname":"Hong Faux","resource_type_ext":"0","artistpic":"http://qukufile2.qianqian.com/data2/music/A449C275305D24437AD88756F2575358/254802363/254802363.jpg@s_0,w_40","albumid":"116943639"}]
     * order : artist,song,album
     * error_code : 22000
     * artist : [{"yyr_artist":"0","artistname":"Hello Sea","artistid":"232805960","artistpic":"","weight":"460"},{"yyr_artist":"0","artistname":"Hello Saferide","artistid":"88001569","artistpic":"http://qukufile2.qianqian.com/data2/music/C7ADC8427F63C60DCCC142ADF2CFAB61/252139284/252139284.jpg@s_0,w_48","weight":"270"},{"yyr_artist":"0","artistname":"Hello Venus","artistid":"87961738","artistpic":"http://qukufile2.qianqian.com/data2/pic/81806861f69a4ea945df7160a494867b/261578751/261578751.jpg@s_0,w_48","weight":"10"},{"yyr_artist":"0","artistname":"HELLOVENUS","artistid":"232686945","artistpic":"","weight":"10"},{"yyr_artist":"0","artistname":"Hello, Dolly! Orchestra","artistid":"340229769","artistpic":"","weight":"0"},{"yyr_artist":"0","artistname":"Hello, Dolly! Ensemble","artistid":"340229768","artistpic":"","weight":"0"},{"yyr_artist":"0","artistname":"Hello Cuca","artistid":"240063901","artistpic":"","weight":"0"},{"yyr_artist":"0","artistname":"Hello, Dolly! Ensemble (2017)","artistid":"340196261","artistpic":"","weight":"0"},{"yyr_artist":"0","artistname":"Hello, Dolly! Orchestra (2017)","artistid":"340196265","artistpic":"","weight":"0"},{"yyr_artist":"0","artistname":"Hello Blue Roses","artistid":"240050739","artistpic":"","weight":"0"}]
     */

    private String order;
    private int error_code;
    private List<SongBean> song;
    private List<AlbumBean> album;
    private List<ArtistBean> artist;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<SongBean> getSong() {
        return song;
    }

    public void setSong(List<SongBean> song) {
        this.song = song;
    }

    public List<AlbumBean> getAlbum() {
        return album;
    }

    public void setAlbum(List<AlbumBean> album) {
        this.album = album;
    }

    public List<ArtistBean> getArtist() {
        return artist;
    }

    public void setArtist(List<ArtistBean> artist) {
        this.artist = artist;
    }

    public static class SongBean {
        /**
         * bitrate_fee : {"0":"0|0","1":"0|0"}
         * weight : 5590
         * songname : Hello
         * songid : 544169425
         * has_mv : 0
         * yyr_artist : 0
         * resource_type_ext : 0
         * artistname : Adele
         * info :
         * resource_provider : 1
         * control : 0000000000
         * encrypted_songid : 8408206F5DD008597ADBDB
         */

        private String bitrate_fee;
        private String weight;
        private String songname;
        private String songid;
        private String has_mv;
        private String yyr_artist;
        private String resource_type_ext;
        private String artistname;
        private String info;
        private String resource_provider;
        private String control;
        private String encrypted_songid;

        public String getBitrate_fee() {
            return bitrate_fee;
        }

        public void setBitrate_fee(String bitrate_fee) {
            this.bitrate_fee = bitrate_fee;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getSongname() {
            return songname;
        }

        public void setSongname(String songname) {
            this.songname = songname;
        }

        public String getSongid() {
            return songid;
        }

        public void setSongid(String songid) {
            this.songid = songid;
        }

        public String getHas_mv() {
            return has_mv;
        }

        public void setHas_mv(String has_mv) {
            this.has_mv = has_mv;
        }

        public String getYyr_artist() {
            return yyr_artist;
        }

        public void setYyr_artist(String yyr_artist) {
            this.yyr_artist = yyr_artist;
        }

        public String getResource_type_ext() {
            return resource_type_ext;
        }

        public void setResource_type_ext(String resource_type_ext) {
            this.resource_type_ext = resource_type_ext;
        }

        public String getArtistname() {
            return artistname;
        }

        public void setArtistname(String artistname) {
            this.artistname = artistname;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getResource_provider() {
            return resource_provider;
        }

        public void setResource_provider(String resource_provider) {
            this.resource_provider = resource_provider;
        }

        public String getControl() {
            return control;
        }

        public void setControl(String control) {
            this.control = control;
        }

        public String getEncrypted_songid() {
            return encrypted_songid;
        }

        public void setEncrypted_songid(String encrypted_songid) {
            this.encrypted_songid = encrypted_songid;
        }
    }

    public static class AlbumBean {
        /**
         * albumname : Hello
         * weight : 5590
         * artistname : Adele
         * resource_type_ext : 0
         * artistpic : http://qukufile2.qianqian.com/data2/pic/22755c0d4921906703ac5f1aab6cb197/544169422/544169422.jpg@s_1,w_40,h_40
         * albumid : 544169422
         */

        private String albumname;
        private String weight;
        private String artistname;
        private String resource_type_ext;
        private String artistpic;
        private String albumid;

        public String getAlbumname() {
            return albumname;
        }

        public void setAlbumname(String albumname) {
            this.albumname = albumname;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getArtistname() {
            return artistname;
        }

        public void setArtistname(String artistname) {
            this.artistname = artistname;
        }

        public String getResource_type_ext() {
            return resource_type_ext;
        }

        public void setResource_type_ext(String resource_type_ext) {
            this.resource_type_ext = resource_type_ext;
        }

        public String getArtistpic() {
            return artistpic;
        }

        public void setArtistpic(String artistpic) {
            this.artistpic = artistpic;
        }

        public String getAlbumid() {
            return albumid;
        }

        public void setAlbumid(String albumid) {
            this.albumid = albumid;
        }
    }

    public static class ArtistBean {
        /**
         * yyr_artist : 0
         * artistname : Hello Sea
         * artistid : 232805960
         * artistpic :
         * weight : 460
         */

        private String yyr_artist;
        private String artistname;
        private String artistid;
        private String artistpic;
        private String weight;

        public String getYyr_artist() {
            return yyr_artist;
        }

        public void setYyr_artist(String yyr_artist) {
            this.yyr_artist = yyr_artist;
        }

        public String getArtistname() {
            return artistname;
        }

        public void setArtistname(String artistname) {
            this.artistname = artistname;
        }

        public String getArtistid() {
            return artistid;
        }

        public void setArtistid(String artistid) {
            this.artistid = artistid;
        }

        public String getArtistpic() {
            return artistpic;
        }

        public void setArtistpic(String artistpic) {
            this.artistpic = artistpic;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }
    }
}
