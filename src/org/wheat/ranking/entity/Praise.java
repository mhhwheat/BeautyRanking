package org.wheat.ranking.entity;

import com.google.gson.annotations.SerializedName;

public class Praise {

	@SerializedName("userPhoneNumber")
	private String userPhoneNumber;
	@SerializedName("praiseTime")
	private String priaiseTime;
	@SerializedName("photoId")
	private String photoId;
	public void setUserPhoneNumber(String userPhoneNumber){
		this.userPhoneNumber=userPhoneNumber;
	}
	public String getUserPhoneNumber(){
		return this.userPhoneNumber;
	}
	public void setPraiseTime(String praiseTime){
		this.priaiseTime=praiseTime;
	}
	public String getPraiseTime(){
		return this.priaiseTime;
	}
	public void setPhotoId(String phoneId){
		this.photoId=phoneId;
	}
	public String getPhotoId(){
		return this.photoId;
	}
}
