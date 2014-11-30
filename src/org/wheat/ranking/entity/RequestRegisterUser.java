package org.wheat.ranking.entity;


import org.wheat.ranking.data.DataType;
import org.wheat.ranking.data.IData;

public class RequestRegisterUser implements IData
{
	private static final long serialVersionUID = 1L;
	private int dataType=DataType.REQUEST_REGISTER;
	private String userPhoneNumber;
	private String password;
	private String nickName;
	private String school;
	private int admissionYear;
	private String sex;
	private String avatarPath;
	public RequestRegisterUser(){}
	public RequestRegisterUser(String userPhoneNumber,String nickName)
	{
		this.userPhoneNumber=userPhoneNumber;
		this.nickName=nickName;
	}
	//用户号码
	public void setUserPhoneNumber(String userPhone)
	{
		this.userPhoneNumber=userPhone;
	}
	public String getUserPhoneNumber()
	{
		return this.userPhoneNumber;
	}
	public void setPassword(String password)
	{
		this.password=password;
	}
	public String getPassword()
	{
		return this.password;
	}
	//别名
	public void setNickName(String nickName)
	{
		this.nickName=nickName;
	}
	public String getNickName()
	{
		return this.nickName;
	}
	//学校
	public void setSchool(String school)
	{
		this.school=school;
	}
	public String getSchool()
	{
		return school;
	}
	//入学年份
	public void setAdmissionYear(int admissionYear)
	{
		this.admissionYear=admissionYear;
	}
	public int getAdmissionYear()
	{
		return this.admissionYear;
	}
	//性别
	public void setSex(String sex)
	{
		this.sex=sex;
	}
	public String getSex()
	{
		return this.sex;
	}
	//头像路径
	public void setAvatarPath(String avatarPath)
	{
		this.avatarPath=avatarPath;
	}
	public String getAvatarPath()
	{
		return this.avatarPath;
	}
	@Override
	public int getDataType()
	{
		return dataType;
	}
	
}
