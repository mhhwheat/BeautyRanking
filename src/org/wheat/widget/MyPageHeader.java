package org.wheat.widget;

import org.wheat.beautyranking.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class MyPageHeader extends Activity{

	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mypage_header);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mycustomtitle);
	}
}
