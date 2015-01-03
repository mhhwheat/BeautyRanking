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
 * description:�����棬����RankingListFragment,MainPageFragment,MinePageFragment
 * @author wheat
 * date: 2014-12-11  
 * time: ����9:23:54
 */
public class MainInterfaceActivity extends FragmentActivity
{
	private RadioGroup mRadioGroup;
	private int radioLen=-1;
	private int lastCheckId=R.id.rb_first_page;//���÷���ҳ����ת����ʱ�ص���һ��Rudiobutton
	
	private Fragment mFirstPageFragment;
	private Fragment mFindPageFragment;
	private Fragment mMinePageFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);//�������ñ�����
		setContentView(R.layout.main_user_interface);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mycustomtitle);
		mRadioGroup=(RadioGroup)findViewById(R.id.main_radio);
		radioLen=mRadioGroup.getChildCount();
//		mRadioGroup.setOnClickListener(new RadioOnClickListener());
		mRadioGroup.setOnCheckedChangeListener(new CheckedChangeListener());
		
		//����ҳ����Ϊ��ʼ��ҳ��
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
				lastCheckId=R.id.rb_first_page;
				break;
			case R.id.rb_find_page:
				if(mFindPageFragment==null)
					mFindPageFragment=new RankingListFragment();
				getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.create_page_title);
				replaceFragment(mFindPageFragment, R.id.replacing_fragment);
				lastCheckId=R.id.rb_find_page;
				break;
			case R.id.rb_mine_page:
				if(mMinePageFragment==null)
					mMinePageFragment=new MyDetailPage();
				replaceFragment(mMinePageFragment, R.id.replacing_fragment);
				lastCheckId=R.id.rb_mine_page;
				break;
			case R.id.rb_create_beauty:
				Intent createIntent= new Intent();
				createIntent.setClass(MainInterfaceActivity.this, CreateBeauty.class);
				startActivity(createIntent);
				mRadioGroup.check(lastCheckId);
				break;
			}
		}
		
	}
	
	/**
	 * ��targetFragmentȥ�滻������idΪsource�Ĳ���
	 * @param targetFragment �滻��Fragment
	 * @param source ���滻�Ĳ�����Դ,�ò��ֱ���ΪFrameLayout
	 */
	public void replaceFragment(Fragment targetFragment,int source)
	{
		getSupportFragmentManager().beginTransaction().
		replace(source, targetFragment).commit();
	}
	
	
	
}
