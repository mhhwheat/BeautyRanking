package org.wheat.ranking.entity;


import java.util.Date;

import com.google.gson.annotations.SerializedName;
public class Photo  
{
	@SerializedName("avatarPath")
	private String avatarPath;
	@SerializedName("nickname")
	private String nickname;
	//是否被赞过了
	@SerializedName("isPraise")
	private boolean isPraise;
	@SerializedName("photoDescription")
	private String photoDescription;
	//属于哪个beauty的beautyId
	@SerializedName("beautyId")
	private int beautyId;
	@SerializedName("photoId")
	private int photoId;
	//照片id
	@SerializedName("commentCount")
	private int commentCount=0;
	//mm的id
	@SerializedName("praiseCount")
	private int praiseCount=0;
	//照片在本地的路径
	@SerializedName("photoPath")
	private String photoPath;
	//用户手机号码(上传该照片的用户)
	@SerializedName("userPhoneNumber")
	private String userPhoneNumber;
	//上传时间
	@SerializedName("uploadTime")
	private Date uploadTime;
	public void setAvatarPath(String userAvatar)
	{
		this.avatarPath=userAvatar;
	}
	public String getAvatarPath()
	{
		return this.avatarPath;
	}
	public void setNickName(String nickName)
	{
		this.nickname=nickName;
	}
	public String getNickName()
	{
		return this.nickname;
	}
	public void setPhotoDescription(String nickName)
	{
		this.photoDescription=nickName;
	}
	public String getPhotoDescription()
	{
		return this.photoDescription;
	}
	public void setIspraise(boolean praise){
		this.isPraise=praise;
	}
	public boolean getIsPraise(){
		return this.isPraise;
	}
	public void setBeautyId(int beautyId){
		this.beautyId=beautyId;
	}
	public int getBeautyId(){
		return this.beautyId;
	}
	public void setPraiseCount(int praiseCount)
	{
		this.praiseCount=praiseCount;
	}
	public int getPraiseCount()
	{
		return this.praiseCount;
	}
	public void setCommentCount(int commentCount)
	{
		this.commentCount=commentCount;
	}
	public int getCommentCount()
	{
		return this.commentCount;
	}
	public void setPhotoPath(String photoPath)
	{
		this.photoPath=photoPath;
	}
	public String getPhotoPath()
	{
		return this.photoPath;
	}
	public void setUserPhoneNumber(String userPhoneNumber)
	{
		this.userPhoneNumber=userPhoneNumber;
	}
	public String getUserPhoneNumber()
	{
		return this.userPhoneNumber;
	}
	public void setUploadTime(Date uploadTime)
	{
		this.uploadTime=uploadTime;
	}
	public Date getUploadTime()
	{
		return this.uploadTime;
	}
	public void setPhotoId(int photoId)
	{
		this.photoId=photoId;
	}
	public int getPhotoId()
	{
		return this.photoId;
	}
}
