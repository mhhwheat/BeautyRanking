package org.wheat.ranking.loader;

import java.io.ByteArrayInputStream;
import java.io.File;
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
import org.wheat.ranking.entity.BeautyIntroduction;
import org.wheat.ranking.entity.BeautyIntroductionList;
import org.wheat.ranking.entity.Comment;
import org.wheat.ranking.entity.ConstantValue;
import org.wheat.ranking.entity.Praise;
import org.wheat.ranking.entity.json.BeautyIntroductionListJson;
import org.wheat.ranking.entity.json.CommentJson;
import org.wheat.ranking.httptools.HttpConnectTools;
public class HttpUploadMethods {

	/**
	 * @author hogachen
	 * @deprecated  创建全新的beauty，跟上传一张照片只有url不同而已
	 * 
	 * @param originFile  原图片文件
	 * @param originFileName 原图片名字
	 * @param thumbnail  缩略图文件
	 * @param thumbnailName  缩略图名字
	 * @param beautyInfo  beauty信息 ,以map形式传送
	 * @param url 请求地址  HttpRoot+UploadBeautyInfo
	 */
	public static void UploadBeautyInfo(File originFile,String originFileName,
			File thumbnail,String thumbnailName,
			HashMap<String,String>beautyInfo){
		int statusCode=-1;//操作状态码 当所有操作成功时为1
		int uploadOriginCode=-1;//上传原有照片是否成功
		int uploadThumbCode=-1;//上传缩略图是否成功
		try {
			uploadOriginCode=uploadPhoto(originFile, originFileName);
			uploadThumbCode=uploadPhoto(thumbnail, thumbnailName);
			if(uploadOriginCode==1&&uploadThumbCode==1){
				if(HttpConnectTools.getReturnJsonData(ConstantValue.HttpRoot+"UploadBeautyInfo", beautyInfo,null)!=null)
					statusCode=1;			
			}
		} catch (Exception e) {	
			e.printStackTrace();
		}
	}
	
	public static void UploadOneBeautyPhoto(File originFile,String originFileName,
			File thumbnail,String thumbnailName,
			HashMap<String,String>photo){
		int statusCode=-1;//操作状态码 当所有操作成功时为1
		int uploadOriginCode=-1;//上传原有照片是否成功
		int uploadThumbCode=-1;//上传缩略图是否成功
		try {
			uploadOriginCode=uploadPhoto(originFile, originFileName);
			uploadThumbCode=uploadPhoto(thumbnail, thumbnailName);
			if(uploadOriginCode==1&&uploadThumbCode==1){
				if(HttpConnectTools.getReturnJsonData(ConstantValue.HttpRoot+"UploadOneBeautyPhoto", photo,null)!=null)
					statusCode=1;			
			}
		} catch (Exception e) {	
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param praise  一个赞的信息
	 * @param beautyId  由于要更新beauty总的赞数，所以需要传入beautyId
	 * @throws Exception
	 */
	public static void UploadPraise(Praise praise,String beautyId)throws Exception{
		HashMap<String  ,String>values= new HashMap<String,String>();
		values.put("photoId",praise.getPhotoId()+"");
		values.put("userPhoneNumber",praise.getUserPhoneNumber()+"");
		values.put("praiseTime",praise.getPraiseTime());
		values.put("beautyId",beautyId);
		System.out.println("in upload praise");
		HttpConnectTools.getReturnJsonData(ConstantValue.HttpRoot+"UploadPraise", values,null);
	}

	public static void UploadOneComment(Comment comment)throws Exception{
		HashMap<String  ,String>commentValues= new HashMap<String,String>();
		commentValues.put("photoId",comment.getPhotoID()+"");
		commentValues.put("userPhoneNumber",comment.getUserPhoneNumber()+"");
		commentValues.put("commentTime",comment.getCommentTime());
		commentValues.put("commentContent",comment.getCommentContent());
		HttpConnectTools.getReturnJsonData(ConstantValue.HttpRoot+"UploadOneComment", commentValues,null);
	}
	
	
	/******************************************************************/
	//以下是一些上传文件通用的方法，上面是直接对应到服务器端的servlet方法
	
	/**
	 * 
	 * @param FILENAME   上传到服务器的图片url
	 * @param PhotoName  生成的图片id，用来保存在服务器端
	 * @return  int -1 失败  1 成功
	 * @throws Exception
	 */
	
	public static int uploadPhoto(File photo,String PhotoName) throws Exception {
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
