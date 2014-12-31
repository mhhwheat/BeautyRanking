/** 
 * description：
 * @author wheat
 * date: 2014-12-23  
 * time: 下午8:22:33
 */ 
package org.wheat.ranking.activity;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.wheat.beautyranking.R;
import org.wheat.ranking.entity.BeautyIntroduction;
import org.wheat.ranking.entity.PhotoParameters;
import org.wheat.ranking.entity.json.BeautyIntroductionListJson;
import org.wheat.ranking.loader.HttpLoderMethods;
import org.wheat.ranking.loader.ImageLoader;

import com.huewu.pla.lib.MultiColumnListView.OnColumnWidthIsMeasureListener;



import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * description:
 * @author wheat
 * date: 2014-12-23  
 * time: 下午8:22:33
 */
public class NeighborGridFragment extends Fragment implements OnScrollListener
{
	private final int PAGE_LENGTH=10;
	private XListView mPullToRefreshGridView;
	private List<BeautyIntroduction> mGridData;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;//加载图片的对象
	private NeighborGridAdapter adapter;
	
	private int mImageWidth=-1;
	
	private boolean isLoadingMore=false;//防止重复开启异步加载线程
	//private GridView mActualGridView;//PulltoRefreshListView中真正的ListView
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGridData=new ArrayList<BeautyIntroduction>();
		mImageLoader=ImageLoader.getInstance(getActivity().getApplicationContext());
		adapter=new NeighborGridAdapter();
		new UpdateDataTask().execute();
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater=inflater;
		View view=inflater.inflate(R.layout.fragment_neighbor_layout, container, false);
		mPullToRefreshGridView=(XListView)view.findViewById(R.id.neighbor_pull_refresh_grid);
//		mActualGridView=mPullToRefreshGridView.getRefreshableView();
		mPullToRefreshGridView.setAdapter(adapter);
		initialGridViewListener();
		
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



	private class NeighborGridAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mGridData.size();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			final BeautyIntroduction GridItem=mGridData.get(position);
			ViewHolder holder=null;
			if(convertView==null)
			{
				holder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.fragment_neighbor_grid_view_item, null);
				holder.ivAvatar=(ImageView)convertView.findViewById(R.id.fragment_neighbor_grid_view_avatar);
				holder.tvDescription=(TextView)convertView.findViewById(R.id.fragment_neighbor_grid_view_description);
				convertView.setTag(holder);
			}
			else
				holder=(ViewHolder)convertView.getTag();
			Log.w("NeighborGridFragment", "is getView");
			mImageLoader.addTask(new PhotoParameters(GridItem.getAvatarPath(), mImageWidth, 2*mImageWidth*mImageWidth, true), holder.ivAvatar);
			holder.tvDescription.setText(GridItem.getDescription());
			return convertView;
		}
		
		private class ViewHolder
		{
			ImageView ivAvatar;
			TextView tvDescription;
		}
		
	}
	
	/**
	 * 初始化PullToRefreshGridView的监听器
	 */
	private void initialGridViewListener()
	{
		mPullToRefreshGridView.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new UpdateDataTask().execute();
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				
			}
		});
		
		mPullToRefreshGridView.setOnColumnWidthIsMeasureListener(new OnColumnWidthIsMeasureListener() {
			
			@Override
			public void onColumnWidthIsMeasure(int mColumnWidth) {
				mImageWidth=mColumnWidth;
			}
		});
		
	}
	
	/**
	 * 
	 * description:刷新数据异步线程
	 * @author wheat
	 * date: 2014-12-23  
	 * time: 下午10:36:04
	 */
	private class UpdateDataTask extends AsyncTask<Void, Void, BeautyIntroductionListJson>
	{
		@Override
		protected BeautyIntroductionListJson doInBackground(Void... params) {
			BeautyIntroductionListJson json=null;
			try {
				json=HttpLoderMethods.getSumPage(0, PAGE_LENGTH);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(BeautyIntroductionListJson result) {
			if(result!=null&&result.getCode()==1000)
			{
				synchronized (mGridData) {
					mGridData.clear();
					mGridData=result.getData().getIntroductionList();
					adapter.notifyDataSetChanged();
				}
			}
			mPullToRefreshGridView.stopRefresh();
			isLoadingMore=false;
			
			super.onPostExecute(result);
		}
		
	}
	
	/**
	 * 
	 * description:加载更多数据异步线程
	 * @author wheat
	 * date: 2014-12-23  
	 * time: 下午10:36:26
	 */
	private class LoadMoreTask extends AsyncTask<Void, Void, BeautyIntroductionListJson>
	{
		private int firstIndex;
		private int count;
		
		public LoadMoreTask(int firstIndex,int count)
		{
			super();
			this.firstIndex=firstIndex;
			this.count=count;
		}

		@Override
		protected BeautyIntroductionListJson doInBackground(Void... params) {
			BeautyIntroductionListJson json=null;
			try {
				json=HttpLoderMethods.getSumPage(firstIndex, count);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(BeautyIntroductionListJson result) {
			if(result!=null&&result.getCode()==1000)
			{
				synchronized (mGridData) {
					mGridData.addAll(result.getData().getIntroductionList());
					adapter.notifyDataSetChanged();
				}
			}
			isLoadingMore=false;
			super.onPostExecute(result);
		}
		
	}
}
