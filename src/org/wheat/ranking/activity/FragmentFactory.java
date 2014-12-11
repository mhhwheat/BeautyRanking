package org.wheat.ranking.activity;

import android.support.v4.app.Fragment;

public class FragmentFactory
{
	public static Fragment getInstanceByIndex(int index)
	{
		Fragment fragment=null;
		switch(index)
		{
		case 1:
			fragment= new RankingListFragment();
			break;
		case 2:
			fragment= new TabNewFragment();
			break;
		case 3:
			fragment= new TabRiseFragment();
			break;
		}
		return fragment;
	}
}
