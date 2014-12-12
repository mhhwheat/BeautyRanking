package org.wheat.ranking.loader;

import java.io.IOException;
import java.util.HashMap;

import org.wheat.ranking.entity.ConstantValue;
import org.wheat.ranking.httptools.HttpConnectTools;
/**
 * 
* @ClassName: HttpDeleteMethods 
* @Description: TODO 
* @author hogachen
* @date 2014年12月11日 下午6:40:39 
*
 */
public class HttpDeleteMethods {
	/**
	 * 
	* @Description: TODO delete one beauty all info ,including the photo ,praise and comment
	* @author hogachen   
	* @date 2014年12月11日 下午6:41:47 
	* @version V1.0  
	* @param beautyId
	* @return
	 */
	public static int deleteBeauty(int beautyId){
		return deleteMethod(ConstantValue.HttpRoot+"DeleteBeauty?beautyId="+beautyId);
	}
	
	
	public  static int deletePhoto(int photoId){
		return deleteMethod(ConstantValue.HttpRoot+"DeletePhoto?photoId="+photoId);
	}
	
	
	
	public static int deleteFollow(int beautyId,String userPhoneNumber){
		return deleteMethod(ConstantValue.HttpRoot+"DeleteFollow?beautyId="+
	beautyId+"&userPhoneNumber="+userPhoneNumber);

	}
	
	
	public static int deleteComment(int commentId){
		return deleteMethod(ConstantValue.HttpRoot+"DeleteComment?commentId="+commentId);
	}
	
	
	
	
	public static int deleteMethod(String url){
		int resultCode = -1;
		try {
			resultCode = HttpConnectTools.getReturnCode(url, null, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultCode ;
	}
}
