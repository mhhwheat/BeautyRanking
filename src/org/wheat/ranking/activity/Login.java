package org.wheat.ranking.activity;

import org.wheat.beautyranking.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class Login extends Activity{

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.login);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.login_title);
	}
}
