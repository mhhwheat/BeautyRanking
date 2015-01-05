package org.wheat.ranking.checkUpdate;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.wheat.beautyranking.R;
import org.wheat.ranking.activity.FirstPage;
import org.wheat.ranking.activity.LoginActivity;
import org.wheat.ranking.data.UserLoginPreference;
import org.wheat.ranking.entity.ConstantValue;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
public class SettingPage extends Activity {
	private final String TAG = this.getClass().getName();
	private final int UPDATA_NONEED = 0;
	private final int UPDATA_CLIENT = 1;
	private final int GET_UNDATAINFO_ERROR = 2;
	private final int SDCARD_NOMOUNTED = 3;
	private final int DOWN_ERROR = 4;
	private final int QUIT=5;
	private LinearLayout getVersion;
	private LinearLayout sendFeedback;
	private LinearLayout share;
	private LinearLayout aboutus;
	private LinearLayout quitLogin;
	private UpdataInfo info;
	private String localVersion;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_setting_page);
		getVersion = (LinearLayout) findViewById(R.id.checkupdate);
		sendFeedback =(LinearLayout)findViewById(R.id.feedback);
		aboutus=(LinearLayout)findViewById(R.id.aboutus);
		share=(LinearLayout)findViewById(R.id.share);
		quitLogin = (LinearLayout)findViewById(R.id.quitlogin);
		aboutus.setOnClickListener(new AboutUsListener());
		share.setOnClickListener(new shareListener());
		sendFeedback .setOnClickListener(new sendMsgToServerListener());
		quitLogin.setOnClickListener(new QuitLoginListener());
		getVersion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					localVersion = getVersionName();
					CheckVersionTask cv = new CheckVersionTask();
					new Thread(cv).start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	private String getVersionName() throws Exception {
		//getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ  
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		return packInfo.versionName;
	}
	public class CheckVersionTask implements Runnable {
		InputStream is;
		public void run() {
			try {
				String path =ConstantValue.HttpRoot+"version.xml";
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET"); 
                int responseCode = conn.getResponseCode(); 
                if (responseCode == 200) { 
                    // �ӷ��������һ�������� 
                	is = conn.getInputStream(); 
                } 
				info = UpdataInfoParser.getUpdataInfo(is);
				if (info.getVersion().equals(localVersion)) {
					Log.i(TAG, "�汾����ͬ");
					Message msg = new Message();
					msg.what = UPDATA_NONEED;
					handler.sendMessage(msg);
					// LoginMain();
				} else {
					Log.i(TAG, "�汾�Ų���ͬ ");
					Message msg = new Message();
					msg.what = UPDATA_CLIENT;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				Message msg = new Message();
				msg.what = GET_UNDATAINFO_ERROR;
				handler.sendMessage(msg);
				e.printStackTrace();
			}
		}
	}
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATA_NONEED:
				Toast.makeText(getApplicationContext(), "����Ҫ����",
						Toast.LENGTH_SHORT).show();
			case UPDATA_CLIENT:
				 //�Ի���֪ͨ�û���������   
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				//��������ʱ   
	            Toast.makeText(getApplicationContext(), "��ȡ������������Ϣʧ��", 1).show(); 
				break;
			case DOWN_ERROR:
				//����apkʧ��  
	            Toast.makeText(getApplicationContext(), "�����°汾ʧ��", 1).show(); 
				break;
			case QUIT:
				showQuitDialog();
				break;
			}
		}
	};
	/* 
	 *  
	 * �����Ի���֪ͨ�û����³���  
	 *  
	 * �����Ի���Ĳ��裺 
	 *  1.����alertDialog��builder.   
	 *  2.Ҫ��builder��������, �Ի��������,��ʽ,��ť 
	 *  3.ͨ��builder ����һ���Ի��� 
	 *  4.�Ի���show()����   
	 */  
	protected void showUpdataDialog() {
		AlertDialog.Builder builer = new Builder(this);
		builer.setTitle("�汾����");
		builer.setMessage(info.getDescription());
		 //����ȷ����ťʱ�ӷ����������� �µ�apk Ȼ��װ   ?
		builer.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "����apk,����");
				downLoadApk();
			}
		});
		builer.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//do sth
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}
	/* 
	 * �ӷ�����������APK 
	 */  
	protected void downLoadApk() {  
	    final ProgressDialog pd;    //�������Ի���  
	    pd = new  ProgressDialog(this);  
	    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
	    pd.setMessage("�������ظ���");  
	    pd.show();  
	    new Thread(){  
	        @Override  
	        public void run() {  
	            try {  
	                File file = DownLoadManager.getFileFromServer(info.getUrl(), pd);  
	                sleep(3000);  
	                installApk(file);  
	                pd.dismiss(); //�������������Ի���  
	            } catch (Exception e) {  
	                Message msg = new Message();  
	                msg.what = DOWN_ERROR;  
	                handler.sendMessage(msg);  
	                e.printStackTrace();  
	            }  
	        }}.start();  
	}  
	  
	//��װapk   
	protected void installApk(File file) {  
	    Intent intent = new Intent();  
	    //ִ�ж���  
	    intent.setAction(Intent.ACTION_VIEW);  
	    //ִ�е���������  
	    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");  
	    startActivity(intent);  
	}  
	/**********************************************************************************************/
	//���ͷ�����������
	
	class sendMsgToServerListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent feedbackIntent =  new Intent();
			feedbackIntent.setClass(SettingPage.this, OpinionBack.class);
			startActivity(feedbackIntent);
		}
		
	}
	
	/*****************************************************************************************/
	//������ѣ�������  
    class shareListener implements   OnClickListener {      
        @Override  
        public void onClick(View v) {  
            Intent intent=new Intent(Intent.ACTION_SEND);    
            intent.setType("image/*");    
            intent.putExtra(Intent.EXTRA_SUBJECT, "Share");    
            intent.putExtra(Intent.EXTRA_TEXT, "I have successfully share my message through my app (������city���˹�)");    
             
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
            startActivity(Intent.createChooser(intent, getTitle()));    
              
        }  
    }  
	
    class AboutUsListener implements   OnClickListener {      
        @Override  
        public void onClick(View v) {  
        	Intent feedbackIntent =  new Intent();
			feedbackIntent.setClass(SettingPage.this, AboutUs.class);
			startActivity(feedbackIntent); 
              
        }  
    }  
	
    /*****************************************************************************************/
    class QuitLoginListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showQuitDialog();
			
		}
    	
    }
    protected void showQuitDialog() {
		AlertDialog.Builder builer = new Builder(this);
		builer.setTitle("beauty");
		builer.setMessage("�˳���¼��");
		 //����ȷ����ťʱ�ӷ����������� �µ�apk Ȼ��װ   ?
		builer.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				clearLocalInfo();
			}
		});
		builer.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//do sth
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}
    
    private void clearLocalInfo(){
    	UserLoginPreference preference = UserLoginPreference.getInstance(getApplicationContext());
    	preference.SetuserPhoneNumber("");
    	Intent intent = new Intent();
    	intent.setClass(this, FirstPage.class);
    	startActivity(intent);
    	finish();
    }
	
}