package org.wheat.ranking.activity;

import org.wheat.beautyranking.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

public class ConfirmUserPhone extends Activity{

	Button btn;
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.comfirm_phone_number);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.comfirm_phone_number_title);
		
		btn=(Button )findViewById(R.id.confirm_phone_number_button);
	}
}
