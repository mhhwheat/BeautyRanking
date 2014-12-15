package org.wheat.ranking.loader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
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
