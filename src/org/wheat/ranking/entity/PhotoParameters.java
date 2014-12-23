package org.wheat.ranking.entity;

public class PhotoParameters 
{
	private String url;
	private int minSideLength;
	private int maxNumOfPixels;
	private boolean mFixWidth;
	public PhotoParameters(String url,int minSideLength,int maxNumOfPixels)
	{
		this(url,minSideLength,maxNumOfPixels,false);
	}
	
	public PhotoParameters(String url,int minSideLength,int maxNumOfPixels,boolean fixWidth)
	{
		this.url=url;
		this.minSideLength=minSideLength;
		this.maxNumOfPixels=maxNumOfPixels;
		this.mFixWidth=fixWidth;
	}
	
	public String getUrl()
	{
		return this.url;
	}
	
	public void setUrl(String url)
	{
		this.url=url;
	}
	
	public int getMinSideLength()
	{
		return this.minSideLength;
	}
	
	public void setMinSideLength(int minSideLength)
	{
		this.minSideLength=minSideLength;
	}
	
	public int getMaxNumOfPixels()
	{
		return this.maxNumOfPixels;
	}
	
	public void setMaxNumOfPixles(int maxNumOfPixels)
	{
		this.maxNumOfPixels=maxNumOfPixels;
	}
	
	public void setFixWidth(boolean fixWidth)
	{
		this.mFixWidth=fixWidth;
	}
	
	public boolean isFixWidth()
	{
		return this.mFixWidth;
	}
}
