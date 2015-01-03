package org.wheat.ranking.entity;



import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BeautyDetail 
{
	
	/*
	 * beauty的创建者电话号码
	 */
	@SerializedName("createTime")
	private Date mCreateTime;
	
	/*
	 * 该beauty被赞的次数
	 */
	@Expose 
	@SerializedName("lat")
	private double mLat;
	
	/*
	 * 该beauty的年级
	 */
	@Expose 
	@SerializedName("lng")
	private double mLng;
	
	/*
	 * beauty的创建者电话号码
	 */
	@SerializedName("userPhoneNumber")
	private String mUserPhoneNumber;
	
	/*
	 * 该beauty被赞的次数
	 */
	@Expose 
	@SerializedName("praiseTimes")
	private int mPraiseTimes;
	
	/*
	 * 该beauty的年级
	 */
	@Expose 
	@SerializedName("admissionYear")
	private String mAdmissionYear;
	
	/*
	 * beauty的生日
	 */
	@Expose 
	@SerializedName("birthday")
	private String mBirthday;
	/*
	 * beauty的真实名字
	 */
	@Expose 
	@SerializedName("trueName")
	private String mTrueName;
	/*
	 * beauty的星座
	 */
	@Expose 
	@SerializedName("constellation")
	private String mConstellation;
	/*
	 * 所在学校
	 */
	@Expose 
	@SerializedName("school")
	private String mSchool;
	
	/*
	 * 描述
	 */
	@Expose 
	@SerializedName("description")
	private String mDescription;
	
	/*
	 * avatar的路径
	 */
	@SerializedName("avatarPath")
	private String mAvatarPath;
	
	@Expose
	@SerializedName("beautyId")
	private int  mBeautyId;
	
	
	public void setBeautyId(int BeautyId)
	{
		this.mBeautyId=BeautyId;
	}
	
	public int getBeautyId()
	{
		return this.mBeautyId;
	}
	public void setUserPhoneNumber(String UserPhoneNumber)
	{
		this.mUserPhoneNumber=UserPhoneNumber;
	}
	
	public String getUserPhoneNumber()
	{
		return this.mUserPhoneNumber;
	}
	
	public void setPraiseTimes(int mPraiseTimes)
	{
		this.mPraiseTimes=mPraiseTimes;
	}
	
	public int getPraiseTimes()
	{
		return this.mPraiseTimes;
	}
	
	
	
	public void setAdmissionYear(String AdmissionYear)
	{
		this.mAdmissionYear=AdmissionYear;
	}
	
	public String getAdmissionYear()
	{
		return this.mAdmissionYear;
	}
	
	
	
	public void setBirthday(String Birthday)
	{
		this.mBirthday=Birthday;
	}
	
	public String getBirthday()
	{
		return this.mBirthday;
	}
	public void setConstellation(String Constellation)
	{
		this.mConstellation=Constellation;
	}
	
	public String getConstellation()
	{
		return this.mConstellation;
	}
	
	
	public void setTrueName(String TrueName)
	{
		this.mTrueName=TrueName;
	}
	
	public String getTrueName()
	{
		return this.mTrueName;
	}
	
	public void setSchool(String school)
	{
		this.mSchool=school;
	}
	
	public String getSchool()
	{
		return this.mSchool;
	}
	
	public void setDescription(String description)
	{
		this.mDescription=description;
	}
	
	public String getDescription()
	{
		return this.mDescription;
	}
	
	public void setAvatarPath(String path)
	{
		this.mAvatarPath=path;
	}
	
	public String getAvatarPath()
	{
		return this.mAvatarPath;
	}
	public void setCreateTime(Date CreateTime)
	{
		this.mCreateTime=CreateTime;
	}
	
	public Date getCreateTime()
	{
		return this.mCreateTime;
	}
	public void setLat(double Lat)
	{
		this.mLat=Lat;
	}
	
	public double getLat()
	{
		return this.mLat;
	}
	
	public void setLng(double mLngs)
	{
		this.mLng=mLngs;
	}
	
	public double getLng()
	{
		return this.mLng;
	}
}
