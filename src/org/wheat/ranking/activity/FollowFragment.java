package org.wheat.ranking.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wheat.beautyranking.R;
import org.wheat.ranking.data.UserLoginPreference;
import org.wheat.ranking.entity.Photo;
import org.wheat.ranking.entity.PhotoParameters;
import org.wheat.ranking.entity.Praise;
import org.wheat.ranking.entity.json.PhotoListJson;
import org.wheat.ranking.loader.HttpLoderMethods;
import org.wheat.ranking.loader.HttpUploadMethods;
import org.wheat.ranking.loader.ImageLoader;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

/** 
 * description
 * @author wheat
 * date: 2014-12-14  
 * time: 下午9:27:44
 */
public class FollowFragment extends Fragment implements OnScrollListener
{

	private final int PAGE_LENGTH=10;//每次请求数据页里面包含的最多数据项
	private PullToRefreshListView mPullToRefreshListView;
	private List<Photo> mListData;//保存listview数据项的数组
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;//加载图片的对象
	private FollowRefreshListAdapter adapter;
	
	private boolean isLoadingMore=false;//防止重复开启异步加载线程
	private View mFooterView;
	private TextView tvFooterText;
	private ProgressBar pbFooterLoading;
	private ListView mActualListView;//PulltoRefreshListView中真正的ListView
	
	private String mLoginUserPhoneNumber;
	
	private int mPhotoWidth=0;
	//已经获取到正确的ImageWidth
	private boolean allowFix=false;
	private Map<String,ImageView> taskPool;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mListData=new ArrayList<Photo>();
		mImageLoader=ImageLoader.getInstance(getActivity().getApplicationContext());
		adapter=new FollowRefreshListAdapter();
		mLoginUserPhoneNumber=getLoginUserPhoneNumber();
		
		new UpdateDataTask().execute();
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater=inflater;
		
		taskPool=new HashMap<String, ImageView>();
		
		View view=inflater.inflate(R.layout.fragment_follow, container, false);
		mPullToRefreshListView=(PullToRefreshListView)view.findViewById(R.id.follow_refresh_list_view);
		
		mActualListView=mPullToRefreshListView.getRefreshableView();
		mFooterView=inflater.inflate(R.layout.refresh_list_footer, null);
		pbFooterLoading=(ProgressBar)mFooterView.findViewById(R.id.refresh_list_footer_progressbar);
		tvFooterText=(TextView)mFooterView.findViewById(R.id.refresh_list_footer_text);
		
		mPullToRefreshListView.setAdapter(adapter);
		mActualListView.addFooterView(mFooterView);
		initialListViewListener();
		
		return view;
	}

	


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		int position=-1;
		int count=-1;
		if(data!=null)
		{
			if(requestCode==1)
			{
				position=data.getIntExtra("position", -1);
				count=data.getIntExtra("count", -1);
			}
		}
		if(position!=1&&mListData.size()>=position&&count>0)
		{
			mListData.get(position).setCommentCount(mListData.get(position).getCommentCount()+count);
			adapter.notifyDataSetChanged();
		}
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
	
	
	public class FollowRefreshListAdapter extends BaseAdapter
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final Photo listItem=mListData.get(position);
			ViewHolder holder=null;
			if(convertView==null)
			{
				holder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.fragment_follow_list_item, null);
				holder.ivUserAvatar=(ImageView)convertView.findViewById(R.id.fragment_follow_user_avatar);
				holder.tvUserNickName=(TextView)convertView.findViewById(R.id.fragment_follow_publisher_nikename);
				holder.ivPhoto=(ImageView)convertView.findViewById(R.id.fragment_follow_photo);
				holder.tvPhotoDescription=(TextView)convertView.findViewById(R.id.fragment_follow_photo_description);
				holder.ivPraiseButton=(ImageView)convertView.findViewById(R.id.fragment_follow_praise_button);
				holder.tvPraiseTimes=(TextView)convertView.findViewById(R.id.fragment_follow_praise_times);
				holder.ivCommentButton=(ImageView)convertView.findViewById(R.id.fragment_follow_comment_button);
				holder.tvCommentTimes=(TextView)convertView.findViewById(R.id.fragment_follow_comment_times);
				holder.tvPublishTime=(TextView)convertView.findViewById(R.id.fragment_follow_publish_time);
				convertView.setTag(holder);
				
				View PraiseView=convertView.findViewById(R.id.fragment_follow_praise_area);
				View CommentView=convertView.findViewById(R.id.fragment_follow_comment_area);
				PraiseView.setOnClickListener(new PraiseAreaOnClickListener());
				CommentView.setOnClickListener(new CommentAreaOnClickListener());
				holder.ivPhoto.setOnClickListener(new PhotoOnClickListener());
			}
			else
				holder=(ViewHolder)convertView.getTag();
			
			
			//为了获取ImageView的宽度,给ImageView设置监听器
			if(mPhotoWidth<=0)
			{
				holder.ivPhoto.getViewTreeObserver().addOnGlobalLayoutListener(new GlobalLayoutLinstener(holder.ivPhoto));
			}
			
			
			addTaskToPool(new PhotoParameters(listItem.getAvatarPath(), 50, 50*50, false),holder.ivUserAvatar);
			holder.tvUserNickName.setText(listItem.getNickName());
			holder.ivPhoto.setTag(R.id.tag_first, listItem);
			addTaskToPool(new PhotoParameters(listItem.getPhotoPath(), mPhotoWidth, 2*mPhotoWidth*mPhotoWidth, true), holder.ivPhoto);
			holder.tvPhotoDescription.setText(listItem.getPhotoDescription());
			holder.tvPraiseTimes.setText(String.valueOf(listItem.getPraiseCount()));
			holder.tvCommentTimes.setText(String.valueOf(listItem.getCommentCount()));
			if(listItem.getIsPraise())
			{
				holder.ivPraiseButton.setImageResource(R.drawable.praisefull);
			}
			else
				holder.ivPraiseButton.setImageResource(R.drawable.praise);
			holder.ivPraiseButton.setTag(listItem);
			holder.tvCommentTimes.setTag(listItem);
			holder.tvPublishTime.setText(getDifferenceFromDate(listItem.getUploadTime()));
			
			return convertView;
		}
		
		private final class ViewHolder
		{
			public ImageView ivUserAvatar;
			public TextView tvUserNickName;
			public TextView  tvPhotoDescription;
			public ImageView ivPhoto;
			public ImageView ivPraiseButton;
			public TextView  tvPraiseTimes;
			public ImageView ivCommentButton;
			public TextView  tvCommentTimes;
			public TextView  tvPublishTime;
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
				new UpdateDataTask().execute();
			}
		});
		
		mPullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
//				Toast.makeText(getActivity(), "End of List!", Toast.LENGTH_SHORT).show();
				if(!isLoadingMore)
				{
					isLoadingMore=true;
					pbFooterLoading.setVisibility(View.VISIBLE);
					tvFooterText.setText(R.string.list_footer_loading);
					new LoadMoreTask(mListData.size(), PAGE_LENGTH).execute();
				}
			}
		});
		
		mPullToRefreshListView.setOnScrollListener(this);
	}
	
	/**
	 * 
	 * description:刷新ListView内容的异步线程
	 * @author wheat
	 * date: 2014-12-15  
	 * time: 上午10:37:59
	 */
	private class UpdateDataTask extends AsyncTask<Void, Void, PhotoListJson>
	{
		@Override
		protected PhotoListJson doInBackground(Void... params) {
			PhotoListJson json=null;
			try {
				json=HttpLoderMethods.getBeautyAllPhotos(0, PAGE_LENGTH, 8,"18825162410");
			} catch (Throwable e) {
				e.printStackTrace();
			}
//			if(json==null)
//			{
//				Log.w("TabSumFragment","json is null------------->");
//				return null;
//			}
//			final ArrayList<BeautyIntroduction> data=(ArrayList<BeautyIntroduction>)json.getData().getIntroductionList();
			return json;
		}

		@Override
		protected void onPostExecute(PhotoListJson result) {
			if(result!=null&&result.getCode()==1000)
			{
				synchronized (mListData) {
					mListData.clear();
					mListData=result.getData().getPhotoList();
					adapter.notifyDataSetChanged();
				}
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
	private class LoadMoreTask extends AsyncTask<Void, Void, PhotoListJson>
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
		protected PhotoListJson doInBackground(Void... params) {
			PhotoListJson json=null;
			try {
				json=HttpLoderMethods.getBeautyAllPhotos(firstIndex, count, 8,"18825162410");
			} catch (Throwable e) {
				e.printStackTrace();
			}
//			if(json==null)
//			{
//				Log.w("TabSumFragment","json is null------------->");
//				return null;
//			}
//			final ArrayList<BeautyIntroduction> data=(ArrayList<BeautyIntroduction>)json.getData().getIntroductionList();
			return json;
		}

		@Override
		protected void onPostExecute(PhotoListJson result) {
			if(result!=null&&result.getCode()==1000)
			{
				synchronized (mListData) {
					mListData.addAll(result.getData().getPhotoList());
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
	
	
	private class PraiseAreaOnClickListener implements OnClickListener
	{
		private Photo photoDetails;
		private ImageView ivPraiseButton;
		@Override
		public void onClick(View v) {
			ivPraiseButton=(ImageView)v.findViewById(R.id.fragment_follow_praise_button);
			photoDetails=(Photo)ivPraiseButton.getTag();
			if(!photoDetails.getIsPraise())
			{
				photoDetails.setIspraise(true);
				photoDetails.setPraiseCount(photoDetails.getPraiseCount()+1);
//				tvPraiseTimes.setText(String.valueOf(photoDetails.getPraiseCount()));
//				ivPraiseButton.setImageResource(R.drawable.praise_select);
				adapter.notifyDataSetChanged();
				//add  praise_record
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						Praise mPraise=new Praise();
						mPraise.setBeautyId(photoDetails.getBeautyId());
						mPraise.setPhotoId(photoDetails.getPhotoId());
						mPraise.setPraiseTime(new Date());
						mPraise.setUserPhoneNumber(mLoginUserPhoneNumber);
						try {
							HttpUploadMethods.UploadPraisePost(mPraise);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
			}
			else
			{
				
			}
		}
		
	}
	
	private class CommentAreaOnClickListener implements OnClickListener
	{
		private Photo mPhotoDetails;
		private TextView tvCommentTimes;
		@Override
		public void onClick(View v) {
			tvCommentTimes=(TextView)v.findViewById(R.id.fragment_follow_comment_times);
			mPhotoDetails=(Photo)tvCommentTimes.getTag();
			Intent intent=new Intent(getActivity(),PhotoCommentActivity.class);
			Bundle bundle=new Bundle();
			bundle.putString("avatarPath", mPhotoDetails.getAvatarPath());
			bundle.putString("nickName", mPhotoDetails.getNickName());
			bundle.putSerializable("uploadTime", mPhotoDetails.getUploadTime());
			bundle.putString("photoPath", mPhotoDetails.getPhotoPath());
			bundle.putString("photoDescription", mPhotoDetails.getPhotoDescription());
			bundle.putInt("photoId", mPhotoDetails.getPhotoId());
			intent.putExtras(bundle);
			startActivityForResult(intent, 1);
		}
		
	}
	
	private class PhotoOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			Photo photo=(Photo)v.getTag(R.id.tag_first);
			Intent intent=new Intent();
			intent.putExtra("mBeautyID", photo.getBeautyId());
			intent.setClass(getActivity(), BeautyPersonalPageActivity.class);
			startActivity(intent);
		}
		
	}
	
	//从SharePreference中获取用户的手机号码
	private String getLoginUserPhoneNumber()
	{
		UserLoginPreference preference=UserLoginPreference.getInstance(getActivity().getApplicationContext());
		return preference.getuserPhoneNumber();
	}
	
	@SuppressLint("SimpleDateFormat")
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

	public class GlobalLayoutLinstener implements OnGlobalLayoutListener
	{
		private View view;
		public GlobalLayoutLinstener(View view)
		{
			this.view=view;
		}

		@Override
		public void onGlobalLayout() {
			mPhotoWidth=view.getWidth();
			if(mPhotoWidth>0)
			{
				unLockTaskPool();
				view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
			Log.w("FollowFragment", "mPhotoWidth="+mPhotoWidth);
		}
		
	}
	
	/**
	 * 锁住时，不能加载自适配图片，只能加载固定图片
	 */
	public void lockTaskPool()
	{
		this.allowFix=false;
	}
	
	/**
	 * 解除锁定后，可以加载自适配图片
	 */
	public void unLockTaskPool()
	{
		if(!allowFix)
		{
			this.allowFix=true;
			doTaskInPool();
		}
	}
	
	public void addTaskToPool(PhotoParameters parameters,ImageView img)
	{
		if(!parameters.isFixWidth())
		{
			mImageLoader.addTask(parameters, img);
		}
		else
		{
			synchronized (taskPool) {
				img.setTag(parameters);
				taskPool.put(Integer.toString(img.hashCode()), img);
			}
			if(allowFix)
			{
				doTaskInPool();
			}	
		}
	}
	
	public void doTaskInPool()
	{
		synchronized (taskPool) {
			Collection<ImageView> con=taskPool.values();
			for(ImageView img:con)
			{
				if(img!=null)
				{
					if(img.getTag()!=null)
					{
						PhotoParameters pp=(PhotoParameters)img.getTag();
						pp.setMinSideLength(mPhotoWidth);
						pp.setMaxNumOfPixles(2*mPhotoWidth*mPhotoWidth);
						mImageLoader.addTask(pp, img);
					}
				}
			}
			taskPool.clear();
		}
	}
}
