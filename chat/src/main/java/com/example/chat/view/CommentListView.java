package com.example.chat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.bean.User;
import com.example.chat.manager.UserCacheManager;
import com.example.chat.manager.UserManager;
import com.example.chat.util.CommonUtils;
import com.example.chat.util.FaceTextUtil;
import com.example.chat.util.LogUtil;

import java.util.List;



/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/4      17:28
 * QQ:             1981367757
 */

public class CommentListView extends LinearLayout {
        private int defaultColor;
        private int selectedColor;
        private LayoutParams params;

        public CommentListView(Context context) {
                this(context, null);
        }

        public CommentListView(Context context, AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public CommentListView(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommentListView);
                defaultColor = typedArray.getColor(R.styleable.CommentListView_comment_default_bg_color, getResources().getColor(R.color.comment_text_default_bg_color));
                selectedColor = typedArray.getColor(R.styleable.CommentListView_comment_click_bg_color, getResources().getColor(R.color.comment_item_pressed));
                typedArray.recycle();
        }


        private CommentItemClickListener mCommentItemClickListener;

        public void setCommentItemClickListener(CommentItemClickListener commentItemClickListener) {
                mCommentItemClickListener = commentItemClickListener;
        }

        public interface CommentItemClickListener {
                void onCommentItemClick(View view, int position, String replyUid);

                void onCommentItemLongClick(int position);

                void onCommentItemNameClick(String uid);

        }


        public void bindData(List<String> list) {
                if (getChildCount() > 0) {
                        LogUtil.e("移除所有的view");
                        removeAllViews();
                }
                if (list == null || list.size() == 0) {

                        LogUtil.e("绑定的评论数据为空!!!");
                        return;
                }
                params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                for (int i = 0; i < list.size(); i++) {
                        if (isFriendComment(list.get(i))) {
                                addView(getView(list.get(i), i), params);
                        }
                }
        }

        private boolean isFriendComment(String commentMsg) {
                final List<String> list = CommonUtils.content2List(commentMsg);
                LogUtil.e("内容" + commentMsg);
                User user = null;
                if (list != null) {
//                        User replyUser = null;
                        if (list.size() == 3) {
                                LogUtil.e("是回复评论");
                                String uid = list.get(0);
                                if (uid.equals(UserManager.getInstance().getCurrentUserObjectId())) {
                                        user = UserManager.getInstance().getCurrentUser();
                                } else {
                                        user = UserCacheManager.getInstance().getUser(uid);
                                }
//                                String replyUid = list.get(1);
//                                if (replyUid.equals(UserManager.getInstance().getCurrentUserObjectId())) {
//                                        replyUser = UserManager.getInstance().getCurrentUser();
//                                } else {
//                                        replyUser = UserCacheManager.getInstance().getUser(replyUid);
//                                }
                        } else if (list.size() == 2) {
                                LogUtil.e("单一评论");
                                String uid = list.get(0);
                                if (uid.equals(UserManager.getInstance().getCurrentUserObjectId())) {
                                        user = UserManager.getInstance().getCurrentUser();
                                } else {
                                        user = UserCacheManager.getInstance().getUser(uid);
                                }
                        }
                }
                return user!=null;
        }


        private View getView(final String commentMessage, final int position) {
                TextView textView = new TextView(getContext());
                textView.setPadding(3, 2, 3, 2);
//                textView.setBackground(getResources().getDrawable(R.drawable.comment_bg_selector));
                textView.setTextColor(getResources().getColor(R.color.base_color_text_grey));
                final CustomMoveMethod customMoveMethod;
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                final List<String> list = CommonUtils.content2List(commentMessage);
                LogUtil.e("内容" + commentMessage);
                if (list != null) {
                        User user = null;
                        User replyUser = null;
                        if (list.size() == 3) {
                                LogUtil.e("是回复评论");
                                String uid = list.get(0);
                                if (uid.equals(UserManager.getInstance().getCurrentUserObjectId())) {
                                        user = UserManager.getInstance().getCurrentUser();
                                } else {
                                        user = UserCacheManager.getInstance().getUser(uid);
                                }
                                String replyUid = list.get(1);
                                if (replyUid.equals(UserManager.getInstance().getCurrentUserObjectId())) {
                                        replyUser = UserManager.getInstance().getCurrentUser();
                                } else {
                                        replyUser = UserCacheManager.getInstance().getUser(replyUid);
                                }
                        } else if (list.size() == 2) {
                                LogUtil.e("单一评论");
                                String uid = list.get(0);
                                if (uid.equals(UserManager.getInstance().getCurrentUserObjectId())) {
                                        user = UserManager.getInstance().getCurrentUser();
                                } else {
                                        user = UserCacheManager.getInstance().getUser(uid);
                                }
                        }
//                        这里得到的用户有可能为空，因为好友的好友我有可能没加,所有不添加，只有好友的评论才可见，非好友评论不可见
                        if (user != null) {
                                String name = user.getNick() != null && !user.getNick().equals("") ? user.getNick() : user.getUsername();
                                spannableStringBuilder.append(getClickableSpan(name, user.getObjectId()));
                                if (replyUser != null) {
                                        LogUtil.e("该评论是回复评论");
                                        spannableStringBuilder.append(" 回复 ");
                                        String replyName = replyUser.getNick() != null && !replyUser.getNick().equals("") ? replyUser.getNick() : replyUser.getUsername();
                                        spannableStringBuilder.append(getClickableSpan(replyName, replyUser.getObjectId()));
                                }
                                spannableStringBuilder.append("：");
                                spannableStringBuilder.append(FaceTextUtil.toSpannableString(getContext(), list.get(list.size() - 1)));
                                textView.setMovementMethod(customMoveMethod = new CustomMoveMethod(selectedColor, defaultColor));
                                textView.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                                if (customMoveMethod.isClickTextView()) {
                                                        if (mCommentItemClickListener != null) {
                                                                mCommentItemClickListener.onCommentItemClick(v, position, CommonUtils.content2List(commentMessage).get(0));
                                                        }
                                                }
                                        }
                                });
                                textView.setOnLongClickListener(new OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View v) {
                                                if (customMoveMethod.isClickTextView()) {
                                                        v.setBackgroundColor(Color.parseColor("#00000000"));
                                                        if (mCommentItemClickListener != null) {
                                                                mCommentItemClickListener.onCommentItemLongClick(position);
                                                        }
                                                        return true;
                                                }
                                                return false;
                                        }
                                });
                                textView.setText(spannableStringBuilder);
                        }
                }
                return textView;
        }

        private SpannableString getClickableSpan(final String name, final String uid) {
                SpannableString spannableString = new SpannableString(name);
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#232121")), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                                if (mCommentItemClickListener != null) {
                                        LogUtil.e("点击了评论的belong用户");
                                        mCommentItemClickListener.onCommentItemNameClick(uid);
                                }
                        }
                }, 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return spannableString;
        }
}
