package com.example.news.util;

import android.util.Base64;

import com.example.commonlibrary.BaseApplication;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/17      9:36
 * QQ:             1981367757
 */

public class NewsUtil {
    public static final String CUG_NEWS = "http://www.cug.edu.cn/index/ddyw.htm";
    public static final String CUG_INDEX = "http://www.cug.edu.cn/";
    public static final String CUG_NOTIFY = "http://www.cug.edu.cn/index/tzgg.htm";
    public static final String CUG_TECHNOLOGY="http://www.cug.edu.cn/index/xsdt.htm";
    public static final String URL = "url";
    public static final String TITLE = "title";

    public static final String CUG_LIBRARY="http://202.114.202.207/opac/openlink.php?location=ALL&title=java&doctype=ALL&lang_code=ALL&match_flag=forward&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=no&with_ebook=&page=2";
    public static final String LIBRARY_LOGIN = "http://202.114.202.207/reader/login.php";
    public static final String LIBRARY_BORROW_BOOK_LIST_URL="http://202.114.202.207/reader/book_lst.php";
    public static final String LIBRARY_BORROW_BOOK_HISTORY_LIST_URL="http://202.114.202.207/reader/book_hist.php";
    public static final String LIBRARY_COOKIE = "library_cookie";
    public static final String LIBRARY_BERIFY_IMAGE_URL = "http://202.114.202.207/reader/captcha.php";
//  http://202.114.202.207/
    public static final String BASE_URL = "http://c.3g.163.com/";
    public static final String CARD_LOGIN_URL = "http://card.cug.edu.cn/";
    public static final String CARD_LOGIN_COOKIE = "card_cookie";
    public static final String CARD_VERIFY_IMAGE_URL="http://card.cug.edu.cn/Login/GetValidateCode";
    public static final String CARD_POST_LOGIN_URL="http://card.cug.edu.cn/Login/LoginBySnoQuery";
    public static final String CARD_POST_LOGIN_COOKIE = "card_post_login_cookie";
    public static final String CARD_PAGE_INFO_URL = "http://card.cug.edu.cn/Page/page";
    public static final String CARD_BANK_INFO_URL = "http://card.cug.edu.cn/User/GetCardInfoByAccountNoParm";
    public static final String PW = "password";
    public static final String ACCOUNT = "account";
    public static final String PAY_URL="http://card.cug.edu.cn/User/Account_Pay";
    public static final String PAY_HISTORY_URL = "http://card.cug.edu.cn/Report/GetPersonTrjn";
    public static final String ERROR_INFO = "error_info";
    public static final String PHOTO_SET = "photoset";
    public static final String SPECIAL_TITLE = "special";
    public static final String SPECIAL_ID = "special_id";
    public static final String POST_ID = "post_id";
    public static final String PHOTO_SET_ID = "photo_set_id";
    public static final String JG_INDEX_URL = "http://jgxy.cug.edu.cn/news.asp?bid=6&sid=1";
    public static final String JG_NOTICE_URL = "http://jgxy.cug.edu.cn/news.asp?bid=6&sid=2";
    public static final String JG_SCIENCE_URL = "http://jgxy.cug.edu.cn/news.asp?bid=2&sid=11";
    public static final String JG_BASE_URL = "http://jgxy.cug.edu.cn/";
    public static final String JG_COOKIE = "jg_cookie";
    public static final String COLLEGE_TYPE_JG = "TYPE_JG";
    public static final String COLLEGE_TYPE = "college_type";
//    http://ggxy.cug.edu.cn/ggxy2014/?sort=31
    public static final String GG_BASE_URL = "http://ggxy.cug.edu.cn/ggxy2014/";
    public static final String COLLEGE_TYPE_GG = "TYPE_GG";
    public static final String GG_INDEX_URL = "http://ggxy.cug.edu.cn/ggxy2014/?sort=31";
    public static final String GG_NOTICE_URL = "http://ggxy.cug.edu.cn/ggxy2014/?sort=32";
    public static final String GG_SCIENCE_URL = "http://ggxy.cug.edu.cn/ggxy2014/?sort=12";
    public static final String JSJ_BASE_URL = "http://jsjxy.cug.edu.cn/";
    public static final String COLLEGE_TYPE_JSJ = "TYPE_JSJ";
    public static final String JSJ_INDEX_URL = "http://jsjxy.cug.edu.cn/xyxw/xwdt.htm";
    public static final String JSJ_NOTICE_URL ="http://jsjxy.cug.edu.cn/tzgg/tzgg.htm" ;
    public static final String DK_BASE_URL = "http://dxy.cug.edu.cn/";
    public static final String COLLEGE_TYPE_DK = "TYPE_DK";
    public static final String DK_INDEX_URL = "http://dxy.cug.edu.cn/xyxw/xyxw.htm";
    public static final String DK_NOTICE_URL = "http://dxy.cug.edu.cn/xyxw/xygg.htm";
    public static final String DK_SCIENCE_URL = "http://dxy.cug.edu.cn/xkjs/xsdt.htm";
    public static final String WY_BASE_URL = "http://wyxy.cug.edu.cn/";
//    http://wyxy.cug.edu.cn/E_Type.asp?E_typeid=7
    public static final String WY_INDEX_URL = "http://wyxy.cug.edu.cn/E_Type.asp?E_typeid=7";
    public static final String COLLEGE_TYPE_WY = "TYPE_WY";
    public static final String WY_SCIENCE_URL = "http://wyxy.cug.edu.cn/E_Type.asp?E_typeid=26";
//    http://wyxy.cug.edu.cn/E_BigClass.asp?E_typeid=27&E_BigClassID=73
    public static final String WY_NOTICE_URL = "http://wyxy.cug.edu.cn/E_BigClass.asp?E_typeid=27&E_BigClassID=73";
    public static final String DY_BASE_URL = "http://lsgxy.cug.edu.cn/";
    public static final String COLLEGE_TYPE_DY = "TYPE_DY";
    public static final String DY_INDEX_URL = "http://lsgxy.cug.edu.cn/xydt.htm";
    public static final String DY_NOTICE_URL = "http://lsgxy.cug.edu.cn/tzgg.htm";
    public static final String DY_PUBLIC_URL = "http://lsgxy.cug.edu.cn/xxgs.htm";
    public static final String XY_BASE_URL = "http://xgxy.cug.edu.cn/";
    public static final String COLLEGE_TYPE_XY = "TYPE_XY";
    public static final String XY_INDEX_URL = "http://xgxy.cug.edu.cn/xyxw.htm";
    public static final String XY_SCIENCE_URL = "http://xgxy.cug.edu.cn/kyxsdt.htm";
    public static final String XY_TECH_URL = "http://xgxy.cug.edu.cn/bkjxxx.htm";
    public static final String XY_GRADUATE_URL = "http://xgxy.cug.edu.cn/yjsglxx.htm";
    public static final String XY_JOB_URL = "http://xgxy.cug.edu.cn/zsjyxx.htm";
    public static final String XY_STUDENT_URL = "http://xgxy.cug.edu.cn/xsgzdt.htm";
    public static final String ZDH_BASE_URL = "http://au.cug.edu.cn/";
    public static final String ZDH_INDEX_URL ="http://au.cug.edu.cn/xyxw.htm" ;
    public static final String COLLEGE_TYPE_ZDH = "TYPE_ZDH";
    public static final String ZDH_NOTICE_URL = "http://au.cug.edu.cn/xygg.htm";
    public static final String ZDH_SCIENCE_URL = "http://au.cug.edu.cn/xsdt.htm";
    public static final String ZDH_STUDENT_URL = "http://au.cug.edu.cn/sybksjy.htm";
    public static final String ZDH_GRADUATE_URL = "http://au.cug.edu.cn/syyjsjy.htm";
    public static final String ZDH_JOB_URL = "http://au.cug.edu.cn/syyjsjy.htm";
    public static final String ZY_BASE_URL = "http://zyxy.cug.edu.cn/";
    public static final String ZY_INDEX_URL = "http://zyxy.cug.edu.cn/xyxw/xyxw.htm";
    public static final String COLLEGE_TYPE_ZY = "TYPE_ZY";
    public static final String ZY_SCIENCE_URL = "http://zyxy.cug.edu.cn/kxyj/xsdt.htm";
    public static final String ZY_STUDENT_NEWS_URL = "http://zyxy.cug.edu.cn/xsgz1/xsdt.htm";
    public static final String ZY_STUDENT_URL = "http://zyxy.cug.edu.cn/rcpy1/bksjy/bkspy.htm";
    public static final String ZY_GRADUATE_URL = "http://zyxy.cug.edu.cn/rcpy1/yjsjy/yjspy.htm";
    public static final String CH_BASE_URL = "http://chxy.cug.edu.cn/";
    public static final String CH_INDEX_URL = "http://chxy.cug.edu.cn/xwzx/xyxw.htm";
    public static final String COLLEGE_TYPE_CH = "TYPE_CH";
    public static final String CH_NOTICE_URL = "http://chxy.cug.edu.cn/xwzx/tzgg.htm";
    public static final String CH_SCIENCE_URL = "http://chxy.cug.edu.cn/kxyj/kydt.htm";
    public static final String COLLEGE_TYPE_GC = "TYPE_GC";
    public static final String GC_INDEX_URL = "http://gccug.com/index.php?m=content&c=index&a=lists&catid=32";
    public static final String GC_NOTICE_URL = "http://gccug.com/index.php?m=content&c=index&a=lists&catid=33";
    public static final String GC_WORK_URL = "http://gccug.com/index.php?m=content&c=index&a=lists&catid=34";
    public static final String GC_BASE_URL = "http://gccug.com/";
    public static final String COLLEGE_TYPE_HJ = "TYPE_HJ";
    public static final String HJ_INDEX_URL = "http://sescug.com/index.php?m=content&c=index&a=lists&catid=65";
    public static final String HJ_SCIENCE_URL = "http://sescug.com/index.php?m=content&c=index&a=lists&catid=66";
    public static final String HJ_STUDENT_URL = "http://sescug.com/index.php?m=content&c=index&a=lists&catid=68";
    public static final String HJ_GRADUATE_URL = "http://sescug.com/index.php?m=content&c=index&a=lists&catid=67";
    public static final String HJ_STUDENT_WORK_URL = "http://sescug.com/index.php?m=content&c=index&a=lists&catid=69";
    public static final String HJ_BASE_URL = "http://sescug.com/";
    public static final String COLLEGE_TYPE_DWK = "TYPE_DWK";
    public static final String DWK_INDEX_URL = "http://dkxy.cug.edu.cn/xydt/xyxw.htm";
    public static final String DWK_NOTICE_URL = "http://dkxy.cug.edu.cn/xydt/tzgg.htm";
    public static final String DWK_SCIENCE_URL = "http://dkxy.cug.edu.cn/xsky/xsdt.htm";
    public static final String DWK_BASE_URL = "http://dkxy.cug.edu.cn/";
    public static final String JD_BASE_URL = "http://jidian.cug.edu.cn/";
    public static final String COLLEGE_TYPE_JD = "TYPE_JD";
    public static final String JD_INDEX_URL = "http://jidian.cug.edu.cn/index/xyxw.htm";
    public static final String JD_NOTICE_URL = "http://jidian.cug.edu.cn/index/tzgg.htm";
    public static final String JD_SCIENCE_URL = "http://jidian.cug.edu.cn/index/xsdt.htm";
    public static final String COLLEGE_TYPE_HY = "TYPE_HY";
    public static final String HY_INDEX_URL = "http://cmst.cug.edu.cn/category/college-news";
    public static final String HY_NOTICE_URL = "http://cmst.cug.edu.cn/category/notice";
    public static final String HY_SCIENCE_URL = "http://cmst.cug.edu.cn/category/academic-trends";
    public static final String HY_STUDENT_URL = "http://cmst.cug.edu.cn/category/undergraduate";
    public static final String HY_GRADUATE_URL = "http://cmst.cug.edu.cn/category/postgraduate";
    public static final String HY_STUDENT_WORK_URL = "http://cmst.cug.edu.cn/category/xue-sheng-gong-zuo";
    public static final String HY_BASE_URL = "http://cmst.cug.edu.cn/";
    public static final String COLLEGE_TYPE_SL = "TYPE_SL";
    public static final String SL_INDEX_URL = "http://slxy.cug.edu.cn/xygk1/xyxw.htm";
    public static final String SL_NOTICE_URL = "http://slxy.cug.edu.cn/xygk1/tzgg.htm";
    public static final String SL_SCIENCE_URL = "http://slxy.cug.edu.cn/xygk1/xsdt.htm";
    public static final String SL_TECH_URL = "http://slxy.cug.edu.cn/xygk1/jyjx.htm";
    public static final String SL_BASE_URL = "http://slxy.cug.edu.cn/";
    public static final String COLLEGE_TYPE_YM = "TYPE_YM";
    public static final String YM_INDEX_URL = "http://sac.cug.edu.cn/channels/71.html";
    public static final String YM_BASE_URL = "http://sac.cug.edu.cn/";
    public static final String YM_NOTICE_URL = "http://sac.cug.edu.cn/channels/33.html";
    public static final String YM_STUDENT_WORK_URL = "http://sac.cug.edu.cn/channels/84.html";


    public static String getRealNewsUrl(String url,int totalPage, int currentNum) {
        if (totalPage > 0) {
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(url.substring(0,url.lastIndexOf(".")))
                    .append("/").append(totalPage - currentNum + 1).append(".htm");
            return stringBuilder.toString();
        }
        return url;
    }



    public static String getSearchLibraryUrl(String text,int page,int num){
        return CUG_LIBRARY.replace("java",text).replace("page=2","page="+page).replace("displaypg=20","displaypg="+num);
    }



    public static String getRealSearchLibraryUrl(String url) {
        if (url != null && !url.startsWith("http")) {
            return "http://202.114.202.207/opac/"+url;
        }
        return url;
    }

    public static String getVerifyUrl(String account, String password, String verify) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("http://202.114.202.207/reader/redr_verify.php?").append("number=").append(account)
        .append("&").append("passwd=").append(password).append("&")
        .append("captcha=").append(verify).append("&").append("select=cert_no&returnUrl=");
        return stringBuilder.toString();
    }


    public static String getRealBorrowLibraryUrl(String url) {
        if (url != null && !url.startsWith("http")) {
            return "http://202.114.202.207/"+url;
        }
        return url;
    }

    public static String getCardVerifyImageUrl() {
        return CARD_VERIFY_IMAGE_URL+"?time="+new Date().getTime();
    }





    public static String getEncodePassWord(String pw) {
        if (pw == null) {
            return null;
        }
        return Base64.encodeToString(pw.getBytes(),Base64.NO_WRAP);
    }

    public static RequestBody getLoginRequestBody(String account, String pw, String verifyCode) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("sno=").append(account).append("&pwd=").append(getEncodePassWord(pw))
                .append("&ValiCode=").append(verifyCode).append("&remember=0&uclass=1&json=true");
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"),stringBuilder.toString());
        return requestBody;
    }



    public static RequestBody getPageRequestBody(){
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),"flowID=1&type=0&apptype=&Url=&MenuName=&EMenuName=&parm11=&parm22=&comeapp=&headnames=&freamenames=&shownamess=");
    return requestBody;
    }



    public static RequestBody getBankAccountRequestBody(){
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"),"json=true");
        return requestBody;
    }


//account=99225&acctype=%23%23%23&tranamt=1000&qpwd=MDQxNjMz&json=true
    public static RequestBody getPayRequestBody(String money) {
        StringBuilder stringBuilder=new StringBuilder();
        String account= BaseApplication.getAppComponent().getSharedPreferences()
                .getString(NewsUtil.ACCOUNT,null);
       int result=Integer.valueOf(money);
        result=result*100;
        String password=getEncodePassWord(BaseApplication.getAppComponent().getSharedPreferences()
        .getString(PW,null));
        stringBuilder.append("account=")
                .append(account).append("&acctype=%23%23%23")
                .append("&tranamt=").append(result+"")
                .append("&qpwd=").append(getEncodePassWord(password))
                .append("&json=true");
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8")
                ,stringBuilder.toString());
        return requestBody;
    }



//    sdate=2017-08-22&edate=2017-09-21&account=99225&page=1&rows=15
    public static RequestBody getPayHistoryRequestBody(int page,int num) {
        StringBuilder stringBuilder=new StringBuilder();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        stringBuilder.append("sdate=")
                .append(simpleDateFormat.format(new Date(System.currentTimeMillis()-ONE_MONTH)))
                .append("&edate=").append(simpleDateFormat.format(new Date()))
                .append("&account=").append(BaseApplication.getAppComponent().getSharedPreferences()
        .getString(ACCOUNT,null)).append("&page=").append(page)
                .append("&rows=").append(num);
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8")
        ,stringBuilder.toString());
        return requestBody;
    }


    private static final long ONE_MONTH=1000*60*60*24*30L;



//    http://202.114.202.207/newbook/newbook_cls_book.php?back_days=30&s_doctype=01&loca_code=00201&cls=ALL&clsname=%E5%85%A8%E9%83%A8%E6%96%B0%E4%B9%A6
    public static String getSearchNewBookUrl(String time, String type, String place,String className,int num) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("http://202.114.202.207/newbook/newbook_cls_book.php?")
                .append("back_days=").append(time)
                .append("&s_doctype=").append(type)
                .append("&loca_code=").append(place)
                .append("&cls=").append(className.split("/")[1])
                .append("&clsname=").append(className.split("/")[0])
                .append("&page=").append(num);
        return stringBuilder.toString();
    }


//    http://202.114.202.207/opac/ajax_lend_avl.php?marc_no=0000764801&time=1506166430560
    public static String getNewsBookNumberUrl(String id) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("http://202.114.202.207/opac/ajax_lend_avl.php?")
                .append("marc_no=").append(id)
                .append("&time=").append(new Date().getTime());
        return stringBuilder.toString();

    }



//    http://gank.io/api/data/福利/10/{page}
    public static String getPhotoListUrl(int size,int num) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("http://gank.io/api/data/福利/").append(size)
                .append("/").append(num);
        return stringBuilder.toString();
    }

    public static String getCollegeNewsUrl(String url, int totalPage, int num) {
        StringBuilder builder=new StringBuilder();
        if (url.startsWith(JG_BASE_URL)) {
            builder.append(url).append("&Curpage=").append(num);
        } else if (url.startsWith(GG_BASE_URL)
                ||url.startsWith(GC_BASE_URL)
                ||url.startsWith(HJ_BASE_URL)) {
//            ggxy2014/?sort=31
            builder.append(url).append("&page=").append(num);
        } else if (url.startsWith(JSJ_BASE_URL)||url.startsWith(DK_BASE_URL)
                ||url.startsWith(DY_BASE_URL)
                ||url.startsWith(XY_BASE_URL)
                ||url.startsWith(ZDH_BASE_URL)
                ||url.startsWith(ZY_BASE_URL)
                ||url.startsWith(CH_BASE_URL)
                ||url.startsWith(DWK_BASE_URL)
                ||url.startsWith(JD_BASE_URL)
                ||url.startsWith(SL_BASE_URL)) {
            if (totalPage > 0) {
                builder.append(url.substring(0,url.lastIndexOf(".")))
                        .append("/").append(totalPage - num + 1).append(".htm");
                return builder.toString();
            }
        } else if (url.startsWith(HY_BASE_URL)) {
                builder.append(url).append("/").append(num);
                return builder.toString();
        } else if (url.startsWith(YM_BASE_URL)) {
            builder.append(url.substring(0,url.lastIndexOf("."))).append("_").append(num)
                    .append(".htm");
            return builder.toString();
        }
        return builder.toString();
    }







    public static String getRealUrl(String href,String baseUrl) {
        if (href != null) {
            if (href.startsWith("http")) {
                return href;
            }else {
                return baseUrl+href;
            }
        }
        return null;
    }

    public static String getDKPage(String url) {
        if (url.startsWith(DK_INDEX_URL)) {
            return "fanye177091";
        } else {
            return "fanye176991";
        }
    }

    public static String getBaseUrl(String url) {
        if (url.startsWith(CUG_INDEX)) {
            return CUG_INDEX;
        } else if (url.startsWith(DK_BASE_URL)) {
            return DK_BASE_URL;
        } else if (url.startsWith(JG_BASE_URL)) {
            return JG_BASE_URL;
        }else if (url.startsWith(GG_BASE_URL)){
            return GG_BASE_URL;
        } else if (url.startsWith(JSJ_BASE_URL)) {
            return JSJ_BASE_URL;
        }else if (url.startsWith(WY_BASE_URL)){
            return WY_BASE_URL;
        }else if (url.startsWith(DY_BASE_URL)){
            return DY_BASE_URL;
        }else if (url.startsWith(XY_BASE_URL)){
            return XY_BASE_URL;
        }else if (url.startsWith(ZDH_BASE_URL)){
            return ZDH_BASE_URL;
        }else if (url.startsWith(ZY_BASE_URL)){
            return ZY_BASE_URL+"sy.htm";
        }else if (url.startsWith(CH_BASE_URL)){
            return CH_BASE_URL;
        }else if (url.startsWith(GC_BASE_URL)){
            return GC_BASE_URL;
        }else if (url.startsWith(HJ_BASE_URL)){
            return HJ_BASE_URL;
        }else if (url.startsWith(DWK_BASE_URL)){
            return DWK_BASE_URL;
        }else if (url.startsWith(JD_BASE_URL)){
            return JD_BASE_URL;
        }else if (url.startsWith(HY_BASE_URL)){
            return HY_BASE_URL;
        }else if (url.startsWith(SL_BASE_URL)){
            return SL_BASE_URL;
        }else if (url.startsWith(YM_BASE_URL)){
            return YM_BASE_URL;
        }else {
            return null;
        }
    }

    public static String getZYPage(String url) {
        if (url.equals(ZY_STUDENT_URL)
                || url.equals(ZY_GRADUATE_URL)) {
            return "fanye193939";
        }else {
            return "fanye193184";
        }
    }
}
