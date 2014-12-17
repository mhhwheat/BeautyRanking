package org.wheat.ranking.loader;

import java.util.HashMap;

import org.wheat.ranking.entity.ConstantValue;
import org.wheat.ranking.entity.json.UserLoginJson;
import org.wheat.ranking.entity.json.UserRegisterJson;
import org.wheat.ranking.httptools.EncryptUrlParamsTools;
import org.wheat.ranking.httptools.HttpConnectTools;
import org.wheat.ranking.httptools.JsonTools;

public class LoginAndRegister 
{
	
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
		String json=HttpConnectTools.post( ConstantValue.HttpRoot+"/servlet/BeautyRankingLogin", data,null);
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
		String json=HttpConnectTools.post( ConstantValue.HttpRoot+"/servlet/RegisterUserServlet", data, null);
		if(json==null)
		{
			return null;
		}
		return JsonTools.fromJson(json, UserRegisterJson.class);
	}
	
	
	
	
}
