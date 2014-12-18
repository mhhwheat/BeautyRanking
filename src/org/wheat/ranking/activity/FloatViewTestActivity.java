/** 
 * description£º
 * @author wheat
 * date: 2014-12-17  
 * time: ÏÂÎç2:25:34
 */ 
package org.wheat.ranking.activity;

import org.wheat.beautyranking.R;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

/** 
 * description:
 * @author wheat
 * date: 2014-12-17  
 * time: ÏÂÎç2:25:34
 */
public class FloatViewTestActivity extends Activity
{
	private WindowManager wm;
	private WindowManager.LayoutParams wmParams;
	
	private ImageView ivPublishButton;
	
	private int mAlpha =0;
	private boolean isHide;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.beauty_personal_page_list_item);
		initFloatView();
	}
	
	private void initFloatView()
	{
		wm=(WindowManager)getApplicationContext().getSystemService("window");
		wmParams=new WindowManager.LayoutParams();
		wmParams.height=50;
		wmParams.width=50;
		wmParams.type=LayoutParams.TYPE_PHONE;
		wmParams.format=PixelFormat.RGBA_8888;
		
		wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL|LayoutParams.FLAG_NOT_FOCUSABLE;
		
		wmParams.x=0;
		wmParams.y=0;
		createFloatView();
		showFloatView();
		
	}
	
	private void createFloatView()
	{
		ivPublishButton=new ImageView(this);
		ivPublishButton.setImageResource(R.drawable.praise_select);
		ivPublishButton.setAlpha(0);
		
		wmParams.gravity=Gravity.RIGHT|Gravity.BOTTOM;
		wm.addView(ivPublishButton, wmParams);
	}
	
	private Handler mHandler =new Handler()
	{
		
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==1&&mAlpha<255)
			{
				mAlpha+=50;
				if(mAlpha>255)
					mAlpha=255;
				ivPublishButton.setAlpha(mAlpha);
				ivPublishButton.invalidate();
				if(!isHide&&mAlpha<255)
				{
					mHandler.sendEmptyMessageDelayed(1, 100);
				}		
			}
			else if(msg.what==0&&mAlpha>0)
			{
				mAlpha-=10;
				if(mAlpha<0)
					mAlpha=0;
				ivPublishButton.setAlpha(mAlpha);
				if(isHide&&mAlpha>0)
					mHandler.sendEmptyMessageDelayed(0, 100);
			}
		}
	};
	
	private void showFloatView()
	{
		isHide=false;
		mHandler.sendEmptyMessage(1);
	}
	
	private void hideFloatView()
	{
		new Thread()
		{
			public void run()
			{
				try{
					Thread.sleep(1500);
					isHide=true;
					mHandler.sendEmptyMessage(0);
				}catch(Exception e){}
			}
		}.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch(event.getAction())
		{
		case MotionEvent.ACTION_MOVE:
		case MotionEvent.ACTION_DOWN:
			showFloatView();
			System.out.println("show FloatView");;
			break;
		case MotionEvent.ACTION_UP:
			hideFloatView();
			System.out.println("hide floatView");
			break;
			
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		wm.removeView(ivPublishButton);
	}
	
	
}
