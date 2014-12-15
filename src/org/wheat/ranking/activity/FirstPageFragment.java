package org.wheat.ranking.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.wheat.beautyranking.R;
import org.wheat.ranking.adapter.RankingFragmentPagerAdapter;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * description:首页标签对应的Fragment(包括FollowFragment,NeighborFragment,MyCreatedFragment)
 * @author wheat
 * date: 2014-12-15  
 * time: 上午9:41:54
 */
public class FirstPageFragment extends Fragment
{
	private final int pageCount=3;
	private ViewPager mViewPager;
	private ArrayList<Fragment> fragmentList;
	private ImageView imageCusor;
	private TextView tabSum,tabNew,tabRise;
	private int currIndex;//当前页编号
	private int offset;
	
	//保存inflate的view
	private View mView;

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView=inflater.inflate(R.layout.first_page_layout, container, false);
		
		System.out.println("------------->Initial");
		InitTextView();
		System.out.println("------------->Initial Text View");
		InitImageCusor();
		Log.w("RankingListActivity", "Initial ViewPager");
		InitViewPager();
		
		return mView;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		try {  
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");  
            childFragmentManager.setAccessible(true);  
            childFragmentManager.set(this, null);  
  
        } catch (NoSuchFieldException e) {  
            throw new RuntimeException(e);  
        } catch (IllegalAccessException e) {  
            throw new RuntimeException(e);  
        }
	}
	
	private void InitTextView()
	{
		tabSum=(TextView)mView.findViewById(R.id.tab_follow);
		tabNew=(TextView)mView.findViewById(R.id.tab_neighbor);
		tabRise=(TextView)mView.findViewById(R.id.tab_my_created);
		
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
		imageCusor=(ImageView)mView.findViewById(R.id.ranking_list_cursor);
		DisplayMetrics dm=new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
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
		mViewPager=(ViewPager)mView.findViewById(R.id.first_page_pager);
		fragmentList=new ArrayList<Fragment>();
		Fragment mFollowFragment=new FollowFragment();
		Fragment mNeighborFragment=new NeighborFragment();
		Fragment mMyCreatedFragment=new MyCreatedFragment();
		fragmentList.add(mFollowFragment);
		fragmentList.add(mNeighborFragment);
		fragmentList.add(mMyCreatedFragment);
		
		
		//给ViewPager设置适配器
		mViewPager.setAdapter(new RankingFragmentPagerAdapter(this.getChildFragmentManager(), fragmentList));
		mViewPager.setCurrentItem(0);//设置当前显示标签页为第一页 
		mViewPager.setOnPageChangeListener(new FirstPageChangeListener());//页面变化时的监听器  
	}
	
	public class FirstPageChangeListener implements OnPageChangeListener
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
