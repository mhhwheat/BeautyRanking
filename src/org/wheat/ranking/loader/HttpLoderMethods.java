package org.wheat.ranking.loader;


import org.wheat.ranking.entity.ConstantValue;
import org.wheat.ranking.entity.json.BeautyDetailJson;
import org.wheat.ranking.entity.json.BeautyIntroductionListJson;
import org.wheat.ranking.entity.json.CommentListJson;
import org.wheat.ranking.entity.json.PhotoListJson;
import org.wheat.ranking.httptools.BitmapTools;
import org.wheat.ranking.httptools.HttpConnectTools;
import org.wheat.ranking.httptools.JsonTools;

import android.graphics.Bitmap;
/**
 * 
* @ClassName: HttpLoderMethods 
* @Description: 所有的获取数据的方法,只要返回null值就是建立链接失败
* @author hogachen
* @date 2014年12月12日 下午4:14:16 
*
 */
public class HttpLoderMethods {


	
	public static BeautyIntroductionListJson getMycreatePage(int firstIndex,int count,String userPhoneNumber)throws Exception{
		String myCreateJson =HttpConnectTools.get(ConstantValue.HttpRoot+"GetMyCreateBeauty?userPhoneNumber="+userPhoneNumber
				+"&firstIndex="+firstIndex+"&count="+count,null);
		if(myCreateJson == null)return null;
		BeautyIntroductionListJson beautyInJson=JsonTools.
				fromJson(new String(myCreateJson.getBytes("8859_1"),"UTF-8"), BeautyIntroductionListJson.class);
//		BeautyIntroductionList beautyInList=beautyInJson.getData();
//		for(BeautyIntroduction beautyIn: beautyInList.getIntroductionList()){
//			System.out.println(beautyIn.getBeautyName());
//		}
		return beautyInJson;
	}
	
	public static PhotoListJson getMyCreatePageWithPhoto(int firstIndex,int count,String userPhoneNumber)throws Exception{
		String myFollowJson =HttpConnectTools.get(ConstantValue.HttpRoot+"GetMyCreatePhoto?userPhoneNumber="+userPhoneNumber
				+"&firstIndex="+firstIndex+"&count="+count,null);
		if (myFollowJson == null)return null;
		PhotoListJson beautyInJson=JsonTools.
				fromJson(new String(myFollowJson.getBytes("8859_1"),"UTF-8"), PhotoListJson.class);
		return beautyInJson;
	}
	
	


	public static BeautyIntroductionListJson getMyFollowPage(int firstIndex,int count,String userPhoneNumber)throws Exception{
		String myFollowJson =HttpConnectTools.get(ConstantValue.HttpRoot+"GetMyFollowPage?userPhoneNumber="+userPhoneNumber
				+"&firstIndex="+firstIndex+"&count="+count,null);
		if (myFollowJson == null)return null;
		BeautyIntroductionListJson beautyInJson=JsonTools.
				fromJson(new String(myFollowJson.getBytes("8859_1"),"UTF-8"), BeautyIntroductionListJson.class);
		
		return beautyInJson;
	}
	public static PhotoListJson getMyFollowPageWithPhoto(int firstIndex,int count,String userPhoneNumber)throws Exception{
		String myFollowJson =HttpConnectTools.get(ConstantValue.HttpRoot+"GetMyFollowPhoto?userPhoneNumber="+userPhoneNumber
				+"&firstIndex="+firstIndex+"&count="+count,null);
		if (myFollowJson == null)return null;
		PhotoListJson beautyInJson=JsonTools.
				fromJson(new String(myFollowJson.getBytes("8859_1"),"UTF-8"), PhotoListJson.class);
		
		return beautyInJson;
	}
	/**
	 * 
	* @Description: TODO
	* @author hogachen   
	* @date 2014年12月12日 下午7:28:10 
	* @version V1.0  
	* @param userPhoneNumber
	* @return  null 表示建立链接失败，非null表示建立链接成功，但是数据不一定获取成功
	* @throws Exception
	 */
	public static BeautyIntroductionListJson getMyNeighourPage(int firstIndex,int count,double lat,double lng)throws Exception{

		String myNeighourJson =HttpConnectTools.get(ConstantValue.HttpRoot+"GetNeighour?lat="+lat+"&lng="+lng
				+"&firstIndex="+firstIndex+"&count="+count,null);
		if(myNeighourJson == null )return null;		
		BeautyIntroductionListJson beautyInJson=JsonTools.
				fromJson(new String(myNeighourJson.getBytes("8859_1"),"UTF-8"), BeautyIntroductionListJson.class);

		return beautyInJson;
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
	public static PhotoListJson getBeautyAllPhotos(int firstIndex,int count,int  beautyId,String userPhoneNumber)throws Exception{
		String photoListStr=HttpConnectTools.get( ConstantValue.HttpRoot+"GetOneBeautyAllPhotos?beautyId="+beautyId
				+"&firstIndex="+firstIndex+"&count="+count+"&userPhoneNumber="+userPhoneNumber,null);
		if(photoListStr==null){
			return null;
		}
		PhotoListJson photoListJson=JsonTools.
				fromJson(new String(photoListStr.getBytes("8859_1"),"UTF-8"), PhotoListJson.class);
		return photoListJson;
	}
	
	
	
	
	
	public static BeautyDetailJson getBeautyDetail (int  beautyId)throws Exception{
		String beautyDetailStr=HttpConnectTools.get( ConstantValue.HttpRoot+"GetBeautyDetail?beautyId="+beautyId,null);
		if(beautyDetailStr==null){
			return null;
		}
		BeautyDetailJson beautyDetail=JsonTools.
				fromJson(new String(beautyDetailStr.getBytes("8859_1"),"UTF-8"),BeautyDetailJson.class);
		
		return beautyDetail;
	}
	
	public static CommentListJson getPhotoComments(int firstIndex,int count,int photoId) throws Exception
	{
		String json=HttpConnectTools.get(ConstantValue.HttpRoot+"servlet/GetPhotoComments?photoId="+photoId+"&firstIndex="+firstIndex+"&count="+count, null);
		if(json==null)
			return null;
		CommentListJson commentListJson=JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), CommentListJson.class);
		return commentListJson;
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
