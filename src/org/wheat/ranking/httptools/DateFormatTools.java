package org.wheat.ranking.httptools;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateFormatTools {

	public static java.util.Date Str2UtilDate(String dateStr){
		java.util.Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date =sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	public static java.sql.Date utilDate2SqlDate(java.util.Date utilDate){
		return new java.sql.Date(utilDate.getTime());
	}
}
