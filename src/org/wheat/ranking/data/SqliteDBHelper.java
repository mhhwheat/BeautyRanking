/** 
 * description£º
 * @author wheat
 * date: 2015-1-4  
 * time: ÏÂÎç4:51:27
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
 * time: ÏÂÎç4:51:27
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
