package org.wheat.ranking.activity;

import java.util.ArrayList;

import org.wheat.beautyranking.R;
import org.wheat.ranking.adapter.RankingFragmentPagerAdapter;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RankingListActivity extends FragmentActivity 
{
	private final int pageCount=3;
	private ViewPager mViewPager;
	private ArrayList<Fragment> fragmentList;
	private ImageView imageCusor;
	private TextView tabSum,tabNew,tabRise;
	private int currIndex;//当前页编号
	private int bmpWidth;
	private int offset;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		//定制activity的title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.ranking_list_layout);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.ranking_list_title);
		//-------------------------------------------------------------------------------------
		
		System.out.println("------------->Initial");
		InitTextView();
		System.out.println("------------->Initial Text View");
		InitImageCusor();
		Log.w("RankingListActivity", "Initial ViewPager");
		InitViewPager();
	}
	private void InitTextView()
	{
		tabSum=(TextView)findViewById(R.id.tab_sum);
		tabNew=(TextView)findViewById(R.id.tab_new);
		tabRise=(TextView)findViewById(R.id.tab_rise);
		
		tabSum.setOnClickListener(new txListener(0));
		tabNew.setOnClickListener(new txListener(1));
		tabRise.setOnClickListener(new txListener(2));
	}
	public class txListener implements View.OnClickListener
	{
		private int index;
		public txListener(int index)
		{
			this.index=index;
		}
		@Override
		public void onClick(View v) 
		{
			mViewPager.setCurrentItem(index); 
		}
	}
	
	/*
	 * 初始化图片的位移像素
	 */
	private void InitImageCusor()
	{
		imageCusor=(ImageView)findViewById(R.id.ranking_list_cursor);
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth=dm.widthPixels;
		offset=screenWidth/pageCount;
		
		//重新设置ImageView的高和宽
		imageCusor.setLayoutParams(new LinearLayout.LayoutParams(offset, 4));
		
		//imgageview设置平移，使下划线平移到初始位置
		Matrix matrix=new Matrix();
		matrix.postTranslate(0, 0);
		imageCusor.setImageMatrix(matrix);
	}
	
	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager()
	{
		mViewPager=(ViewPager)findViewById(R.id.ranking_list_pager);
		fragmentList=new ArrayList<Fragment>();
		Fragment tabNewFragment=new TabNewFragment();
		Fragment tabRiseFragment=new TabRiseFragment();
		Fragment tabSumFragment=new TabSumFragment();
		fragmentList.add(tabSumFragment);
		fragmentList.add(tabNewFragment);
		fragmentList.add(tabRiseFragment);
		
		
		//给ViewPager设置适配器
		mViewPager.setAdapter(new RankingFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
		mViewPager.setCurrentItem(0);//设置当前显示标签页为第一页 
		mViewPager.setOnPageChangeListener(new RankingPageChangeListener());//页面变化时的监听器  
	}
	
	public class RankingPageChangeListener implements OnPageChangeListener
	{
		private int one=offset;
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			Animation animation=new TranslateAnimation(currIndex*one, arg0*one, 0, 0);//平移动画
			currIndex=arg0;
			animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
			animation.setDuration(200);//是用ImageView来显示动画的
			imageCusor.startAnimation(animation);
		}
		
	}
	
	
	
	
}
