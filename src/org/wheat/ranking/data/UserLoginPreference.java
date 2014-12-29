package org.wheat.ranking.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/** 
 * description:±£¥Ê”√ªßµ«¬º–≈œ¢
 * @author wheat
 * date: 2014-12-16  
 * time: …œŒÁ11:31:40
 */
public class UserLoginPreference 
{
	private static UserLoginPreference preference = null;  
    private SharedPreferences sharedPreference;  
    private String packageName;  
      
    private static final String USER_PHONE_NUMBER = "userPhoneNumber"; //µ«¬º√˚  
    private static final String PASSWORD = "password";  //√‹¬Î  
    private static final String IS_SAVE_PWD = "isSavePwd"; // «∑Ò±£¡Ù√‹¬Î  
    private static final String USER_INFO_CREATE_NUM="userInfoCreateNum";
    private static final String USER_INFO_FOCUS_NUM="userInfoFocusNum";
    private static final String USER_INFO_NICKNAME="userInfoNickName";
    private static final String USER_INFO_LIKE="userInfoLike";
    private static final String USER_INFO_PERSION_SIGN="userInfoPersonSign";
    public static synchronized UserLoginPreference getInstance(Context context){  
        if(preference == null)  
            preference = new UserLoginPreference(context);  
        return preference;  
    }  
      
      
    private  UserLoginPreference(Context context){  
        packageName = context.getPackageName() + "_user_login_preferences";  
        sharedPreference = context.getSharedPreferences(  
                packageName, Context.MODE_PRIVATE);  
    }  
      
    public void setUserInfoPersionSign(String userInfoPersionSign){
    	Editor editor = sharedPreference.edit();  
        editor.putString(USER_INFO_PERSION_SIGN, userInfoPersionSign);  
        editor.commit();
    }
    public  String getUserInfoPersionSign() {
    	String userPhoneNumber = sharedPreference.getString(USER_INFO_PERSION_SIGN, "ƒ·¬Í");  
        return userPhoneNumber;
	}


	public  int getUserInfoCreateNum() {
    	int userPhoneNumber = sharedPreference.getInt(USER_INFO_CREATE_NUM,0);  
        return userPhoneNumber;
	}


	public  int getUserInfoFocusNum() {
		int userPhoneNumber = sharedPreference.getInt(USER_INFO_FOCUS_NUM, 0);  
        return userPhoneNumber;
	}

	public  String getUserInfoNickname() {
		String userPhoneNumber = sharedPreference.getString(USER_INFO_NICKNAME, "∫Íº—");  
        return userPhoneNumber;
	}
	
	
	public  int getUserInfoLike() {
		int userPhoneNumber = sharedPreference.getInt(USER_INFO_LIKE, 0);  
        return userPhoneNumber;
	}
	public void setUserInfoLike(int likenum){
		Editor editor = sharedPreference.edit();  
        editor.putInt(USER_INFO_LIKE, likenum);  
        editor.commit(); 
	}

	public  void setUserInfoNickname(String userinfonickname) {
		Editor editor = sharedPreference.edit();  
        editor.putString(USER_PHONE_NUMBER, userinfonickname);  
        editor.commit(); 
	}


	public  void setUserInfoCreateNum(int userInfoCreateNum) {
		Editor editor = sharedPreference.edit();  
        editor.putInt(USER_PHONE_NUMBER, userInfoCreateNum);  
        editor.commit(); 
	}


	public  void setUserInfoFocusNum(int userInfoFocusNum) {
		Editor editor = sharedPreference.edit();  
        editor.putInt(USER_PHONE_NUMBER, userInfoFocusNum);  
        editor.commit(); 
	}


	

	
	public String getuserPhoneNumber(){  
        String userPhoneNumber = sharedPreference.getString(USER_PHONE_NUMBER, "");  
        return userPhoneNumber;  
    }  
      
      
    public void SetuserPhoneNumber(String userPhoneNumber){  
        Editor editor = sharedPreference.edit();  
        editor.putString(USER_PHONE_NUMBER, userPhoneNumber);  
        editor.commit();  
    }  
      
      
    public String getPassword(){  
        String password = sharedPreference.getString(PASSWORD, "");  
        return password;  
    }  
      
      
    public void SetPassword(String password){  
        Editor editor = sharedPreference.edit();  
        editor.putString(PASSWORD, password);  
        editor.commit();  
    }  
      
      
    public boolean IsSavePwd(){  
        Boolean isSavePwd = sharedPreference.getBoolean(IS_SAVE_PWD, false);  
        return isSavePwd;  
    }  
      
      
    public void SetIsSavePwd(Boolean isSave){  
        Editor edit = sharedPreference.edit();  
        edit.putBoolean(IS_SAVE_PWD, isSave);  
        edit.commit();  
    }  
}
