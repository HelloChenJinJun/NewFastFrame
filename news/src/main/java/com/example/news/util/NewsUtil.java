package com.example.news.util;

import android.util.Base64;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.news.bean.ConsumeRequestBean;
import com.example.news.bean.ScoreRequestJson;
import com.example.news.bean.SystemUserRequestBean;
import com.example.commonlibrary.rxbus.event.UserInfoEvent;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public static final String CUG_TECHNOLOGY = "http://www.cug.edu.cn/index/xsdt.htm";
    public static final String URL = "url";
    public static final String TITLE = "title";

    public static final String CUG_LIBRARY = "http://202.114.202.207/opac/openlink.php?location=ALL&title=java&doctype=ALL&lang_code=ALL&match_flag=forward&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=no&with_ebook=&page=2";
    public static final String LIBRARY_LOGIN = "http://202.114.202.207/reader/login.php";
    public static final String LIBRARY_BORROW_BOOK_LIST_URL = "http://202.114.202.207/reader/book_lst.php";
    public static final String LIBRARY_BORROW_BOOK_HISTORY_LIST_URL = "http://202.114.202.207/reader/book_hist.php";
    public static final String LIBRARY_COOKIE = "library_cookie";
    public static final String LIBRARY_BERIFY_IMAGE_URL = "http://202.114.202.207/reader/captcha.php";
    //  http://202.114.202.207/
    public static final String BASE_URL = "http://c.3g.163.com/";
    public static final String CARD_LOGIN_URL = "http://card.cug.edu.cn/";
    public static final String CARD_LOGIN_COOKIE = "card_cookie";
    public static final String CARD_VERIFY_IMAGE_URL = "http://card.cug.edu.cn/Login/GetValidateCode";
    public static final String CARD_POST_LOGIN_URL = "http://card.cug.edu.cn/Login/LoginBySnoQuery";
    public static final String CARD_POST_LOGIN_COOKIE = "card_post_login_cookie";
    public static final String CARD_PAGE_INFO_URL = "http://card.cug.edu.cn/Page/page";
    public static final String CARD_BANK_INFO_URL = "http://card.cug.edu.cn/User/GetCardInfoByAccountNoParm";
    public static final String PW = "password";
    public static final String ACCOUNT = "news_account";
    public static final String PAY_URL = "http://card.cug.edu.cn/User/Account_Pay";
    public static final String PAY_HISTORY_URL = "http://card.cug.edu.cn/Report/GetPersonTrjn";
    public static final String ERROR_INFO = "error_info";
    public static final String PHOTO_SET = "photoset";
    public static final String SPECIAL_TITLE = "special";
    public static final String SPECIAL_ID = "special_id";
    public static final String POST_ID = "post_id";
    public static final String PHOTO_SET_ID = "photo_set_id";
    public static final String JG_INDEX_URL = "http://jgxy.cug.edu.cn/xwdt/xyxw.htm";
    public static final String JG_NOTICE_URL = "http://jgxy.cug.edu.cn/xwdt/tzgg.htm";
    public static final String JG_SCIENCE_URL = "http://jgxy.cug.edu.cn/kxyj/xsdt.htm";
    public static final String JG_BASE_URL = "http://jgxy.cug.edu.cn/";
    public static final String JG_COOKIE = "jg_cookie";
    public static final String COLLEGE_TYPE_JG = "TYPE_JG";
    public static final String COLLEGE_TYPE_DD = "TYPE_DD";
    public static final String COLLEGE_TYPE = "college_type";
    //    http://ggxy.cug.edu.cn/ggxy2014/?sort=31
    public static final String GG_BASE_URL = "http://ggxy.cug.edu.cn/";
    public static final String COLLEGE_TYPE_GG = "TYPE_GG";
    public static final String GG_INDEX_URL = "http://ggxy.cug.edu.cn/xyxw.htm";
    public static final String GG_NOTICE_URL = "http://ggxy.cug.edu.cn/tzgg.htm";
    public static final String GG_SCIENCE_URL = "http://ggxy.cug.edu.cn/xsdt.htm";
    public static final String JSJ_BASE_URL = "http://cs.cug.edu.cn/";
    public static final String COLLEGE_TYPE_JSJ = "TYPE_JSJ";
    public static final String JSJ_INDEX_URL = "http://cs.cug.edu.cn/list-37.html";
    public static final String JSJ_NOTICE_URL = "http://cs.cug.edu.cn/list-16.html";
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
    public static final String ZDH_INDEX_URL = "http://au.cug.edu.cn/xyxw.htm";
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
    public static final String HY_INDEX_URL = "http://cmst.cug.edu.cn/xwzx.htm";
    public static final String HY_NOTICE_URL = "http://cmst.cug.edu.cn/xwzx/tzgg.htm";
    public static final String HY_SCIENCE_URL = "http://cmst.cug.edu.cn/xwzx/xsdt.htm";
    //    public static final String HY_STUDENT_URL = "http://cmst.cug.edu.cn/category/undergraduate";
//    public static final String HY_GRADUATE_URL = "http://cmst.cug.edu.cn/category/postgraduate";
//    public static final String HY_STUDENT_WORK_URL = "http://cmst.cug.edu.cn/category/xue-sheng-gong-zuo";
    public static final String HY_BASE_URL = "http://cmst.cug.edu.cn/";
    public static final String COLLEGE_TYPE_SL = "TYPE_SL";
    public static final String SL_INDEX_URL = "http://slxy.cug.edu.cn/xygk1/xyxw.htm";
    public static final String SL_NOTICE_URL = "http://slxy.cug.edu.cn/xygk1/tzgg.htm";
    public static final String SL_SCIENCE_URL = "http://slxy.cug.edu.cn/xygk1/xsdt.htm";
    public static final String SL_TECH_URL = "http://slxy.cug.edu.cn/xygk1/jyjx.htm";
    public static final String SL_BASE_URL = "http://slxy.cug.edu.cn/";
    public static final String COLLEGE_TYPE_YM = "TYPE_YM";
    public static final String YM_INDEX_URL = "http://sac.cug.edu.cn/index/xyxw.htm";
    public static final String YM_BASE_URL = "http://sac.cug.edu.cn/";
    public static final String YM_NOTICE_URL = "http://sac.cug.edu.cn/index/xxgg.htm";
    public static final String YM_STUDENT_WORK_URL = "http://sac.cug.edu.cn/index/xshd.htm";
    public static final String COLLEGE_TYPE_MY = "TYPE_MY";
    public static final String MY_INDEX_URL = "http://mkszyxy.cug.edu.cn/xyxw.htm";
    public static final String MY_NOTICE_URL = "http://mkszyxy.cug.edu.cn/tzgg.htm";
    public static final String MY_SCIENCE_URL = "http://mkszyxy.cug.edu.cn/kxyj/xsdt.htm";
    public static final String MY_STUDENT_URL = "http://mkszyxy.cug.edu.cn/rcpy/bkspy.htm";
    public static final String MY_GRADUATE_URL = "http://mkszyxy.cug.edu.cn/rcpy/yjspy.htm";
    public static final String MY_BASE_URL = "http://mkszyxy.cug.edu.cn/";
    public static final String CARD_POST_LOGIN_COOKIE_USER_NAME = "CARD_POST_LOGIN_USER_NAME";
    public static final String SYSTEM_INFO_COOKIE = "system_info_cookie";
    public static final String SYSTEM_INFO_INDEX_URL = "http://sfrz.cug.edu.cn/tpass/login?service=http%3A%2F%2Fxyfw.cug.edu.cn%2Ftp_up%2F";
    public static final String SYSTEM_INFO_LOGIN_LT = "system_info_login_lt";
    public static final String STSTEM_INFO_CASTGC = "system_info_castgc";
    public static final String SYSTEM_INFO_GET_TICKET = "system_info_get_ticket";
    public static final String SYSTEM_INFO_TP_UP = "system_info_tp_up";
    public static final String SCORE_QUERY_URL = "http://xyfw.cug.edu.cn/tp_up/up/sysintegration/findUserCourseScore";
    public static final String COLLEGE_TYPE_VOICE = "TYPE_VOICE";
    public static final String CUG_VOICE_INDEX = "http://voice.cug.edu.cn/zhxw.htm";
    public static final String VOICE_BASE_URL = "http://voice.cug.edu.cn/";
    public static final String CUG_VOICE_NOTIFY = "http://voice.cug.edu.cn/ggtz.htm";
    public static final String CUG_VOICE_IMAGE = "http://voice.cug.edu.cn/tpxw.htm";
    public static final String IS_LOGIN = "is_login";
    public static final String CONSUME_QUERY_URL = "http://xyfw.cug.edu.cn/tp_up/up/sysintegration/getCardConsumList";
    public static final String SYSTEM_USER_INFO_URL = "http://xyfw.cug.edu.cn/tp_up/sys/uacm/profile/getUserById";
    public static final String COURSE_VERIFY_ACCOUNT_URL = "http://sfrz.cug.edu.cn/tpass/login?service=http%3A%2F%2F202.114.207.137%3A80%2Fssoserver%2Flogin%3Fywxt%3Djw";
    public static final String COURSE_TICKET_URL = "course_ticket_url";
    public static final String COURSE_JSESSION_URL = "course_jsession_url";
    public static final String COURSE_REAL_VERIFY_URL = "course_real_verify_url";
    public static final String COURSE_JSESSION_ID = "course_jsession_id";
    public static final String COURSE_QUERY_URL = "http://jwgl.cug.edu.cn/jwglxt/kbcx/xskbcx_cxXsKb.html?gnmkdm=N2151";
    public static final String COURSE_TEMP_JS_ID = "course_temp_js_id";
    public static final String LIBRARY_BORROW_VERIFY_URL = "http://202.114.202.207/reader/captcha.php";
    public static final int REQUEST_CODE_ADJUST = 11;
    public static final String HEADER_AGENT = "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";
    public static final String CACHE_CONTROL = "Cache-Control: public, max-age=3600";
    public static final String PW_RESET_URL = "http://xyfw.cug.edu.cn/tp_up/sys/uacm/pwdreset/reset";





    public static String getSearchLibraryUrl(String text, int page, int num) {
        return CUG_LIBRARY.replace("java", text).replace("page=2", "page=" + page).replace("displaypg=20", "displaypg=" + num);
    }


    public static String getRealSearchLibraryUrl(String url) {
        if (url != null && !url.startsWith("http")) {
            return "http://202.114.202.207/opac/" + url;
        }
        return url;
    }

    public static String getVerifyUrl(String account, String password, String verify) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://202.114.202.207/reader/redr_verify.php?").append("number=").append(account)
                .append("&").append("passwd=").append(password).append("&")
                .append("captcha=").append(verify).append("&").append("select=cert_no&returnUrl=");
        return stringBuilder.toString();
    }


    public static String getRealBorrowLibraryUrl(String url) {
        if (url != null && !url.startsWith("http")) {
            return "http://202.114.202.207/" + url;
        }
        return url;
    }

    public static String getCardVerifyImageUrl() {
        return CARD_VERIFY_IMAGE_URL + "?time=" + new Date().getTime();
    }


    public static String getEncodePassWord(String pw) {
        if (pw == null) {
            return null;
        }
        return Base64.encodeToString(pw.getBytes(), Base64.NO_WRAP);
    }


    //    sno=20141000341&pwd=MDQxNjMz&ValiCode=62231&remember=1&uclass=1&zqcode=&json=true
    public static RequestBody getLoginRequestBody(String account, String pw, String verifyCode) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("sno=").append(account).append("&pwd=").append(getEncodePassWord(pw))
                .append("&ValiCode=").append(verifyCode).append("&remember=1&uclass=1&zqcode=&json=true");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), stringBuilder.toString());
        return requestBody;
    }


    public static RequestBody getPageRequestBody() {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), "flowID=1&type=0&apptype=&Url=&MenuName=&EMenuName=&parm11=&parm22=&comeapp=&headnames=&freamenames=&shownamess=");
        return requestBody;
    }


    public static RequestBody getBankAccountRequestBody() {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), "json=true");
        return requestBody;
    }


    //account=99225&acctype=%23%23%23&tranamt=1000&qpwd=MDQxNjMz&json=true
    public static RequestBody getPayRequestBody(String money) {
        StringBuilder stringBuilder = new StringBuilder();
        String account = BaseApplication.getAppComponent().getSharedPreferences()
                .getString(NewsUtil.ACCOUNT, null);
        int result = Integer.valueOf(money);
        result = result * 100;
        String password = getEncodePassWord(BaseApplication.getAppComponent().getSharedPreferences()
                .getString(PW, null));
        stringBuilder.append("account=")
                .append(account).append("&acctype=%23%23%23")
                .append("&tranamt=").append(result + "")
                .append("&qpwd=").append(getEncodePassWord(password))
                .append("&json=true");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8")
                , stringBuilder.toString());
        return requestBody;
    }


    //    sdate=2017-08-22&edate=2017-09-21&account=99225&page=1&rows=15
    public static RequestBody getPayHistoryRequestBody(int page, int num) {
        StringBuilder stringBuilder = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        stringBuilder.append("sdate=")
                .append(simpleDateFormat.format(new Date(System.currentTimeMillis() - ONE_MONTH)))
                .append("&edate=").append(simpleDateFormat.format(new Date()))
                .append("&account=").append(BaseApplication.getAppComponent().getSharedPreferences()
                .getString(ACCOUNT, null)).append("&page=").append(page)
                .append("&rows=").append(num);
        return RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8")
                , stringBuilder.toString());
    }


    private static final long ONE_MONTH = 1000 * 60 * 60 * 24 * 30L;


    //    http://202.114.202.207/newbook/newbook_cls_book.php?back_days=30&s_doctype=01&loca_code=00201&cls=ALL&clsname=%E5%85%A8%E9%83%A8%E6%96%B0%E4%B9%A6
    public static String getSearchNewBookUrl(String time, String type, String place, String className, int num) {
        StringBuilder stringBuilder = new StringBuilder();
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://202.114.202.207/opac/ajax_lend_avl.php?")
                .append("marc_no=").append(id)
                .append("&time=").append(new Date().getTime());
        return stringBuilder.toString();

    }


    //    http://gank.io/api/data/福利/10/{page}
    public static String getPhotoListUrl(int size, int num) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://gank.io/api/data/福利/").append(size)
                .append("/").append(num);
        return stringBuilder.toString();
    }

    public static String getCollegeNewsUrl(String url, int totalPage, int num) {
        StringBuilder builder = new StringBuilder();
        if (url.startsWith(GC_BASE_URL)
                || url.startsWith(HJ_BASE_URL)) {
//            ggxy2014/?sort=31
            builder.append(url).append("&page=").append(num);
        } else if (url.startsWith(JSJ_BASE_URL)) {
            builder.append(url).append("-").append(num);
        } else if (url.startsWith(DK_BASE_URL)
                || url.startsWith(DY_BASE_URL)
                || url.startsWith(XY_BASE_URL)
                || url.startsWith(ZDH_BASE_URL)
                || url.startsWith(ZY_BASE_URL)
                || url.startsWith(CH_BASE_URL)
                || url.startsWith(DWK_BASE_URL)
                || url.startsWith(JD_BASE_URL)
                || url.startsWith(SL_BASE_URL)
                || url.startsWith(HY_BASE_URL)
                || url.startsWith(MY_BASE_URL)
                || url.startsWith(JG_BASE_URL)
                || url.startsWith(GG_BASE_URL)
                || url.startsWith(HY_BASE_URL)
                || url.startsWith(CUG_INDEX)
                ||url.startsWith(YM_BASE_URL)
                || url.startsWith(VOICE_BASE_URL)) {
            if (totalPage > 0) {
                builder.append(url.substring(0, url.lastIndexOf(".")))
                        .append("/").append(totalPage - num + 1).append(".htm");
                return builder.toString();
            }
        }
        return builder.toString();
    }


    public static String getRealUrl(String href, String baseUrl) {
        if (href != null) {
            if (href.startsWith("http")) {
                return href;
            } else {
                return baseUrl + href;
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
        } else if (url.startsWith(GG_BASE_URL)) {
            return GG_BASE_URL;
        } else if (url.startsWith(JSJ_BASE_URL)) {
            return JSJ_BASE_URL;
        } else if (url.startsWith(WY_BASE_URL)) {
            return WY_BASE_URL;
        } else if (url.startsWith(DY_BASE_URL)) {
            return DY_BASE_URL;
        } else if (url.startsWith(XY_BASE_URL)) {
            return XY_BASE_URL;
        } else if (url.startsWith(ZDH_BASE_URL)) {
            return ZDH_BASE_URL;
        } else if (url.startsWith(ZY_BASE_URL)) {
            return ZY_BASE_URL + "sy.htm";
        } else if (url.startsWith(CH_BASE_URL)) {
            return CH_BASE_URL;
        } else if (url.startsWith(GC_BASE_URL)) {
            return GC_BASE_URL;
        } else if (url.startsWith(HJ_BASE_URL)) {
            return HJ_BASE_URL;
        } else if (url.startsWith(DWK_BASE_URL)) {
            return DWK_BASE_URL;
        } else if (url.startsWith(JD_BASE_URL)) {
            return JD_BASE_URL;
        } else if (url.startsWith(HY_BASE_URL)) {
            return HY_BASE_URL;
        } else if (url.startsWith(SL_BASE_URL)) {
            return SL_BASE_URL;
        } else if (url.startsWith(YM_BASE_URL)) {
            return YM_BASE_URL;
        } else if (url.startsWith(MY_BASE_URL)) {
            return MY_BASE_URL;
        } else if (url.startsWith(VOICE_BASE_URL)) {
            return VOICE_BASE_URL;
        } else {
            return null;
        }
    }

    public static String getZYPage(String url) {
        if (url.equals(ZY_STUDENT_URL)
                || url.equals(ZY_GRADUATE_URL)) {
            return "fanye193939";
        } else {
            return "fanye193184";
        }
    }

    public static String getRealLoginUrl(String cookie) {
        if (cookie == null) {
            return "";
        }
//        http://sfrz.cug.edu.cn/tpass/login;jsessionid=hyRjRKsMcW6JgBuQFEJ9h8ldLZlhAoBxRsUoVATePff_-GOVH9sn!-552142256?service=http%3A%2F%2Fxyfw.cug.edu.cn%2Ftp_up%2F
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://sfrz.cug.edu.cn/tpass/login;")
                .append(cookie.replace("JSESSIONID", "jsessionid")).append("?service=http%3A%2F%2Fxyfw.cug.edu.cn%2Ftp_up%2F");
        return stringBuilder.toString();
    }


    /*
    * rsa=0426ECAE0453A1B5938E1D54E79EA79BD1A4399C7F624041962DFA9DCD391CE1EA049D4A54499B6269A20DCA07FB9C08D5A28467F0DB6ABEF2FBDA251839014F2B7CBA490DE1F8F63D382967D02BDD76C9AF7511ED53EB23DA9A93D9C7C61C0991A92D42FB1A8AA95512C6937B185B300032B8F34281760C9668FE464C1DBD45&ul=11&pl=6&lt=LT-471811-NfuB5rxwAIogDMfpHHPXCLnJb7qc9X-tpass
    * &execution=e1s1&_eventId=submit
    *
    *
    * */

    public static RequestBody getSystemInfoRequestBody(String account, String pw, String lt) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("rsa=")
                .append(DesUtil.strEnc(account + pw + lt, "1", "2", "3"))
                .append("&ul=").append(account.length())
                .append("&pl=").append(pw.length()).append("&lt=")
                .append(lt).append("&execution=e1s1&_eventId=submit");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8")
                , stringBuilder.toString());
        return requestBody;
    }

    public static RequestBody getScoreQueryBody(String time, int page, int pageNum, String courseName) {
        ScoreRequestJson item = new ScoreRequestJson();
        item.setCoursename(courseName);
        item.setDraw(page);
        item.setPageSize(pageNum);
        item.setPageNum(page);
        item.setStart((page - 1) * pageNum);
        item.setLength(pageNum);
        ScoreRequestJson.OrderBean orderBean = new ScoreRequestJson.OrderBean();
        orderBean.setColumn(3);
        orderBean.setDir("desc");
        orderBean.setName("KCMC");
        List<ScoreRequestJson.OrderBean> list = new ArrayList<>();
        list.add(orderBean);
        item.setOrder(list);
        item.setXn(time);
        item.setMapping("getcourseScoreList");
        Gson gson = new Gson();
        String json = gson.toJson(item);
        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), json);
    }

    public static RequestBody getConsumeRequestBody(int page, int pageSize) {
        ConsumeRequestBean consumeRequestBean = new ConsumeRequestBean();
        ConsumeRequestBean.OrderBean orderBean = new ConsumeRequestBean.OrderBean();
        orderBean.setColumn(1);
        orderBean.setDir("desc");
        orderBean.setName("JYSJ");
        List<ConsumeRequestBean.OrderBean> list = new ArrayList<>();
        list.add(orderBean);
        consumeRequestBean.setOrder(list);
        consumeRequestBean.setDraw(page);
        consumeRequestBean.setPageNum(page);
        consumeRequestBean.setPageSize(pageSize);
        consumeRequestBean.setStart((page - 1) * pageSize);
        consumeRequestBean.setLength(pageSize);
        consumeRequestBean.setMapping("getCardListInfo");
        consumeRequestBean.setAppointTime("");
        consumeRequestBean.setStartDate("");
        consumeRequestBean.setEndDate("");
        consumeRequestBean.setDateSearch("");
        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(consumeRequestBean));
    }

    public static RequestBody getSystemUserRequestBody(String account) {
//        {"ID_NUMBER":"20141000111"}
        SystemUserRequestBean bean = new SystemUserRequestBean();
        bean.setID_NUMBER(account);
        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(bean));
    }

    public static void clearAllUserCache() {
//        清除并通知改变
        ToastUtils.showShortToast("清除并通知改变");
        UserInfoEvent userInfoEvent = new UserInfoEvent();
        BaseApplication.getAppComponent().getSharedPreferences()
                .edit().putBoolean(ConstantUtil.LOGIN_STATUS, false)
                .putString(ConstantUtil.ACCOUNT, userInfoEvent.getAccount())
                .putBoolean(ConstantUtil.FIRST_STATUS,true)
                .putString(ConstantUtil.PASSWORD, userInfoEvent.getPassword())
                .putString(ConstantUtil.AVATAR, userInfoEvent.getAvatar())
                .putString(ConstantUtil.NAME, userInfoEvent.getNick())
                .putBoolean(ConstantUtil.SEX, false)
                .putString(ConstantUtil.BG_HALF, userInfoEvent.getHalfBg())
                .putString(ConstantUtil.BG_ALL, userInfoEvent.getAllBg())
                .putString(ConstantUtil.NICK, userInfoEvent.getNick()).apply();
    }

    public static RequestBody getCourseQueryRequestBody(String year, int examNum) {
//        xnm=2016&xqm=3
        String stringBuilder = "xnm=" +
                year + "&xqm=" + examNum;
        return RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8")
                , stringBuilder);
    }

    public static String getBorrowBookUrl(String verify, String number, String check) {
//        http://202.114.202.207/reader/ajax_renew.php?bar_code=C1504828&check=3FE99B17&captcha=5607&time=1514371945828
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("http://202.114.202.207/reader/ajax_renew.php?bar_code=")
                .append(number)
                .append("&check=").append(check).append("&captcha=")
                .append(verify).append("&time=").append(System.currentTimeMillis());
        return stringBuilder.toString();
    }

    public static String getOtherUserInfoUrl(String account) {
//        http://jwgl.cug.edu.cn/jwglxt/xsxxxggl/xsgrxxwh_cxXsgrxx.html?gnmkdm=N100801&layout=default&su=

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("http://jwgl.cug.edu.cn/jwglxt/xsxxxggl/xsgrxxwh_cxXsgrxx.html?gnmkdm=N100801&layout=default&su=")
                .append(account);
        return stringBuilder.toString();
    }

    public static RequestBody getResetPwRequestBody(String old, String news) {
//        oldPwd=041633&newPwd=chen3929249
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("oldPwd=").append(old)
                .append("&newPwd=").append(news);
       return RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"),stringBuilder.toString());
    }

    public static String getTypeFromName(String college) {
        if (college == null) {
            return null;
        }
        if (college.equals("经济管理学院")) {
            return COLLEGE_TYPE_JG;
        } else if (college.equals("外国语学院")) {
                return COLLEGE_TYPE_WY;
        } else if (college.equals("艺术与传媒学院")) {
                return COLLEGE_TYPE_YM;
        } else if (college.equals("数学与物理学院")) {
            return COLLEGE_TYPE_SL;
        } else if (college.equals("海洋学院")) {
return COLLEGE_TYPE_HY;
        } else if (college.equals("机械与电子信息学院")) {
return COLLEGE_TYPE_JD;
        } else if (college.equals("地球物理与空间信息学院")) {
return COLLEGE_TYPE_DWK;
        } else if (college.equals("环境学院")) {
return COLLEGE_TYPE_HJ;
        } else if (college.equals("工程学院")) {
        return COLLEGE_TYPE_GC;
        } else if (college.equals("材料与化学学院")) {
return COLLEGE_TYPE_CH;
        } else if (college.equals("资源学院")) {
return COLLEGE_TYPE_ZY;
        } else if (college.equals("自动化学院")) {
return COLLEGE_TYPE_ZDH;
        } else if (college.equals("信息工程学院")) {
return COLLEGE_TYPE_XY;
        } else if (college.equals("地球科学学院")) {
return COLLEGE_TYPE_DK;
        } else if (college.equals("公共管理学院")) {
return COLLEGE_TYPE_GG;
        } else if (college.equals("计算机学院")) {
return COLLEGE_TYPE_JSJ;
        } else if (college.equals("李四光学院")) {
            return COLLEGE_TYPE_DY;
        } else if (college.equals("马克思主义学院")) {
            return COLLEGE_TYPE_MY;
        }else {
            return null;
        }
    }
}
