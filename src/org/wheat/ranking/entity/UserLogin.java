package org.wheat.ranking.entity;

import com.google.gson.annotations.SerializedName;

public class UserLogin 
{
	/*
	 * �û����ֻ���
	 */
	@SerializedName("userPhoneNumber")
	private String mUserPhoneNumber;
	
	/*
	 * �û�������
	 */
	@SerializedName("password")
	private String mPassword;
	
	public String getUserPhoneNumber()
	{
		return this.mUserPhoneNumber;
	}
	
	public void setUserPhoneNumber(String userPhoneNumber)
	{
		this.mUserPhoneNumber=userPhoneNumber;
	}
	
	public String getPassword()
	{
		return this.mPassword;
	}
	
	public void setPassword(String password)
	{
		this.mPassword=password;
	}
}
