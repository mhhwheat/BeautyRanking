/** 
 * description£º
 * @author wheat
 * date: 2015-2-3  
 * time: ÏÂÎç9:09:31
 */ 
package org.wheat.ranking.activity;

import org.wheat.beautyranking.R;
import org.wheat.ranking.httptools.HttpConnectTools;

import android.app.Activity;
import android.os.Bundle;

/** 
 * description:
 * @author wheat
 * date: 2015-2-3  
 * time: ÏÂÎç9:09:31
 */
public class TestActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutus);
		new TestJson().start();
	}
	
	private class TestJson extends Thread
	{

		@Override
		public void run() {
			super.run();
			String json=null;
			
			try
			{
				json=HttpConnectTools.get("http://120.24.161.139/project/mysite/polls/apis/testjson/", null);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			if(json!=null)
			{
				System.out.println("json="+json);
			}
		}
		
	}
	
}
