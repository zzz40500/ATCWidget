package com.mingle.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mingle.ui.R;
import com.mingle.utils.Display;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义控件
 * Created by zzz40500 on 15/2/13.
 */
public class ThumbTextView extends FrameLayout implements View.OnClickListener{




    public ThumbTextView(Context context) {
        super(context);
    }

    public ThumbTextView(Context context, AttributeSet attrs) {
       this(context, attrs,0);
    }

    public ThumbTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ThumbTextView);

        titleStr=a.getString(R.styleable.ThumbTextView_mainTitle);

        contentStr=a.getString(R.styleable.ThumbTextView_content);

        maxNumOfContent =a.getInt(R.styleable.ThumbTextView_maxNumOfContent,100);
        titleAppearance=a.getResourceId(R.styleable.ThumbTextView_titleAppearance,R.style.CommonTextAppearance_Big_Gray);
        contentAppearance=a.getResourceId(R.styleable.ThumbTextView_contentAppearance,R.style.CommonTextAppearance_Mini_Gray);
        a.recycle();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ThumbTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        init();
        
    }
    private TextView titleTV;
    private TextView contentTV;
    private TextView moreTV;
    private int maxNumOfContent =0;

    private ScrollView scrollView;

    private Button foldBtn;
    private List<View> aboveView=new ArrayList<View>();
    private List<View> belowView=new ArrayList<View>();
    private int height=0;
    private float top;
    private boolean isExpand=false;
    private  boolean initView=false;
    private int parentHeight;

    private int titleAppearance=-1;
    private int contentAppearance=-1;


    private int screenHeight= Display.getSceenHeight(getContext());
    private String titleStr;
    private String contentStr;

    private int foldBtnHeight;

    private  String thumbStr;

    public ThumbListener thumbListener;



    private void init() {
        View  thumbView=LayoutInflater.from(getContext()).inflate(R.layout.item_thumb_text,null);
        titleTV= (TextView) thumbView.findViewById(R.id.titleTV);
        contentTV= (TextView) thumbView.findViewById(R.id.contentTV);
        moreTV= (TextView) thumbView.findViewById(R.id.detailTV);
        titleTV.setTextAppearance(getContext(),titleAppearance);
        contentTV.setTextAppearance(getContext(),contentAppearance);

        foldBtn= (Button) thumbView.findViewById(R.id.fold);
        moreTV.setOnClickListener(this);
        injectString();
        addView(thumbView);
        foldBtn.setOnClickListener(this);
    }




    private void injectString() {
        titleTV.setText(titleStr);

        if(contentStr !=null && contentStr.length()> maxNumOfContent){
            thumbStr=contentStr.substring(0, maxNumOfContent)+"...";
            moreTV.setVisibility(VISIBLE);
        }else{
            thumbStr=contentStr;
            moreTV.setVisibility(GONE);

        }


        contentTV.setText(thumbStr);
    }


    public void addAboveView(View   view){
        aboveView.add(view);
    }

    public void addBelowView(View   view){
        belowView.add(view);
    }


    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.detailTV){

            ViewGroup vg = (ViewGroup) getParent();
            if (getParent() != null && !initView) {


                for (int i = 0; i < vg.getChildCount(); i++) {

                    View childView = vg.getChildAt(i);

                    if (childView.getTop() > getTop()) {

                        belowView.add(childView);

                    } else if (childView.getTop() < getTop()) {
                        aboveView.add(childView);
                    }
                }
                initView = true;
            }



                if(vg.getParent()!=null && vg.getParent() instanceof ScrollView){


                    scrollView=((ScrollView)vg.getParent());
                    parentHeight=scrollView.getMeasuredHeight();
                }else{
                    parentHeight=vg.getMeasuredHeight();
                }
            int[] location = new int[2];
            this.getLocationInWindow(location);
            int childY = location[1] ;
            int[] parentLocation = new int[2];
            vg.getLocationInWindow(parentLocation);
            int parentY = vg.getTop() ;




            top=childY-parentY- Resources.getSystem().getDimensionPixelSize(
                    Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));;


            translateOut();


        }else if(v.getId()==R.id.fold){
            translateIn();
        }


    }

    private void translateOut() {

        height=getMeasuredHeight();


        //todo 计算 height的大小与


//        if(height)

        AnimatorSet set=new AnimatorSet();
        List<ObjectAnimator>  list=new ArrayList<ObjectAnimator>();

        for (int i = 0; i < aboveView.size(); i++) {
            View  childView=aboveView.get(i);
            ObjectAnimator objeAnim=ObjectAnimator.ofFloat(childView,"translationY",-screenHeight);
            list.add(objeAnim);
        }
        for (int i = 0; i < belowView.size(); i++) {
            View  childView=belowView.get(i);
            ObjectAnimator objeAnim=ObjectAnimator.ofFloat(childView,"translationY",screenHeight);
            list.add(objeAnim);
        }


        ObjectAnimator translateY=ObjectAnimator.ofFloat(this,"translationY",-top);

        translateY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
           ViewGroup.LayoutParams   params=ThumbTextView.this.getLayoutParams();


                float  percent=  animation.getAnimatedFraction();

                int  appendHeight=(int)((parentHeight-height)*percent);
                params.height=height+appendHeight;


                requestLayout();


            }
        });
        list.add(translateY);
        ObjectAnimator[] a= new ObjectAnimator[list.toArray().length];
        for (int i = 0; i < a.length; i++) {
            a[i]=list.get(i);
        }

        isExpand=true;

        set.playTogether(a);
        set.setDuration(400);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                contentTV.setText(contentStr);
                moreTV.setVisibility(GONE);
                ViewHelper.setAlpha(foldBtn, 0);
                foldBtn.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.setInterpolator(new AccelerateInterpolator());


        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(foldBtn, "alpha", 0.5f, 1.0f);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(foldBtn, "translationY", 170f, 0f);

        AnimatorSet foldBtnShow = new AnimatorSet();
        foldBtnShow.playTogether(objectAnimator, objectAnimator2);
        foldBtnShow.setDuration(300);
        foldBtnShow.setInterpolator(new DecelerateInterpolator());
        foldBtnShow.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setDuration(700);
        animatorSet.playSequentially(set, foldBtnShow);
//        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.start();


        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (thumbListener!=null){
                    thumbListener.start(isExpand);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {



                if(scrollView !=null){
                    scrollView.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
                }

                if (thumbListener!=null){
                    thumbListener.start(isExpand);
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

    public void  translateIn(){


        isExpand=false;
        final AnimatorSet set=new AnimatorSet();
        List<ObjectAnimator>  list=new ArrayList<ObjectAnimator>();

        for (int i = 0; i < aboveView.size(); i++) {


            View  childView=aboveView.get(i);


            ObjectAnimator objeAnim=ObjectAnimator.ofFloat(childView,"translationY",0);
            list.add(objeAnim);



        }
        for (int i = 0; i < belowView.size(); i++) {
            View  childView=belowView.get(i);
            ObjectAnimator objeAnim=ObjectAnimator.ofFloat(childView,"translationY",0);
            list.add(objeAnim);
        }


        ObjectAnimator translateY=ObjectAnimator.ofFloat(this,"translationY",0);

        translateY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams   params=ThumbTextView.this.getLayoutParams();

                float  percent=  animation.getAnimatedFraction();
                int  appendHeight=(int)((parentHeight-height)*(1-percent));
                params.height=height+appendHeight;
                requestLayout();

            }
        });

        list.add(translateY);


        final ObjectAnimator[] a= new ObjectAnimator[list.toArray().length];
        for (int i = 0; i < a.length; i++) {
            a[i]=list.get(i);
        }

        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(foldBtn,"alpha",0.9f,0.6f,0);

        ObjectAnimator objectAnimator2=ObjectAnimator.ofFloat(foldBtn, "translationY", 0, 120f);

        AnimatorSet firstSet=new AnimatorSet();
        firstSet.playTogether(objectAnimator, objectAnimator2);
        firstSet.setDuration(100);
        set.playTogether(a);
        set.setDuration(300);
        firstSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                foldBtn.setVisibility(GONE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                foldBtn.setVisibility(GONE);
                contentTV.setText(thumbStr);
            }

            @Override
            public void onAnimationEnd(Animator animation) {


                moreTV.setVisibility(VISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playSequentially(firstSet,set);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(300);



        animatorSet.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        if (thumbListener != null) {
                                            thumbListener.start(isExpand);
                                        }
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        if (thumbListener != null) {
                                            thumbListener.start(isExpand);
                                        }
                                        if(scrollView!= null){

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
        animatorSet.start();






    }




    public boolean isExpand() {
        return isExpand;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
        injectString();
    }

    public String getContentStr() {
        return contentStr;
    }

    public void setContentStr(String contentStr) {
        this.contentStr = contentStr;
        injectString();
    }

    public ThumbListener getThumbListener() {
        return thumbListener;
    }

    public void setThumbListener(ThumbListener thumbListener) {
        this.thumbListener = thumbListener;
    }

    public abstract  class ThumbListener{

        public abstract  void start(boolean expand);
        public abstract  void end(boolean expand);

    }

}
