package org.wheat.ranking.entity;

import com.google.gson.annotations.SerializedName;

public class UserLogin 
{
	/*
	 * 用户的手机号
	 */
	@SerializedName("userPhoneNumber")
	private String mUserPhoneNumber;
	
	/*
	 * 用户的密码
	 */
	@SerializedName("password")
	private String mPassword;
	
	/*
	 * 用户昵称
	 */
	@SerializedName("nikename")
	private String mNikeName;
	
	/*
	 * 学校
	 */
	@SerializedName("school")
	private String mSchool;
	
	/*
	 * 入学年份
	 */
	@SerializedName("admissionYear")
	private int mAdmissionYear;
	
	/*
	 * 性别
	 */
	@SerializedName("sex")
	private String mSex;
	
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
	
	public String getNikeName()
	{
		return this.mNikeName;
	}
	
	public void setNikeName(String nikeName)
	{
		this.mNikeName=nikeName;
	}
	
	public String getSchool()
	{
		return this.mSchool;
	}
	
	public void setSchool(String school)
	{
		this.mSchool=school;
	}
	
	public int getAdmissionYear()
	{
		return this.mAdmissionYear;
	}
	
	public void setAdmissionYear(int admissionYear)
	{
		this.mAdmissionYear=admissionYear;
	}
	
	public String getSex()
	{
		return this.mSex;
	}
	
	public void setSex(String sex)
	{
		this.mSex=sex;
	}
}
