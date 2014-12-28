//package org.wheat.ranking.activity;
//
//import org.wheat.beautyranking.R;
//
//import android.app.Activity;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//
//public class UpdateSetting extends Activity {
//
//	Button btnSure;
//	LinearLayout changeAvatar;  String 
//	LinearLayout changeMyNickname;
//	EditText edtPersonSign;
//	public void onCreate(Bundle savedInstanceState){
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.my_setting_page);
//		init();
//	}
//	
//	private void init (){
//		btnSure = (Button )findViewById(R.id.mysure);
//		changeAvatar=(LinearLayout)findViewById(R.id.changeavatar);
//		changeMyNickname= (LinearLayout)findViewById(R.id.changenickname);
//		edtPersonSign =(EditText )findViewById(R.id.mypersonalsign);
//		btnSure.setOnClickListener(new BtnListener());
//	}
//	
//	private class BtnListener implements android.view.View.OnClickListener{
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			
//		}
//	}
//}
