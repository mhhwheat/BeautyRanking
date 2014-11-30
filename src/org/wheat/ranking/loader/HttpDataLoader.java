package org.wheat.ranking.loader;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import org.wheat.ranking.coders.Aes;
import org.wheat.ranking.entity.json.BeautyIntroductionListJson;
import org.wheat.ranking.entity.json.UserLoginJson;
import org.wheat.ranking.entity.json.UserRegisterJson;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpDataLoader 
{
	/*
	 * 密钥
	 */
	private static final byte[] PSW = "AjGfpbbQmU7EAnkJ".getBytes();
	
	private static final String HttpRoot="http://192.168.202.197:8080/BeautyRankingServer";
	
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
	
	private static String encryptParamsToGet(HashMap<String, String> params) throws Throwable {
        byte[] buff = encryptParamsToPost(params);
        if (buff == null) {
            return null;
        }
        return Base64.encodeToString(buff, Base64.DEFAULT);
    }
	
	/**
     * 加密参数，并将向量置于密文前头。
     *
     * @param params
     * @return
     * @throws Throwable
     */
	private static byte[] encryptParamsToPost(HashMap<String, String> params) throws Throwable
	{
		if(params==null)
		{
			return null;
		}
		StringBuilder sb=new StringBuilder();
		Iterator<Entry<String,String>> iter=params.entrySet().iterator();
		while(iter.hasNext())
		{
			Entry<String,String> entry=iter.next();
			String key=entry.getKey();
			String value=entry.getValue();
			if(sb.length()>0)
			{
				sb.append('&');
			}
			sb.append(key).append('=').append(Uri.encode(value));
		}
		
		byte[] buff=sb.toString().getBytes("UTF-8");
		byte[] iv=new byte[16];
		Random rd=new Random();
		//填充16位的字节数组
		rd.nextBytes(iv);
		byte[] data=Aes.encrypt(buff, PSW, iv);
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		baos.write(iv,0,iv.length);
		baos.write(data,0,data.length);
		
		byte[] out=baos.toByteArray();
		try{
			baos.close();
		}catch(Throwable e){
			
		}
		return out;
	}
	
	/**
	 * 用户登录
	 * @param context
	 * @param userPhoneNumber
	 * @param password
	 * @return
	 * @throws Throwable
	 */
	public synchronized static UserLoginJson synLogin(String userPhoneNumber,String password) throws Throwable
	{
		HashMap<String, String> hm=new HashMap<String, String>();
		hm.put("userPhoneNumber", userPhoneNumber);
		hm.put("password", password);
		byte[] data=encryptParamsToPost(hm);
		String json=HttpLoader.post( HttpRoot+"/servlet/BeautyRankingLogin", data,null);
		if(json==null)
		{
			return null;
		}
		return fromJson(json,UserLoginJson.class);
	}
	
	/**
	 * 注册新的用户
	 * @param context
	 * @param userPhoneNumber
	 * @param password
	 * @param nikeName
	 * @param sex
	 * @param school
	 * @param admissionYear
	 * @return
	 * @throws Throwable
	 */
	public synchronized static UserRegisterJson synRegister(String userPhoneNumber,String password,String nikeName,String sex,String school,int admissionYear) throws Throwable
	{
		HashMap<String,String> hm=new HashMap<String, String>();
		hm.put("userPhoneNumber", userPhoneNumber);
		hm.put("password", password);
		hm.put("nikeName", nikeName);
		hm.put("sex", sex);
		hm.put("school", school);
		hm.put("admissionYear", Integer.valueOf(admissionYear).toString());
		byte[] data=encryptParamsToPost(hm);
		String json=HttpLoader.post( HttpRoot+"/servlet/RegisterUserServlet", data, null);
		if(json==null)
		{
			return null;
		}
		return fromJson(json, UserRegisterJson.class);
	}
	
	/**
	 * 
	 * @param context
	 * @param bitmapPath 图片在服务端的路径
	 * @return
	 * @throws Throwable
	 */
	public static Bitmap downLoadBitmap(String bitmapPath,int minSideLength,int maxNumOfPixels) throws Throwable
	{
		/*
		HashMap<String,String> hm=new HashMap<String,String>();
		hm.put("path", bitmapPath);
		byte[] data=encryptParamsToPost(hm);
		*/
		//Bitmap bm=HttpLoader.getPhoto( HttpRoot+"/servlet/DownLoadPhotoServlet",data, null);
		Bitmap bm=HttpLoader.getPhoto( HttpRoot+"/servlet/DownLoadPhotoServlet?name="+bitmapPath, null,minSideLength,maxNumOfPixels);
		return bm;
	}
	
	public synchronized static BeautyIntroductionListJson getNewPage(int firstIndex,int count) throws Throwable
	{
		String json=HttpLoader.get( HttpRoot+"/servlet/GetNewPage?firstIndex="+firstIndex+"&count="+count,null);
		return fromJson(new String(json.getBytes("8859_1"),"UTF-8"), BeautyIntroductionListJson.class);
	}
	
	public synchronized static BeautyIntroductionListJson getRisePage(int firstIndex,int count) throws Throwable
	{
		System.out.println("step in getRisePage method-------------->");
		String json=HttpLoader.get( HttpRoot+"/servlet/GetRisePage?firstIndex="+firstIndex+"&count="+count,null);
		return fromJson(new String(json.getBytes("8859_1"),"UTF-8"), BeautyIntroductionListJson.class);
	}
	
	public synchronized static BeautyIntroductionListJson getSumPage(int firstIndex,int count) throws Throwable
	{
		String json=HttpLoader.get( HttpRoot+"/servlet/GetSumPage?firstIndex="+firstIndex+"&count="+count,null);
		return fromJson(new String(json.getBytes("8859_1"),"UTF-8"), BeautyIntroductionListJson.class);
	}
	
	
}
