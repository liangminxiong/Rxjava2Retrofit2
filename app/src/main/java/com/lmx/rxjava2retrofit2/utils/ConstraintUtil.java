package com.lmx.rxjava2retrofit2.utils;

import android.os.Build;

import androidx.annotation.IdRes;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.TransitionManager;

/**
 * Created by lmx on 2020/10/15
 * Describe:用于修改ConstraintLayout布局中的控件的依赖关系
 * 添加了End_to_Start_of/Start_to_End_of/Start_to_Start_of/End_to_End_of关系
 * 添加了setWidthAndHeight方法设置控件高度和宽度
 * 备注：动态生成的控件，请使用这个工具类中的setWidth()，setHeight()方法来修改其大小
 * 使用方法：
 * ConstraintLayoutUtil constraintUtilNew = new ConstraintLayoutUtil(constraintLayout);
 * ConstraintLayoutUtil.ConstraintBegin drawer = constraintUtilNew.begin()/constraintUtilNew.beginWithAnim();
 * <p>
 * //绘制刚刚加入的button按钮，设置高度和宽度为0，
 * //为button和辅助线guideline添加Start_toStartof和End_toStartof依赖
 * drawer.setWidthAndHeight(button.getId(),0,0);
 * drawer.Start_toStartof(button.getId(),R.id.guideline_r6);
 * drawer.End_toStartof(button.getId(),R.id.guideline_r7);
 * <p>
 * //绘制结束后记得提交绘制结果
 * drawer.commit();
 */
public class ConstraintUtil {

    private ConstraintLayout constraintLayout;
    private ConstraintBegin begin;
    private ConstraintSet applyConstraintSet = new ConstraintSet();
    private ConstraintSet resetConstraintSet = new ConstraintSet();

    public ConstraintUtil(ConstraintLayout constraintLayout) {
        this.constraintLayout = constraintLayout;
        resetConstraintSet.clone(constraintLayout);
    }

    /**
     * 开始修改
     */
    public ConstraintBegin begin() {
        synchronized (ConstraintBegin.class) {
            if (begin == null) {
                begin = new ConstraintBegin();
            }
        }
        applyConstraintSet.clone(constraintLayout);
        return begin;
    }

    /**
     * 带动画的修改
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ConstraintBegin beginWithAnim() {
        TransitionManager.beginDelayedTransition(constraintLayout);
        return begin();
    }

    /**
     * 重置依赖
     */
    public void reSet() {
        resetConstraintSet.applyTo(constraintLayout);
    }

    /**
     * 带动画的重置
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void reSetWidthAnim() {
        TransitionManager.beginDelayedTransition(constraintLayout);
        resetConstraintSet.applyTo(constraintLayout);
    }

    public class ConstraintBegin {
        /**
         * 清除某个空间的关系
         * 注意：这里不仅仅会清除关系，还会清除对应控件的宽高为 w:0,h:0
         *
         * @param viewIds
         */
        public ConstraintBegin clear(@IdRes int... viewIds) {
            for (int viewId : viewIds) {
                applyConstraintSet.clear(viewId);
            }
            return this;
        }

        /**
         * 清除某个控件的，某个关系
         *
         * @param viewId
         * @param anchor
         * @return
         */
        public ConstraintBegin clear(int viewId, int anchor) {
            applyConstraintSet.clear(viewId, anchor);
            return this;
        }

        /**
         * 为某个控件设置 margin
         *
         * @param viewId 某个控件ID
         * @param left   marginLeft
         * @param top    marginTop
         * @param right  marginRight
         * @param bottom marginBottom
         * @return
         */
        public ConstraintBegin setMargin(@IdRes int viewId, int left, int top, int right, int bottom) {
            setMarginLeft(viewId, left);
            setMarginTop(viewId, top);
            setMarginRight(viewId, right);
            setMarginBottom(viewId, bottom);
            return this;
        }

        /**
         * 为某个控件设置 marginLeft
         *
         * @param viewId 某个控件ID
         * @param left   marginLeft
         * @return
         */
        public ConstraintBegin setMarginLeft(@IdRes int viewId, int left) {
            applyConstraintSet.setMargin(viewId, ConstraintSet.LEFT, left);
            return this;
        }

        /**
         * 为某个控件设置 marginRight
         *
         * @param viewId 某个控件ID
         * @param right  marginRight
         * @return
         */
        public ConstraintBegin setMarginRight(@IdRes int viewId, int right) {
            applyConstraintSet.setMargin(viewId, ConstraintSet.RIGHT, right);
            return this;
        }

        /**
         * 为某个控件设置 marginTop
         *
         * @param viewId 某个控件ID
         * @param top    marginTop
         * @return
         */
        public ConstraintBegin setMarginTop(@IdRes int viewId, int top) {
            applyConstraintSet.setMargin(viewId, ConstraintSet.TOP, top);
            return this;
        }

        /**
         * 为某个控件设置marginBottom
         *
         * @param viewId 某个控件ID
         * @param bottom marginBottom
         * @return
         */
        public ConstraintBegin setMarginBottom(@IdRes int viewId, int bottom) {
            applyConstraintSet.setMargin(viewId, ConstraintSet.BOTTOM, bottom);
            return this;
        }

        /**
         * 为某个控件设置关联关系 End_to_Start_of
         *
         * @param startId
         * @param endId
         * @return
         */
        public ConstraintBegin End_toStartof(@IdRes int startId, @IdRes int endId) {
            applyConstraintSet.connect(startId, ConstraintSet.END, endId, ConstraintSet.START);
            return this;
        }

        /**
         * 为某个控件设置关联关系 Start_to_End_of
         *
         * @param startId
         * @param endId
         * @return
         */
        public ConstraintBegin Start_toEndof(@IdRes int startId, @IdRes int endId) {
            applyConstraintSet.connect(startId, ConstraintSet.START, endId, ConstraintSet.END);
            return this;
        }

        /**
         * 为某个控件设置关联关系 Start_to_Start_of
         *
         * @param startId
         * @param endId
         * @return
         */
        public ConstraintBegin Start_toStartof(@IdRes int startId, @IdRes int endId) {
            applyConstraintSet.connect(startId, ConstraintSet.START, endId, ConstraintSet.START);
            return this;
        }

        /**
         * 为某个控件设置关联关系 End_to_End_of
         *
         * @param startId
         * @param endId
         * @return
         */
        public ConstraintBegin End_to_End_of(@IdRes int startId, @IdRes int endId) {
            applyConstraintSet.connect(startId, ConstraintSet.END, endId, ConstraintSet.END);
            return this;
        }

        /**
         * 为某个控件设置关联关系 left_to_left_of
         *
         * @param startId
         * @param endId
         * @return
         */
        public ConstraintBegin Left_toLeftOf(@IdRes int startId, @IdRes int endId) {
            applyConstraintSet.connect(startId, ConstraintSet.LEFT, endId, ConstraintSet.LEFT);
            return this;
        }

        /**
         * 为某个控件设置关联关系 left_to_right_of
         *
         * @param startId
         * @param endId
         * @return
         */
        public ConstraintBegin Left_toRightOf(@IdRes int startId, @IdRes int endId) {
            applyConstraintSet.connect(startId, ConstraintSet.LEFT, endId, ConstraintSet.RIGHT);
            return this;
        }

        /**
         * 为某个控件设置关联关系 top_to_top_of
         *
         * @param startId
         * @param endId
         * @return
         */
        public ConstraintBegin Top_toTopOf(@IdRes int startId, @IdRes int endId) {
            applyConstraintSet.connect(startId, ConstraintSet.TOP, endId, ConstraintSet.TOP);
            return this;
        }

        /**
         * 为某个控件设置关联关系 top_to_bottom_of
         *
         * @param startId
         * @param endId
         * @return
         */
        public ConstraintBegin Top_toBottomOf(@IdRes int startId, @IdRes int endId) {
            applyConstraintSet.connect(startId, ConstraintSet.TOP, endId, ConstraintSet.BOTTOM);
            return this;
        }

        /**
         * 为某个控件设置关联关系 right_to_left_of
         *
         * @param startId
         * @param endId
         * @return
         */
        public ConstraintBegin Right_toLeftOf(@IdRes int startId, @IdRes int endId) {
            applyConstraintSet.connect(startId, ConstraintSet.RIGHT, endId, ConstraintSet.LEFT);
            return this;
        }

        /**
         * 为某个控件设置关联关系 right_to_right_of
         *
         * @param startId
         * @param endId
         * @return
         */
        public ConstraintBegin Right_toRightOf(@IdRes int startId, @IdRes int endId) {
            applyConstraintSet.connect(startId, ConstraintSet.RIGHT, endId, ConstraintSet.RIGHT);
            return this;
        }

        /**
         * 为某个控件设置关联关系 bottom_to_bottom_of
         *
         * @param startId
         * @param endId
         * @return
         */
        public ConstraintBegin Bottom_toBottomOf(@IdRes int startId, @IdRes int endId) {
            applyConstraintSet.connect(startId, ConstraintSet.BOTTOM, endId, ConstraintSet.BOTTOM);
            return this;
        }

        /**
         * 为某个控件设置关联关系 bottom_to_top_of
         *
         * @param startId
         * @param endId
         * @return
         */
        public ConstraintBegin Bottom_toTopOf(@IdRes int startId, @IdRes int endId) {
            applyConstraintSet.connect(startId, ConstraintSet.BOTTOM, endId, ConstraintSet.TOP);
            return this;
        }

        /**
         * 为某个控件设置宽度
         *
         * @param viewId
         * @param width
         * @return
         */
        public ConstraintBegin setWidth(@IdRes int viewId, int width) {
            applyConstraintSet.constrainWidth(viewId, width);
            return this;
        }

        /**
         * 某个控件设置高度
         *
         * @param viewId
         * @param height
         * @return
         */
        public ConstraintBegin setHeight(@IdRes int viewId, int height) {
            applyConstraintSet.constrainHeight(viewId, height);
            return this;
        }

        /**
         * 某个控件设置宽度和高度
         *
         * @param viewId
         * @param height
         * @return
         */
        public ConstraintBegin setWidthAndHeight(@IdRes int viewId, int height, int width) {
            applyConstraintSet.constrainHeight(viewId, height);
            applyConstraintSet.constrainWidth(viewId, width);
            return this;
        }

        /**
         * 提交应用生效
         */
        public void commit() {
            applyConstraintSet.applyTo(constraintLayout);
        }

    }
}


