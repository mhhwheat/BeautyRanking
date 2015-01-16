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

import org.wheat.ranking.entity.BeautyIntroduction;
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
	
	/**
	 * 把NeighborGridFragment里面的内容存到sqlite数据库里
	 * @param list
	 */
	public void addToNeighborPage(List<BeautyIntroduction> list)
	{
		String sql="INSERT INTO neighbor_page VALUES(null, ?, ?, ?, ?, ?, ?, ?)";
		db.beginTransaction();
		try{
			for(BeautyIntroduction beauty:list)
			{
				db.execSQL(sql, new Object[]{beauty.getBeautyId(),beauty.getBeautyName(),beauty.getSchool(),beauty.getAvatarPath(),beauty.getDescription(),beauty.getPraiseTimes(),beauty.getCommentTimes()});
				
			}
			db.setTransactionSuccessful();
		}finally
		{
			db.endTransaction();
		}
	}
	
	/**
	 * 从sqlite数据库中获取NeighborGridFragment的缓存
	 * @return
	 */
	public List<BeautyIntroduction> getFromNeighborPage()
	{
		ArrayList<BeautyIntroduction> list=new ArrayList<BeautyIntroduction>();
		Cursor cursor=db.rawQuery("select * from neighbor_page", null);
		while(cursor.moveToNext())
		{
			BeautyIntroduction beauty=new BeautyIntroduction();
			beauty.setBeautyId(cursor.getInt(cursor.getColumnIndex("beautyId")));
			beauty.setBeautyName(cursor.getString(cursor.getColumnIndex("beautyName")));
			beauty.setSchool(cursor.getString(cursor.getColumnIndex("school")));
			beauty.setAvatarPath(cursor.getString(cursor.getColumnIndex("avatarPath")));
			beauty.setDescription(cursor.getString(cursor.getColumnIndex("photoDescription")));
			beauty.setPraiseTimes(cursor.getInt(cursor.getColumnIndex("praiseTimes")));
			beauty.setCommentTimes(cursor.getInt(cursor.getColumnIndex("commentTimes")));
			
			list.add(beauty);
		}
		return list;
	}
	
	/**
	 * 清空NeighborGridFragment在sqlite数据库中的缓存
	 */
	public void clearNeighborPage()
	{
		db.execSQL("delete from neighbor_page");
		db.execSQL("update sqlite_sequence SET seq = 0 where name ='neighbor_page'");
	}
	
	
	/**
	 * 把BeautyPersonalPageActivity里面的内容存到sqlite数据库里
	 * @param list
	 */
	public void addToBeautyPersonalPage(List<Photo> list)
	{
		String sql="INSERT INTO beauty_personal_page VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
	 * 从sqlite数据库中获取BeautyPersonalPageActivity的缓存
	 * @return
	 */
	public List<Photo> getFromBeautyPersonalPage()
	{
		ArrayList<Photo> list=new ArrayList<Photo>();
		Cursor cursor=db.rawQuery("select * from beauty_personal_page", null);
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
	 * 清空BeautyPersonalPageActivity在sqlite数据库中的缓存
	 */
	public void clearBeautyPersonalPage()
	{
		db.execSQL("delete from beauty_personal_page");
		db.execSQL("update sqlite_sequence SET seq = 0 where name ='beauty_personal_page'");
	}
	
	/**
	 * 把TabSumFragment里面的内容存到sqlite数据库里
	 * @param list
	 */
	public void addToSumPage(List<BeautyIntroduction> list)
	{
		String sql="INSERT INTO sum_page VALUES(null, ?, ?, ?, ?, ?, ?, ?)";
		db.beginTransaction();
		try{
			for(BeautyIntroduction beauty:list)
			{
				db.execSQL(sql, new Object[]{beauty.getBeautyId(),beauty.getBeautyName(),beauty.getSchool(),beauty.getAvatarPath(),beauty.getDescription(),beauty.getPraiseTimes(),beauty.getCommentTimes()});
				
			}
			db.setTransactionSuccessful();
		}finally
		{
			db.endTransaction();
		}
	}
	
	/**
	 * 从sqlite数据库中获取TabSumFragment的缓存
	 * @return
	 */
	public List<BeautyIntroduction> getFromSumPage()
	{
		ArrayList<BeautyIntroduction> list=new ArrayList<BeautyIntroduction>();
		Cursor cursor=db.rawQuery("select * from sum_page", null);
		while(cursor.moveToNext())
		{
			BeautyIntroduction beauty=new BeautyIntroduction();
			beauty.setBeautyId(cursor.getInt(cursor.getColumnIndex("beautyId")));
			beauty.setBeautyName(cursor.getString(cursor.getColumnIndex("beautyName")));
			beauty.setSchool(cursor.getString(cursor.getColumnIndex("school")));
			beauty.setAvatarPath(cursor.getString(cursor.getColumnIndex("avatarPath")));
			beauty.setDescription(cursor.getString(cursor.getColumnIndex("photoDescription")));
			beauty.setPraiseTimes(cursor.getInt(cursor.getColumnIndex("praiseTimes")));
			beauty.setCommentTimes(cursor.getInt(cursor.getColumnIndex("commentTimes")));
			
			list.add(beauty);
		}
		return list;
	}
	
	/**
	 * 清空TabSumFragment在sqlite数据库中的缓存
	 */
	public void clearSumPage()
	{
		db.execSQL("delete from sum_page");
		db.execSQL("update sqlite_sequence SET seq = 0 where name ='sum_page'");
	}
	
	
	/**
	 * 把TabNewFragment里面的内容存到sqlite数据库里
	 * @param list
	 */
	public void addToNewPage(List<BeautyIntroduction> list)
	{
		String sql="INSERT INTO new_page(beautyId,beautyName,school,avatarPath,photoDescription,praiseTimes,commentTimes) VALUES(?, ?, ?, ?, ?, ?, ?)";
		db.beginTransaction();
		try{
			for(BeautyIntroduction beauty:list)
			{
				db.execSQL(sql, new Object[]{beauty.getBeautyId(),beauty.getBeautyName(),beauty.getSchool(),beauty.getAvatarPath(),beauty.getDescription(),beauty.getPraiseTimes(),beauty.getCommentTimes()});
				
			}
			db.setTransactionSuccessful();
		}finally
		{
			db.endTransaction();
		}
	}
	
	/**
	 * 从sqlite数据库中获取TabNewFragment的缓存
	 * @return
	 */
	public List<BeautyIntroduction> getFromNewPage()
	{
		ArrayList<BeautyIntroduction> list=new ArrayList<BeautyIntroduction>();
		Cursor cursor=db.rawQuery("select * from new_page", null);
		while(cursor.moveToNext())
		{
			BeautyIntroduction beauty=new BeautyIntroduction();
			beauty.setBeautyId(cursor.getInt(cursor.getColumnIndex("beautyId")));
			beauty.setBeautyName(cursor.getString(cursor.getColumnIndex("beautyName")));
			beauty.setSchool(cursor.getString(cursor.getColumnIndex("school")));
			beauty.setAvatarPath(cursor.getString(cursor.getColumnIndex("avatarPath")));
			beauty.setDescription(cursor.getString(cursor.getColumnIndex("photoDescription")));
			beauty.setPraiseTimes(cursor.getInt(cursor.getColumnIndex("praiseTimes")));
			beauty.setCommentTimes(cursor.getInt(cursor.getColumnIndex("commentTimes")));
			
			list.add(beauty);
		}
		return list;
	}
	
	/**
	 * 清空TabNewFragment在sqlite数据库中的缓存
	 */
	public void clearNewPage()
	{
		db.execSQL("delete from new_page");
		db.execSQL("update sqlite_sequence SET seq = 0 where name ='new_page'");
	}
	
	/**
	 * 把TabRiseFragment里面的内容存到sqlite数据库里
	 * @param list
	 */
	public void addToRisePage(List<BeautyIntroduction> list)
	{
		String sql="INSERT INTO rise_page VALUES(null, ?, ?, ?, ?, ?, ?, ?)";
		db.beginTransaction();
		try{
			for(BeautyIntroduction beauty:list)
			{
				db.execSQL(sql, new Object[]{beauty.getBeautyId(),beauty.getBeautyName(),beauty.getSchool(),beauty.getAvatarPath(),beauty.getDescription(),beauty.getPraiseTimes(),beauty.getCommentTimes()});
				
			}
			db.setTransactionSuccessful();
		}finally
		{
			db.endTransaction();
		}
	}
	
	/**
	 * 从sqlite数据库中获取TabRiseFragment的缓存
	 * @return
	 */
	public List<BeautyIntroduction> getFromRisePage()
	{
		ArrayList<BeautyIntroduction> list=new ArrayList<BeautyIntroduction>();
		Cursor cursor=db.rawQuery("select * from rise_page", null);
		while(cursor.moveToNext())
		{
			BeautyIntroduction beauty=new BeautyIntroduction();
			beauty.setBeautyId(cursor.getInt(cursor.getColumnIndex("beautyId")));
			beauty.setBeautyName(cursor.getString(cursor.getColumnIndex("beautyName")));
			beauty.setSchool(cursor.getString(cursor.getColumnIndex("school")));
			beauty.setAvatarPath(cursor.getString(cursor.getColumnIndex("avatarPath")));
			beauty.setDescription(cursor.getString(cursor.getColumnIndex("photoDescription")));
			beauty.setPraiseTimes(cursor.getInt(cursor.getColumnIndex("praiseTimes")));
			beauty.setCommentTimes(cursor.getInt(cursor.getColumnIndex("commentTimes")));
			
			list.add(beauty);
		}
		return list;
	}
	
	/**
	 * 清空MyDetailPage在sqlite数据库中的缓存
	 */
	public void clearRisePage()
	{
		db.execSQL("delete from rise_page");
		db.execSQL("update sqlite_sequence SET seq = 0 where name ='rise_page'");
	}
	
	/**
	 * 把FollowFragment的内容存储到表my_detail_page中
	 * @param list
	 */
	public void addToMyDetailPage(List<Photo> list)
	{
		String sql="INSERT INTO my_detail_page VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
	 * 获得sqlite中my_detail_page表的内容
	 * @return 返回一个Photo对象的list
	 * @throws ParseException 
	 */
	public List<Photo> getMyDetailPage()
	{
		ArrayList<Photo> list=new ArrayList<Photo>();
		Cursor cursor=db.rawQuery("select * from my_detail_page", null);
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
	 * 清空sqlite中my_detail_page表的所用内容，并且重置自增id的初始值
	 */
	public void clearMyDetailPage()
	{
		db.execSQL("delete from my_detail_page");
		db.execSQL("update sqlite_sequence SET seq = 0 where name ='my_detail_page'");
	}
	
}
