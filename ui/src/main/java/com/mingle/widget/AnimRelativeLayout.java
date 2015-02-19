package com.mingle.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 *
 * Created by zzz40500 on 15/2/4.
 */
public class AnimRelativeLayout extends RelativeLayout {


    public AnimRelativeLayout(Context context) {
        super(context);
        init();
    }

    public AnimRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init(){
    }




    public int onLayoutCount=0;


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if(onLayoutCount<2) {
            onLayoutCount++;
            for (int i = 0; i < getChildCount(); i++) {
                final View button = getChildAt(i);
                ViewHelper.setAlpha(button,0);
                if (button.getTop() < getMeasuredHeight()) {


                    ObjectAnimator o1=ObjectAnimator.ofFloat(button, "translationY", 300, 0);
                    o1.setDuration(700);
                    ObjectAnimator o2=ObjectAnimator.ofFloat(button, "alpha", 0, 1);
                    o2.setDuration(700 * 3 / 2);

                    AnimatorSet animator =new AnimatorSet();
                    int delay = (int) ((button.getTop() * 2000.0) / getMeasuredHeight());
                    animator.playTogether(o1,o2);
                    animator.setStartDelay(delay);
                    animator.setInterpolator(new DecelerateInterpolator());
                    animator.setDuration(700);
                    animator.start();
                }
            }
        }


    }

}
