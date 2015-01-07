/** 
 * description：
 * @author wheat
 * date: 2014-12-25  
 * time: 下午8:50:54
 */ 
package org.wheat.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

/** 
 * description:
 * @author wheat
 * date: 2014-12-25  
 * time: 下午8:50:54
 */
public class QuickReturnRelativeLayout extends RelativeLayout
{
	private QuickReturnRelativeLayout mQuickReturnRelativeLayout=this;
	
	/**
	 * 待添加的QuickReturnView
	 */
	private View mQuickReturnView=null;
	
	/**
	 * 当前实例的QuickRetuanRelativeLayout的高度
	 */
	private float mQuickReturnRelativeLayoutHeight;
	
	/**
	 * QuickReturnView的高度
	 */
	private float mQuickReturnViewHeight;
	
	/**
	 * 前一个MotionEvent触发的x和y坐标
	 */
	private float mLastEventPositionY;
	private float mLastEventPositionX;
	
	/**
	 * 当前MotionEvent触发的x和y坐标
	 */
	private float mCurrentEventPositionY;
	private float mCurrentEventPositionX;
	
	
	/**
	 * QuickReturnView应该出现的位置的Y轴的大小
	 */
	private float mWantedPositionY;
	
	/**
	 * QuickReturnView完全显示在屏幕QuickReturnLinearLayout之上
	 */
	private static final int STATE_ONSCREEN = 0;
	
	/**
	 * QuickReturnView显示在QuickReturnLinearLayout之下
	 */
	private static final int STATE_OFFSCREEN = 1;
	
	/**
	 * QuickReturnView正在从离开QuickReturnLinearLayout的状态到逐渐回到QuickReturnLinearLayout
	 */
	private static final int STATE_RETURNING = 2;
	
	/**
	 * QuickReturnView的位置状态
	 */
	private int mState = STATE_ONSCREEN;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public QuickReturnRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener());
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public QuickReturnRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener());
	}

	/**
	 * @param context
	 */
	public QuickReturnRelativeLayout(Context context) {
		super(context);
		this.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener());
	}
	

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(ev.getAction()==MotionEvent.ACTION_DOWN)
		{
			mCurrentEventPositionX=ev.getX();
			mCurrentEventPositionY=ev.getY();
			onScrollGesture(0, 0);
		}
		else
		{
			mLastEventPositionX=mCurrentEventPositionX;
			mLastEventPositionY=mCurrentEventPositionY;
			mCurrentEventPositionX=ev.getX();
			mCurrentEventPositionY=ev.getY();
			onScrollGesture(mCurrentEventPositionX-mLastEventPositionX, mCurrentEventPositionY-mLastEventPositionY);
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 添加QuickReturnView,并把QuickReturnView置于QuickReturnRelativeLayout的底部
	 * @param view
	 */
	public void addQuickReturnView(View view)
	{
		this.mQuickReturnView=view;
		view.measure(0, 0);
		mQuickReturnViewHeight=mQuickReturnView.getMeasuredHeight();
		LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		view.setLayoutParams(params);
		this.addView(view);
	}
	
	/**
	 * 监听viewTree的Listener，主要为了获取QuickReturnRelativeLayout的真实高度
	 * description:
	 * @author wheat
	 * date: 2014-12-29  
	 * time: 下午12:41:34
	 */
	public class OnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener
	{
		@Override
		public void onGlobalLayout() {
			mQuickReturnRelativeLayoutHeight=mQuickReturnRelativeLayout.getHeight();
			if(mQuickReturnView!=null)
			{
				mWantedPositionY=mQuickReturnRelativeLayoutHeight-mQuickReturnViewHeight;
			}
			mQuickReturnRelativeLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		}
		
	}
	
	
	/**
	 * QuickReturnView的Y轴位置发生改变，重新设置QuickReturnView的位置
	 */
	public void notifyQuickReturnViewTranslationYChange()
	{
		float mTranslationY=mWantedPositionY-mQuickReturnView.getTop();
		
		/** this can be used if the build is below honeycomb **/
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
			TranslateAnimation anim = new TranslateAnimation(0, 0, mTranslationY,
					mTranslationY);
			anim.setFillAfter(true);
			anim.setDuration(0);
			mQuickReturnView.startAnimation(anim);
		} else {
			mQuickReturnView.setTranslationY(mTranslationY);
		}
	}
	
	public void onScrollGesture(float distanceX,float distanceY)
	{
		switch(mState)
		{
		case STATE_OFFSCREEN:
			if(distanceY>0)
			{
				mWantedPositionY=mQuickReturnRelativeLayoutHeight-distanceY;
				if(mWantedPositionY<(mQuickReturnRelativeLayoutHeight-mQuickReturnViewHeight))
				{
					mWantedPositionY=mQuickReturnRelativeLayoutHeight-mQuickReturnViewHeight;
					mState=STATE_ONSCREEN;
				}
				else
					mState=STATE_RETURNING;
			}
			else
				mWantedPositionY=mQuickReturnRelativeLayoutHeight;
			break;
		case STATE_RETURNING:
			if(distanceY>0)
			{
				mWantedPositionY=mWantedPositionY-distanceY;
				if(mWantedPositionY<(mQuickReturnRelativeLayoutHeight-mQuickReturnViewHeight))
				{
					mWantedPositionY=mQuickReturnRelativeLayoutHeight-mQuickReturnViewHeight;
					mState=STATE_ONSCREEN;
				}
			}
			else
			{
				mWantedPositionY=mWantedPositionY-distanceY;
				if(mWantedPositionY>mQuickReturnRelativeLayoutHeight)
				{
					mWantedPositionY=mQuickReturnRelativeLayoutHeight;
					mState=STATE_OFFSCREEN;
				}
			}
			break;
		case STATE_ONSCREEN:
			if(distanceY<0)
			{
				mWantedPositionY-=distanceY;
				if(mWantedPositionY>mQuickReturnRelativeLayoutHeight)
				{
					mWantedPositionY=mQuickReturnRelativeLayoutHeight;
					mState=STATE_OFFSCREEN;
				}
				else
				{
					mState=STATE_RETURNING;
				}
			}
			break;
			
		}
		
		if(mQuickReturnView!=null)
			notifyQuickReturnViewTranslationYChange();
	}
	
}
