package org.wheat.ranking.activity;

import java.util.ArrayList;

import org.wheat.beautyranking.R;
import org.wheat.ranking.entity.BeautyIntroduction;
import org.wheat.ranking.entity.PhotoParameters;
import org.wheat.ranking.entity.json.BeautyIntroductionListJson;
import org.wheat.ranking.loader.HttpLoderMethods;
import org.wheat.ranking.loader.ImageLoader;
import org.wheat.widget.RefreshListView;
import org.wheat.widget.RefreshListView.RefreshListener;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class TabNewFragment extends Fragment implements RefreshListener
{
	private final int PAGE_LENGTH=10;//每次请求数据页里面包含的最多数据项
	private RefreshListView listView;
	private ArrayList<BeautyIntroduction> listData;//保存listview数据项的数组
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;//加载图片的对象
	private NewRefreshListAdapter adapter;
	
	//滚屏的状态
	//private int localScrollState;
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.w("TabNewFragment","BBBBBBBBBB____onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.w("TabNewFragment","BBBBBBBBBB____onCreated");
		//inited
        listData=new ArrayList<BeautyIntroduction>();
        adapter=new NewRefreshListAdapter();
        new NewPageThread(new UpdateDataHandler(), 0, PAGE_LENGTH).start();
        mImageLoader=ImageLoader.getInstance(getActivity().getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		Log.w("TabNewFragment","BBBBBBBBBB____onCreatedView");
		View view= inflater.inflate(R.layout.fragment_new, container,false);
		listView=(RefreshListView)view.findViewById(R.id.newTab);
		mInflater=inflater;
		listView.setOnRefreshListener(this);
		listView.setAdapter(adapter);
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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.w("TabNewFragment","BBBBBBBBBB____onActivityCreated");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.w("TabNewFragment","BBBBBBBBBB____onDestroyView");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.w("TabNewFragment","BBBBBBBBBB____onDetach");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.w("TabNewFragment","BBBBBBBBBB____onPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.w("TabNewFragment","BBBBBBBBBB____onResume");
		listData.clear();
        if(adapter!=null)
        	adapter.notifyDataSetChanged();
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.w("TabNewFragment","BBBBBBBBBB____onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.w("TabNewFragment","BBBBBBBBBB____onStop");
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


	public class NewRefreshListAdapter extends BaseAdapter
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
			Log.w("TabNewFragment","------------------------>getView");
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
			mImageLoader.addTask(new PhotoParameters(listItem.getAvatarPath(), 100, 10000), holder.photo);
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
	class NewPageThread extends Thread
	{
		private int firstIndex;
		private int count;
		private Handler handler;
		
		public NewPageThread(Handler handler,int firstIndex,int count)
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
				json=HttpLoderMethods.getNewPage(firstIndex, count);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			if(json==null)
			{
				Log.w("TabNewFragment","json is null-------------->");
				return;
			}
			ArrayList<BeautyIntroduction> data=(ArrayList<BeautyIntroduction>)json.getData().getIntroductionList();
			for(int index=0;index<data.size();index++)
			{
				Message msg=Message.obtain();
				msg.obj=data.get(index);
				msg.what=200;
				handler.sendMessage(msg);
			}
		}
	}
	
	public class UpdateDataHandler extends Handler
	{

		@Override
		public void handleMessage(Message msg) {
			if(msg.what==200)
			{
				listData.add((BeautyIntroduction)msg.obj);
				adapter.notifyDataSetChanged();
			}
			Log.w("TabNewFragment","datachanged----------->");
		}
		
	}
}