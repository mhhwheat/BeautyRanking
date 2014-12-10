package org.wheat.ranking.httptools;

import java.io.File;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import android.os.Environment;
/**
 * 
 * @author hogachen
 * @deprecated 使用httpclient上传文件
 *
 */
public class UploadPhotoHttpclientClient {

	/**
	 * 
	 * @param FILENAME   上传到服务器的图片url
	 * @param PhotoName  生成的图片id，用来保存在服务器端
	 * @return  int -1 失败  1 成功
	 * @throws Exception
	 */
	
	public static int PostPhoto(File photo,String PhotoName) throws Exception {
		System.out.println("in post photo method");
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

//		File file = new File(Environment.getExternalStorageDirectory(),
//				FILENAME);
		HttpPost httppost = new HttpPost(
				"http://192.168.202.236:8080/BeautyRankingServer/UploadPhotoHttpclient"
						+ "?PhotoName=" + PhotoName);

		FileEntity reqEntity = new FileEntity(photo, "binary/octet-stream");

		httppost.setEntity(reqEntity);
		reqEntity.setContentType("binary/octet-stream");
		System.out.println("executing request " + httppost.getRequestLine());
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();

		System.out.println(response.getStatusLine());
		if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
			return 1;//上传成功
		else return -1;//上传失败
//		if (resEntity != null) {
//			System.out.println(EntityUtils.toString(resEntity));
//		}
//		if (resEntity != null) {
//			resEntity.consumeContent();
//		}
//
//		httpclient.getConnectionManager().shutdown();
//		if (resEntity != null)
//			return "上传成功！";
//		else
//			return "上传失败！";
	}
}