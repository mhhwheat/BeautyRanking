package org.wheat.ranking.activity;

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

public class RankingListFragment extends Fragment
{
	private final int pageCount=3;
	private ViewPager mViewPager;
	private ArrayList<Fragment> fragmentList;
	private ImageView imageCusor;
	private TextView tabSum,tabNew,tabRise;
	private int currIndex;//��ǰҳ���
	private int offset;
	
	//����inflate��view
	private View mView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView=inflater.inflate(R.layout.ranking_list_layout, container, false);
		
		System.out.println("------------->Initial");
		InitTextView();
		System.out.println("------------->Initial Text View");
		InitImageCusor();
		Log.w("RankingListActivity", "Initial ViewPager");
		InitViewPager();
		
		return mView;
	}
	
	private void InitTextView()
	{
		tabSum=(TextView)mView.findViewById(R.id.tab_sum);
		tabNew=(TextView)mView.findViewById(R.id.tab_new);
		tabRise=(TextView)mView.findViewById(R.id.tab_rise);
		
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
	 * ��ʼ��ͼƬ��λ������
	 */
	private void InitImageCusor()
	{
		imageCusor=(ImageView)mView.findViewById(R.id.ranking_list_cursor);
		DisplayMetrics dm=new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth=dm.widthPixels;
		offset=screenWidth/pageCount;
		
		//��������ImageView�ĸߺͿ�
		imageCusor.setLayoutParams(new LinearLayout.LayoutParams(offset, 4));
		
		//imgageview����ƽ�ƣ�ʹ�»���ƽ�Ƶ���ʼλ��
		Matrix matrix=new Matrix();
		matrix.postTranslate(0, 0);
		imageCusor.setImageMatrix(matrix);
	}
	
	/**
	 * ��ʼ��ViewPager
	 */
	private void InitViewPager()
	{
		mViewPager=(ViewPager)mView.findViewById(R.id.ranking_list_pager);
		fragmentList=new ArrayList<Fragment>();
		Fragment tabNewFragment=new TabNewFragment();
		Fragment tabRiseFragment=new TabRiseFragment();
		Fragment tabSumFragment=new TabSumFragment();
		fragmentList.add(tabSumFragment);
		fragmentList.add(tabNewFragment);
		fragmentList.add(tabRiseFragment);
		
		
		//��ViewPager����������
		mViewPager.setAdapter(new RankingFragmentPagerAdapter(this.getChildFragmentManager(), fragmentList));
		mViewPager.setCurrentItem(0);//���õ�ǰ��ʾ��ǩҳΪ��һҳ 
		mViewPager.setOnPageChangeListener(new RankingPageChangeListener());//ҳ��仯ʱ�ļ�����  
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
			Animation animation=new TranslateAnimation(currIndex*one, arg0*one, 0, 0);//ƽ�ƶ���
			currIndex=arg0;
			animation.setFillAfter(true);//������ֹʱͣ�������һ֡����Ȼ��ص�û��ִ��ǰ��״̬
			animation.setDuration(200);//����ImageView����ʾ������
			imageCusor.startAnimation(animation);
		}
		
	}
}
