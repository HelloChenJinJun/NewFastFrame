package com.example.chat.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.util.LogUtil;
import com.example.chat.util.PixelUtil;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/5      21:09
 * QQ:             1981367757
 */

public class CommentPopupWindow extends PopupWindow implements View.OnClickListener {
        private TextView like;
        private TextView comment;
        private String likerName = "赞";
        private long mLasttime = 0;


        public CommentPopupWindow(Context context) {
                View view = LayoutInflater.from(context).inflate(R.layout.comment_popup_window_layout, null);
                like = (TextView) view.findViewById(R.id.tv_comment_popup_window_like);
                comment = (TextView) view.findViewById(R.id.tv_comment_popup_window_comment);
                setContentView(view);
                like.setOnClickListener(this);
                comment.setOnClickListener(this);
                setWidth(PixelUtil.todp(120));
                setHeight(PixelUtil.todp(40));
                setAnimationStyle(R.style.btn_comment_animation);
                update();
        }


        public void setLikerName(String likerName) {
                this.likerName = likerName;
        }

        public String getLikerName() {
                return likerName;
        }

        public void showPopupWindow(View view) {
                if (isShowing()) {
                        LogUtil.e("正在显示，所以关闭");
                        dismiss();
                } else {
                        like.setText(likerName);
                        int[] locations = new int[2];
//                        获取view在屏幕的位置
//                        location[0]为x
//                        location[1]为y
                        view.getLocationOnScreen(locations);
                        LogUtil.e("x=" + (locations[0] - getWidth()) + "   y=" + ((locations[1] - (getHeight() - view.getHeight()) / 2)));
                        showAtLocation(view, Gravity.NO_GRAVITY, locations[0] - getWidth(), (locations[1] - (getHeight() - view.getHeight()) / 2));
                }
        }


        public interface OnCommentPopupItemClickListener {
                void onItemClick(View view, int position, boolean isLike);
        }


        private OnCommentPopupItemClickListener mOnCommentPopupItemClickListener;

        public void setOnCommentPopupItemClickListener(OnCommentPopupItemClickListener onCommentPopupItemClickListener) {
                mOnCommentPopupItemClickListener = onCommentPopupItemClickListener;
        }

        @Override
        public void onClick(View v) {
                if (isShowing()) {
                        dismiss();
                }
                int i = v.getId();
                if (i == R.id.tv_comment_popup_window_like) {
                        if (mOnCommentPopupItemClickListener != null) {
                                if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                                        return;
                                mLasttime = System.currentTimeMillis();
                                if (likerName.equals("赞")) {
                                        mOnCommentPopupItemClickListener.onItemClick(v, 0, false);
                                } else {
                                        mOnCommentPopupItemClickListener.onItemClick(v, 0, true);
                                }
                        }

                } else if (i == R.id.tv_comment_popup_window_comment) {
                        if (mOnCommentPopupItemClickListener != null) {
                                if (likerName.equals("赞")) {
                                        mOnCommentPopupItemClickListener.onItemClick(v, 1, false);
                                } else {
                                        mOnCommentPopupItemClickListener.onItemClick(v, 1, true);
                                }
                        }

                }
        }
}
