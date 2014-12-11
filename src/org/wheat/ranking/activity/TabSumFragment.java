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
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class TabSumFragment extends Fragment implements RefreshListener
{
	private final int PAGE_LENGTH=10;//每次请求数据页里面包含的最多数据项
	private RefreshListView listView;
	private ArrayList<BeautyIntroduction> listData;//保存listview数据项的数组
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;//加载图片的对象
	private SumRefreshListAdapter adapter;
	
	
	 @Override
	 	public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        Log.w("TabSumFragment","AAAAAAAAAA____onAttach");
	    }

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        Log.w("TabSumFragment","AAAAAAAAAA____onCreate");
	        
	        //inited
	        listData=new ArrayList<BeautyIntroduction>();
	        adapter=new SumRefreshListAdapter();
//	        adapter.notifyDataSetChanged();
	        new SumPageThread(new UpdateDataHandler(),0, PAGE_LENGTH).start();
	        mImageLoader=ImageLoader.getInstance(getActivity().getApplicationContext());
	        
	    }

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        Log.w("TabSumFragment","AAAAAAAAAA____onCreateView");
	        
	        View view=inflater.inflate(R.layout.fragment_sum, container, false);
	        listView=(RefreshListView)view.findViewById(R.id.sumTab);
			mInflater=inflater;
			listView.setOnRefreshListener(this);
			listView.setAdapter(adapter);
			listView.setSelection(1);
			
			return view;
	    }

	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        Log.w("TabSumFragment","AAAAAAAAAA____onActivityCreated");
	    }

	    @Override
	    public void onStart() {
	        super.onStart();
	        Log.w("TabSumFragment","AAAAAAAAAA____onStart");
	    }

	    @Override
	    public void onResume() {
	        super.onResume();
	        Log.w("TabSumFragment","AAAAAAAAAA____onResume");
	        listData.clear();
	        if(adapter!=null)
	        	adapter.notifyDataSetChanged();
	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	        Log.w("TabSumFragment","AAAAAAAAAA____onPause");
	    }

	    @Override
	    public void onStop() {
	        super.onStop();
	        Log.w("TabSumFragment","AAAAAAAAAA____onStop");
	    }

	    @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        Log.w("TabSumFragment","AAAAAAAAAA____onDestroyView");
	    }

	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        Log.w("TabSumFragment","AAAAAAAAAA____onDestroy");
	    }

	    @Override
	    public void onDetach() {
	        super.onDetach();
	        Log.w("TabSumFragment","AAAAAAAAAA____onDetach");
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
	
	public class SumRefreshListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.w("TabSumFragment", "listData.size="+listData.size());
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
			//new AddTaskThread(listItem.getAvatarPath(), holder.photo).start();
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
	
	class SumPageThread extends Thread
	{
		private int firstIndex;
		private int count;
		private Handler handler;
		
		public SumPageThread(Handler handler,int firstIndex,int count)
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
				json=HttpLoderMethods.getSumPage(firstIndex, count);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			if(json==null)
			{
				Log.w("TabSumFragment","json is null-------------->");
				return;
			}
			final ArrayList<BeautyIntroduction> data=(ArrayList<BeautyIntroduction>)json.getData().getIntroductionList();
			for(int index=0;index<data.size();index++)
			{
				/*
				listData.add(data.get(index));
				Log.w("TabSumFragment", "listData size add to"+listData.size());
				*/
				
				Message msg=Message.obtain();
				msg.obj=data.get(index);
				msg.what=200;
				handler.sendMessage(msg);
				
				/*
				final int i = index;
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						listData.add((BeautyIntroduction)data.get(i));
						adapter.notifyDataSetChanged();
					}
				});
				*/
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
			Log.w("TabSumFragment","datachanged----------->");
		}
		
	}
	/*
	public class AddTaskThread extends Thread
	{
		private String avatarPath;
		private ImageView view;
		public AddTaskThread(String path,ImageView view)
		{
			this.avatarPath=path;
			this.view=view;
		}
		@Override
		public void run() {
			mImageLoader.addTask(new PhotoParameters(avatarPath, -1, -1), view);
		}
		
	}
	*/
}
