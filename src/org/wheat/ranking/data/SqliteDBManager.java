/** 
 * description：
 * @author wheat
 * date: 2015-1-7  
 * time: 下午3:36:37
 */ 
package org.wheat.ranking.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.wheat.ranking.entity.Photo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/** 
 * description:
 * @author wheat
 * date: 2015-1-7  
 * time: 下午3:36:37
 */
public class SqliteDBManager 
{
	private SqliteDBHelper dbHelper;
	private SQLiteDatabase db;
	
	public SqliteDBManager(Context context)
	{
		dbHelper=new SqliteDBHelper(context);
		db=dbHelper.getWritableDatabase();
	}
	
	/**
	 * 把FollowFragment的内容存储到表follow_page中
	 * @param list
	 */
	public void addToFollowPage(List<Photo> list)
	{
		String sql="INSERT INTO follow_page VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		db.beginTransaction();
		try
		{
			for(Photo photo:list)
			{
				db.execSQL(sql, new Object[]{photo.getAvatarPath(),photo.getNickName(),photo.getIsPraise(),photo.getPhotoDescription(),photo.getBeautyId(),
						photo.getPhotoId(),photo.getCommentCount(),photo.getPraiseCount(),photo.getPhotoPath(),photo.getUserPhoneNumber(),photo.getUpLoadTimeToString()});
			}
			db.setTransactionSuccessful();
		}
		finally
		{
			db.endTransaction();
		}
	}
	
	/**
	 * 获得sqlite中follow_page表的内容
	 * @return 返回一个Photo对象的list
	 * @throws ParseException 
	 */
	public List<Photo> getFromFollowPage()
	{
		ArrayList<Photo> list=new ArrayList<Photo>();
		Cursor cursor=db.rawQuery("select * from follow_page", null);
		while(cursor.moveToNext())
		{
			Photo photo=new Photo();
			photo.setAvatarPath(cursor.getString(cursor.getColumnIndex("avatarPath")));
			photo.setNickName(cursor.getString(cursor.getColumnIndex("nickname")));
			photo.setIspraise(cursor.getInt(cursor.getColumnIndex("isPraise")));
			photo.setPhotoDescription(cursor.getString(cursor.getColumnIndex("photoDescription")));
			photo.setBeautyId(cursor.getInt(cursor.getColumnIndex("beautyId")));
			photo.setPhotoId(cursor.getInt(cursor.getColumnIndex("photoId")));
			photo.setCommentCount(cursor.getInt(cursor.getColumnIndex("commentCount")));
			photo.setPraiseCount(cursor.getInt(cursor.getColumnIndex("praiseCount")));
			photo.setPhotoPath(cursor.getString(cursor.getColumnIndex("photoPath")));
			photo.setUserPhoneNumber(cursor.getString(cursor.getColumnIndex("userPhoneNumber")));
			try {
				photo.setUploadTime(cursor.getString(cursor.getColumnIndex("uploadTime")));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			list.add(photo);
		}
		return list;
	}
	
	/**
	 * 清空sqlite中follow_page表的所用内容，并且重置自增id的初始值
	 */
	public void clearFollowPage()
	{
		db.execSQL("delete from follow_page");
		db.execSQL("update sqlite_sequence SET seq = 0 where name ='follow_page'");
	}
	
	
	
}
