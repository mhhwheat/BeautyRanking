package org.wheat.ranking.activity;

import java.util.Arrays;
import java.util.List;

import org.wheat.beautyranking.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MenuFragment extends Fragment
{
	private View mView;
	private ListView mCategories;
	private List<String> mDatas=Arrays.asList("首页","发现","我的");
	private ListAdapter mAdapter;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mView==null)
		{
			initView(inflater,container);
		}
		return mView;
	}
	
	private void initView(LayoutInflater inflater,ViewGroup container)
	{
		mView=inflater.inflate(R.layout.left_menu, container, false);
		mCategories=(ListView)mView.findViewById(R.id.menuList);
		mAdapter=new ArrayAdapter<String>(getActivity(),R.layout.menu_list_item, mDatas);
		mCategories.setAdapter(mAdapter);
	}
	
}
