package org.wheat.ranking.activity;

import org.wheat.beautyranking.R;
import org.wheat.ranking.checkUpdate.SettingPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * description:主界面，包含RankingListFragment,MainPageFragment,MinePageFragment
 * @author wheat
 * date: 2014-12-11  
 * time: 下午9:23:54
 */
public class MainInterfaceActivity extends FragmentActivity
{
	
	private Fragment mFirstPageFragment;
	private Fragment mFindPageFragment;
	private Fragment mMinePageFragment;
	
	private View mFirstPageLayout;
	private View mFindPageLayout;
	private View mMinePageLayout;
	private View mCreateBeautyLayout;
	
	private ViewGroup mTitleContainer;
	

	private int mCurrentCheckID=R.id.tab_first_page;
//	ImageView settingImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);//请求设置标题栏
		setContentView(R.layout.main_user_interface);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mycustomtitle);
//		LayoutInflater inflater = this.getLayoutInflater();
//		View mypageTitle=inflater.inflate(R.layout.mypage_title, null);
//		settingImg= (ImageView)findViewById(R.id.setting_img);
//		settingImg.setOnClickListener(new SettingClickListener());
		
		mTitleContainer=(ViewGroup)findViewById(getTitleContainerId());
		
		initialTab();
		checkTab(mCurrentCheckID);
		
	}
	
	public void initialTab()
	{
		mFirstPageLayout=(LinearLayout)findViewById(R.id.tab_first_page);
		mFindPageLayout=findViewById(R.id.tab_find_page);
		mMinePageLayout=findViewById(R.id.tab_mine_page);
		mCreateBeautyLayout=findViewById(R.id.tab_create_beauty);

		mFirstPageLayout.setOnClickListener(new TabOnClickListener());

		mFindPageLayout.setOnClickListener(new TabOnClickListener());

		mMinePageLayout.setOnClickListener(new TabOnClickListener());

		mCreateBeautyLayout.setOnClickListener(new TabOnClickListener());

	}
	
	public class TabOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			if(v.getId()!=mCurrentCheckID)
			{
				unCheckTab(mCurrentCheckID);
				mCurrentCheckID=v.getId();
				checkTab(mCurrentCheckID);
			}
		}
		
	}
	
	/**
	 * 
	 * @param tabID
	 */
	public void checkTab(int tabID)
	{
		View v=findViewById(tabID);
		switch(tabID)
		{
		case R.id.tab_first_page:
			ImageView mFirstPageImg=(ImageView)v.findViewById(R.id.tab_first_page_img);
			TextView mFirstPageText=(TextView)v.findViewById(R.id.tab_first_page_text);

			mFirstPageImg.setImageResource(R.drawable.shouyefull);
			mFirstPageText.setTextColor(this.getResources().getColor(R.color.tab_check_color_more));
			
			if(mFirstPageFragment==null)
			{
				Log.w("MainInterfaceActivity", "mFirstPageFragment is null!");
				mFirstPageFragment=new FirstPageFragment();
			}
			replaceFragment(mFirstPageFragment, R.id.replacing_fragment);
			break;
		case R.id.tab_find_page:
			ImageView mFindPageImg=(ImageView)v.findViewById(R.id.tab_find_page_img);
			TextView mFindPageText=(TextView)v.findViewById(R.id.tab_find_page_text);

			mFindPageImg.setImageResource(R.drawable.foundfull);
			mFindPageText.setTextColor(this.getResources().getColor(R.color.tab_check_color_more));
			
			if(mFindPageFragment==null)
				mFindPageFragment=new RankingListFragment();
			replaceFragment(mFindPageFragment, R.id.replacing_fragment);
			break;
		case R.id.tab_mine_page:
			mTitleContainer.removeAllViews();
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mypage_title);
			ImageView settingImg=(ImageView)findViewById(R.id.setting_img);
			settingImg.setOnClickListener(new SettingClickListener());
			
			ImageView mMinePageImg=(ImageView)v.findViewById(R.id.tab_mine_page_img);
			TextView mMinePageText=(TextView)v.findViewById(R.id.tab_mine_page_text);

			mMinePageImg.setImageResource(R.drawable.me2full);
			mMinePageText.setTextColor(this.getResources().getColor(R.color.tab_check_color_more));
			
			if(mMinePageFragment==null)
				mMinePageFragment=new MyDetailPage();
			replaceFragment(mMinePageFragment, R.id.replacing_fragment);
			break;
		case R.id.tab_create_beauty:
			ImageView mCreateBeautyImg=(ImageView)v.findViewById(R.id.tab_create_beauty_img);
			TextView mCreateBeautyText=(TextView)v.findViewById(R.id.tab_create_beauty_text);

			mCreateBeautyImg.setImageResource(R.drawable.fabufull);
			mCreateBeautyText.setTextColor(this.getResources().getColor(R.color.tab_check_color_more));
			
			Intent createIntent= new Intent();
			createIntent.setClass(MainInterfaceActivity.this, CreateBeauty.class);
			startActivity(createIntent);
			break;
		}
	}                                                              
	
	private class SettingClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent settingIntent = new Intent();
			settingIntent.setClass(MainInterfaceActivity.this,SettingPage.class);
			startActivity(settingIntent);
		}
		
	}
	/**
	 * 
	 * @param tabID
	 */
	public void unCheckTab(int tabID)
	{
		View v=findViewById(tabID);
		switch(tabID)
		{
		case R.id.tab_first_page:
			ImageView mFirstPageImg=(ImageView)v.findViewById(R.id.tab_first_page_img);
			TextView mFirstPageText=(TextView)v.findViewById(R.id.tab_first_page_text);

			mFirstPageImg.setImageResource(R.drawable.shouye);
			mFirstPageText.setTextColor(this.getResources().getColor(R.color.tab_uncheck_color));
			break;
		case R.id.tab_find_page:
			ImageView mFindPageImg=(ImageView)v.findViewById(R.id.tab_find_page_img);
			TextView mFindPageText=(TextView)v.findViewById(R.id.tab_find_page_text);

			mFindPageImg.setImageResource(R.drawable.found);
			mFindPageText.setTextColor(this.getResources().getColor(R.color.tab_uncheck_color));
			break;
		case R.id.tab_mine_page:
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mycustomtitle);
			ImageView mMinePageImg=(ImageView)v.findViewById(R.id.tab_mine_page_img);
			TextView mMinePageText=(TextView)v.findViewById(R.id.tab_mine_page_text);

			mMinePageImg.setImageResource(R.drawable.me2);
			mMinePageText.setTextColor(this.getResources().getColor(R.color.tab_uncheck_color));
			break;
		case R.id.tab_create_beauty:
			ImageView mCreateBeautyImg=(ImageView)v.findViewById(R.id.tab_create_beauty_img);
			TextView mCreateBeautyText=(TextView)v.findViewById(R.id.tab_create_beauty_text);

			mCreateBeautyImg.setImageResource(R.drawable.fabu);
			mCreateBeautyText.setTextColor(this.getResources().getColor(R.color.tab_uncheck_color));
			break;
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
	
	
	
	
	
	public static Object reflactFiled(String className, String filedName){
    	Object result = null;
		try {
			result = Class.forName(className).getField(filedName).get(null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
    }
	
	protected int getTitleContainerId(){
		Object obj = reflactFiled("com.android.internal.R$id", "title_container");
		if(obj != null){
			return (Integer) obj;
		}
		else{
			return -1;
		}
	}
	
	
}
