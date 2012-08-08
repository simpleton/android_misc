package com.silde.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.silde.demo.R;

/**
 * 
 * 主界面框架
 *
 */
public class MainSildeView extends RelativeLayout implements SlideWorkSpace.ScrollingListener {
	
	public static final int FLAG_SCREEN_LEFT = 0;
	public static final int FLAG_SCREEN_CENTER = 1;
	public static final int FLAG_SCREEN_RIGHT = 2;

    private FrameLayout mLeftView;

    private FrameLayout mRightView;

    private SlideWorkSpace mCenterView;

    public MainSildeView(Context context) {
        super(context);
        initView(context);
    }

    public MainSildeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mLeftView = new FrameLayout(context);
        mRightView = new FrameLayout(context);
        mCenterView = new SlideWorkSpace(context);

        mCenterView.setScrollingListener(this);

        RelativeLayout.LayoutParams lpLeft = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
            RelativeLayout.LayoutParams.FILL_PARENT);
        RelativeLayout.LayoutParams lpCenter = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
            RelativeLayout.LayoutParams.FILL_PARENT);
        RelativeLayout.LayoutParams lpRight = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
            RelativeLayout.LayoutParams.FILL_PARENT);

        lpRight.setMargins(SlideWorkSpace.mSlideLength, 0, 0, 0);

        addView(mLeftView, lpLeft);
        addView(mRightView, lpRight);
        addView(mCenterView, lpCenter);

    }

    @Override
    public void scrolling(int scrollX, int direction) {
        if (direction == -1) {
            mLeftView.setVisibility(View.VISIBLE);
            mRightView.setVisibility(View.INVISIBLE);
        } else if (direction == 1) {
            mLeftView.setVisibility(View.INVISIBLE);
            mRightView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置左边显示的View
     * @param v
     */
    public void setLeftView(View v) {
    	
    	mLeftView.removeAllViews();
    	mLeftView.addView(v, FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT);
    	
/*        removeViewAt(0);
        		RelativeLayout.LayoutParams lpLeft = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        		lpLeft.setMargins(0, 0, SlideWorkSpace.mSlideLength, 0);

        addView(v, 0);

        RelativeLayout.LayoutParams lpLeft = (RelativeLayout.LayoutParams) v.getLayoutParams();
        lpLeft.height = RelativeLayout.LayoutParams.FILL_PARENT;
        lpLeft.width = RelativeLayout.LayoutParams.FILL_PARENT;
        lpLeft.setMargins(0, 0, SlideWorkSpace.mSlideLength, 0);*/
    }

    /**
     * 设置右边显示的View
     * @param v
     */
    public void setRightView(View v) {
    	mRightView.removeAllViews();
    	mRightView.addView(v, FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT);

        /*		RelativeLayout.LayoutParams lpLeft = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        		lpLeft.setMargins(0, 0, SlideWorkSpace.mSlideLength, 0);*/

        /*addView(v, 1);

        RelativeLayout.LayoutParams lpRight = (RelativeLayout.LayoutParams) v.getLayoutParams();
        lpRight.height = RelativeLayout.LayoutParams.FILL_PARENT;
        lpRight.width = RelativeLayout.LayoutParams.FILL_PARENT;
        lpRight.setMargins(SlideWorkSpace.mSlideLength, 0, 0, 0);*/

    }

    /**
     * 设置中间显示View的内容
     * @param v
     */
    public void setCenterView(View v) {
        mCenterView.setCenterView(v);
    }

    /**
     * 移动到中间屏幕
     */
    public void toCenterView() {
    	 toCenterView(false);
    }
    
    /**
     ** 移动到中间屏幕
     * @param enableSpringBack 是否需要回弹效果
     */
    public void toCenterView(boolean enableSpringBack) {
        int curPos = mCenterView.getCurrentScreenPos();
        if (curPos == 1)
            return;
        if(enableSpringBack) {
        	mCenterView.snapToScreenSpringBack(1);
        } else {
            mCenterView.snapToScreen(1);
        }
    }

    /**
     * 移动到左边屏幕
     */
    public void toLeftView() {
        int curPos = mCenterView.getCurrentScreenPos();
        if (curPos == 0) {
            return;
        } else if (curPos == 1) {
            mCenterView.snapToScreen(0);
        } else {
            mCenterView.snapToScreen(1);
        }
    }

    /**
     * 移动到右边屏幕
     */
    public void toRightView() {
        int curPos = mCenterView.getCurrentScreenPos();
        if (curPos == 0) {
            mCenterView.snapToScreen(1);
        } else if (curPos == 1) {
            mCenterView.snapToScreen(2);
        } else {
            return;
        }
    }
    
    /**
     * 返回当前所在屏幕
     * 左屏  FLAG_SCREEN_LEFT;
     * 中屏  FLAG_SCREEN_CENTER;
     * 右屏  FLAG_SCREEN_RIGHT;
     * @return
     */
    public int getCurrentPos() {
    	return mCenterView.getCurrentScreenPos();
    }

    public void setOnScreenChangeListener(OnScreenChangeListener listener) {
    	mCenterView.setOnScreenChangeListener(listener);
    }
    
    /**
     * 监听当前满屏幕的切换
     * 左屏  FLAG_SCREEN_LEFT;
     * 中屏  FLAG_SCREEN_CENTER;
     * 右屏  FLAG_SCREEN_RIGHT;
     * @author leozhan
     *
     */
	public interface OnScreenChangeListener {
		void onScreenChangeStart(int whichScreen);
		void onScreenChangeEnd(boolean change, int whichScreen);
	}
    
/*    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	if(getCurrentPos() != FLAG_SCREEN_CENTER) {
        		toCenterView();
        		return true;
        	}
        }
    	return super.onKeyUp(keyCode, event);
    }*/
    
    /**
     * 设置是否可以滑动到左屏幕
     * @param enable
     */
    public void setLeftViewAvailable(boolean enable) {
    	mCenterView.setLeftViewAvailable(enable);
    }
    
    /**
     * 设置是否可以滑动到右屏幕
     * @param enable
     */
    public void setRightViewAvailable(boolean enable) {
    	mCenterView.setRightViewAvailable(enable);
    }

}
