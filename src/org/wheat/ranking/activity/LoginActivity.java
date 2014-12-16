package org.wheat.ranking.activity;

import java.lang.ref.WeakReference;

import org.apache.http.conn.ConnectTimeoutException;
import org.wheat.beautyranking.R;
import org.wheat.ranking.data.UserLoginPreference;
import org.wheat.ranking.entity.json.UserLoginJson;
import org.wheat.ranking.loader.LoginAndRegister;
import org.wheat.ranking.coders.Coder_Md5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity
{
	private EditText etUserId=null;
	private EditText etPassword=null;
	private Button btLogin=null;
	private Button btRegister=null;
	
    private LoginHandler loginHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		etUserId=(EditText)findViewById(R.id.user_id);
		etPassword=(EditText)findViewById(R.id.pwd);
		btLogin=(Button)findViewById(R.id.btLogin);
		btRegister=(Button)findViewById(R.id.btRegister);
		loginHandler=new LoginHandler(this);
		btLogin.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				new Thread()
				{
					@Override
					public void run() 
					{
						System.out.println("login button is on click");
						String userPhoneNumber=etUserId.getText().toString().trim();
						String pwd=etPassword.getText().toString().trim();
						String toastText;
						Message msg=Message.obtain();
						if(userPhoneNumber.equals("")||pwd.equals(""))
						{
							toastText="�˺ź����벻��Ϊ��";
							msg.obj=toastText;
							loginHandler.sendMessage(msg);
							return;
						}
						UserLoginJson userJson=null;
						try {
							userJson = LoginAndRegister.synLogin(userPhoneNumber,Coder_Md5.md5(pwd));
							if(userJson==null)
							{
								toastText="��������ʧ��";
								msg.obj=toastText;
								loginHandler.sendMessage(msg);
								return;
							}
							int jsonCode=userJson.getCode();
							if(jsonCode==-1)
							{
								toastText="���벻��ȷ";
								msg.obj=toastText;
								loginHandler.sendMessage(msg);
								return;
							}
							if(jsonCode==0)
							{
								toastText="���벻��ȷ";
								msg.obj=toastText;
								loginHandler.sendMessage(msg);
								return;
							}
							if(jsonCode==1)
							{
								UserLoginPreference preference=UserLoginPreference.getInstance(getApplicationContext());
								preference.SetuserPhoneNumber(userPhoneNumber);
								preference.SetPassword(pwd);
								Intent intent=new Intent(LoginActivity.this,MainInterfaceActivity.class);
								startActivity(intent);
							}
						} catch (ConnectTimeoutException e) {
							toastText="�������ӳ�ʱ";
							msg.obj=toastText;
							loginHandler.sendMessage(msg);
							return;
						}catch(Throwable th)
						{
							th.printStackTrace();
						}
						
					}
					
				}.start();
				
			}
		});
		btRegister.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
		});
	}
	static class  LoginHandler extends Handler
	{
		WeakReference<LoginActivity> loginActivity;
		public LoginHandler(LoginActivity activity) 
		{
			loginActivity=new WeakReference<LoginActivity>(activity);
		}
		@Override
		public void handleMessage(Message msg) 
		{
			String toastText=(String)msg.obj;
			Toast toast=Toast.makeText(loginActivity.get(), toastText, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}	
	}
	
}
