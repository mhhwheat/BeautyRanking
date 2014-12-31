package org.wheat.ranking.checkUpdate;

import org.wheat.beautyranking.R;
import org.wheat.ranking.data.UserLoginPreference;
import org.wheat.ranking.entity.ConstantValue;
import org.wheat.ranking.loader.HttpUploadMethods;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OpinionBack extends Activity{
	EditText etOpinion; String feedbackMsg;
	Button btnSend;
	Handler handler ;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opinion_back);
		etOpinion=(EditText)findViewById(R.id.etopinionfeedback);
		btnSend= (Button)findViewById(R.id.tvopinionfeedback);
		
		btnSend.setOnClickListener(new sendListener());
		handler= new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 1000:
					Toast.makeText(getApplicationContext(), "感谢您的反馈",
							Toast.LENGTH_SHORT).show();
					etOpinion.setText("");
					break;
				case -1:
					Toast.makeText(getApplicationContext(), "信息未发出", 1000).show();
					break;
				}
			}
		};
		
	}
	class sendListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			
			new Thread(){
				public void run(){
					// TODO Auto-generated method stub
					feedbackMsg=etOpinion.getText().toString().trim();
					UserLoginPreference preference=UserLoginPreference.getInstance(getApplicationContext());
					String userPhoneNumber  = preference.getuserPhoneNumber();
					int code = HttpUploadMethods.sendFeedbackMsg(feedbackMsg,userPhoneNumber);
					if(code == ConstantValue.operateSuccess){
						Message msg=new Message();
						msg.what=code;
						handler.sendMessage(msg);
					}else{
						Message msg=new Message();
						msg.what=-1;
						handler.sendMessage(msg);
					}
					
				}
			}.start();
		}		
	}
}
