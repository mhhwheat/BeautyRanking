package org.wheat.ranking.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class BeautyIntroductionList 
{
	/*
	 * ºÚΩÈ¡–±Ì
	 */
	@SerializedName("introdutionList")
	private List<BeautyIntroduction> mIntroductionList;
	
	public void setIntroductionList(List<BeautyIntroduction> list)
	{
		this.mIntroductionList=list;
	}
	
	public List<BeautyIntroduction> getIntroductionList()
	{
		return this.mIntroductionList;
	}
}
