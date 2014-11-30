package org.wheat.ranking.data;

import android.graphics.Bitmap;

public class ListItem 
{
	private Bitmap bm;
	private String name;
	private String school;
	private String description;
	public ListItem(String name,String school,String description)
	{
		this.name=name;
		this.school=school;
		this.description=description;
	}
	public void setPhoto(Bitmap bm)
	{
		this.bm=bm;
	}
	public String getName()
	{
		return this.name;
	}
	public String getSchool()
	{
		return this.school;
	}
	public Bitmap getPhoto()
	{
		return this.bm;
	}
	public String getDescription()
	{
		return this.description;
	}
}
