package org.wheat.ranking.activity;

import java.util.ArrayList;

import org.wheat.beautyranking.R;
import org.wheat.ranking.data.ListItem;
import org.wheat.widget.RefreshListView;
import org.wheat.widget.RefreshListView.RefreshListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TabNewFragment extends Fragment implements RefreshListener
{
	private RefreshListView listView;
	private ArrayList<ListItem> listData;
	private LayoutInflater mInflater;
	//滚屏的状态
	//private int localScrollState;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		System.out.println("BBBBBBBBBB____onActivityCreated");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		System.out.println("BBBBBBBBBB____onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("BBBBBBBBBB____onCreated");
		listData=new ArrayList<ListItem>();
		InitListData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		System.out.println("BBBBBBBBBB____onCreatedView");
		View view= inflater.inflate(R.layout.fragment_new, container,false);
		listView=(RefreshListView)view.findViewById(R.id.newTab);
		mInflater=inflater;
		listView.setOnRefreshListener(this);
		listView.setAdapter(new RefreshListAdapter());
		listView.setSelection(1);
		
		/*
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) 
			{
				localScrollState=scrollState;
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
			}
		});
		*/
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		System.out.println("BBBBBBBBBB____onDestroyView");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		System.out.println("BBBBBBBBBB____onDetach");
	}

	@Override
	public void onPause() {
		super.onPause();
		System.out.println("BBBBBBBBBB____onPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("BBBBBBBBBB____onResume");
	}

	@Override
	public void onStart() {
		super.onStart();
		System.out.println("BBBBBBBBBB____onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		System.out.println("BBBBBBBBBB____onStop");
	}

	@Override
	public Object refreshing() 
	{
		return null;
	}

	@Override
	public void refreshed(Object obj) 
	{
		
	}

	@Override
	public void more() 
	{
		
	}
	
	
	@Override
	public void scrollStateChanged(int scrollState) 
	{
		
	}


	public class RefreshListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			System.out.println("------------------------>getView");
			final ListItem item=listData.get(position);
			ViewHolder holder=null;
			if(convertView==null)
			{
				holder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.refresh_list_item, null);
				holder.photo=(ImageView)convertView.findViewById(R.id.avatar);
				holder.name=(TextView)convertView.findViewById(R.id.trueName);
				holder.school=(TextView)convertView.findViewById(R.id.school);
				holder.description=(TextView)convertView.findViewById(R.id.impression);
				convertView.setTag(holder);
			}
			else
				holder=(ViewHolder)convertView.getTag();
			
			holder.name.setText(item.getName());
			holder.school.setText(item.getSchool());
			holder.description.setText(item.getDescription());
			holder.photo.setImageBitmap(item.getPhoto());
			return convertView;
		}
		
	}
	public final class ViewHolder
	{
		public ImageView photo;
		public TextView name;
		public TextView school;
		public TextView description;
	}
	public void InitListData()
	{
		Bitmap bm=BitmapFactory.decodeResource(getResources(), R.drawable.img);
		String name="麦华华";
		String school="广东外语外贸大学";
		String description="当地时间6月4日，英国伦敦绘公司将亚洲狮的脸描摹在人体模特身上，为伦敦动物学学会分支机构揭幕，呼吁保护濒危生物。";
		for(int i=0;i<50;i++)
		{
			ListItem item=new ListItem(name, school, description);
			item.setPhoto(bm);
			listData.add(item);
		}
	}
}