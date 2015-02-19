package com.mingle.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.mingle.ui.R;
import com.mingle.ui.fragment.CarryFragment;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzz40500 on 15/2/17.
 */
public class CarryFragmentLayout extends RelativeLayout {
    public CarryFragmentLayout(Context context) {
        super(context);
    }

    public CarryFragmentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CarryFragmentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CarryFragmentLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private float startY;

    private boolean isOpen=false;

    private float settingViewDistanceOfParent=0;

    private float carryFragmentY=0;


//

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        carryRL = LayoutInflater.from(getContext()).inflate(R.layout.carry_fragment_view,null);
        carryFL= (FrameLayout) carryRL.findViewById(R.id.carryFL);

        ViewHelper.setTranslationY(carryFL,-getMeasuredHeight());

        carryFL.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startY=event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float touchY=event.getRawY();
                        if (touchY-startY>60){

                            moveFragment(settingView.getMeasuredHeight()+10+(touchY-startY),0);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:

                        float touchUpY=event.getRawY();


                        if((touchUpY-startY)<(CarryFragmentLayout.this.getMeasuredHeight()-settingView.getMeasuredHeight()-10)/4){
//                            moveFragment(settingView.getMeasuredHeight()+10,200);
                            translationOut(settingView);
                        }else{

                            closeFragment();
                        }



                        break;
                }


                return isOpen;
            }
        });


        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        addView(carryRL,params);
    }

    public boolean isOpen() {
        return isOpen;
    }

    /**
     * 滑动 Fragment 组件
     * @param distanceY
     */
    private void moveFragment(float distanceY,int duration) {


        int distanceYInt= (int) distanceY;

         ViewHelper.setTranslationY(carryFL,distanceY );
         carryFragmentY=distanceY;
//         ViewHelper.setRotationX(carryFL,-10*distanceY/(CarryFragmentLayout.this.getMeasuredHeight()-settingView.getMeasuredHeight()-10) );


    }

    private FrameLayout carryFL;
    private List<View> aboveView=new ArrayList<View>();
    private List<View> belowView=new ArrayList<View>();
    private View carryRL;
    private ScrollView scrollView;


   public void openFragment(FragmentManager fm,Fragment fragment,View view ){

       if(isOpen){
           return;
       }
       ViewGroup.LayoutParams params=carryFL.getLayoutParams();
       params.height=CarryFragmentLayout.this.getMeasuredHeight()-view.getMeasuredHeight()-10;
       carryFL.requestLayout();


       if(fragment instanceof CarryFragment){
           CarryFragment carryFragment= (CarryFragment) fragment;

           carryFragment.setCarryFragmentLayout(this);
       }

       fm.beginTransaction().replace(R.id.carryFL,fragment).commit();

       carryFragmentY=getMeasuredHeight();


       isOpen=true;

       settingViewDistanceOfParent=
               (getTopForOnScreen(view)- getTopForOnScreen(CarryFragmentLayout.this));
       sortView(view);
   }


   public void closeFragment(){
       translationIn();
       isOpen=false;
   }


    private View settingView;

    private void sortView(View view) {

        this.settingView =view;

        ViewGroup vg;
        if(getChildCount() == 2 && (getChildAt(0) instanceof ScrollView)){
            scrollView= (ScrollView) getChildAt(0);


            vg= (ViewGroup) scrollView.getChildAt(0);
        }else{
            vg=this;
        }
        belowView.clear();
        aboveView.clear();

        for (int i = 0; i < vg.getChildCount(); i++) {

            View childView = vg.getChildAt(i);

            if (childView.getTop() > view.getTop()) {
                if(childView!=carryRL) {
                    belowView.add(childView);
                }

            } else if (childView.getTop() < view.getTop()) {
                if(childView!=carryRL) {
                    aboveView.add(childView);
                }
            }
        }
        translationOut(view);

    }




    private void translationOut(View view) {


        List<ObjectAnimator> objectAnimatorList=new ArrayList<ObjectAnimator>();

        for (int i = 0; i < belowView.size(); i++) {

               View childView=belowView.get(i);
               ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(childView, "translationY",  getMeasuredHeight());
               ObjectAnimator alphaOut = ObjectAnimator.ofFloat(childView, "alpha", 1, 0.7f, 0.8f, 0);
               objectAnimatorList.add(objectAnimator);
               objectAnimatorList.add(alphaOut);

        }
        for (int i = 0; i < aboveView.size(); i++) {


            View childView=aboveView.get(i);
            ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(childView, "translationY",-settingViewDistanceOfParent
            );
            objectAnimatorList.add(objectAnimator);
        }

        ObjectAnimator translateView=ObjectAnimator.ofFloat(view,"translationY",
                -settingViewDistanceOfParent
        );
        objectAnimatorList.add(translateView);

        ObjectAnimator stickyTo=ObjectAnimator.ofFloat(carryFL,"translationY",carryFragmentY,view.getMeasuredHeight()+10);

        carryFragmentY=view.getMeasuredHeight()+10;

        ObjectAnimator rotate=ObjectAnimator.ofFloat(carryFL,"rotationX",-10f,0f);
//        ObjectAnimator alphaStickyTo=ObjectAnimator.ofFloat(carryFL,"alpha",0,0.8f,1);
        objectAnimatorList.add(stickyTo);
//        objectAnimatorList.add(alphaStickyTo);
//        objectAnimatorList.add(rotate);


        ObjectAnimator[] objectAnimators=new ObjectAnimator[objectAnimatorList.size()];

        for (int i = 0; i < objectAnimators.length; i++) {
             objectAnimators[i]=objectAnimatorList.get(i);

        }
        AnimatorSet set=new AnimatorSet();
        set.playTogether(objectAnimators);
        set.setDuration(600);
        set.setInterpolator(new DecelerateInterpolator());
        set.start();

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if(scrollView != null){
                    scrollView.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
                }
            }
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    public int getTopForOnScreen(View view){


        int[] location = new int[2];
        view.getLocationInWindow(location);
        int childY = location[1] ;


        return  childY;


    }


    /**
     * 关闭
     */
    private void translationIn(){


        List<ObjectAnimator> objectAnimatorList=new ArrayList<ObjectAnimator>();

        for (int i = 0; i < belowView.size(); i++) {

            View childView=belowView.get(i);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(childView, "translationY", 0);
            ObjectAnimator alphaOut = ObjectAnimator.ofFloat(childView, "alpha", 1);
            objectAnimatorList.add(objectAnimator);
            objectAnimatorList.add(alphaOut);

        }
        for (int i = 0; i < aboveView.size(); i++) {
            View childView=aboveView.get(i);
            ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(childView, "translationY", 0);
            objectAnimatorList.add(objectAnimator);
        }

        ObjectAnimator translateView=ObjectAnimator.ofFloat(settingView,"translationY",0
        );
        objectAnimatorList.add(translateView);

        ObjectAnimator stickyTo=ObjectAnimator.ofFloat(carryFL,"translationY",getMeasuredHeight());
        ObjectAnimator rotate=ObjectAnimator.ofFloat(carryFL,"rotationX",0f);
//        ObjectAnimator alphaStickyTo=ObjectAnimator.ofFloat(carryFL,"alpha",0,0.8f,1);
        objectAnimatorList.add(stickyTo);
//        objectAnimatorList.add(alphaStickyTo);
        objectAnimatorList.add(rotate);

        ObjectAnimator[] objectAnimators=new ObjectAnimator[objectAnimatorList.size()];

        for (int i = 0; i < objectAnimators.length; i++) {
            objectAnimators[i]=objectAnimatorList.get(i);

        }
        AnimatorSet set=new AnimatorSet();
        set.playTogether(objectAnimators);
        set.setDuration(600);
        set.setInterpolator(new DecelerateInterpolator());
        set.start();

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {


                if(scrollView != null){
                    scrollView.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }






}
