package org.wheat.ranking.loader;


import org.wheat.ranking.entity.BeautyDetail;
import org.wheat.ranking.entity.ConstantValue;
import org.wheat.ranking.httptools.HttpConnectTools;

public class HttpUpdateMethods {

	

	
	public static int updateBeautyInfo(BeautyDetail beautyInfo){
		if(beautyInfo == null)return ConstantValue.ClientParameterErr;
		int resultCode =-1;
		try {
			resultCode = HttpConnectTools.postJsonReturnCode(ConstantValue.HttpRoot+"UpdateBeautyInfo", beautyInfo, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultCode;
		
	}
}
