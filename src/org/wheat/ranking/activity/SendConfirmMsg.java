package org.wheat.ranking.activity;

import org.wheat.beautyranking.R;
import org.wheat.ranking.loader.LoginAndRegister;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SendConfirmMsg extends Activity{

	String userPhoneNumber;
	String sex;
	Button btnSendMsg;
	Button btnSure;
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comfirm_phone_number);
		btnSendMsg=(Button)findViewById(R.id.confirm_phone_number_button);
		btnSure=(Button)findViewById(R.id.registergo);
		
		btnSendMsg.setOnClickListener(new SendMsgListener());
		btnSure.setOnClickListener(new SureListener());
	}
	
	class SendMsgListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			int code = LoginAndRegister.sendMessage();
		}	
	}
	
	class SureListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			int  code = LoginAndRegister.confirmAndRegister();
//			if(code ==1)
		}
	}
}
