package com.example.commonlibrary.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import com.example.commonlibrary.R;
import com.example.commonlibrary.utils.DensityUtil;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * 项目名称:    zhuayu_android
 * 创建人:      陈锦军
 * 创建时间:    2018/11/13     15:29
 */
public class CustomEditText extends AppCompatEditText {


    private Drawable mDrawable;

    public CustomEditText(Context context) {
        this(context, null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDrawable = getResources().getDrawable(R.drawable.ic_input_delete);
        mDrawable.setBounds(0, 0, DensityUtil.toDp(15), DensityUtil.toDp(15));
    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setDeleteIconVisible(hasFocus() && length() > 0);
    }

    private void setDeleteIconVisible(boolean deleteVisible) {
        setCompoundDrawables(null, null, deleteVisible ? mDrawable : null, null);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                    && (event.getX() < ((getWidth() - getPaddingRight())));
            if (touchable) {
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }



    public static TranslateAnimation getShakeAnimation(int count) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(count));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    public void startShakeAnimation() {
        startAnimation(getShakeAnimation(3));
    }

}
