package org.wheat.ranking.activity;

import org.wheat.beautyranking.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * description:主界面，包含RankingListFragment,MainPageFragment,MinePageFragment
 * @author wheat
 * date: 2014-12-11  
 * time: 下午9:23:54
 */
public class MainInterfaceActivity extends FragmentActivity
{
	private RadioGroup mRadioGroup;
	private int radioLen=-1;
	
	private Fragment mFirstPageFragment;
	private Fragment mFindPageFragment;
	private Fragment mMinePageFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);//请求设置标题栏
		setContentView(R.layout.main_user_interface);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mycustomtitle);
		mRadioGroup=(RadioGroup)findViewById(R.id.main_radio);
		radioLen=mRadioGroup.getChildCount();
//		mRadioGroup.setOnClickListener(new RadioOnClickListener());
		mRadioGroup.setOnCheckedChangeListener(new CheckedChangeListener());
		
		//把首页设置为初始化页面
		mRadioGroup.check(R.id.rb_first_page);
	}
//	public class RadioOnClickListener implements OnClickListener{
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			for(int i=0;i<radioLen;i++){
//				
//			}
//		}
//		
//	}
	public class CheckedChangeListener implements OnCheckedChangeListener
	{
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) 
		{
			switch(checkedId)
			{
			case R.id.rb_first_page:
				if(mFirstPageFragment==null)
				{
					Log.w("MainInterfaceActivity", "mFirstPageFragment is null!");
					mFirstPageFragment=new FirstPageFragment();
				}
				replaceFragment(mFirstPageFragment, R.id.replacing_fragment);
				break;
			case R.id.rb_find_page:
				if(mFindPageFragment==null)
					mFindPageFragment=new RankingListFragment();
				replaceFragment(mFindPageFragment, R.id.replacing_fragment);
				break;
			case R.id.rb_mine_page:
				if(mMinePageFragment==null)
					mMinePageFragment=new MyDetailPage();
				replaceFragment(mMinePageFragment, R.id.replacing_fragment);
				break;
			case R.id.rb_create_beauty:
				Intent createIntent= new Intent();
				createIntent.setClass(MainInterfaceActivity.this, CreateBeauty.class);
				startActivity(createIntent);
				break;
			}
		}
		
	}
	
	/**
	 * 用targetFragment去替换布局中id为source的布局
	 * @param targetFragment 替换的Fragment
	 * @param source 被替换的布局资源,该布局必须为FrameLayout
	 */
	public void replaceFragment(Fragment targetFragment,int source)
	{
		getSupportFragmentManager().beginTransaction().
		replace(source, targetFragment).commit();
	}
	
	
	
}
