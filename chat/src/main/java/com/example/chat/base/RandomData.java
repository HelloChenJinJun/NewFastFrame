package com.example.chat.base;

import com.example.chat.manager.MsgManager;
import com.example.chat.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/28      21:38
 * QQ:             1981367757
 */

public class RandomData {
        private static List<String> nickList = new ArrayList<>();
        private static List<String> avatarList = new ArrayList<>();
        private static List<String> wallPaperList = new ArrayList<>();
        private static List<String> titleWallPaper = new ArrayList<>();

        static {
                String a = "你好帅";
                String b = "我好帅";
                String c = "罗志强";
                String d = "萌萌";
                String e = "梦梦";
                String f = "你好";
                nickList.add(a);
                nickList.add(b);
                nickList.add(c);
                nickList.add(d);
                nickList.add(e);
                nickList.add(f);
        }


        public static String getRandomNick() {
                Random random = new Random();
                int index = random.nextInt(nickList.size() - 1);
                if (index >= 0 && index < nickList.size()) {
                        return nickList.get(index);
                } else {
                        return nickList.get(0);
                }
        }

        public static void initAllRanDomData() {
                initRandomAvatar();
                initRandomWallPaper();
                initTitleWallPaper();
        }


        public static void initRandomAvatar() {
                if (avatarList.size() > 0) {
                        avatarList.clear();
                }
                MsgManager.getInstance().getAllDefaultAvatarFromServer(new FindListener<String>() {
                        @Override
                        public void done(List<String> list, BmobException e) {
                                if (e == null) {
                                        if (list != null && list.size() > 0) {
                                                LogUtil.e("获取到默认头像数据拉");
                                                for (String url :
                                                        list) {
                                                        LogUtil.e(url);
                                                }
                                                avatarList.addAll(list);
                                        } else {
                                                LogUtil.e("从服务器上获取的到的默认的头像数据为空");
                                        }
                                }else {
                                        LogUtil.e("从服务器上获取的到的默认的头像数据失败" +e.toString());
                                }
                        }

                });
        }


        public static void initRandomWallPaper() {
                MsgManager.getInstance().getAllDefaultWallPaperFromServer(new FindListener<String>() {
                        @Override
                        public void done(List<String> list, BmobException e) {
                                if (e == null) {
                                        if (list != null && list.size() > 0) {
                                                LogUtil.e("获取到默认背景数据拉");
                                                for (String url :
                                                        list) {
                                                        LogUtil.e(url);
                                                }
                                                wallPaperList.addAll(list);
                                        } else {
                                                LogUtil.e("从服务器上获取的到的默认的背景数据为空");
                                        }
                                }else {
                                        LogUtil.e("从服务器上获取的到的默认的背景数据失败" +e.toString());
                                }
                        }
                });

        }

        public static void initTitleWallPaper() {
                MsgManager.getInstance().getAllDefaultTitleWallPaperFromServer(new FindListener<String>() {
                        @Override
                        public void done(List<String> list, BmobException e) {
                                if (e == null) {
                                        if (list != null && list.size() > 0) {
                                                LogUtil.e("获取到默认背景数据拉");
                                                for (String url :
                                                        list) {
                                                        LogUtil.e(url);
                                                }
                                                titleWallPaper.addAll(list);
                                        } else {
                                                LogUtil.e("从服务器上获取的到的默认的背景数据为空");
                                        }
                                }else {
                                        LogUtil.e("从服务器上获取的到的默认的背景数据失败" +e.toString());
                                }
                        }

                });
        }


        public static String getRandomAvatar() {
                if (avatarList != null && avatarList.size() > 0) {
                        Random random = new Random();
                        int index = random.nextInt(avatarList.size() - 1);
                        LogUtil.e("大小:" + index);
                        if (index >= 0 && index < avatarList.size()) {
                                return avatarList.get(index);
                        }
                } else {
                        LogUtil.e("为空？？？？？？？");
                }
                return null;
        }


        public static String getRandomTitleWallPaper() {
                if (titleWallPaper != null && titleWallPaper.size() > 0) {
                        Random random = new Random();
                        int index = random.nextInt(titleWallPaper.size() - 1);
                        LogUtil.e("大小:" + index);
                        if (index >= 0 && index < titleWallPaper.size()) {
                                return titleWallPaper.get(index);
                        }
                } else {
                        LogUtil.e("为空？？？？？？？");
                }
                return null;
        }

        public static String getRandomWallPaper() {
                if (wallPaperList != null && wallPaperList.size() > 0) {
                        Random random = new Random();
                        int index = random.nextInt(wallPaperList.size() - 1);
                        LogUtil.e("大小:" + index);
                        if (index >= 0 && index < wallPaperList.size()) {
                                return wallPaperList.get(index);
                        }
                } else {
                        LogUtil.e("为空？？？？？？？");
                }
                return null;
        }
}
