package org.wheat.ranking.entity;

import com.google.gson.annotations.SerializedName;

public class UserInfo {

	/*
	 * �û��ֻ�
	 */
	@SerializedName("userPhoneNumber")
	private String mUserPhoneNumber;
	
	/*
	 * ��ע��
	 */
	@SerializedName("focusNum")
	private int focusNum;
	
	/*
	 * ���������� 
	 */
	@SerializedName("createNum")
	private int createNum;
	
	public String getmUserPhoneNumber() {
		return mUserPhoneNumber;
	}
	public void setmUserPhoneNumber(String mUserPhoneNumber) {
		this.mUserPhoneNumber = mUserPhoneNumber;
	}
	public int getFocusNum() {
		return focusNum;
	}
	public void setFocusNum(int focusNum) {
		this.focusNum = focusNum;
	}
	public int getCreateNum() {
		return createNum;
	}
	public void setCreateNum(int createNum) {
		this.createNum = createNum;
	}
	
}
