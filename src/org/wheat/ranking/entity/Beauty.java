package org.wheat.ranking.entity;

import org.wheat.ranking.data.DataType;
import org.wheat.ranking.data.IData;


public class Beauty implements IData
{
	/**
	 * @author wheat
	 */
	private static final long serialVersionUID = 1L;
	private int dataType=DataType.ENTITY_BEAUTY;
	//MM的ID
	private int beautyID;
	//头像的路径
	private String avatarPath;
	//MM的真实名字
	private String trueName;
	//上传该MM信息的
	private String userPhoneNumber;
	//赞的次数
	private Long praseTimes;
	//MM的学校
	private String school;
	//入学年份
	private String admissionYear;
	//生日时间
	private String birthday;
	//星座
	private String constellation;
	//描述
	private String description;
	public Beauty(int beautyID,String userPhoneNumber,String trueName,Long praseTimes,String school,String description)
	{
		this.beautyID=beautyID;
		this.userPhoneNumber=userPhoneNumber;
		this.trueName=trueName;
		this.praseTimes=praseTimes;
		this.school=school;
		this.description=description;
	}
	public void setBeautyID(int beautyID)
	{
		this.beautyID=beautyID;
	}
	public int getBeautyID()
	{
		return this.beautyID;
	}
	public void setAvatarPath(String avatarPath)
	{
		this.avatarPath=avatarPath;
	}
	public String getAvatarPath()
	{
		return this.avatarPath;
	}
	public void setTrueName(String trueName)
	{
		this.trueName=trueName;
	}
	public String getTrueName()
	{
		return trueName;
	}
	public void setUserPhoneNumber(String userPhoneNmuber)
	{
		this.userPhoneNumber=userPhoneNmuber;
	}
	public String getUserPhoneNumber()
	{
		return this.userPhoneNumber;
	}
	public void setPraseTimes(Long praseTimes)
	{
		this.praseTimes=praseTimes;
	}
	public Long getPraseTimes()
	{
		return this.praseTimes;
	}
	public void setSchool(String school)
	{
		this.school=school;
	}
	public String getSchool()
	{
		return school;
	}
	public void setAdmissionYear(String admissionYear)
	{
		this.admissionYear=admissionYear;
	}
	public String getAdmissionYear()
	{
		return admissionYear;
	}
	public void setBirthday(String birthday)
	{
		this.birthday=birthday;
	}
	public String getBirthday()
	{
		return this.birthday;
	}
	public void setConstellation(String constellation)
	{
		this.constellation=constellation;
	}
	public String getConstellation()
	{
		return this.constellation;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public String getDescription()
	{
		return this.description;
	}
	@Override
	public int getDataType() 
	{
		return dataType;
	}
}
