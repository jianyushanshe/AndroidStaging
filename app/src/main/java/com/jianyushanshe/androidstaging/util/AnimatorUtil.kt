package com.jianyushanshe.androidstaging.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation

/**
 * Author:wangjianming
 * Description:AnimatorUtil 动画工具类
 */
object AnimatorUtil {
    /**
     * 属性缩放动画
     *
     * @param view
     * @param time
     */
    fun scaleAnimator(view: View?, time: Int) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f)
        val animatorSet = AnimatorSet()
        animatorSet.duration = time.toLong()
        animatorSet.playTogether(scaleX, scaleY)
        animatorSet.interpolator = DecelerateInterpolator()
        animatorSet.start()
    }

    /**
     * View缩放动画
     *
     * @param view
     * @param time
     * @param count
     */
    fun scaleViewAnimator(view: View, time: Int, count: Int) {
        val scaleAnimation = ScaleAnimation(
            0f,
            1f,
            0f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        scaleAnimation.duration = time.toLong()
        scaleAnimation.repeatCount = count
        scaleAnimation.fillAfter = true
        view.startAnimation(scaleAnimation)
    }

    /**
     * 百度地图mark点击动画
     *
     * @param view
     * @param time
     * @param count
     */
    fun scaleViewAnimatorForBaiduMark(view: View, time: Int, count: Int) {
        val mScaleAnimation = ScaleAnimation(
            1f, 1.2f, 1f, 1.2f, Animation.RELATIVE_TO_SELF, 0.3f, Animation.RELATIVE_TO_SELF,
            0.55f
        )
        mScaleAnimation.duration = time.toLong()
        mScaleAnimation.repeatCount = count
        mScaleAnimation.fillAfter = true
        mScaleAnimation.interpolator = DecelerateInterpolator()
        view.startAnimation(mScaleAnimation)
    }

    /**
     * 百度地图中心点站点数量信息条动画
     *
     * @param view
     * @param time
     * @param count
     */
    fun scaleViewAnimatorForBaiduCenter(view: View, time: Int, count: Int) {
        val mScaleAnimation = ScaleAnimation(
            1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )
        mScaleAnimation.duration = time.toLong()
        mScaleAnimation.repeatCount = count
        mScaleAnimation.fillAfter = true
        mScaleAnimation.interpolator = DecelerateInterpolator()
        view.startAnimation(mScaleAnimation)
    }

    /**
     * View平移动画
     *
     * @param view
     * @param time
     * @param count
     * @param fromXValue
     * @param toXValue
     * @param fromYValue
     * @param toYValue
     */
    fun translateViewAnimator(
        view: View, time: Int, count: Int, fromXValue: Float, toXValue: Float,
        fromYValue: Float, toYValue: Float
    ) {
        val translateAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT,
            fromXValue,
            Animation.RELATIVE_TO_PARENT,
            toXValue,
            Animation.RELATIVE_TO_PARENT,
            fromYValue,
            Animation.RELATIVE_TO_PARENT,
            toYValue
        )
        translateAnimation.duration = time.toLong()
        translateAnimation.repeatCount = count
        translateAnimation.repeatMode = Animation.REVERSE
        view.startAnimation(translateAnimation)
    }

    /**
     * 属性动画平移
     *
     * @param view
     * @param time
     * @param fromXValue
     * @param toXValue
     * @param fromYValue
     * @param toYValue
     */
    fun translateAnimator(
        view: View?, time: Int, fromXValue: Float, toXValue: Float,
        fromYValue: Float, toYValue: Float
    ) {
        val translateX = ObjectAnimator.ofFloat(view, "translationX", fromXValue, toXValue)
        val translateY = ObjectAnimator.ofFloat(view, "translationY", fromYValue, toYValue)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translateX, translateY)
        animatorSet.duration = time.toLong()
        animatorSet.start()
    }
}