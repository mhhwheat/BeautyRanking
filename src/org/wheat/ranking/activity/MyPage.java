package org.wheat.ranking.activity;


import org.wheat.beautyranking.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyPage extends Activity{
	private ListView listview;
	
	private static final String[]data={"背景","上海","广州","武汉","天津","湖南","湖北","广西"};
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_page);
		setTheme(R.style.Transparent);
		listview = (ListView)findViewById(R.id.my_page_listview);
		listview.addHeaderView(new View(this));
		listview.addFooterView(new View(this));
		listview.setAdapter(new ArrayAdapter<String>(this,R.layout.my_page_item,R.id.text1,data));	
	}
}
