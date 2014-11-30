package org.wheat.ranking.entity;

import org.wheat.ranking.data.DataType;
import org.wheat.ranking.data.IData;


public class Comment implements IData
{
	private static final long serialVersionUID = 1L;
	private int dataType=DataType.ENTITY_COMMENT;
	//照片ID
	private int photoID;
	//mmid
	private int beautyID;
	//用户手机号
	private String userPhoneNumber;
	//评论时间
	private String commentTime;
	//评论的内容
	private String commentContent;
	public Comment(int photoID,int beautyID,String userPhoneNumber,String commentTime,String commentContent)
	{
		this.photoID=photoID;
		this.beautyID=beautyID;
		this.userPhoneNumber=userPhoneNumber;
		this.commentTime=commentTime;
		this.commentContent=commentContent;
	}
	public void setPhotoID(int photoID)
	{
		this.photoID=photoID;
	}
	public int getPhotoID()
	{
		return photoID;
	}
	public void setBeautyID(int beautyID)
	{
		this.beautyID=beautyID;
	}
	public int getBeautyID()
	{
		return this.beautyID;
	}
	public void setUserPhoneNumber(String userPhoneNumber)
	{
		this.userPhoneNumber=userPhoneNumber;
	}
	public String getUserPhoneNumber()
	{
		return this.userPhoneNumber;
	}
	public void setCommentTime(String commentTime)
	{
		this.commentTime=commentTime;
	}
	public String getCommentTime()
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
	@Override
	public int getDataType() 
	{
		return dataType;
	}
}
