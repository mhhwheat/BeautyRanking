package org.wheat.ranking.loader;

import java.util.HashMap;

import org.wheat.ranking.entity.BeautyIntroduction;
import org.wheat.ranking.entity.BeautyIntroductionList;
import org.wheat.ranking.entity.ConstantValue;
import org.wheat.ranking.entity.Photo;
import org.wheat.ranking.entity.PhotoList;
import org.wheat.ranking.entity.json.BeautyIntroductionListJson;
import org.wheat.ranking.entity.json.PhotoListJson;
import org.wheat.ranking.httptools.BitmapTools;
import org.wheat.ranking.httptools.HttpConnectTools;
import org.wheat.ranking.httptools.JsonTools;

import android.graphics.Bitmap;

public class HttpLoderMethods {

	/**
	 * 
	 * @param beautyId  要查询的beautyId
	 * @return  某个beauty下所有照片
	 * @throws Exception
	 */
	public static PhotoList  getOneBeautyAllPhoto(String beautyId)throws Exception{
		String json=HttpConnectTools.get( ConstantValue.HttpRoot+"GetOneBeautyAllPhotos?beautyId="+beautyId,null);
		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), PhotoList.class);
	}
	
	public static void getMycreatePage(String userPhoneNumber)throws Exception{
		HashMap<String,String>map = new HashMap<String,String>();
		map.put("userPhoneNumber", userPhoneNumber);
		String myCreateJson =HttpConnectTools.getReturnJsonData(ConstantValue.HttpRoot+"GetMyCreateBeauty", map,null);
		BeautyIntroductionListJson beautyInJson=JsonTools.
				fromJson(new String(myCreateJson.getBytes("8859_1"),"UTF-8"), BeautyIntroductionListJson.class);
		BeautyIntroductionList beautyInList=beautyInJson.getData();
		for(BeautyIntroduction beautyIn: beautyInList.getIntroductionList()){
			System.out.println(beautyIn.getBeautyName());
		}
	}


	public static void getMyFollowPage(String userPhoneNumber)throws Exception{
		HashMap<String,String>map = new HashMap<String,String>();
		map.put("userPhoneNumber", userPhoneNumber);
		String myFollowJson =HttpConnectTools.getReturnJsonData(ConstantValue.HttpRoot+"GetMyFollowPage", map,null);
		BeautyIntroductionListJson beautyInJson=JsonTools.
				fromJson(new String(myFollowJson.getBytes("8859_1"),"UTF-8"), BeautyIntroductionListJson.class);
		BeautyIntroductionList beautyInList=beautyInJson.getData();
		for(BeautyIntroduction beautyIn: beautyInList.getIntroductionList()){
			System.out.println(beautyIn.getBeautyName());
		}
	}
	
	
	public static void getMyNeighourPage(String userPhoneNumber)throws Exception{
		HashMap<String,String>map = new HashMap<String,String>();
		map.put("userPhoneNumber", userPhoneNumber);
		String myNeighourJson =HttpConnectTools.getReturnJsonData(ConstantValue.HttpRoot+"GetMyFollowPage", map,null);
		BeautyIntroductionListJson beautyInJson=JsonTools.
				fromJson(new String(myNeighourJson.getBytes("8859_1"),"UTF-8"), BeautyIntroductionListJson.class);
		BeautyIntroductionList beautyInList=beautyInJson.getData();
		for(BeautyIntroduction beautyIn: beautyInList.getIntroductionList()){
			System.out.println(beautyIn.getBeautyName());
		}
	}
	
	
	public static void getBeautyAllPhotos(String beautyId)throws Exception{
		HashMap<String,String>map = new HashMap<String,String>();
		map.put("beautyId", beautyId);
		String photoListStr =HttpConnectTools.getReturnJsonData(ConstantValue.HttpRoot+"GetOneBeautyAllPhotos", map,null);
		PhotoListJson photoListJson=JsonTools.
				fromJson(new String(photoListStr.getBytes("8859_1"),"UTF-8"), PhotoListJson.class);
		PhotoList photoList=photoListJson.getData();
		for(Photo photo: photoList.getPhotoList()){
			System.out.println(photo.getPhotoPath());
		}
	}
	
	/**
	 * 
	 * @param context
	 * @param bitmapPath 图片在服务端的路径
	 * @return
	 * @throws Throwable
	 */
	public static Bitmap downLoadBitmap(String bitmapPath,int minSideLength,int maxNumOfPixels) throws Throwable
	{
		/*
		HashMap<String,String> hm=new HashMap<String,String>();
		hm.put("path", bitmapPath);
		byte[] data=encryptParamsToPost(hm);
		*/
		//Bitmap bm=HttpLoader.getPhoto( HttpRoot+"/servlet/DownLoadPhotoServlet",data, null);
		Bitmap bm=BitmapTools.getPhoto( ConstantValue.HttpRoot+"/servlet/DownLoadPhotoServlet?name="+bitmapPath, null,minSideLength,maxNumOfPixels);
		return bm;
	}
	
	public synchronized static BeautyIntroductionListJson getNewPage(int firstIndex,int count) throws Throwable
	{
		String json=HttpConnectTools.get( ConstantValue.HttpRoot+"/servlet/GetNewPage?firstIndex="+firstIndex+"&count="+count,null);
		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), BeautyIntroductionListJson.class);
	}
	
	public synchronized static BeautyIntroductionListJson getRisePage(int firstIndex,int count) throws Throwable
	{
		System.out.println("step in getRisePage method-------------->");
		String json=HttpConnectTools.get( ConstantValue.HttpRoot+"/servlet/GetRisePage?firstIndex="+firstIndex+"&count="+count,null);
		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), BeautyIntroductionListJson.class);
	}
	
	public synchronized static BeautyIntroductionListJson getSumPage(int firstIndex,int count) throws Throwable
	{
		String json=HttpConnectTools.get( ConstantValue.HttpRoot+"/servlet/GetSumPage?firstIndex="+firstIndex+"&count="+count,null);
		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), BeautyIntroductionListJson.class);
	}
}
