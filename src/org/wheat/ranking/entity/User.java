package org.wheat.ranking.entity;

import com.google.gson.annotations.SerializedName;

public class User 
{
	/*
	 * 用户手机
	 */
	@SerializedName("userPhoneNumber")
	private String mUserPhoneNumber;
	
	/*
	 * 昵称
	 */
	@SerializedName("nikeName")
	private String mNikeName;
	
	/*
	 * 学校
	 */
	@SerializedName("school")
	private String mSchool;
	
	/*
	 * 性别
	 */
	@SerializedName("sex")
	private String mSex;
	
	/*
	 * 入学年份
	 */
	@SerializedName("admissionYear")
	private int mAdmissionYear;
	
	public void setUserPhoneNumber(String userPhoneNumber)
	{
		this.mUserPhoneNumber=userPhoneNumber;
	}
	
	public String getUserPhoneNumber()
	{
		return this.mUserPhoneNumber;
	}
	
	public void setNikename(String nikeName)
	{
		this.mNikeName=nikeName;
	}
	
	public String getNikename()
	{
		return this.mNikeName;
	}
	
	public void setSchool(String school)
	{
		this.mSchool=school;
	}
	
	public String getSchool()
	{
		return this.mSchool;
	}
	
	public void setSex(String sex)
	{
		this.mSex=sex;
	}
	
	public String getSex()
	{
		return this.mSex;
	}
	
	public void setAdmissionYear(int admissionYear)
	{
		this.mAdmissionYear=admissionYear;
	}
	
	public int getAdmissionYear()
	{
		return this.mAdmissionYear;
	}
}
