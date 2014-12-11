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
	public int deleteBeauty(String beautyId){
		int resultCode=-1;
		HashMap<String ,String >data = new HashMap<String ,String>();
		data.put("beautyId", beautyId);
		try {
			resultCode = HttpConnectTools.getReturnCode(ConstantValue.HttpRoot+"DeleteBeauty", data, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultCode;
	}
	
	
	public int deletePhoto(String photoId){
		int resultCode = -1;
		HashMap<String ,String>data = new HashMap<String ,String>();
		data .put("photoId",photoId);
		try {
			resultCode = HttpConnectTools.getReturnCode(ConstantValue.HttpRoot+"DeletePhoto", data, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultCode ;
	}
}
