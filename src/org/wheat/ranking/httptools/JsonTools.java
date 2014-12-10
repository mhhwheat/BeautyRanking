package org.wheat.ranking.httptools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonTools {

	/*
	 * 把json格式的字符串转为实体对象
	 */
	public static <T> T fromJson(String json,Class<T> classOfT)
	{
		Gson gson=new GsonBuilder().create();
		return gson.fromJson(json, classOfT);
	}
	
	/*
	 * 把对象转为json格式的字符串
	 */
	public static String toJson(Object object)
	{
		Gson gson=new GsonBuilder().create();
		return gson.toJson(object);
	}
}
