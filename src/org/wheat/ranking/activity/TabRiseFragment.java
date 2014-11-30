package org.wheat.ranking.activity;

import java.util.ArrayList;

import org.wheat.beautyranking.R;
import org.wheat.ranking.entity.BeautyIntroduction;
import org.wheat.ranking.entity.PhotoParameters;
import org.wheat.ranking.entity.json.BeautyIntroductionListJson;
import org.wheat.ranking.loader.HttpDataLoader;
import org.wheat.ranking.loader.ImageLoader;
import org.wheat.widget.RefreshListView;
import org.wheat.widget.RefreshListView.RefreshListener;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TabRiseFragment extends Fragment implements RefreshListener
{
	private final int PAGE_LENGTH=10;//每次请求数据页里面包含的最多数据项
	private RefreshListView listView;
	private ArrayList<BeautyIntroduction> listData;//保存listview数据项的数组
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;//加载图片的图像
	private RiseRefreshListAdapter adapter;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		System.out.println("CCCCCCCCCC____onActivityCreated");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		System.out.println("CCCCCCCCCC____onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("CCCCCCCCCC____onCreated");
		
		//inited
		listData=new ArrayList<BeautyIntroduction>();
		new RisePageThread(new UpdateDataHandler(),0, PAGE_LENGTH).start();
		mImageLoader=ImageLoader.getInstance(this.getActivity().getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		System.out.println("CCCCCCCCCC____onCreatedView");
		
		View view=inflater.inflate(R.layout.fragment_rise, container, false);
		listView=(RefreshListView)view.findViewById(R.id.riseTab);
		mInflater=inflater;
		listView.setOnRefreshListener(this);
		adapter=new RiseRefreshListAdapter();
		listView.setAdapter(adapter);
		listView.setSelection(1);
		
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		System.out.println("CCCCCCCCCC____onDestroyView");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		System.out.println("CCCCCCCCCC____onDetach");
	}

	@Override
	public void onPause() {
		super.onPause();
		System.out.println("CCCCCCCCCC____onPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("CCCCCCCCCC____onResume");
	}

	@Override
	public void onStart() {
		super.onStart();
		System.out.println("CCCCCCCCCC____onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		System.out.println("CCCCCCCCCC____onStop");
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
		switch(scrollState)
		{
		case OnScrollListener.SCROLL_STATE_FLING:
			mImageLoader.lock();
			break;
		case OnScrollListener.SCROLL_STATE_IDLE:
			mImageLoader.unlock();
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			mImageLoader.lock();
			break;
		default:
			break;
		}
	}
	
	public class RiseRefreshListAdapter extends BaseAdapter
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
			final BeautyIntroduction listItem=listData.get(position);
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
			
				holder.name.setText(listItem.getBeautyName());
				holder.school.setText(listItem.getSchool());
				holder.description.setText(listItem.getDescription());
			
			mImageLoader.addTask(new PhotoParameters(listItem.getAvatarPath(), -1, -1), holder.photo);
			System.out.println("Rise Fragment----------->getView");
			System.out.println("path"+listItem.getAvatarPath());
			
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
		BeautyIntroductionListJson json=null;
		try {
			json=HttpDataLoader.getRisePage(0, PAGE_LENGTH);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if(json==null)
		{
			System.out.println("json is null------------>");
			return;
		}
		listData=(ArrayList<BeautyIntroduction>)json.getData().getIntroductionList();
	}
	
	class RisePageThread extends Thread
	{
		private int firstIndex;
		private int count;
		private Handler handler;
		
		public RisePageThread(Handler handler,int firstIndex,int count)
		{
			super();
			this.firstIndex=firstIndex;
			this.count=count;
			this.handler=handler;
		}

		@Override
		public void run() {
			BeautyIntroductionListJson json=null;
			try {
				json=HttpDataLoader.getRisePage(firstIndex, count);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			if(json==null)
			{
				System.out.println("json is null------------>");
				return;
			}
			ArrayList<BeautyIntroduction> data=(ArrayList<BeautyIntroduction>)json.getData().getIntroductionList();
			for(int index=0;index<data.size();index++)
			{
				listData.add(data.get(index));
			}
			handler.sendEmptyMessage(200);
			
		}
		
	}
	public class UpdateDataHandler extends Handler
	{

		@Override
		public void handleMessage(Message msg) {
			if(msg.what==200)
				adapter.notifyDataSetChanged();
		}
		
	}
	
}
