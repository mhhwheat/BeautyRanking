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
import org.wheat.ranking.httptools.EncryptUrlParamsTools;
import org.wheat.ranking.httptools.HttpConnectTools;
import org.wheat.ranking.httptools.JsonTools;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LoginAndRegister 
{
	
	
	private static final String HttpRoot="http://192.168.202.236:8080/BeautyRankingServer";
	
	
	
	
	
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
		byte[] data=EncryptUrlParamsTools.encryptParamsToPost(hm);
		String json=HttpConnectTools.post( HttpRoot+"/servlet/BeautyRankingLogin", data,null);
		if(json==null)
		{
			return null;
		}
		return JsonTools.fromJson(json,UserLoginJson.class);
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
		byte[] data=EncryptUrlParamsTools.encryptParamsToPost(hm);
		String json=HttpConnectTools.post( HttpRoot+"/servlet/RegisterUserServlet", data, null);
		if(json==null)
		{
			return null;
		}
		return JsonTools.fromJson(json, UserRegisterJson.class);
	}
	
	
	
	
}
