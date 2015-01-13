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
import org.wheat.ranking.entity.BeautyDetail;
import org.wheat.ranking.entity.Photo;
import org.wheat.ranking.entity.PhotoParameters;
import org.wheat.ranking.entity.Praise;
import org.wheat.ranking.entity.json.BeautyDetailJson;
import org.wheat.ranking.entity.json.PhotoListJson;
import org.wheat.ranking.loader.HttpLoderMethods;
import org.wheat.ranking.loader.HttpUploadMethods;
import org.wheat.ranking.loader.ImageLoader;
import org.wheat.widget.QuickReturnRelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
	
	private int mPhotoWidth=0;
	
	private QuickReturnRelativeLayout mQuickReturnRelativeLayout;
	private View mQuickReturnView;
	
	
	//�Ѿ���ȡ����ȷ��ImageWidth
	private boolean allowFix=false;
	private Map<String,ImageView> taskPool;
	
	//listview��һ��item������
	private BeautyDetail mBeauty=null;
	
	
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.beauty_personal_page_layout);
		mInflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		//��ʼ��ͼƬ���������
		taskPool=new HashMap<String, ImageView>();
		
		mBeautyId=getBeautyIdFromIntent();
		mLoginUserPhoneNumber=getLoginUserPhoneNumber();
		mListData=new ArrayList<Photo>();
		mImageLoader=ImageLoader.getInstance(this);
		adapter=new BeautyPersonalPageListAdapter();
		
		mPullToRefreshListView=(PullToRefreshListView)findViewById(R.id.beauty_personal_page_refresh_list_view);
		mActualListView=mPullToRefreshListView.getRefreshableView();
		
		//���footerview
		mFooterView=mInflater.inflate(R.layout.refresh_list_footer, null);
		pbFooterLoading=(ProgressBar)mFooterView.findViewById(R.id.refresh_list_footer_progressbar);
		tvFooterText=(TextView)mFooterView.findViewById(R.id.refresh_list_footer_text);
		
		mPullToRefreshListView.setAdapter(adapter);
		mActualListView.addFooterView(mFooterView);
		
		mQuickReturnRelativeLayout=(QuickReturnRelativeLayout)findViewById(R.id.beauty_personal_page_quick_return_linearlayout);
		mQuickReturnView=mInflater.inflate(R.layout.beauty_personal_page_quick_return_view, null);
		Button btnAddPhoto=(Button) mQuickReturnView.findViewById(R.id.beauty_personal_page_return_view);
		btnAddPhoto.setOnClickListener(addphotoListener);
		mQuickReturnRelativeLayout.addQuickReturnView(mQuickReturnView);
		
		initialListViewListener();
		new UpdateDataTask().execute();
		new UpdateAlbumCoverTask().execute();
	}
	OnClickListener addphotoListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			popAddPhotoView(v);
			Intent intent = new Intent();
			intent.setClass(BeautyPersonalPageActivity.this, AddPhotoMethod.class);
			startActivity(intent);
		}
	};
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
		Log.w("BeautyPersonalPageActivity", "onScrollStateChanged");
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
		
	}
	
	private class BeautyPersonalPageListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
				return mListData.size()+1;
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
			if(position==0)
			{
				convertView=mInflater.inflate(R.layout.beauty_personal_album_cover, null);
				if(mBeauty!=null)
				{
					
					RelativeLayout ivAlbumCover=(RelativeLayout)convertView.findViewById(R.id.beauty_personal_album_cover);
					TextView tvAlbumCoverName=(TextView)convertView.findViewById(R.id.beauty_personal_album_cover_name);
					TextView tvAlbumCoverSchool=(TextView)convertView.findViewById(R.id.beauty_personal_album_cover_school);
					TextView tvAlbumCoverDescription=(TextView)convertView.findViewById(R.id.beauty_personal_album_cover_description);
					
					mImageLoader.addTask(new PhotoParameters(mBeauty.getAvatarPath(), -1, -1,true,getScreenWidth()), ivAlbumCover);
					tvAlbumCoverName.setText(mBeauty.getTrueName());
					tvAlbumCoverSchool.setText(mBeauty.getSchool());
					tvAlbumCoverDescription.setText(mBeauty.getDescription());
				}
				return convertView;
			}
			
			final Photo photo=mListData.get(position-1);
			ViewHolder holder;
			if(convertView==null||convertView.getTag()==null)
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
				holder.tvPhotoDescription=(TextView)convertView.findViewById(R.id.beauty_personal_photo_description);
				
				View PraiseView=convertView.findViewById(R.id.beauty_personal_praise_area);
				View CommentView=convertView.findViewById(R.id.beauty_personal_comment_area);
				PraiseView.setOnClickListener(new PraiseAreaOnClickListener());
				CommentView.setOnClickListener(new CommentAreaOnClickListener());
//				holder.ivPraiseButton.setOnClickListener(new PraiseButtonOnClickListener());
//				holder.ivCommentButton.setOnClickListener(new CommentButtonOnClickListener());
				convertView.setTag(holder);
			}
			else
				holder=(ViewHolder)convertView.getTag();
			
			if(mPhotoWidth<=0)
			{
				holder.ivPhoto.getViewTreeObserver().addOnGlobalLayoutListener(new GlobalLayoutLinstener(holder.ivPhoto));
			}
			
			addTaskToPool(new PhotoParameters(photo.getAvatarPath(), 50, 50*50), holder.ivUserAvatar);
			holder.tvUserNikeName.setText(photo.getNickName());
			holder.tvPublishTime.setText(getDifferenceFromDate(photo.getUploadTime()));
			addTaskToPool(new PhotoParameters(photo.getPhotoPath(),mPhotoWidth,2*mPhotoWidth*mPhotoWidth,true,mPhotoWidth), holder.ivPhoto);
			if(photo.getIsPraise())
			{
				holder.ivPraiseButton.setImageResource(R.drawable.praise_select);
			}
			else
				holder.ivPraiseButton.setImageResource(R.drawable.praise);
			holder.ivPraiseButton.setTag(photo);
			holder.tvPraiseTimes.setText(String.valueOf(photo.getPraiseCount()));
			holder.tvCommentTimes.setTag(photo);
			holder.tvCommentTimes.setText(String.valueOf(photo.getCommentCount()));
			holder.tvPhotoDescription.setText(photo.getPhotoDescription());
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
			public TextView  tvPhotoDescription;
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
				new UpdateAlbumCoverTask().execute();
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
		
		
		
		mPullToRefreshListView.setOnScrollListener(this);
	}
	
	private class UpdateAlbumCoverTask extends AsyncTask<Void, Void, BeautyDetailJson>
	{

		@Override
		protected BeautyDetailJson doInBackground(Void... params) {
			BeautyDetailJson json=null;
			try {
				json=HttpLoderMethods.getBeautyDetail(mBeautyId);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(BeautyDetailJson result) {
			if(result!=null&&result.getCode()==1000)
			{
				if(result.getData()!=null)
				{
					mBeauty=result.getData();
					adapter.notifyDataSetChanged();
				}
			}
			super.onPostExecute(result);
		}
		
		
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
			return json;
		}

		@Override
		protected void onPostExecute(PhotoListJson result) {
					
			if(result!=null&&result.getCode()==1000)
			{
				if(result.getData().getPhotoList().size()>0)
				{
					synchronized (mListData) {
						mListData.clear();
						mListData=result.getData().getPhotoList();
						adapter.notifyDataSetChanged();
					}
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
	
	private class PraiseAreaOnClickListener implements OnClickListener
	{
		private Photo photoDetails;
		private ImageView ivPraiseButton;
		@Override
		public void onClick(View v) {
			ivPraiseButton=(ImageView)v.findViewById(R.id.beauty_personal_praise_button);
			photoDetails=(Photo)ivPraiseButton.getTag();
			if(!photoDetails.getIsPraise())
			{
				photoDetails.setIspraise(true);
				photoDetails.setPraiseCount(photoDetails.getPraiseCount()+1);
				adapter.notifyDataSetChanged();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						Praise mPraise=new Praise();
						mPraise.setBeautyId(mBeautyId);
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
			tvCommentTimes=(TextView)v.findViewById(R.id.beauty_personal_comment_times);
			mPhotoDetails=(Photo)tvCommentTimes.getTag();
			Intent intent=new Intent(BeautyPersonalPageActivity.this,PhotoCommentActivity.class);
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
		}
		
	}
	
	
	/**
	 * ��סʱ�����ܼ���������ͼƬ��ֻ�ܼ��ع̶�ͼƬ
	 */
	public void lockTaskPool()
	{
		this.allowFix=false;
	}
	
	/**
	 * ��������󣬿��Լ���������ͼƬ
	 */
	public void unLockTaskPool()
	{
		if(!allowFix)
		{
			this.allowFix=true;
		}
		doTaskInPool();
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
						pp.setImageViewWidth(mPhotoWidth);
						pp.setMinSideLength(mPhotoWidth);
						pp.setMaxNumOfPixles(2*mPhotoWidth*mPhotoWidth);
						mImageLoader.addTask(pp, img);
					}
				}
			}
			taskPool.clear();
		}
		
	}
	
	

	private void popAddPhotoView(View parent){
		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		//�Զ��岼��
		View menuView = mLayoutInflater.inflate(
				R.layout.add_beauty_photo, null, false);
		PopupWindow mPop = new PopupWindow(menuView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		// ʹ��ۼ� ��Ҫ������˵���ؼ����¼��ͱ���Ҫ���ô˷���
		mPop.setFocusable(true);
		// ����������������ʧ
		mPop.setOutsideTouchable(false);
		//�����ҪPopupWindow��Ӧ���ؼ�����ô�����PopupWindow����һ����������
		ColorDrawable dw = new ColorDrawable(0X00000000);
		mPop.setBackgroundDrawable(dw);
//		mPop.setContentView(menuView );//���ð�����ͼ
		int width = (int) (getScreenWidth()*0.9);
		int height = (int) (getScreenHeight()*0.9);
		mPop.setWidth(width);
		mPop.setHeight(height );//���õ������С
		mPop.showAsDropDown(parent,40,40);
		mPop.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
	}
	private int getScreenWidth() {
		WindowManager wm = this.getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();
		return width;
	}
	private int getScreenHeight() {
		WindowManager wm = this.getWindowManager();
		int width = wm.getDefaultDisplay().getHeight();
		return width;
	}
}
