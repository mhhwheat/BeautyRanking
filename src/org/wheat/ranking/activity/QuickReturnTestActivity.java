package org.wheat.ranking.activity;
import org.wheat.beautyranking.R;
import org.wheat.widget.QuickReturnRelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;



/** 
 * description:
 * @author wheat
 * date: 2014-12-24  
 * time: ÏÂÎç9:10:29
 */
public class QuickReturnTestActivity extends Activity
{
	private LayoutInflater mInflater;
	
	private PullToRefreshListView mListView;
	private View mQuickReturnView;
	private QuickReturnRelativeLayout mQuickReturnRelativeLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quick_return_view_test_layout);
		mInflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mQuickReturnRelativeLayout=(QuickReturnRelativeLayout)findViewById(R.id.quick_return_linearlayout);
		mListView=(PullToRefreshListView)findViewById(R.id.beauty_personal_page_refresh_list_view);
		mQuickReturnView=mInflater.inflate(R.layout.quick_return_view, null);
		mQuickReturnRelativeLayout.addQuickReturnView(mQuickReturnView);
		
	}

	
}
