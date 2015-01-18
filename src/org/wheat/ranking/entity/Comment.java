package org.wheat.ranking.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.annotations.SerializedName;


public class Comment
{
	//评论ID
	@SerializedName("commentId")
	private int commentID;
	
	//照片ID
	@SerializedName("photoId")
	private int photoID;
	
	//用户手机号
	@SerializedName("userPhoneNumber")
	private String userPhoneNumber;
	
	//用户头像地址
	@SerializedName("userAvatar")
	private String userAvatar;
	
	//用户昵称
	@SerializedName("userNickName")
	private String userNickName;
	
	//评论时间
	@SerializedName("commentTime")
	private Date commentTime;
	
	//评论的内容
	@SerializedName("commentContent")
	private String commentContent;
	
	public void setCommentID(int commentID)
	{
		this.commentID=commentID;
	}
	
	public int getCommentID()
	{
		return this.commentID;
	}
	
	public void setPhotoID(int photoID)
	{
		this.photoID=photoID;
	}
	
	public int getPhotoID()
	{
		return photoID;
	}
	
	public void setUserPhoneNumber(String userPhoneNumber)
	{
		this.userPhoneNumber=userPhoneNumber;
	}
	
	public String getUserPhoneNumber()
	{
		return this.userPhoneNumber;
	}
	
	public void setUserAvatar(String userAvatar)
	{
		this.userAvatar=userAvatar;
	}
	
	public String getUserAvatar()
	{
		return this.userAvatar;
	}
	
	public void setUserNickName(String userNickName)
	{
		this.userNickName=userNickName;
	}
	
	public String getUserNickName()
	{
		return this.userNickName;
	}
	
	public void setCommentTime(Date commentTime)
	{
		this.commentTime=commentTime;
	}
	
	public void setCommentTime(String date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.commentTime=sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getCommentTimeToString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(this.commentTime);
	}
	
	public Date getCommentTime()
	{
		return this.commentTime;
	}
	
	public void setCommentContent(String commentContent)
	{
		this.commentContent=commentContent;
	}
	
	public String getCommentContent()
	{
		return this.commentContent;
	}
}
