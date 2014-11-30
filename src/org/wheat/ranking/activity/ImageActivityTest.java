package org.wheat.ranking.activity;

import org.wheat.beautyranking.R;
import org.wheat.ranking.loader.HttpDataLoader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ImageActivityTest extends Activity
{
	private ImageView image;
	private Button bt;
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atext);
		image=(ImageView)findViewById(R.id.testImage);
		bt=(Button)findViewById(R.id.showImage);
		handler=new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				if(msg.obj!=null)
					image.setImageBitmap((Bitmap)msg.obj);
				else
					System.out.println("bmp is null----->");
			}
			
		};
		bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				new Thread()
				{

					@Override
					public void run() 
					{
						Bitmap bmp=null;
						try {
							bmp=HttpDataLoader.downLoadBitmap( "C:/Users/Administrator/Desktop/httpservletrequest.png",-1,-1);
						} catch (Throwable e) {
							e.printStackTrace();
							System.out.println("exception------->");
						}
						Message msg=Message.obtain();
						msg.obj=bmp;
						handler.sendMessage(msg);
					}
					
				}.start();
			}
		});
	}
	
}
