package com.example.chat.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.util.PixelUtil;


/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/24      12:02
 * QQ:             1981367757
 */

public class MyLetterView extends View {
        private static String[] letter={"#","A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z",};
        private Paint mPaint=new Paint();
        private int choose=-1;
        private int itemHeight;
        private TextView mTextView;
        MyLetterChangeListener mListener;

        public void setListener(MyLetterChangeListener listener) {
                if (mListener!=listener) {
                        mListener = listener;
                }
        }

        public interface MyLetterChangeListener{
                 void onLetterChanged(String s);


        }

        public void setTextView(TextView textView) {
                mTextView = textView;
        }

        public MyLetterView(Context context) {
                super(context);
        }

        public MyLetterView(Context context, AttributeSet attrs) {
                super(context, attrs);
        }

        public MyLetterView(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
                int height=getHeight();
                int width=getWidth();
                itemHeight=height/letter.length;
                for (int i = 0; i < letter.length; i++) {
                        mPaint.setColor(Color.parseColor("#9da0a4"));
                        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
                        mPaint.setAntiAlias(true);
                        mPaint.setTextSize(PixelUtil.dp2px(12));
                        if (i == choose) {
                                mPaint.setColor(Color.parseColor("#3399ff"));
                                mPaint.setFakeBoldText(true);
                        }
                        float x=width/2-mPaint.measureText(letter[i])/2;
                        float y=itemHeight*(i+1);
                        canvas.drawText(letter[i],x,y,mPaint);
                        mPaint.reset();
                }
                super.onDraw(canvas);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
                int action=event.getAction();
                float y=event.getY();
                int index= (int) (y/itemHeight);
                switch (action){
                        case MotionEvent.ACTION_UP:
                                setBackgroundResource(R.color.transparent);
                                choose=-1;
                                invalidate();
                                if (mTextView!=null) {
                                        mTextView.setVisibility(INVISIBLE);
                                }
                                break;
                        default:
                                String item="";
                                if (index>=0&&index<letter.length) {
                                        item = letter[index];
                                }
                                if (mListener != null) {
                                        mListener.onLetterChanged(item);
                                }
                                mTextView.setText(item);
                                if (mTextView != null) {
                                        mTextView.setVisibility(VISIBLE);
                                }
                                choose=index;
                                invalidate();
                                break;
                }
                return true;
        }
}
