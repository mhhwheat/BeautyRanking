package org.wheat.ranking.entity;

import com.google.gson.annotations.SerializedName;

public class BeautyIntroduction 
{
	@SerializedName("beautyId")
	private int beautyId;
	/*
	 * beauty的真实名字
	 */
	@SerializedName("beautyName")
	private String mBeautyName;
	
	/*
	 * 所在学校
	 */
	@SerializedName("school")
	private String mSchool;
	
	/*
	 * 描述
	 */
	@SerializedName("description")
	private String mDescription;
	
	/*
	 * avatar的路径
	 */
	@SerializedName("avatarPath")
	private String mAvatarPath;
	
	@SerializedName("praiseTimes")
	private int mPraiseTimes;
	
	@SerializedName("commentTimes")
	private int mCommentTimes;
	
	
	
	public void setBeautyName(String beautyName)
	{
		this.mBeautyName=beautyName;
	}
	
	public String getBeautyName()
	{
		return this.mBeautyName;
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
	
	public void setBeautyId(int beautyId){
		this.beautyId=beautyId;
	}
	
	public int getBeautyId(){
		return this.beautyId;
	}
	
	public void setPraiseTimes(int mPraiseTimes)
	{
		this.mPraiseTimes=mPraiseTimes;
	}
	
	public int getPraiseTimes()
	{
		return this.mPraiseTimes;
	}
	
	public void setCommentTimes(int mCommentTimes)
	{
		this.mCommentTimes=mCommentTimes;
	}
	
	public int getCommentTimes()
	{
		return this.mCommentTimes;
	}
	
}