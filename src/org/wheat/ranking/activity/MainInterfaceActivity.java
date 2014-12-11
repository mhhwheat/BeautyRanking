package org.wheat.ranking.activity;

import org.wheat.beautyranking.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainInterfaceActivity extends FragmentActivity
{
	private FragmentManager mFragmentManager;
	private RadioGroup mRadioGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_user_interface);
		
		mFragmentManager=getSupportFragmentManager();
		mRadioGroup=(RadioGroup)findViewById(R.id.main_radio);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				FragmentTransaction transaction = mFragmentManager.beginTransaction();  
	            Fragment fragment = FragmentFactory.getInstanceByIndex(checkedId);  
	            transaction.replace(R.id.replacing_fragment, fragment);  
	            transaction.commit(); 
			}
		});
	}
	
	
	
}
