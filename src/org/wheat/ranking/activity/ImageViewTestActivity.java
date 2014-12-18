package org.wheat.ranking.activity;

import org.wheat.beautyranking.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

/** 
 * description:
 * @author wheat
 * date: 2014-12-17  
 * time: ÏÂÎç9:13:42
 */
public class ImageViewTestActivity extends Activity
{
	private ImageView imageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_test_layout);
		imageView=(ImageView)findViewById(R.id.image_test_photo);
		imageView.setImageResource(R.drawable.liu);
	}
	private int getDeviceScreenWidth()
	{
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
}
