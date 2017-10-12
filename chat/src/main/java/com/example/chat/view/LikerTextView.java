package com.example.chat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;

import com.example.chat.R;
import com.example.chat.bean.User;
import com.example.chat.manager.UserCacheManager;
import com.example.chat.manager.UserManager;

import java.util.List;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/4      17:25
 * QQ:             1981367757
 */

public class LikerTextView extends android.support.v7.widget.AppCompatTextView {
        private int clickBgColor;
        private int defaultBgColor;


        public interface LikerTextViewItemClickListener {
                void onItemTextClick(String uid);
        }


        private LikerTextViewItemClickListener listener;

        public void setLikerTextItemClickListener(LikerTextViewItemClickListener listener) {
                this.listener = listener;
        }

        public LikerTextView(Context context) {
                this(context, null);
        }

        public LikerTextView(Context context, AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public LikerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LikerTextView);
                clickBgColor = typedArray.getColor(R.styleable.LikerTextView_liker_click_bg_color, getResources().getColor(R.color.liker_click_default_bg_color));
                defaultBgColor = typedArray.getColor(R.styleable.LikerTextView_liker_default_bg_color, getResources().getColor(R.color.liker_default_bg_color));
                typedArray.recycle();
        }


        public void bindData(List<String> list) {
                setText(null);
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
//                添加点赞图标
                spannableStringBuilder.append(setImageSpan(list));
//                if (list == null || list.size() == 0) {
//                        LogUtil.e("传入的点赞item为空!!!!!!!");
//                        return;
//                }
                int size = list.size();
                for (int i = 0; i < size; i++) {
                        String uid=list.get(i);
                        if (!uid.equals(UserManager.getInstance().getCurrentUserObjectId())&& UserCacheManager.getInstance().getUser(uid)==null) {
//                                不是好友的点赞，不显示出来啊
                                continue;
                        }
                        spannableStringBuilder.append(getClickableSpan(uid));
                        if (i != size - 1) {
                                spannableStringBuilder.append(",");
                        }
                }
                setText(spannableStringBuilder);
//                这里设置点击的背景颜色和点击事件的传递
                setMovementMethod(new CustomMoveMethod(clickBgColor, defaultBgColor));
        }

        private SpannableString getClickableSpan(final String uid) {
                User user;
                if (uid.equals(UserManager.getInstance().getCurrentUserObjectId())) {
                        user = UserManager.getInstance().getCurrentUser();
                } else {
                        user = UserCacheManager.getInstance().getUser(uid);
                }
                SpannableString spannableString = new SpannableString(user.getUsername());
                spannableString.setSpan(new ClickableSpan() {

                                                @Override
                                                public void updateDrawState(TextPaint ds) {
                                                        super.updateDrawState(ds);
                                                        ds.setColor(defaultBgColor);
                                                        ds.setUnderlineText(false);
                                                        ds.clearShadowLayer();
                                                }

                                                @Override
                                                public void onClick(View widget) {
                                                        if (listener != null) {
                                                                listener.onItemTextClick(uid);
                                                        }
                                                }
                                        }
                        , 0, user.getUsername().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return spannableString;
        }

        private SpannableString setImageSpan(List<String> list) {
                SpannableString spannableString = new SpannableString("..");
                if (list != null && list.contains(UserManager.getInstance().getCurrentUser().getObjectId())) {
                        spannableString.setSpan(new ImageSpan(getContext(), R.drawable.ic_favorite_deep_orange_a700_24dp, DynamicDrawableSpan.ALIGN_BASELINE), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                        spannableString.setSpan(new ImageSpan(getContext(), R.drawable.ic_favorite_border_deep_orange_a700_24dp, DynamicDrawableSpan.ALIGN_BASELINE), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                return spannableString;
        }


}
