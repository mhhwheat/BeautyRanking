package org.wheat.ranking.activity;

import java.lang.ref.WeakReference;

import org.wheat.beautyranking.R;
import org.wheat.ranking.coders.Coder_Md5;
import org.wheat.ranking.entity.json.UserRegisterJson;
import org.wheat.ranking.loader.HttpDataLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity
{
	
	private EditText etUserPhoneNumber;
	private EditText etPassword;
	private EditText etNikeName;
	private EditText etSex;
	private EditText etSchool;
	private EditText etAdmissionYear;
	private Button btRegister;
	
	private String mUserPhoneNumber;
	private String mPassword;
	private String mNikeName;
	private String mSex;
	private String mSchool;
	private String mAdmissionYear;
	
    private RegisterHandler registerHandler;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		
		etUserPhoneNumber=(EditText)findViewById(R.id.evRegister_phone_number);
		etPassword=(EditText)findViewById(R.id.evRegister_password);
		etNikeName=(EditText)findViewById(R.id.evRegister_nikeName);
		etSex=(EditText)findViewById(R.id.evRegister_sex);
		etSchool=(EditText)findViewById(R.id.evRegister_school);
		etAdmissionYear=(EditText)findViewById(R.id.evRegister_admissionYear);
		btRegister=(Button)findViewById(R.id.btRegister);
		
		registerHandler=new RegisterHandler(this);
		
		
		btRegister.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				if(!checkUserInfo())
				{
					Toast toast=Toast.makeText(getApplicationContext(), "用户信息不完整", Toast.LENGTH_LONG);
					toast.show();
				}
				else
				{
					new Thread()
					{
						@Override
						public void run() 
						{
							UserRegisterJson json=null;
							try {
								json=HttpDataLoader.synRegister(mUserPhoneNumber, Coder_Md5.md5(mPassword), mNikeName, mSex, mSchool, Integer.parseInt(mAdmissionYear));
							} catch (NumberFormatException e) {
								e.printStackTrace();
							} catch (Throwable e) {
								e.printStackTrace();
							}
							Message msg=Message.obtain();
							if(json==null)
							{
								msg.what=-1;
								return;
							}
							msg.what=json.getCode();
							registerHandler.sendMessage(msg);
							if(msg.what==1)
							{
								Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
								startActivity(intent);
							}
						}
						
					}.start();
				}
			}
		});
	}
	static class RegisterHandler extends Handler
	{
		WeakReference<RegisterActivity> registerActivity;
		public RegisterHandler(RegisterActivity activity)
		{
			registerActivity=new WeakReference<RegisterActivity>(activity);
		}
		@Override
		public void handleMessage(Message msg) 
		{
			String toastText="";
			switch (msg.what) {
			case 0:
				toastText="用户注册失败";
				break;
			case 1:
				toastText="用户注册成功";
				break;
			case -1:
				toastText="用户注册请求失败";
				break;
			}
			Toast toast=Toast.makeText(registerActivity.get(), toastText, Toast.LENGTH_LONG);
			toast.show();
		}
		
	}
	private boolean checkUserInfo()
	{
		mUserPhoneNumber=etUserPhoneNumber.getText().toString().trim();
		if(mUserPhoneNumber.equals(""))
			return false;
		
		mPassword=etPassword.getText().toString().trim();
		if(mPassword.equals(""))
			return false;
		
		mNikeName=etNikeName.getText().toString().trim();
		if(mNikeName.equals(""))
			return false;
		
		mSex=etSex.getText().toString().trim();
		if(mSex.equals(""))
			mSex="男";
		
		mSchool=etSchool.getText().toString().trim();
		if(mSchool.equals(""))
			return false;
		
		mAdmissionYear=etAdmissionYear.getText().toString().trim();
		if(mAdmissionYear.equals(""))
			return false;
		return true;
	}
}
