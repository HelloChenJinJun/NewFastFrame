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
        private static List<String>  collegeList=new ArrayList<>();
        private static List<String> nameList=new ArrayList<>();
        private static List<String> yearList=new ArrayList<>();
        private static List<String> majorList=new ArrayList<>();
        private static List<String> classList=new ArrayList<>();

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
                collegeList.add("经济管理学院");
                collegeList.add("艺术与传媒学院");
                collegeList.add("马克思主义学院");
                collegeList.add("计算机学院");
                collegeList.add("李四光学院");
                collegeList.add("工程学院");
                collegeList.add("外国语学院");
                yearList.add("2013级");
                yearList.add("2014级");
                yearList.add("2015级");
                yearList.add("2016级");
                yearList.add("2017级");
                nameList.add("陈锦军");
                nameList.add("习近平");
                nameList.add("李克强");
                nameList.add("毛泽东");
                nameList.add("周恩来");
                nameList.add("温家宝");
                nameList.add("胡锦涛");
                majorList.add("信息管理与信息系统");
                majorList.add("计算机科学技术");
                majorList.add("会计学");
                majorList.add("金融学");
                majorList.add("旅游管理");
                majorList.add("工商管理");
                majorList.add("信息安全");
                majorList.add("软件工程");
                classList.add("086141");
                classList.add("054863");
                classList.add("089653");
                classList.add("053246");
                classList.add("087546");
                classList.add("095642");
        }


        public static String getRandomNick() {
                Random random = new Random();
                int index = random.nextInt(nickList.size());
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

        public static String getRandomCollege() {
                Random random = new Random();
                int index = random.nextInt(collegeList.size());
                if (index >= 0 && index < collegeList.size()) {
                        return collegeList.get(index);
                } else {
                        return collegeList.get(0);
                }
        }



        public static String getRandomName(){
                Random random = new Random();
                int index = random.nextInt(nameList.size());
                if (index >= 0 && index < nameList.size()) {
                        return nameList.get(index);
                } else {
                        return nameList.get(0);
                }
        }


        public static String getRandomEducation(){
                Random random = new Random();
                if (random.nextInt(10) % 2 == 0) {
                        return "本科";
                }else {
                        return "研究生";
                }
        }


        public static String getRandomYear(){
                Random random = new Random();
                int index = random.nextInt(yearList.size());
                if (index >= 0 && index < yearList.size()) {
                        return yearList.get(index);
                } else {
                        return yearList.get(0);
                }
        }


        public static String getRandomMajor(){
                Random random = new Random();
                int index = random.nextInt(majorList.size());
                if (index >= 0 && index < majorList.size()) {
                        return majorList.get(index);
                } else {
                        return majorList.get(0);
                }
        }

        public static String getRandomClassNumber(){
                Random random = new Random();
                int index = random.nextInt(classList.size());
                if (index >= 0 && index < classList.size()) {
                        return classList.get(index);
                } else {
                        return classList.get(0);
                }
        }
}
