package org.wheat.ranking.entity;

public class PhotoParameters 
{
	private String url;
	private int minSideLength;
	private int maxNumOfPixels;
	private boolean mFixWidth;
	private int mImageViewWidth;
	public PhotoParameters(String url,int minSideLength,int maxNumOfPixels)
	{
		this(url,minSideLength,maxNumOfPixels,false,0);
	}
	
	/**
	 * 
	 * @param url				图片的下载地址
	 * @param minSideLength		图片最小边的像素
	 * @param maxNumOfPixels	图片的总像素
	 * @param fixWidth			是否保持图片的高宽比例，如果为true，imageViewWidth不能小于0，如果fixWidth为false，imageViewWidth参数无效
	 * @param imageViewWidth	图片所在ImageView的宽度
	 */
	public PhotoParameters(String url,int minSideLength,int maxNumOfPixels,boolean fixWidth,int imageViewWidth)
	{
		this.url=url;
		this.minSideLength=minSideLength;
		this.maxNumOfPixels=maxNumOfPixels;
		this.mFixWidth=fixWidth;
		this.mImageViewWidth=imageViewWidth;
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
	
	public void setImageViewWidth(int imageViewWidth)
	{
		this.mImageViewWidth=imageViewWidth;
	}
	
	public int getImageViewWidth()
	{
		return this.mImageViewWidth;
	}
}
