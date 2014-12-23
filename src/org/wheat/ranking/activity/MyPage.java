package org.wheat.ranking.activity;


import org.wheat.beautyranking.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyPage extends Activity{
	private ListView listview;
	
	private static final String[]data={"����","�Ϻ�","����","�人","���","����","����","����"};
	
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
