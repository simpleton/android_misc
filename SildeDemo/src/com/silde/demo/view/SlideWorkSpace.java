package com.silde.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.silde.demo.R;
import com.silde.demo.R.dimen;
import com.silde.demo.R.drawable;
import com.silde.demo.view.MainSildeView.OnScreenChangeListener;

/**
 * 
 * 左右滑动的控件
 *
 */
public class SlideWorkSpace extends ViewGroup {
	
	private static final int INVALID_SCREEN = -1;
	
	private static final int SNAP_VELOCITY = 500;
	
    private static final int MAX_DURATION_SNAP = 300;
    private static final int MAX_DURATION_OVER = 200;
	
	/**
	 * 默认侧边需要显示的大小
	 */
	public static  int mSlideLength = 70;
	
    private int mTouchSlop;
    private int mMaximumVelocity;
	
    private float mLastMotionX;

    private float mLastMotionY;
	
    private Scroller mScroller;

    private final static int TOUCH_STATE_REST = 0;

    private final static int TOUCH_STATE_SCROLLING = 1;

    private int mTouchState = TOUCH_STATE_REST;

    private VelocityTracker mVelocityTracker;
    
    private int mCurrentScreen;
    
    private int mNextScreen = INVALID_SCREEN;
    
    private ScrollingListener scrollingListener;
    
    private OnScreenChangeListener onScreenChangeListener;

    public SlideWorkSpace(Context context) {
    	super(context);
    	initView(context);
    }
    
	public SlideWorkSpace(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		
		mSlideLength = (int)context.getResources().getDimension(R.dimen.slide_length);
		
        mScroller = new Scroller(getContext(), new ElasticInterpolator());

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        
        FrameLayout frameLeft = new FrameLayout(context);
        FrameLayout frameCenter = new FrameLayout(context);
        FrameLayout frameRight = new FrameLayout(context);
        
        frameLeft.setBackgroundResource(R.drawable.slide_left_shadow);
        frameRight.setBackgroundResource(R.drawable.slide_right_shadow);
        
        addView(frameLeft);
        addView(frameCenter);
        addView(frameRight);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childLeft = 0;
		final int count = getChildCount();
		if(count != 3) {
			throw new IllegalStateException("the SlideWorkSpace must contains three child views");
		}
		
		final int childWidth = getMeasuredWidth();
		
		getChildAt(0).layout(childLeft, 0, childWidth - mSlideLength, getMeasuredHeight());
		childLeft += (childWidth - mSlideLength);
		
		getChildAt(1).layout(childLeft, 0, childLeft + childWidth, getMeasuredHeight());
		childLeft += childWidth;
		
		getChildAt(2).layout(childLeft, 0, childLeft + childWidth - mSlideLength, getMeasuredHeight());
		
	}
	
	boolean mFirstLayout = true;
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int count = getChildCount();
		if(count != 3) {
			throw new IllegalStateException("the SlideWorkSpace must contains three child views");
		}
		
		int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
		
		getChildAt(0).measure(MeasureSpec.makeMeasureSpec(widthMeasureSize - mSlideLength, widthMeasureMode), heightMeasureSpec);
		getChildAt(1).measure(widthMeasureSpec, heightMeasureSpec);
		getChildAt(2).measure(MeasureSpec.makeMeasureSpec(widthMeasureSize - mSlideLength, widthMeasureMode), heightMeasureSpec);
		
		if(mFirstLayout) {
			mCurrentScreen = 1;
			scrollTo(widthMeasureSize - mSlideLength, 0);
			mFirstLayout = false;
		}
    }
    
    boolean enableScrollFlag = true;
    boolean firstMove = true;
    
    private long onClickStartTime = 0;
    private long onClickEndTime = 0;
    private float onClickDownX = 0;
    private float onClickDownY = 0;
    private boolean onClickAble = false;

	private boolean mLeftViewAvailable = true;

	private boolean mRightViewAvailable = true;
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	
    	
        final int action = ev.getAction();

       // Log.e("","-------------------onInterceptTouchEvent = " + action);
    	
        final float x = ev.getX();
        final float y = ev.getY();
        
        switch (action) {
            case MotionEvent.ACTION_MOVE:
            	
            	if(!enableScrollFlag) return false;
            	
                final int xDiff = (int) Math.abs(x - mLastMotionX);
                final int yDiff = (int) Math.abs(y - mLastMotionY);

                final int touchSlop = mTouchSlop;
                boolean xMoved = xDiff > touchSlop  && (((long)yDiff * 1.73f) < (long)xDiff);
                boolean yMoved = yDiff > touchSlop;
                
                if(yMoved && xDiff < yDiff && firstMove) {
                	enableScrollFlag = false;
                	return false;
                }
                firstMove = false;
                
                if (xMoved || mCurrentScreen != 1) {
                    // Scroll if the user moved far enough along the X axis
                    mTouchState = TOUCH_STATE_SCROLLING;
                    enableChildrenCache();
                    if(onScreenChangeListener != null) {
                    	onScreenChangeListener.onScreenChangeStart(mCurrentScreen);
                    }
                }
                
                break;
            case MotionEvent.ACTION_DOWN:
            	
                mLastMotionX = x;
                mLastMotionY = y;
                
                firstMove = true;
                
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
                
                if(mCurrentScreen == 0) {
                	if(x < (getWidth() - mSlideLength)) {
                		enableScrollFlag = false;
                		return false;
                	} else {
                		enableScrollFlag = true;
                        onClickStartTime = System.currentTimeMillis();
                        onClickDownX = x;
                        onClickDownY = y;
                        onClickAble = true;
                		return true;
                	}
                } else if(mCurrentScreen == 2) {
					if(x > mSlideLength) {
						enableScrollFlag = false;
					    return false;		
					} else {
						enableScrollFlag = true;
		                onClickStartTime = System.currentTimeMillis();
		                onClickDownX = x;
		                onClickDownY = y;
		                onClickAble = true;
						return true;
					}
                }
                enableScrollFlag = true;
                
/*                onClickStartTime = System.currentTimeMillis();
                onClickDownX = x;
                onClickDownY = y;
                onClickAble = true;*/
                
                break;
            case MotionEvent.ACTION_UP:
            	

                mTouchState = TOUCH_STATE_REST;
                break;
        }
        return mTouchState != TOUCH_STATE_REST;

    }
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
    	//Log.e("","-------------------onTouchEvent = " + event.getAction());
    	
    	if(!enableScrollFlag) return false;

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        final int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();
       

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            	
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastMotionX = x;
                mTouchState = TOUCH_STATE_SCROLLING;
                break;
            case MotionEvent.ACTION_MOVE:
            	
                if (mTouchState == TOUCH_STATE_SCROLLING) {
                    // Scroll to follow the motion event
                    final int deltaX = (int) (mLastMotionX - x);
                    mLastMotionX = x;

                    final int scrollX = getScrollX();

                    if (deltaX < 0) {
                        if (scrollX > 0) {
                        	
                        	if(mLeftViewAvailable) {
                        		scrollBy(Math.max(-scrollX, deltaX), 0);
                            } else if(scrollX > getChildAt(0).getRight()) {
                            	scrollBy(Math.max(-(scrollX - getChildAt(0).getRight()), deltaX), 0);
                            }
                        }
                    } else if (deltaX > 0) {
                        int availableToScroll = 0;
                        if(mRightViewAvailable) {
                        	availableToScroll = getChildAt(getChildCount() - 1).getRight() - scrollX - getWidth();
                        } else {
                        	availableToScroll = getChildAt(1).getRight() - scrollX - getWidth();
                        }
                        
                        if (availableToScroll > 0) {
                            scrollBy(Math.min(availableToScroll, deltaX), 0);
                        }
                    }
                }
                
                if(Math.abs(onClickDownX - x) > 50 || Math.abs(onClickDownY - y) > 50) {
                	onClickAble = false;
                }
                
                
                break;
            case MotionEvent.ACTION_UP:
            	
            	boolean clickHandle = false;
            	long deltaTime = System.currentTimeMillis() - onClickStartTime;
            	if(mCurrentScreen != 1 && onClickAble && deltaTime < 2000) {
            		if(mCurrentScreen == 0) {
            			if(x > getWidth() - mSlideLength) {
            				snapToScreen(1);
            				clickHandle = true;
            			}
            		} else if(mCurrentScreen == 2) {
            			if(x < mSlideLength) {
            				snapToScreen(1);
            				clickHandle = true;
            			}
            		}
            	}
            	
                if (mTouchState == TOUCH_STATE_SCROLLING) {
                	
                	if(!clickHandle) {
                		final VelocityTracker velocityTracker = mVelocityTracker;
                        velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                        int velocityX = (int) velocityTracker.getXVelocity();

                        if (velocityX > SNAP_VELOCITY && mCurrentScreen > 0) {
                            // Fling hard enough to move left
                            snapToScreen(mCurrentScreen - 1);
                        } else if (velocityX < -SNAP_VELOCITY && mCurrentScreen < getChildCount() - 1) {
                            // Fling hard enough to move right
                            snapToScreen(mCurrentScreen + 1);
                        } else {
                            snapToDestination();
                        }
                	}

                    if (mVelocityTracker != null) {
                        mVelocityTracker.recycle();
                        mVelocityTracker = null;
                    }
                }
                
                break;
            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        
        if(scrollingListener != null) {
        	int scrollX = getScrollX();
        	if(scrollX == (getWidth() - mSlideLength)) {
        		scrollingListener.scrolling(scrollX, 0);
        	} else if(scrollX < (getWidth() - mSlideLength)) {
        		scrollingListener.scrolling(scrollX, -1);
        	} else {
        		scrollingListener.scrolling(scrollX, 1);
        	}
        }
        
        return true;
    }

    private void enableChildrenCache() {
    	
    	setChildrenDrawingCacheEnabled(true);
    	setChildrenDrawnWithCacheEnabled(true);
    }
    
    private void disableChildrenCache() {
    	setChildrenDrawingCacheEnabled(false);
    	setChildrenDrawnWithCacheEnabled(false);
    }
    
    public void snapToScreen(int whichScreen) {

        //enableChildrenCache();

        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        
        if(whichScreen == 0 && !mLeftViewAvailable) return;
        
        if(whichScreen == 2 && !mRightViewAvailable) return;
        
        boolean changingScreens = whichScreen != mCurrentScreen;
        
        mNextScreen = whichScreen;
        
/*        View focusedChild = getFocusedChild();
        if (focusedChild != null && changingScreens && focusedChild == getChildAt(mCurrentScreen)) {
            focusedChild.clearFocus();
        }*/
        
        /*final int newX = whichScreen * getWidth();
        final int delta = newX - getScrollX();
        int duration = 0;*/
        
        int newX = 0;
        if(whichScreen == 1) {
        	newX = getWidth() - mSlideLength;
        } else if(whichScreen == 2) {
        	newX = (getWidth() - mSlideLength) * 2;
        }
        final int delta = newX - getScrollX();
        int durationOffset = 1;
        if (!changingScreens) {
			durationOffset = 50;
		}
        
        int duration = Math.abs(delta*2)+ durationOffset;
        duration = duration>MAX_DURATION_OVER?MAX_DURATION_OVER:duration;
        
        mSpringBack = false;
        mScroller.startScroll(getScrollX(), 0, delta, 0, duration);
        
        invalidate();

    }
    
    public void snapToScreenSpringBack(int whichScreen) {

        //enableChildrenCache();

        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        
        if(whichScreen == 0 && !mLeftViewAvailable) return;
        
        if(whichScreen == 2 && !mRightViewAvailable) return;
        
        boolean changingScreens = whichScreen != mCurrentScreen;
        
        mNextScreen = whichScreen;
        
        View focusedChild = getFocusedChild();
        if (focusedChild != null && changingScreens && focusedChild == getChildAt(mCurrentScreen)) {
            focusedChild.clearFocus();
        }
        
        /*final int newX = whichScreen * getWidth();
        final int delta = newX - getScrollX();
        int duration = 0;*/
        
        int newX = 0;
        if(whichScreen == 1) {
        	newX = getWidth() - mSlideLength;
        } else if(whichScreen == 2) {
        	newX = (getWidth() - mSlideLength) * 2;
        }
        final int delta = newX - getScrollX();
        int durationOffset = 1;
        if (!changingScreens) {
			durationOffset = 200;
		}
        
        int duration = Math.abs(delta*2)+ durationOffset;
        duration = duration>600?600:duration;
        
        mSpringBack = true;
        mTension = (float)mSlideLength / (float)getWidth() + 0.2f;
        mScroller.startScroll(getScrollX(), 0, delta, 0, duration);
        
        invalidate();

    }
    
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            
            if(scrollingListener != null) {
            	int scrollX = getScrollX();
            	if(scrollX == (getWidth() - mSlideLength)) {
            		scrollingListener.scrolling(scrollX, 0);
            	} else if(scrollX < (getWidth() - mSlideLength)) {
            		scrollingListener.scrolling(scrollX, -1);
            	} else {
            		scrollingListener.scrolling(scrollX, 1);
            	}
            }
            
            postInvalidate();
        } else if (mNextScreen != INVALID_SCREEN) {
            int oldScreenPos = mCurrentScreen;
            mCurrentScreen = Math.max(0, Math.min(mNextScreen, getChildCount() - 1));
            mNextScreen = INVALID_SCREEN;
            if(onScreenChangeListener != null) {
            	onScreenChangeListener.onScreenChangeEnd(oldScreenPos != mCurrentScreen, mCurrentScreen);
            
            }
        }
    }
    
    @Override
    public boolean dispatchTrackballEvent(MotionEvent event) {
    	if(mCurrentScreen != 1) {
    		return true;
    	} 
    	return super.dispatchTrackballEvent(event);
    }
    
    public void snapToDestination() {
        final int screenWidth = getWidth();
        final int whichScreen = (getScrollX() + (screenWidth / 2)) / screenWidth;

        snapToScreen(whichScreen);
    }
    
    public void setScrollingListener(ScrollingListener listener) {
    	scrollingListener = listener;
    }
    
	public static  float mTension;
	public static boolean mSpringBack = false;
	
    public class ElasticInterpolator implements Interpolator {

    	/**
         * Constructor
         * 
         * @param factor Degree to which the animation should be eased. Seting factor to 1.0f produces
         *        an upside-down y=x^2 parabola. Increasing factor above 1.0f makes exaggerates the
         *        ease-out effect (i.e., it starts even faster and ends evens slower)
         */
        public ElasticInterpolator() {
        }
           
        public float getInterpolation(float t) {
        	if(mSpringBack) {
        		float x = (1.0f + 2.0f * mTension ) * t - 2.0f * mTension;
        		if(x < -mTension) {
        			x = -(mTension * 2 + x); 
        		}
        		return x;
        	}
        	return t;
        }
    }
    
    public interface ScrollingListener {
    	/**
    	 * 当前偏移量，还有滑动方向（-1为左边，0为正中间，1为显示右边
    	 * @param scrollX
    	 * @param direction
    	 */
    	void scrolling(int scrollX, int direction);
    }

	public void setCenterView(View v) {
		removeViewAt(1);
		addView(v, 1);
	}
	
	public int getCurrentScreenPos() {
		return mCurrentScreen;
	}
	
    public void setOnScreenChangeListener(OnScreenChangeListener listener) {
    	onScreenChangeListener = listener;
    }
    
    /**
     * 设置是否可以滑动到左屏幕
     * @param enable
     */
    public void setLeftViewAvailable(boolean enable) {
    	mLeftViewAvailable = enable;
    }
    
    /**
     * 设置是否可以滑动到右屏幕
     * @param enable
     */
    public void setRightViewAvailable(boolean enable) {
    	mRightViewAvailable = enable;
    }
    
    
}
