package org.wheat.ranking.entity;
import java.util.List;

import com.google.gson.annotations.SerializedName;
public class PhotoList {

	@SerializedName("photoList")
	private List<Photo> photoList ;
	
	public void setPhotoList(List<Photo> list)
	{
		this.photoList=list;
	}
	
	public List<Photo> getPhotoList()
	{
		return this.photoList;
	}
}
