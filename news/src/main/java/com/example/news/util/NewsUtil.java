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

    public static String getHref(String href) {
        if (href != null && !href.startsWith("http")) {
            return CUG_INDEX + href;
        }
        return href;
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
}
