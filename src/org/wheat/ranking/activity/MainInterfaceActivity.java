package org.wheat.ranking.activity;

import org.wheat.beautyranking.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

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
					mFirstPageFragment=new FocusOnFragment();
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
