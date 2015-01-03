package org.wheat.ranking.activity;

import org.wheat.ranking.data.UserLoginPreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import org.wheat.ranking.startpage.WelcomePage;
public class FirstPage extends Activity{
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UserLoginPreference pre = UserLoginPreference.getInstance(getApplicationContext());
		if(pre.getuserPhoneNumber().equals("")){
			//启动第一次启动界面
			Intent welIntent = new Intent();
			welIntent.setClass(FirstPage.this, WelcomePage.class);
			startActivity(welIntent);
		}else {
			//启动主界面
			Intent mainActivity = new Intent();
			mainActivity.setClass(FirstPage.this, MainInterfaceActivity.class);
			startActivity(mainActivity);
		}
		finish();
	}
}
