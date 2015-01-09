package org.wheat.ranking.activity;


import org.wheat.beautyranking.R;
import org.wheat.beautyranking.R.layout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ModifyNickName extends Activity {

	EditText tvTrueName; String truename;
	EditText tvBirthday; String birthday;
	EditText tvSchool;   String school;
	EditText etDescription;  String description;
	Button btnSure ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_my_info);
		init();
		btnSure.setOnClickListener(new commitInfolistener());
		
	}
	class commitInfolistener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(!checkInfo()){
				return;
			}else{
				Intent returnIntent = new Intent();
				returnIntent.putExtra("nickname", truename);
				returnIntent.putExtra("birthday", birthday);
				returnIntent.putExtra("school",school);
				returnIntent.putExtra("personSign", description);
				setResult(1, returnIntent);
				finish();
				
			}
		}
	};
	private void init(){
		tvTrueName= (EditText)findViewById(R.id.persontruename);
		tvBirthday =(EditText)findViewById(R.id.birthday);
		tvSchool=(EditText)findViewById(R.id.persionschool);
		etDescription = (EditText)findViewById(R.id.persondescription);
		btnSure=(Button )findViewById(R.id.sure);
	}
	private boolean checkInfo(){
		truename = tvTrueName.getText().toString().trim();
		birthday= tvBirthday.getText().toString().trim();
		school= tvSchool.getText().toString().trim();
		description = etDescription.getText().toString().trim();
//		if(description.equals("")){
//			Toast.makeText(this, "���ٸ�����ܰ�", Toast.LENGTH_LONG).show();
//			return false;
//		}
		return true;
	}
}
