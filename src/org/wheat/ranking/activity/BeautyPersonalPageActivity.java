package org.wheat.ranking.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.wheat.beautyranking.R;
import org.wheat.ranking.entity.Photo;
import org.wheat.ranking.entity.PhotoParameters;
import org.wheat.ranking.entity.json.PhotoListJson;
import org.wheat.ranking.loader.HttpLoderMethods;
import org.wheat.ranking.loader.ImageLoader;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/** 
 * description:beauty的个人主页
 * @author wheat
 * date: 2014-12-15  
 * time: 下午7:32:05
 */
public class BeautyPersonalPageActivity extends Activity implements OnScrollListener
{
	private int mDeviceScreenWidth;
	private int mBeautyId;//该页面显示该BeautyId对应的Beauty的所有图片
	private final int PAGE_LENGTH=10;//每次请求数据页里面包含的最多数据项
	private PullToRefreshListView mPullToRefreshListView;
	private List<Photo> mListData;//保存listview数据项的数组
	private ImageLoader mImageLoader;//加载图片的对象
	private BeautyPersonalPageListAdapter adapter;
	private LayoutInflater mInflater;
	
	private boolean isLoadingMore=false;//防止重复开启异步加载线程
	private View mFooterView;
	private TextView tvFooterText;
	private ProgressBar pbFooterLoading;
	private ListView mActualListView;//PulltoRefreshListView中真正的ListView

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBeautyId=getBeautyIdFromIntent();
		setContentView(R.layout.beauty_personal_page_layout);
		mInflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mListData=new ArrayList<Photo>();
		mImageLoader=ImageLoader.getInstance(this);
		adapter=new BeautyPersonalPageListAdapter();
		
		mPullToRefreshListView=(PullToRefreshListView)findViewById(R.id.beauty_personal_page_refresh_list_view);
		mActualListView=mPullToRefreshListView.getRefreshableView();
		
		mFooterView=mInflater.inflate(R.layout.refresh_list_footer, null);
		pbFooterLoading=(ProgressBar)mFooterView.findViewById(R.id.refresh_list_footer_progressbar);
		tvFooterText=(TextView)mFooterView.findViewById(R.id.refresh_list_footer_text);
		
		mPullToRefreshListView.setAdapter(adapter);
		mActualListView.addFooterView(mFooterView);
		
		initialListViewListener();
		
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
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
	
	private class BeautyPersonalPageListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListData.size();
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
			// TODO Auto-generated method stub
			final Photo photo=mListData.get(position);
			ViewHolder holder;
			if(convertView==null)
			{
				holder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.beauty_personal_page_list_item, null);
				holder.ivUserAvatar=(ImageView)convertView.findViewById(R.id.beauty_personal_avatar);
				holder.tvUserNikeName=(TextView)convertView.findViewById(R.id.beauty_personal_publisher_nikename);
				holder.tvPublishTime=(TextView)convertView.findViewById(R.id.beauty_personal_publish_time);
				holder.ivPhoto=(ImageView)convertView.findViewById(R.id.beauty_personal_photo);
				holder.ivPraiseButton=(ImageView)convertView.findViewById(R.id.beauty_personal_praise_button);
				holder.tvPraiseTimes=(TextView)convertView.findViewById(R.id.beauty_personal_praise_times);
				holder.ivCommentButton=(ImageView)convertView.findViewById(R.id.beauty_personal_comment_button);
				holder.tvCommentTimes=(TextView)convertView.findViewById(R.id.beauty_personal_comment_times);
			}
			else
				holder=(ViewHolder)convertView.getTag();
			int minSideLength=holder.ivPhoto.getWidth();
			holder.tvPublishTime.setText(getDifferenceFromDate(photo.getUploadTime()));
			mImageLoader.addTask(new PhotoParameters(photo.getPhotoPath(),minSideLength , 2*minSideLength*minSideLength), holder.ivPhoto);
			holder.tvPraiseTimes.setText(String.valueOf(photo.getPraiseCount()));
			holder.tvCommentTimes.setText(String.valueOf(photo.getCommentCount()));
			return convertView;
		}
		private final class ViewHolder
		{
			public ImageView ivUserAvatar;
			public TextView  tvUserNikeName;
			public TextView  tvPublishTime;
			public ImageView ivPhoto;
			public ImageView ivPraiseButton;
			public TextView  tvPraiseTimes;
			public ImageView ivCommentButton;
			public TextView  tvCommentTimes;
		}
		
	}

	
	private void initialListViewListener()
	{
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				new UpdateDataTask().execute();
			}
		});
		
		mPullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				if(!isLoadingMore)
				{
					isLoadingMore=true;
					pbFooterLoading.setVisibility(View.VISIBLE);
					tvFooterText.setText(R.string.list_footer_loading);
					new LoadMoreTask(mListData.size(), PAGE_LENGTH).execute();
					Toast.makeText(BeautyPersonalPageActivity.this, "End of List!", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	/**
	 * 
	 * description:刷新ListView内容的异步线程
	 * @author wheat
	 * date: 2014-12-15  
	 * time: 上午10:37:59
	 */
	private class UpdateDataTask extends AsyncTask<Void, Void, ArrayList<Photo>>
	{
		@Override
		protected ArrayList<Photo> doInBackground(Void... params) {
			PhotoListJson json=null;
			try {
				json=HttpLoderMethods.getBeautyAllPhotos(0, PAGE_LENGTH, mBeautyId);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			if(json==null)
			{
				Log.w("TabSumFragment","json is null------------->");
				return null;
			}
			final ArrayList<Photo> data=(ArrayList<Photo>)json.getData().getPhotoList();
			return data;
		}

		@Override
		protected void onPostExecute(ArrayList<Photo> result) {
			
			synchronized (mListData) {
				mListData.clear();
				mListData=result;
				adapter.notifyDataSetChanged();
			}
			mPullToRefreshListView.onRefreshComplete();
			
			if(result==null)
				onLoadComplete(true);
			else
				onLoadComplete(false);
			
			super.onPostExecute(result);
		}
		
	}
	
	/**
	 * 
	 * description：加载更多内容的异步线程
	 * @author wheat
	 * date: 2014-12-15  
	 * time: 下午5:10:57
	 */
	private class LoadMoreTask extends AsyncTask<Void, Void, ArrayList<Photo>>
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
		protected ArrayList<Photo> doInBackground(Void... params) {
			PhotoListJson json=null;
			try {
				json=HttpLoderMethods.getBeautyAllPhotos(firstIndex, count, mBeautyId);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			if(json==null)
			{
				Log.w("TabSumFragment","json is null------------->");
				return null;
			}
			final ArrayList<Photo> data=(ArrayList<Photo>)json.getData().getPhotoList();
			return data;
		}

		@Override
		protected void onPostExecute(ArrayList<Photo> result) {
			if(result!=null)
			{
				synchronized (mListData) {
					mListData.addAll(result);
					adapter.notifyDataSetChanged();
				}
				onLoadComplete(false);
			}
			else
				onLoadComplete(true);
			super.onPostExecute(result);
		}
		
	}
	
	/**
	 * 
	 * @param wasLoadNothing 加载完成后，是否内容没有增加,true表示内容没有增加,false表示内容增加了
	 */
	private void onLoadComplete(boolean wasLoadNothing)
	{
		isLoadingMore=false;
		if(wasLoadNothing)
		{
			pbFooterLoading.setVisibility(View.GONE);
			tvFooterText.setText(R.string.list_footer_no_more);
		}
	}
	
	/**
	 * 通过Intent获取上一个Activity传来的BeautyId;
	 * @return
	 */
	private int getBeautyIdFromIntent()
	{
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		return bundle.getInt("mBeautyID");
	}
	
	private String getDifferenceFromDate(Date date)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date now=new Date();
		long between=(now.getTime()-date.getTime())/1000;//把时差转为秒
		
		long day=between/(24*3600);
		long hour=between%(24*3600)/3600;
		long minute=between%3600/60;
		long second=between%60/60;
		
		if(day>0)
		{
			return format.format(date);
		}
		else if(hour>0)
		{
			return hour+new String("小时前");
		}
		else if(minute>0)
		{
			return minute+new String("分钟前");
		}
		else
		{
			return second+new String("秒前");
		}
	}
	
}
