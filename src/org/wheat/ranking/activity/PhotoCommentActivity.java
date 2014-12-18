package org.wheat.ranking.activity;

import org.wheat.beautyranking.R;
import org.wheat.ranking.entity.Photo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class PhotoCommentActivity extends Activity
{
	private ListView mListView;
	private Photo mCurrentPhoto;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_comment_layout);
		mListView=(ListView)findViewById(R.id.photo_comment_list_view);
	}
	
	private Photo getPhotoFromIntent()
	{
		Intent intent=this.getIntent();
		return (Photo)intent.getSerializableExtra("photo");
//		Photo photo=new Photo();
//		photo.setAvatarPath(intent.getStringExtra("avatarPath"));
//		photo.setNickName(intent.getStringExtra("nickname"));
//		photo.setPhotoDescription(intent.getStringExtra("photoDescription"));
		
	}
}
