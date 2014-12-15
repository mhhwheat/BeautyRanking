package org.wheat.ranking.activity;

import org.wheat.beautyranking.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
	
	
	private Fragment mFirstPageFragment;
	private Fragment mFindPageFragment;
	private Fragment mMinePageFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_user_interface);
		
		mRadioGroup=(RadioGroup)findViewById(R.id.main_radio);
		mRadioGroup.setOnCheckedChangeListener(new CheckedChangeListener());
		
		//����ҳ����Ϊ��ʼ��ҳ��
		mRadioGroup.check(R.id.rb_first_page);
	}
	
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
					mMinePageFragment=new NeighborFragment();
				replaceFragment(mMinePageFragment, R.id.replacing_fragment);
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
