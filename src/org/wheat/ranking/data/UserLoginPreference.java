package org.wheat.ranking.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/** 
 * description:保存用户登录信息
 * @author wheat
 * date: 2014-12-16  
 * time: 上午11:31:40
 */
public class UserLoginPreference 
{
	private static UserLoginPreference preference = null;  
    private SharedPreferences sharedPreference;  
    private String packageName;  
      
    private static final String USER_PHONE_NUMBER = "userPhoneNumber"; //登录名  
    private static final String PASSWORD = "password";  //密码  
    private static final String IS_SAVE_PWD = "isSavePwd"; //是否保留密码  
      
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
