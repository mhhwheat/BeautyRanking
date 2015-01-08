package org.wheat.ranking.activity;

import java.util.ArrayList;
import java.util.List;

import org.wheat.beautyranking.R;
import org.wheat.ranking.checkUpdate.SettingPage;
import org.wheat.ranking.data.UserLoginPreference;
import org.wheat.ranking.entity.Photo;
import org.wheat.ranking.entity.PhotoParameters;
import org.wheat.ranking.entity.UserInfo;
import org.wheat.ranking.entity.json.PhotoListJson;
import org.wheat.ranking.loader.HttpLoderMethods;
import org.wheat.ranking.loader.ImageLoader;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/** 
 * description:beauty�ĸ�����ҳ
 * @author wheat
 * date: 2014-12-15  
 * time: ����7:32:05
 */
public class MyDetailPage extends Fragment implements OnScrollListener
{
	int whichButton=1;//���õ�ǰ����һ����ť��Ĭ�ϵ�һλΪ�ҵĴ���ҳ��
	
	
//	private final int mDeviceScreenWidth=getDeviceScreenWidth();//�豸��Ļ���
	private int mBeautyId;//��ҳ����ʾ��BeautyId��Ӧ��Beauty������ͼƬ
	private int mBeautyId2=7;
	private String mLoginUserPhoneNumber;//�豸�ϵ�¼���û����ֻ�����
	private UserInfo userInfo;//�û��ҵ�ҳ�����Ϣ
	
	private final int PAGE_LENGTH=10;//ÿ����������ҳ������������������
	private PullToRefreshListView mPullToRefreshListView;
	private List<Photo> mListData;//����listview�����������
	private List<Photo> mMyFocusListData;//�����ҵĹ�ע������
	private ImageLoader mImageLoader;//����ͼƬ�Ķ���
	private BeautyPersonalPageListAdapter adapter;
	private LayoutInflater mInflater;
	
	private boolean isLoadingMore=false;//��ֹ�ظ������첽�����߳�
	private View mFooterView;
	private TextView tvFooterText;
	private ProgressBar pbFooterLoading;
	private ListView mActualListView;//PulltoRefreshListView��������ListView
	
	
	
	
	//header
		private View mHeaderView;
		private ImageView ivHeaderAvatar;
		private TextView tvHeaderNickName;
		private TextView tvHeaderBeautyId;
		private ImageView ivSetting;
		private TextView tvCreate;//����
		private TextView tvLike;//ϲ��
		private TextView tvFollow;//��˿
		private TextView tvFocus;//��ע
		private TextView tvPersonSign;//����ǩ��
		private LinearLayout rlCreate;
		private LinearLayout rlLike;
		private LinearLayout rlFollow;
		private LinearLayout rlFocus;

		Activity parentActivity;
	@Override
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
			super.onAttach(activity);
			parentActivity=activity;
		}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//���ñ������Ĳ��֣���ɫ��С��Ҫ��styles�����ã����콾��AndroidManifest.xml�ļ���
		mBeautyId=getBeautyIdFromIntent();
		mLoginUserPhoneNumber=getLoginUserPhoneNumber();
		
		mListData=new ArrayList<Photo>();
		mMyFocusListData=new ArrayList<Photo>();
		mImageLoader=ImageLoader.getInstance(getActivity().getApplicationContext());
		adapter=new BeautyPersonalPageListAdapter();
		
		
		new UpdateDataTask().execute();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater=inflater;
		View view=inflater.inflate(R.layout.my_detail_page, container, false);
		mPullToRefreshListView=(PullToRefreshListView)view.findViewById(R.id.my_detail_page_refresh_listview);
		mActualListView=mPullToRefreshListView.getRefreshableView();
		
		mFooterView=mInflater.inflate(R.layout.refresh_list_footer, null);
		pbFooterLoading=(ProgressBar)mFooterView.findViewById(R.id.refresh_list_footer_progressbar);
		tvFooterText=(TextView)mFooterView.findViewById(R.id.refresh_list_footer_text);
		
		mPullToRefreshListView.setAdapter(adapter);
		mActualListView.addFooterView(mFooterView);
		/**
		 * ���mypageͷ��
		 */
		initialHeader();
		
		rlFocus.setOnClickListener(new rlFocusOnClickListener());
		rlCreate.setOnClickListener(new rlCreateOnClickListener());
		mActualListView.addHeaderView(mHeaderView);
		initialListViewListener();
		return view;
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
			if(whichButton==1)
				return mListData.size();
			else 
				return mMyFocusListData.size();
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
			final Photo photo;
			if(whichButton==1)
				photo=mListData.get(position);
			else
				photo=mMyFocusListData.get(position);
			
			ViewHolder holder;
			if(convertView==null)
			{
				holder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.my_page_item, null);
				holder.ivPhoto=(ImageView)convertView.findViewById(R.id.my_page_item_img);
				holder.tvPraiseTimes=(TextView)convertView.findViewById(R.id.mypageitemprisetimes);
				holder.tvCommentTimes=(TextView)convertView.findViewById(R.id.mypageitemcommenttimes);
				convertView.setTag(holder);
			}
			else
				holder=(ViewHolder)convertView.getTag();
			
			mImageLoader.addTask(new PhotoParameters(photo.getPhotoPath(),-1 , -1,true), holder.ivPhoto);

			holder.tvPraiseTimes.setText(String.valueOf(photo.getPraiseCount()));
			holder.tvCommentTimes.setText(String.valueOf(photo.getCommentCount()));
			return convertView;
		}
		
		private final class ViewHolder
		{
			public ImageView ivPhoto;
			public TextView  tvPraiseTimes;
			public TextView  tvCommentTimes;
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
				if(!isLoadingMore)
				{
					isLoadingMore=true;
					pbFooterLoading.setVisibility(View.VISIBLE);
					tvFooterText.setText(R.string.list_footer_loading);
					if(whichButton==1)
						new LoadMoreTask(mListData.size(), PAGE_LENGTH).execute();
					else
						new LoadMoreTask(mMyFocusListData.size(), PAGE_LENGTH).execute();
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
				//������Ҫ�޸�Ϊ�Լ���ҳ��
				if(whichButton==1){
					json=HttpLoderMethods.getMyCreatePageWithPhoto
							(0, PAGE_LENGTH, mLoginUserPhoneNumber);
				}else{
					json=HttpLoderMethods.getMyFollowPageWithPhoto
							(0, PAGE_LENGTH,mLoginUserPhoneNumber);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(PhotoListJson result) {
			if(result!=null&&result.getCode()==1000)
			{
				if(whichButton==1){
					synchronized (mListData) {
						mListData.clear();
						mListData=result.getData().getPhotoList();
						adapter.notifyDataSetChanged();
					}
				}else{
					synchronized (mMyFocusListData) {
						mMyFocusListData.clear();
						mMyFocusListData=result.getData().getPhotoList();
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
				if(whichButton==1){
					json=HttpLoderMethods.getMyCreatePageWithPhoto
							(firstIndex, count,mLoginUserPhoneNumber);
				}else{
					json=HttpLoderMethods.getMyFollowPageWithPhoto
							(firstIndex, count,mLoginUserPhoneNumber);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(PhotoListJson result) {
			if(result!=null&&result.getCode()==1000)
			{
				if(whichButton==1){
					synchronized (mListData) {
						mListData.addAll(result.getData().getPhotoList());
						adapter.notifyDataSetChanged();
					}
				}else{
					synchronized (mMyFocusListData) {
						mMyFocusListData.addAll(result.getData().getPhotoList());
						adapter.notifyDataSetChanged();
					}
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
	
	
	/**
	 * 
	* @ClassName: rlFocusOnClickListener 
	* @Description: ���ù�ע��ť���¼� 
	* @author hogachen
	* @date 2014��12��25�� ����1:27:53 
	*
	 */
	
	private class rlFocusOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			whichButton=2;
			adapter.notifyDataSetChanged();
			new UpdateDataTask().execute();
		}	
	}
	
	
	private class rlCreateOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			whichButton=1;
			adapter.notifyDataSetChanged();
			new UpdateDataTask().execute();
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
//		UserLoginPreference preference=UserLoginPreference.getInstance(getApplicationContext());
//		return preference.getuserPhoneNumber();
		return "18825162410";
	}
	
	

	
	private void initialHeader()
	{
		mHeaderView=mInflater.inflate(R.layout.mypage_header, null);
		ivHeaderAvatar=(ImageView)mHeaderView.findViewById(R.id.myavatar);
		tvHeaderNickName=(TextView)mHeaderView.findViewById(R.id.mynickname);
		tvHeaderBeautyId=(TextView)mHeaderView.findViewById(R.id.mybeautyid);
		ivSetting=(ImageView)mHeaderView.findViewById(R.id.mysettingtrue);
		tvCreate=(TextView)mHeaderView.findViewById(R.id.mycreatenum);
		rlCreate=(LinearLayout)mHeaderView.findViewById(R.id.mycreate);
//		tvLike=(TextView)mHeaderView.findViewById(R.id.mylikenum);
//		rlLike=(RelativeLayout)mHeaderView.findViewById(R.id.mylike);
		tvFollow=(TextView)mHeaderView.findViewById(R.id.myfollownum);
		rlFollow=(LinearLayout)mHeaderView.findViewById(R.id.myfollow);
		tvFocus=(TextView)mHeaderView.findViewById(R.id.myfocusnum);
		rlFocus=(LinearLayout)mHeaderView.findViewById(R.id.myfocus);
		tvPersonSign=(TextView)mHeaderView.findViewById(R.id.mypersonalsign);
		//�ӱ��ػ�ȡͷ���ǳƣ�����ǩ�������д��������ݵȣ��������ݵ�ʱ��Ҳ��Ҫ�����ݴ���local�ļ�
		getDataFromLocal();
		ivSetting.setOnClickListener(new SettingListener());
	}
	class SettingListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent settingIntent = new Intent();
			settingIntent.setClass(parentActivity,SettingPage.class);
			startActivity(settingIntent);
		}
		
	}
	/**
	 * 
	* @Description: �ӱ����ļ���ȡ������Ϣ
	* @author hogachen   
	* @date 2014��12��24�� ����5:22:52 
	* @version V1.0
	 */
	private void getDataFromLocal(){
		String fileName = "/data/data/com.test/aa.png"; 
		Bitmap bm = BitmapFactory.decodeFile(fileName); 
		if(bm!=null){
			System.err.println("hogahcen the avatar is null");
			ivHeaderAvatar.setImageBitmap(bm);
		}	 
		UserLoginPreference preference=UserLoginPreference.getInstance(getActivity().getApplicationContext());

		tvCreate.setText(String.valueOf(preference.getUserInfoCreateNum()));
//		tvLike.setText(String.valueOf(preference.getUserInfoLike()));
		tvFocus.setText(String.valueOf(preference.getUserInfoFocusNum()));
		tvFollow.setText("0");//��ʱ����Ϊ0�������Ժ�ȥ���ù���
		tvPersonSign.setText(preference.getUserInfoPersionSign());
		tvHeaderNickName.setText(preference.getUserInfoNickname());
		tvHeaderBeautyId.setText("");
	}

	
}
