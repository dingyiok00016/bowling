package com.cloudysea.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.widget.ImageView;

/**
 * @author roof 2020-02-26.
 * @email lyj@yhcs.com
 * @detail
 */
public class AnimationFactory {

    public static final int ANIMATION_STYLE_ONE = 0x01;
    public static final int ANIMATION_STYLE_TWO = 0x02;
    public static final int ANIMATION_STYLE_THREE = 0x03;
    public static final int ANIMATION_STYLE_FOUR = 0x04;


    /**
     *
     * @param current 当前view
     * @param next 下一个view
     * @param index 动画索引值
     * @return 返回动画对象
     */
    public static AnimatorSet getAnimationStyle(int index,ImageView current,ImageView next){
        switch (index){
            case ANIMATION_STYLE_ONE:
                return getAnimationStyleOne(current,next);
            case ANIMATION_STYLE_TWO:
                return getAnimationStyleTwo(current,next);
            case ANIMATION_STYLE_THREE:
                return getAnimationStyleThree(current,next);
            case ANIMATION_STYLE_FOUR:
                return getAnimationStyleFour(current,next);

        }
        return getAnimationStyleOne(current,next);
    }


    public static AnimatorSet getAnimationStyleOne(ImageView current,ImageView next){
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(current, "alpha", 1.0f, 0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(next, "alpha", 0f, 1.0f);
        ObjectAnimator animatorScale1 = ObjectAnimator.ofFloat(current, "scaleX", 1.0f, 1.3f);
        ObjectAnimator animatorScale2 = ObjectAnimator.ofFloat(current, "scaleY", 1.0f, 1.3f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(5000);
        animatorSet.play(animator1).with(animator2).with(animatorScale1).with(animatorScale2);
        return animatorSet;
    }

    public static AnimatorSet getAnimationStyleTwo(ImageView current,ImageView next){
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(current, "alpha", 1.0f, 0f);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(next, "alpha", 0f, 1.0f);
        ObjectAnimator animatorScale3 = ObjectAnimator.ofFloat(current, "scaleX", 1.0f, 1.3f);
        ObjectAnimator animatorScale4 = ObjectAnimator.ofFloat(current, "scaleY", 1.0f, 1.3f);
        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.setDuration(5000);
        animatorSet2.play(animator3).with(animator4).with(animatorScale3).with(animatorScale4);
        return animatorSet2;
    }

    public static AnimatorSet getAnimationStyleThree(ImageView current,ImageView next){
        ObjectAnimator animator5 = ObjectAnimator.ofFloat(current, "alpha", 1.0f, 0f);
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(next, "alpha", 0f, 1.0f);
        ObjectAnimator animatorScale5 = ObjectAnimator.ofFloat(current, "scaleX", 1.0f, 1.3f);
        ObjectAnimator animatorScale6 = ObjectAnimator.ofFloat(current, "scaleY", 1.0f, 1.3f);
        AnimatorSet animatorSet3 = new AnimatorSet();
        animatorSet3.setDuration(5000);
        animatorSet3.play(animator5).with(animator6).with(animatorScale5).with(animatorScale6);
        return animatorSet3;
    }

    public static AnimatorSet getAnimationStyleFour(ImageView current,ImageView next){
        ObjectAnimator animator7 = ObjectAnimator.ofFloat(current, "alpha", 1.0f, 0f);
        ObjectAnimator animator8 = ObjectAnimator.ofFloat(next, "alpha", 0f, 1.0f);
        ObjectAnimator animatorScale7 = ObjectAnimator.ofFloat(current, "scaleX", 1.0f, 1.3f);
        ObjectAnimator animatorScale8 = ObjectAnimator.ofFloat(current, "scaleY", 1.0f, 1.3f);
        AnimatorSet animatorSet4 = new AnimatorSet();
        animatorSet4.setDuration(5000);
        animatorSet4.play(animator7).with(animator8).with(animatorScale7).with(animatorScale8);
        return animatorSet4;
    }


}
