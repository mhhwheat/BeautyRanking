package org.wheat.ranking.activity;

import org.wheat.beautyranking.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class testSetting extends Activity{

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);//请求设置标题栏
		setContentView(R.layout.my_setting_page);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mysettingtitle);
	}
}
