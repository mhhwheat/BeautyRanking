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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class TabNewFragment extends Fragment implements OnScrollListener
{
	private final int PAGE_LENGTH=10;//ÿ����������ҳ������������������
	private PullToRefreshListView mPullToRefreshListView;
	private List<BeautyIntroduction> mListData;//����listview�����������
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;//����ͼƬ�Ķ���
	private NewRefreshListAdapter adapter;
	
	private boolean isLoadingMore=false;//��ֹ�ظ������첽�����߳�
	private View mFooterView;
	private TextView tvFooterText;
	private ProgressBar pbFooterLoading;
	private ListView mActualListView;//PulltoRefreshListView��������ListView
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mListData=new ArrayList<BeautyIntroduction>();
		mImageLoader=ImageLoader.getInstance(getActivity().getApplicationContext());
		adapter=new NewRefreshListAdapter();
		new UpdateDataTask().execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
mInflater=inflater;
		
		View view=inflater.inflate(R.layout.fragment_new, container, false);
		mPullToRefreshListView=(PullToRefreshListView)view.findViewById(R.id.new_refresh_list_view);
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


	public class NewRefreshListAdapter extends BaseAdapter
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
			Log.w("TabNewFragment","------------------------>getView");
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
					Toast.makeText(getActivity(), "End of List!", Toast.LENGTH_SHORT).show();
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
	private class UpdateDataTask extends AsyncTask<Void, Void, BeautyIntroductionListJson>
	{
		@Override
		protected BeautyIntroductionListJson doInBackground(Void... params) {
			BeautyIntroductionListJson json=null;
			try {
				json=HttpLoderMethods.getNewPage(0, PAGE_LENGTH);
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
		protected void onPostExecute(BeautyIntroductionListJson result) {
			if(result!=null&&result.getCode()==1000)
			{
				synchronized (mListData) {
					mListData.clear();
					mListData=result.getData().getIntroductionList();
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
	 * description�����ظ������ݵ��첽�߳�
	 * @author wheat
	 * date: 2014-12-15  
	 * time: ����5:10:57
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
				json=HttpLoderMethods.getNewPage(firstIndex, count);
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
		protected void onPostExecute(BeautyIntroductionListJson result) {
			if(result!=null&&result.getCode()==1000)
			{
				synchronized (mListData) {
					mListData.addAll(result.getData().getIntroductionList());
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
	
}