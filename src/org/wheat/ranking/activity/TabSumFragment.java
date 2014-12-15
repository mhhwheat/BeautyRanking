package org.wheat.ranking.activity;

import java.util.ArrayList;
import java.util.List;

import org.wheat.beautyranking.R;
import org.wheat.ranking.entity.BeautyIntroduction;
import org.wheat.ranking.entity.PhotoParameters;
import org.wheat.ranking.entity.json.BeautyIntroductionListJson;
import org.wheat.ranking.loader.HttpLoderMethods;
import org.wheat.ranking.loader.ImageLoader;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

/**
 * description:总排行榜Fragment
 * @author wheat
 * date: 2014-12-14  
 * time: 下午9:22:15
 */
public class TabSumFragment extends Fragment implements OnScrollListener
{
	private final int PAGE_LENGTH=10;//每次请求数据页里面包含的最多数据项
	private PullToRefreshListView mPullToRefreshListView;
	private List<BeautyIntroduction> mListData;//保存listview数据项的数组
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;//加载图片的对象
	private SumRefreshListAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mListData=new ArrayList<BeautyIntroduction>();
		mImageLoader=ImageLoader.getInstance(getActivity().getApplicationContext());
		adapter=new SumRefreshListAdapter();
		new UpdateDataTask(0, PAGE_LENGTH).execute();
		}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	mInflater=inflater;
		
		View view=inflater.inflate(R.layout.fragment_sum, container, false);
		mPullToRefreshListView=(PullToRefreshListView)view.findViewById(R.id.sum_refresh_list_view);
		
		mPullToRefreshListView.setAdapter(adapter);
		initialListViewListener();
		
		return view;
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
	
	
	public class SumRefreshListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(mListData!=null)
				return mListData.size();
			else 
				return 0;
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
			final BeautyIntroduction listItem=mListData.get(position);
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
		
		private final class ViewHolder
		{
			public ImageView photo;
			public TextView name;
			public TextView school;
			public TextView description;
		}
	}
	
	
	
	private void initialListViewListener()
	{
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				new UpdateDataTask(0, PAGE_LENGTH).execute();
			}
		});
		
		mPullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(getActivity(), "End of List!", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private class UpdateDataTask extends AsyncTask<Void, Void, ArrayList<BeautyIntroduction>>
	{
		private int firstIndex;
		private int count;
		
		public UpdateDataTask(int firstIndex,int count)
		{
			super();
			this.firstIndex=firstIndex;
			this.count=count;
		}

		@Override
		protected ArrayList<BeautyIntroduction> doInBackground(Void... params) {
			BeautyIntroductionListJson json=null;
			try {
				json=HttpLoderMethods.getSumPage(firstIndex, count);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			if(json==null)
			{
				Log.w("TabSumFragment","json is null------------->");
				return null;
			}
			final ArrayList<BeautyIntroduction> data=(ArrayList<BeautyIntroduction>)json.getData().getIntroductionList();
			return data;
		}

		@Override
		protected void onPostExecute(ArrayList<BeautyIntroduction> result) {
			
			mListData=result;
			adapter.notifyDataSetChanged();
			mPullToRefreshListView.onRefreshComplete();
			super.onPostExecute(result);
		}
		
	}
}
