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
 * description:��ҳ��ǩ��Ӧ��Fragment(����FollowFragment,NeighborFragment,MyCreatedFragment)
 * @author wheat
 * date: 2014-12-15  
 * time: ����9:41:54
 */
public class FirstPageFragment extends Fragment
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
		mViewPager=(ViewPager)mView.findViewById(R.id.first_page_pager);
		fragmentList=new ArrayList<Fragment>();
		Fragment mFollowFragment=new FollowFragment();
		Fragment mNeighborFragment=new NeighborFragment();
		Fragment mMyCreatedFragment=new MyCreatedFragment();
		fragmentList.add(mFollowFragment);
		fragmentList.add(mNeighborFragment);
		fragmentList.add(mMyCreatedFragment);
		
		
		//��ViewPager����������
		mViewPager.setAdapter(new RankingFragmentPagerAdapter(this.getChildFragmentManager(), fragmentList));
		mViewPager.setCurrentItem(0);//���õ�ǰ��ʾ��ǩҳΪ��һҳ 
		mViewPager.setOnPageChangeListener(new FirstPageChangeListener());//ҳ��仯ʱ�ļ�����  
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
			Animation animation=new TranslateAnimation(currIndex*one, arg0*one, 0, 0);//ƽ�ƶ���
			currIndex=arg0;
			animation.setFillAfter(true);//������ֹʱͣ�������һ֡����Ȼ��ص�û��ִ��ǰ��״̬
			animation.setDuration(200);//����ImageView����ʾ������
			imageCusor.startAnimation(animation);
		}
		
	}
}
