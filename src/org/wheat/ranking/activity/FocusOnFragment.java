package org.wheat.ranking.activity;

import java.util.ArrayList;

import org.wheat.beautyranking.R;
import org.wheat.ranking.entity.BeautyIntroduction;
import org.wheat.ranking.entity.PhotoParameters;
import org.wheat.ranking.entity.json.BeautyIntroductionListJson;
import org.wheat.ranking.loader.HttpLoderMethods;
import org.wheat.ranking.loader.ImageLoader;
import org.wheat.widget.RefreshableView;
import org.wheat.widget.RefreshableView.PullToRefreshListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FocusOnFragment extends Fragment implements OnScrollListener ,PullToRefreshListener
{
	private final int PAGE_LENGTH=10;//每次请求数据页里面包含的最多数据项
	private ArrayList<BeautyIntroduction> listData;//保存listview数据项的数组
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;//加载图片的对象
	private FocusOnListAdapter adapter;
	private RefreshableView mRefreshableView;
	private ListView mListView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listData=new ArrayList<BeautyIntroduction>();
		adapter=new FocusOnListAdapter();
		new FocusOnPageThread(new UpdateDataHandler(),0, PAGE_LENGTH).start();
		mImageLoader=ImageLoader.getInstance(getActivity().getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater=inflater;
		
		View view=inflater.inflate(R.layout.focus_on_fragment_layout, container, false);
		
		mRefreshableView=(RefreshableView)view.findViewById(R.id.focus_on_refresh_view);
		mRefreshableView.setOnRefreshListener(this, 0);
		//mRefreshableView.hideHeader();
		
		mListView=(ListView)view.findViewById(R.id.focus_on_listview);
		mListView.setOnScrollListener(this);
		mListView.setAdapter(adapter);
		return view;
	}
	
	
	
	public class FocusOnListAdapter extends BaseAdapter
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
			return listData.get(position);
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
	
	public class FocusOnPageThread extends Thread
	{
		private int firstIndex;
		private int count;
		private Handler handler;
		
		public FocusOnPageThread(Handler handler,int firstIndex,int count)
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
		}
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
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

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	
	
	//实现PullToRefreshListener接口
	@Override
	public void onRefresh() 
	{
		try {
		     Thread.sleep(3000);
		    } catch (InterruptedException e) {
		     e.printStackTrace();
		    }
		mRefreshableView.finishRefreshing();
	}

}
