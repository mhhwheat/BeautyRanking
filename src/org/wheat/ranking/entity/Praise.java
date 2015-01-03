package org.wheat.ranking.entity;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class Praise {

	@SerializedName("userPhoneNumber")
	private String userPhoneNumber;
	
	@SerializedName("praiseTime")
	private Date priaiseTime;
	
	@SerializedName("photoId")
	private int photoId;
	
	@SerializedName("beautyId")
	private int beautyId=-1;
	
	public void setUserPhoneNumber(String userPhoneNumber){
		this.userPhoneNumber=userPhoneNumber;
	}
	
	public String getUserPhoneNumber(){
		return this.userPhoneNumber;
	}
	
	public void setPraiseTime(Date praiseTime){
		this.priaiseTime=praiseTime;
	}
	
	public Date getPraiseTime(){
		return this.priaiseTime;
	}
	
	public void setPhotoId(int phoneId){
		this.photoId=phoneId;
	}
	
	public int getPhotoId(){
		return this.photoId;
	}
	
	public void setBeautyId(int beautyId){
		this.beautyId=beautyId;
	}
	
	public int getBeautyId(){
		return this.beautyId;
	}
}