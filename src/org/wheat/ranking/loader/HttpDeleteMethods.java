package org.wheat.ranking.loader;

import java.io.IOException;
import java.util.HashMap;

import org.wheat.ranking.entity.ConstantValue;
import org.wheat.ranking.httptools.HttpConnectTools;

public class HttpDeleteMethods {
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
