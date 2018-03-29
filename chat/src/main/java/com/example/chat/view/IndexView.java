package com.example.chat.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.chat.R;
import com.example.commonlibrary.utils.DensityUtil;


/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/24      12:02
 * QQ:             1981367757
 */

public class IndexView extends View {
        private static String[] letter={"#","A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z",};
        private Paint mPaint=new Paint();
        private int choose=-1;
        private int itemHeight;
        MyLetterChangeListener mListener;

        public void setListener(MyLetterChangeListener listener) {
                if (mListener!=listener) {
                        mListener = listener;
                }
        }

        public interface MyLetterChangeListener{
                 void onLetterChanged(String s);
                void onFinished();
        }



        public IndexView(Context context) {
                super(context);
        }

        public IndexView(Context context, AttributeSet attrs) {
                super(context, attrs);
        }

        public IndexView(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
                int height=getHeight();
                int width=getWidth();
                itemHeight=height/letter.length;
                for (int i = 0; i < letter.length; i++) {
                        float x=width/2-mPaint.measureText(letter[i])/2;
                        float y=itemHeight*(i+1);
                        if (i == choose) {
                                mPaint.setStyle(Paint.Style.FILL);
                                mPaint.setColor(getResources().getColor(R.color.blue_500));
                                canvas.drawRect(new Rect(0,itemHeight*i,width, (int) y),mPaint);
                                mPaint.reset();
                                mPaint.setColor(getResources().getColor(R.color.white));
                                mPaint.setFakeBoldText(true);
                        }else {
                                mPaint.setColor(getResources().getColor(R.color.grey_350));
                        }
                        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
                        mPaint.setAntiAlias(true);
                        mPaint.setTextSize(DensityUtil.dip2px(getContext(),12));
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
                        case MotionEvent.ACTION_DOWN:
                                case MotionEvent.ACTION_MOVE:
                                        String item="";
                                        if (index>=0&&index<letter.length) {
                                                item = letter[index];
                                        }
                                        if (mListener != null) {
                                                mListener.onLetterChanged(item);
                                        }
                                        choose=index;
                                        invalidate();
                                        break;
                        default:
                                choose=-1;
                                invalidate();
                                if (mListener != null) {
                                        mListener.onFinished();
                                }
                                break;
                }
                return true;
        }
}
