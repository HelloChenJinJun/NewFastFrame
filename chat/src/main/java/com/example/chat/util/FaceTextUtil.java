package com.example.chat.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.Browser;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;

import com.example.chat.bean.FaceText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/13      19:36
 * QQ:             1981367757
 */
public class FaceTextUtil {
        private static List<FaceText> faceTextList = new ArrayList<>();

        static {
                faceTextList.add(new FaceText("\\ue056"));
                faceTextList.add(new FaceText("\\ue057"));
                faceTextList.add(new FaceText("\\ue058"));
                faceTextList.add(new FaceText("\\ue059"));
                faceTextList.add(new FaceText("\\ue105"));
                faceTextList.add(new FaceText("\\ue106"));
                faceTextList.add(new FaceText("\\ue107"));
                faceTextList.add(new FaceText("\\ue108"));
                faceTextList.add(new FaceText("\\ue401"));
                faceTextList.add(new FaceText("\\ue402"));
                faceTextList.add(new FaceText("\\ue403"));
                faceTextList.add(new FaceText("\\ue404"));
                faceTextList.add(new FaceText("\\ue405"));
                faceTextList.add(new FaceText("\\ue406"));
                faceTextList.add(new FaceText("\\ue407"));
                faceTextList.add(new FaceText("\\ue408"));
                faceTextList.add(new FaceText("\\ue409"));
                faceTextList.add(new FaceText("\\ue40a"));
                faceTextList.add(new FaceText("\\ue40b"));
                faceTextList.add(new FaceText("\\ue40d"));
                faceTextList.add(new FaceText("\\ue40e"));
                faceTextList.add(new FaceText("\\ue40f"));
                faceTextList.add(new FaceText("\\ue410"));
                faceTextList.add(new FaceText("\\ue411"));
                faceTextList.add(new FaceText("\\ue412"));
                faceTextList.add(new FaceText("\\ue413"));
                faceTextList.add(new FaceText("\\ue414"));
                faceTextList.add(new FaceText("\\ue415"));
                faceTextList.add(new FaceText("\\ue416"));
                faceTextList.add(new FaceText("\\ue417"));
                faceTextList.add(new FaceText("\\ue418"));
                faceTextList.add(new FaceText("\\ue41f"));
                faceTextList.add(new FaceText("\\ue00e"));
                faceTextList.add(new FaceText("\\ue421"));
        }

        /**
         * 把聊天content中的表情文本转化为真实表情图像
         *
         * @param context
         * @param content
         * @return
         */
        public static CharSequence toSpannableString(Context context, String content) {
                if (!TextUtils.isEmpty(content)) {
                        SpannableString spannableString = new SpannableString(content);

//                        处理URL匹配
                        Pattern urlPattern = Pattern.compile("(http|https|ftp|svn)://([a-zA-Z0-9]+[/?.?])" +
                                "+[a-zA-Z0-9]*\\??([a-zA-Z0-9]*=[a-zA-Z0-9]*&?)*");
                        Matcher urlMatcher = urlPattern.matcher(content);
                        while (urlMatcher.find()) {
                                final String url = urlMatcher.group();
                                spannableString.setSpan(new ClickableSpan() {
                                        @Override
                                        public void onClick(View widget) {
                                                Uri uri = Uri.parse(url);
                                                Context context1 = widget.getContext();
                                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                intent.putExtra(Browser.EXTRA_APPLICATION_ID, context1.getPackageName());
                                                context1.startActivity(intent);
                                        }
                                }, urlMatcher.start(), urlMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
//                        处理表情匹配
                        int start = 0;
                        Pattern pattern = Pattern.compile("\\\\ue[a-z0-9]{3}", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(content);
                        while (matcher.find()) {
                                String faceText = matcher.group();
                                String key = faceText.substring(1);
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                LogUtil.e("mipmap");
                                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), context.getResources().getIdentifier(key, "mipmap", context.getPackageName()), options);
                                ImageSpan imageSpan = new ImageSpan(context, bitmap);
                                int startIndex = content.indexOf(faceText, start);
                                int endIndex = startIndex + faceText.length();
                                if (startIndex >= 0) {
                                        spannableString.setSpan(imageSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                                start = (endIndex - 1);
                        }
                        return spannableString;
                } else {
                        return new SpannableString("");
                }
        }

        public static List<FaceText> getFaceTextList() {
                return faceTextList;
        }
}
