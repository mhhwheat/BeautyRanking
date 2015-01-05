/** 
 * description��
 * @author wheat
 * date: 2015-1-4  
 * time: ����4:51:27
 */ 
package org.wheat.ranking.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/** 
 * description:
 * @author wheat
 * date: 2015-1-4  
 * time: ����4:51:27
 */
public class SqliteDBHelper extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "beauty_ranking.db";  
    private static final int DATABASE_VERSION = 1; 
    
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public SqliteDBHelper(Context context) {
		super(context, DATABASE_NAME, null,DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS follow_photo "+
	"(_id INTEGER PRIMARY KEY AUTOINCREMENT, avatarPath VARCHAR, nickname VARCHAR, isPraise BOOLEAN, photoDescription VARCHAR, beautyId INTEGER, "+
				" photoId INTEGER, commentCount INTEGER, praiseCount INTEGER, photoPath VARCHAR, userPhoneNumber VARCHAR, uploadTime VARCHAR)");
		
		db.execSQL("");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}