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
			//������һ����������
			Intent welIntent = new Intent();
			welIntent.setClass(FirstPage.this, WelcomePage.class);
			startActivity(welIntent);
		}else {
			//����������
			Intent mainActivity = new Intent();
			mainActivity.setClass(FirstPage.this, MainInterfaceActivity.class);
			startActivity(mainActivity);
		}
		finish();
	}
}
