package org.wheat.ranking.entity;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.annotations.SerializedName;
public class Photo  
{
	@SerializedName("avatarPath")
	private String avatarPath;
	
	@SerializedName("nickname")
	private String nickname;
	
	//�Ƿ��޹���
	@SerializedName("isPraise")
	private boolean isPraise;
	
	@SerializedName("photoDescription")
	private String photoDescription;
	
	//�����ĸ�beauty��beautyId
	@SerializedName("beautyId")
	private int beautyId;
	
	@SerializedName("photoId")
	private int photoId;
	
	//��Ƭid
	@SerializedName("commentCount")
	private int commentCount=0;
	
	//mm��id
	@SerializedName("praiseCount")
	private int praiseCount=0;
	
	//��Ƭ�ڱ��ص�·��
	@SerializedName("photoPath")
	private String photoPath;
	
	//�û��ֻ�����(�ϴ�����Ƭ���û�)
	@SerializedName("userPhoneNumber")
	private String userPhoneNumber;
	
	//�ϴ�ʱ��
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
	
	public void setIspraise(int praise) 
	{
		if(praise>0)
			this.isPraise=true;
		else
			this.isPraise=false;
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
	
	public void setUploadTime(String date) throws ParseException 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.uploadTime=sdf.parse(date);
		
	}
	
	public String getUpLoadTimeToString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(this.uploadTime);
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
