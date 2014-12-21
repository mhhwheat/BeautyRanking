package org.wheat.ranking.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.wheat.beautyranking.R;
import org.wheat.ranking.entity.Comment;
import org.wheat.ranking.entity.Photo;
import org.wheat.ranking.entity.PhotoParameters;
import org.wheat.ranking.entity.json.CommentListJson;
import org.wheat.ranking.loader.HttpLoderMethods;
import org.wheat.ranking.loader.ImageLoader;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoCommentActivity extends Activity implements OnScrollListener
{
	private final int COMMENT_LENGTH=20;
	
	private ListView mListView;
	private List<Comment> mListData;//保存listview数据项的链表数组
	private Photo mCurrentPhoto;
	private ImageLoader mImageLoader;
	private PhotoCommentListAdapter adapter;
	private LayoutInflater mInflater;
	
	private boolean mLastItemVisible;
	private boolean isLoadingMore=false;//防止重复开启异步加载线程
	
	//header
	private View mHeaderView;
	private ImageView ivHeaderAvatar;
	private TextView tvHeaderNickName;
	private TextView tvHeaderPublishTime;
	private ImageView ivHeaderPhoto;
	private TextView tvHeaderPhotoDescription;
	
//	//Footer
//	private View mFooterView;
//	private TextView tvFooterText;
//	private ProgressBar pbFooterLoading;
	
	
	//弹出的编辑窗口
	private PopupWindow mPopWindow;
	private Button btComment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.photo_comment_layout);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.photo_comment_title);
		
		mCurrentPhoto=getPhotoFromIntent();
		
		mInflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mListData=new LinkedList<Comment>();
		mImageLoader=ImageLoader.getInstance(this);
		adapter=new PhotoCommentListAdapter();
		
		mListView=(ListView)findViewById(R.id.photo_comment_list_view);
		btComment=(Button)findViewById(R.id.photo_comment_comment_button);
		initialHeader();
//		initialFooter();
		
		mListView.addHeaderView(mHeaderView);
//		mListView.addFooterView(mFooterView);
		mListView.setAdapter(adapter);
		btComment.setOnClickListener(new CommentOnClickListener());
		
		new UpdateCommentsTask().execute();
		
	}
	
	private void initialHeader()
	{
		mHeaderView=mInflater.inflate(R.layout.photo_comment_list_header, null);
		ivHeaderAvatar=(ImageView)mHeaderView.findViewById(R.id.photo_comment_header_avatar);
		tvHeaderNickName=(TextView)mHeaderView.findViewById(R.id.photo_comment_header_nike_name);
		tvHeaderPublishTime=(TextView)mHeaderView.findViewById(R.id.photo_comment_header_publish_time);
		ivHeaderPhoto=(ImageView)mHeaderView.findViewById(R.id.photo_comment_header_photo);
		tvHeaderPhotoDescription=(TextView)mHeaderView.findViewById(R.id.photo_comment_header_description);
		
		mImageLoader.addTask(new PhotoParameters(mCurrentPhoto.getAvatarPath(), 50, 50*50), ivHeaderAvatar);
		tvHeaderNickName.setText(mCurrentPhoto.getNickName());
		tvHeaderPublishTime.setText(getDifferenceFromDate(mCurrentPhoto.getUploadTime()));
		mImageLoader.addTask(new PhotoParameters(mCurrentPhoto.getPhotoPath(), -1, -1), ivHeaderPhoto);
		tvHeaderPhotoDescription.setText(mCurrentPhoto.getPhotoDescription());
	}
	
//	private void initialFooter()
//	{
//		mFooterView=mInflater.inflate(R.layout.photo_comment_list_footer, null);
//		tvFooterText=(TextView)mFooterView.findViewById(R.id.photo_comment_list_footer_text);
//		pbFooterLoading=(ProgressBar)mFooterView.findViewById(R.id.photo_comment_list_footer_progressbar);
//	}
	
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
		
		//最后一个item可见，并且滑屏停止的情况下调用onLastItemVisible这个方法
		if(scrollState==OnScrollListener.SCROLL_STATE_IDLE&&mLastItemVisible)
			onLastItemVisible();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		mLastItemVisible=(totalItemCount>0)&&(firstVisibleItem + visibleItemCount >= totalItemCount - 1);
	}
	
	private class PhotoCommentListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(mListData.size()>0)
				return mListData.size();
			else
				return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final Comment comment=mListData.get(position);
			ViewHolder holder=null;
			if(convertView==null)
			{
				holder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.photo_comment_list_comment_item, null);
				holder.ivUserAvatar=(ImageView)convertView.findViewById(R.id.photo_comment_user_avatar);
				holder.tvUserNickName=(TextView)convertView.findViewById(R.id.photo_comment_user_nike_name);
				holder.tvCommentTime=(TextView)convertView.findViewById(R.id.photo_comment_comment_time);
				holder.tvCommentContent=(TextView)convertView.findViewById(R.id.photo_comment_comment_content);
				convertView.setTag(holder);
			}
			else
				holder=(ViewHolder)convertView.getTag();
			
			mImageLoader.addTask(new PhotoParameters(comment.getUserAvatar(), holder.ivUserAvatar.getWidth(), holder.ivUserAvatar.getWidth()*holder.ivUserAvatar.getHeight()), holder.ivUserAvatar);
			holder.tvUserNickName.setText(comment.getUserNickName());
			holder.tvCommentTime.setText(getDifferenceFromDate(comment.getCommentTime()));
			holder.tvCommentContent.setText(comment.getCommentContent());
			
			return convertView;
		}
		
		private final class ViewHolder
		{
			public ImageView ivUserAvatar;
			public TextView tvUserNickName;
			public TextView tvCommentTime;
			public TextView tvCommentContent;
		}
	}
	
	private class UpdateCommentsTask extends AsyncTask<Void, Void, CommentListJson>
	{

		@Override
		protected CommentListJson doInBackground(Void... params) {
			CommentListJson json=null;
			try{
				json=HttpLoderMethods.getPhotoComments(0, COMMENT_LENGTH, mCurrentPhoto.getPhotoId());
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(CommentListJson result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result!=null&&result.getCode()==1000)
			{
				synchronized (mListData) {
					List<Comment> list=result.getData().getCommentList();
					mListData.clear();
					for(Comment comment:list)
					{
						mListData.add(comment);
					}
					adapter.notifyDataSetChanged();
				}
			}
		}
	}
	
	private class LoadMoreTask extends AsyncTask<Void, Void, CommentListJson>
	{

		@Override
		protected CommentListJson doInBackground(Void... params) {
			// TODO Auto-generated method stub
			CommentListJson json=null;
			try{
				json=HttpLoderMethods.getPhotoComments(mListData.size(), COMMENT_LENGTH, mCurrentPhoto.getPhotoId());
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(CommentListJson result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result!=null&&result.getCode()==1000)
			{
				synchronized (mListData) {
					List<Comment> list=result.getData().getCommentList();
					for(Comment comment:list)
					{
						mListData.add(comment);
					}
					adapter.notifyDataSetChanged();
				}
			}
			isLoadingMore=false;
		}
		
		
	}
	
	private class CommentOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			showPopupView(btComment);
		}
		
		
		
	}
	private void showPopupView(View parent)
	{
		if(mPopWindow==null)
		{
			View view=mInflater.inflate(R.layout.photo_comment_popup_edit_text, null);
			mPopWindow=new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		}
		// 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
		mPopWindow.setFocusable(true);
		// 设置允许在外点击消失
		mPopWindow.setOutsideTouchable(true);
		//如果需要PopupWindow响应返回键，那么必须给PopupWindow设置一个背景才行
		ColorDrawable dw = new ColorDrawable(0X00000000);
		mPopWindow.setBackgroundDrawable(dw);
		//软键盘不会挡着popupwindow
		mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		//设置菜单显示的位置
		mPopWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		
	}
	
	public void onLastItemVisible() {
		if(!isLoadingMore)
		{
			isLoadingMore=true;
//			pbFooterLoading.setVisibility(View.VISIBLE);
//			tvFooterText.setText(R.string.list_footer_loading);
			new LoadMoreTask().execute();
			Toast.makeText(PhotoCommentActivity.this, "End of List!", Toast.LENGTH_SHORT).show();
		}
	}
	
	private Photo getPhotoFromIntent()
	{
		Intent intent=this.getIntent();
		//return (Photo)intent.getSerializableExtra("photo");
		Bundle bundle=intent.getExtras();
		Photo photo=new Photo();
		photo.setAvatarPath(bundle.getString("avatarPath"));
		photo.setNickName(bundle.getString("nickName"));
		photo.setUploadTime((Date)bundle.getSerializable("uploadTime"));
		photo.setPhotoPath(bundle.getString("photoPath"));
		photo.setPhotoDescription(bundle.getString("photoDescription"));
		photo.setPhotoId(bundle.getInt("photoId"));
		return photo;
		
	}
	
	/**
	 * 计算当前时间和参数时间的时间差,返回XX秒钟前,XX分钟前,XX小时前,yyyy-MM-dd HH:mm
	 * @param date
	 * @return
	 */
	private String getDifferenceFromDate(Date date)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
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
