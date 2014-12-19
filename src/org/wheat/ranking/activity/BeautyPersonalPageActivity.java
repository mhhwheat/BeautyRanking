package org.wheat.ranking.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.wheat.beautyranking.R;
import org.wheat.ranking.data.UserLoginPreference;
import org.wheat.ranking.entity.Photo;
import org.wheat.ranking.entity.PhotoParameters;
import org.wheat.ranking.entity.Praise;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
 * description:beauty�ĸ�����ҳ
 * @author wheat
 * date: 2014-12-15  
 * time: ����7:32:05
 */
public class BeautyPersonalPageActivity extends Activity implements OnScrollListener
{
//	private final int mDeviceScreenWidth=getDeviceScreenWidth();//�豸��Ļ���
	private int mBeautyId;//��ҳ����ʾ��BeautyId��Ӧ��Beauty������ͼƬ
	private String mLoginUserPhoneNumber;//�豸�ϵ�¼���û����ֻ�����
	
	
	private final int PAGE_LENGTH=10;//ÿ����������ҳ������������������
	private PullToRefreshListView mPullToRefreshListView;
	private List<Photo> mListData;//����listview�����������
	private ImageLoader mImageLoader;//����ͼƬ�Ķ���
	private BeautyPersonalPageListAdapter adapter;
	private LayoutInflater mInflater;
	
	private boolean isLoadingMore=false;//��ֹ�ظ������첽�����߳�
	private View mFooterView;
	private TextView tvFooterText;
	private ProgressBar pbFooterLoading;
	private ListView mActualListView;//PulltoRefreshListView��������ListView

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.beauty_personal_page_layout);
		
		mBeautyId=getBeautyIdFromIntent();
		mLoginUserPhoneNumber=getLoginUserPhoneNumber();
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
		new UpdateDataTask().execute();
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
				
				holder.ivPraiseButton.setOnClickListener(new PraiseButtonOnClickListener());
				holder.ivCommentButton.setOnClickListener(new CommentButtonOnClickListener());
			}
			else
				holder=(ViewHolder)convertView.getTag();
			
			int minSideLength=holder.ivPhoto.getWidth();
			mImageLoader.addTask(new PhotoParameters(photo.getAvatarPath(), 50, 50*50), holder.ivUserAvatar);
			holder.tvUserNikeName.setText(photo.getNickName());
			holder.tvPublishTime.setText(getDifferenceFromDate(photo.getUploadTime()));
			mImageLoader.addTask(new PhotoParameters(photo.getPhotoPath(),minSideLength , 2*minSideLength*minSideLength), holder.ivPhoto);
			holder.ivPraiseButton.setTag(photo);
			holder.tvPraiseTimes.setText(String.valueOf(photo.getPraiseCount()));
			holder.ivCommentButton.setTag(photo);
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
	 * description:ˢ��ListView���ݵ��첽�߳�
	 * @author wheat
	 * date: 2014-12-15  
	 * time: ����10:37:59
	 */
	private class UpdateDataTask extends AsyncTask<Void, Void, PhotoListJson>
	{
		@Override
		protected PhotoListJson doInBackground(Void... params) {
			PhotoListJson json=null;
			try {
				json=HttpLoderMethods.getBeautyAllPhotos(0, PAGE_LENGTH, mBeautyId,mLoginUserPhoneNumber);
			} catch (Throwable e) {
				e.printStackTrace();
			}
//			if(json==null)
//			{
//				Log.w("TabSumFragment","json is null------------>");
//				return null;
//			}
//			if(json.getData()==null)
//				return null;
//			final ArrayList<Photo> data=(ArrayList<Photo>)json.getData().getPhotoList();
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
			
			if(result==null||result.getCode()!=1000)
				onLoadComplete(true);
			else
				onLoadComplete(false);
			
			super.onPostExecute(result);
		}
		
	}
	
	/**
	 * 
	 * description�����ظ������ݵ��첽�߳�
	 * @author wheat
	 * date: 2014-12-15  
	 * time: ����5:10:57
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
				json=HttpLoderMethods.getBeautyAllPhotos(firstIndex, count, mBeautyId,mLoginUserPhoneNumber);
			} catch (Throwable e) {
				e.printStackTrace();
			}
//			if(json==null)
//			{
//				Log.w("TabSumFragment","json is null------------->");
//				return null;
//			}
//			final ArrayList<Photo> data=(ArrayList<Photo>)json.getData().getPhotoList();
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
	 * @param wasLoadNothing ������ɺ��Ƿ�����û������,true��ʾ����û������,false��ʾ����������
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
	
	
	
	private String getDifferenceFromDate(Date date)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date now=new Date();
		long between=(now.getTime()-date.getTime())/1000;//��ʱ��תΪ��
		
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
			return hour+new String("Сʱǰ");
		}
		else if(minute>0)
		{
			return minute+new String("����ǰ");
		}
		else
		{
			return second+new String("��ǰ");
		}
	}
	
	private class PraiseButtonOnClickListener implements OnClickListener
	{
		private Photo photoDetails;
		@Override
		public void onClick(View v) {
			photoDetails=(Photo)v.getTag();
			ImageView view=(ImageView)v;
			if(photoDetails.getIsPraise())
			{
				photoDetails.setIspraise(false);
				view.setImageResource(R.drawable.praise);
				//add praise_record
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						Praise mPraise=new Praise();
						mPraise.setBeautyId(mBeautyId);
						mPraise.setPhotoId(photoDetails.getPhotoId());
						mPraise.setPraiseTime(new Date());
						mPraise.setUserPhoneNumber(mLoginUserPhoneNumber);
					}
				}).start();
			}
			else
			{
				photoDetails.setIspraise(true);
				view.setImageResource(R.drawable.praise_select);
				//delete praise_record
			}
		}
		
	}
	
	private class CommentButtonOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			
		}
		
	}
	
	/**
	 * ͨ��Intent��ȡ��һ��Activity������BeautyId;
	 * @return
	 */
	private int getBeautyIdFromIntent()
	{
//		Intent intent=getIntent();
//		Bundle bundle=intent.getExtras();
//		return bundle.getInt("mBeautyID");
		return 8;
	}
	
	//��SharePreference�л�ȡ�û����ֻ�����
	private String getLoginUserPhoneNumber()
	{
		UserLoginPreference preference=UserLoginPreference.getInstance(getApplicationContext());
		return preference.getuserPhoneNumber();
	}
	
	/*
	private int getDeviceScreenWidth()
	{
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	*/
	
}
