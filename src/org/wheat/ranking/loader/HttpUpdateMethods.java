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
import org.wheat.ranking.entity.ConstantValue;
import org.wheat.ranking.httptools.HttpConnectTools;

public class HttpUpdateMethods {

	

	
	public int updateBeautyInfo(HashMap<String ,String> values){
		if(values == null)return -1;
		int resultCode =-1;
		try {
			resultCode = HttpConnectTools.getReturnCode(ConstantValue.HttpRoot+"UpdateBeautyInfo", values, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultCode;
		
	}
}
