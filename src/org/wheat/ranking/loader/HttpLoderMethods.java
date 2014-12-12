package org.wheat.ranking.loader;

import java.util.HashMap;

import org.wheat.ranking.entity.BeautyDetail;
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
/**
 * 
* @ClassName: HttpLoderMethods 
* @Description: 所有的获取数据的方法，只要是没有获取到数据就会得到一个null值
* @author hogachen
* @date 2014年12月12日 下午4:14:16 
*
 */
public class HttpLoderMethods {


	
	public static BeautyIntroductionList getMycreatePage(String userPhoneNumber)throws Exception{
		String myCreateJson =HttpConnectTools.get(ConstantValue.HttpRoot+"GetMyCreateBeauty?userPhoneNumber="+userPhoneNumber,null);
		if(myCreateJson == null)return null;
		BeautyIntroductionListJson beautyInJson=JsonTools.
				fromJson(new String(myCreateJson.getBytes("8859_1"),"UTF-8"), BeautyIntroductionListJson.class);
		BeautyIntroductionList beautyInList=beautyInJson.getData();
		for(BeautyIntroduction beautyIn: beautyInList.getIntroductionList()){
			System.out.println(beautyIn.getBeautyName());
		}
		return beautyInList;
	}


	public static BeautyIntroductionList getMyFollowPage(String userPhoneNumber)throws Exception{
		String myFollowJson =HttpConnectTools.get(ConstantValue.HttpRoot+"GetMyFollowPage?userPhoneNumber="+userPhoneNumber,null);
		if (myFollowJson == null)return null;
		BeautyIntroductionListJson beautyInJson=JsonTools.
				fromJson(new String(myFollowJson.getBytes("8859_1"),"UTF-8"), BeautyIntroductionListJson.class);
		BeautyIntroductionList beautyInList=beautyInJson.getData();
		for(BeautyIntroduction beautyIn: beautyInList.getIntroductionList()){
			System.out.println(beautyIn.getBeautyName());
		}
		
		return beautyInList;
	}
	
	
	public static BeautyIntroductionList getMyNeighourPage(String userPhoneNumber)throws Exception{

		String myNeighourJson =HttpConnectTools.get(ConstantValue.HttpRoot+"GetMyFollowPage?userPhoneNumber="+userPhoneNumber,null);
		if(myNeighourJson == null )return null;		
		BeautyIntroductionListJson beautyInJson=JsonTools.
				fromJson(new String(myNeighourJson.getBytes("8859_1"),"UTF-8"), BeautyIntroductionListJson.class);
		BeautyIntroductionList beautyInList=beautyInJson.getData();
		for(BeautyIntroduction beautyIn: beautyInList.getIntroductionList()){
			System.out.println(beautyIn.getBeautyName());
		}
		return beautyInList;
	}
	
	/**
	 * 
	* @Description: TODO
	* @author Hogachen   
	* @date 2014年12月12日 上午11:33:03 
	* @version V1.0  
	* @param beautyId
	* @return if can't get  the data , return null
	* @throws Exception
	 */
	public static PhotoList getBeautyAllPhotos(int  beautyId)throws Exception{
		String photoListStr=HttpConnectTools.get( ConstantValue.HttpRoot+"GetOneBeautyAllPhotos?beautyId="+beautyId,null);
		if(photoListStr==null){
			return null;
		}
		PhotoListJson photoListJson=JsonTools.
				fromJson(new String(photoListStr.getBytes("8859_1"),"UTF-8"), PhotoListJson.class);
		PhotoList photoList=photoListJson.getData();
		for(Photo photo: photoList.getPhotoList()){
			System.out.println(photo.getPhotoPath());
		}
		return photoList;
	}
	
	
	
	
	
	public static BeautyDetail getBeautyDetail (int  beautyId)throws Exception{
		String beautyDetailStr=HttpConnectTools.get( ConstantValue.HttpRoot+"GetBeautyDetail?beautyId="+beautyId,null);
		if(beautyDetailStr==null){
			return null;
		}
		BeautyDetail beautyDetail=JsonTools.
				fromJson(new String(beautyDetailStr.getBytes("8859_1"),"UTF-8"), BeautyDetail.class);
		
		System.out.println(beautyDetail.getBirthday());
		return beautyDetail;
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
