package org.wheat.ranking.activity;

import org.wheat.beautyranking.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class Register extends Activity{

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.register);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.register_title);
	}
}
