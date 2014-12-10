package org.wheat.ranking.loader;

import java.util.HashMap;

import org.wheat.ranking.entity.BeautyIntroduction;
import org.wheat.ranking.entity.BeautyIntroductionList;
import org.wheat.ranking.entity.ConstantValue;
import org.wheat.ranking.entity.Photo;
import org.wheat.ranking.entity.PhotoList;
import org.wheat.ranking.entity.json.BeautyIntroductionListJson;
import org.wheat.ranking.entity.json.PhotoListJson;

public class HttpLoderTools {

	/**
	 * 
	 * @param beautyId  要查询的beautyId
	 * @return  某个beauty下所有照片
	 * @throws Exception
	 */
	public static PhotoList  getOneBeautyAllPhoto(String beautyId)throws Exception{
		String json=HttpLoader.get( ConstantValue.HttpRoot+"GetOneBeautyAllPhotos?beautyId="+beautyId,null);
		return HttpDataLoader.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), PhotoList.class);
	}
	
	public static void getMycreatePage(String userPhoneNumber)throws Exception{
		HashMap<String,String>map = new HashMap<String,String>();
		map.put("userPhoneNumber", userPhoneNumber);
		String myCreateJson =HttpLoader.getData(ConstantValue.HttpRoot+"GetMyCreateBeauty", map,null);
		BeautyIntroductionListJson beautyInJson=HttpDataLoader.
				fromJson(new String(myCreateJson.getBytes("8859_1"),"UTF-8"), BeautyIntroductionListJson.class);
		BeautyIntroductionList beautyInList=beautyInJson.getData();
		for(BeautyIntroduction beautyIn: beautyInList.getIntroductionList()){
			System.out.println(beautyIn.getBeautyName());
		}
	}


	public static void getMyFollowPage(String userPhoneNumber)throws Exception{
		HashMap<String,String>map = new HashMap<String,String>();
		map.put("userPhoneNumber", userPhoneNumber);
		String myFollowJson =HttpLoader.getData(ConstantValue.HttpRoot+"GetMyFollowPage", map,null);
		BeautyIntroductionListJson beautyInJson=HttpDataLoader.
				fromJson(new String(myFollowJson.getBytes("8859_1"),"UTF-8"), BeautyIntroductionListJson.class);
		BeautyIntroductionList beautyInList=beautyInJson.getData();
		for(BeautyIntroduction beautyIn: beautyInList.getIntroductionList()){
			System.out.println(beautyIn.getBeautyName());
		}
	}
	
	
	public static void getMyNeighourPage(String userPhoneNumber)throws Exception{
		HashMap<String,String>map = new HashMap<String,String>();
		map.put("userPhoneNumber", userPhoneNumber);
		String myNeighourJson =HttpLoader.getData(ConstantValue.HttpRoot+"GetMyFollowPage", map,null);
		BeautyIntroductionListJson beautyInJson=HttpDataLoader.
				fromJson(new String(myNeighourJson.getBytes("8859_1"),"UTF-8"), BeautyIntroductionListJson.class);
		BeautyIntroductionList beautyInList=beautyInJson.getData();
		for(BeautyIntroduction beautyIn: beautyInList.getIntroductionList()){
			System.out.println(beautyIn.getBeautyName());
		}
	}
	
	
	public static void getBeautyAllPhotos(String beautyId)throws Exception{
		HashMap<String,String>map = new HashMap<String,String>();
		map.put("beautyId", beautyId);
		String photoListStr =HttpLoader.getData(ConstantValue.HttpRoot+"GetOneBeautyAllPhotos", map,null);
		PhotoListJson photoListJson=HttpDataLoader.
				fromJson(new String(photoListStr.getBytes("8859_1"),"UTF-8"), PhotoListJson.class);
		PhotoList photoList=photoListJson.getData();
		for(Photo photo: photoList.getPhotoList()){
			System.out.println(photo.getPhotoPath());
		}
	}
}
