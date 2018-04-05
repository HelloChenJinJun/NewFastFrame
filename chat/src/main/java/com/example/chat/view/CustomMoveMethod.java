package com.example.chat.view;

import android.graphics.Color;
import android.text.Layout;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.BaseMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.chat.util.LogUtil;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/5      17:08
 * QQ:             1981367757
 */

public class CustomMoveMethod extends BaseMovementMethod {


        private ClickableSpan[] clickableSpans;
        private BackgroundColorSpan colorSpan;
        private boolean isClickTextView = true;
        private int selectedColor;


        public CustomMoveMethod(int selectedColor) {
                this.selectedColor = selectedColor;
        }

        public CustomMoveMethod(){
                selectedColor=Color.GREEN;
        }


        @Override
        public boolean onTouchEvent(TextView widget, Spannable text, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                        case MotionEvent.ACTION_DOWN:
//                                                这里得到的x ,  y值只是相对于屏幕的位置
                                int x = (int) event.getX();
                                int y = (int) event.getY();
                                x -= widget.getTotalPaddingLeft();
                                y -= widget.getTotalPaddingTop();
                                x += widget.getScrollX();
                                y += widget.getScrollY();
                                Layout layout = widget.getLayout();
                                int location = layout.getOffsetForHorizontal(layout.getLineForVertical(y), x);
                                clickableSpans = text.getSpans(location, location, ClickableSpan.class);
                                if (clickableSpans.length > 0) {
                                        isClickTextView = false;
                                        LogUtil.e("点击clickSpan位置");
//                                                        选中点击位置
//                                        Selection.setSelection(text, text.getSpanStart(clickableSpans[0]), text.getSpanEnd(clickableSpans[0]));
                                        text.setSpan(colorSpan = new BackgroundColorSpan(selectedColor), text.getSpanStart(clickableSpans[0]), text.getSpanEnd(clickableSpans[0]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        return true;
                                } else {
                                        isClickTextView = true;
                                }
                                break;
                        case MotionEvent.ACTION_MOVE:
                                break;
                        case MotionEvent.ACTION_UP:
                                if (clickableSpans.length > 0) {
                                        LogUtil.e("点击span的up11");
//                                                        设置点击时间
                                        clickableSpans[0].onClick(widget);
                                        text.removeSpan(colorSpan);
//                                        Selection.removeSelection(text);
                                        return true;
                                } else {
                                        LogUtil.e("点击textView文字的up11");
                                }
                                break;
                        default:
                                if (colorSpan != null) {
                                        text.removeSpan(colorSpan);
//                                        Selection.removeSelection(text);
                                        return true;
                                }
                                break;
                }
                return super.onTouchEvent(widget, text, event);
        }
}
