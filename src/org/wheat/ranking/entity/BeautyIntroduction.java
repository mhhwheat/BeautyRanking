package org.wheat.ranking.entity;

import com.google.gson.annotations.SerializedName;

public class BeautyIntroduction 
{
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
}
