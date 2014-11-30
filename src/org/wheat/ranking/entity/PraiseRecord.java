package org.wheat.ranking.entity;

import org.wheat.ranking.data.DataType;
import org.wheat.ranking.data.IData;

public class PraiseRecord implements IData
{
	private static final long serialVersionUID = 1L;
	private int dataType=DataType.ENTITY_PRAISERECORD;
	//用户手机号
	private String userPhoneNumber;
	//mm的id
	private int beautyID;
	//赞的事件
	private String praiseTime;
	public void setUserPhoneNumber(String userPhoneNumber)
	{
		this.userPhoneNumber=userPhoneNumber;
	}
	public String getUserPhoneNumber()
	{
		return this.userPhoneNumber;
	}
	public void setbeautyID(int beautyID)
	{
		this.beautyID=beautyID;
	}
	public int getBeauty()
	{
		return this.beautyID;
	}
	public void setPraiseTime(String praiseTime)
	{
		this.praiseTime=praiseTime;
	}
	public String getPraiseTime()
	{
		return this.praiseTime;
	}
	@Override
	public int getDataType() 
	{
		return dataType;
	}
}
