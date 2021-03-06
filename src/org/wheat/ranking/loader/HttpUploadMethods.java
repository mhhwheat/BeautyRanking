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
import org.wheat.ranking.entity.Comment;
import org.wheat.ranking.entity.ConstantValue;
import org.wheat.ranking.entity.Photo;
import org.wheat.ranking.entity.Praise;
import org.wheat.ranking.httptools.HttpConnectTools;
public class HttpUploadMethods {

	/**
	 * @author hogachen
	 * @description  创建全新的beauty，跟上传一张照片只有url不同而已
	 * 
	 * @param originFile  原图片文件
	 * @param originFileName 原图片名字
	 * @param thumbnail  缩略图文件
	 * @param thumbnailName  缩略图名字
	 * @param beautyInfo  beauty信息 ,以map形式传送
	 * @param url 请求地址  HttpRoot+UploadBeautyInfo
	 */
	public static int  UploadBeautyInfoGet(File originFile,String originFileName,
			File thumbnail,String thumbnailName,
			HashMap<String,String> beautyInfo){
		int statusCode=-1;//操作状态码 当所有操作成功时为才认为成功
		int uploadOriginCode=-1;//上传原有照片是否成功
		int uploadThumbCode=-1;//上传缩略图是否成功
		try {
			uploadOriginCode=uploadPhoto(originFile, originFileName);
			uploadThumbCode=uploadPhoto(thumbnail, thumbnailName);
			if(uploadOriginCode==ConstantValue.operateSuccess
					&&uploadThumbCode==ConstantValue.operateSuccess){//只有在上传成功两张图片的时候才插入信息到数据库
				statusCode=HttpConnectTools.getReturnCode
						(ConstantValue.HttpRoot+"UploadBeautyInfo", beautyInfo,null);	
				System.out.println("上传beauty信息返回码："+statusCode);
			}
		} catch (Exception e) {	
			e.printStackTrace();
		}
		return statusCode;
	}
	public static int  UploadBeautyInfoPost(File originFile,String originFileName,
			File thumbnail,String thumbnailName,
			BeautyDetail beautyInfo){
		int statusCode=-1;//操作状态码 当所有操作成功时为才认为成功
		int uploadOriginCode=-1;//上传原有照片是否成功
		int uploadThumbCode=-1;//上传缩略图是否成功
		try {
			uploadOriginCode=uploadPhoto(originFile, originFileName);
			uploadThumbCode=uploadPhoto(thumbnail, thumbnailName);
			if(uploadOriginCode==ConstantValue.operateSuccess
					&&uploadThumbCode==ConstantValue.operateSuccess){//只有在上传成功两张图片的时候才插入信息到数据库
				statusCode=HttpConnectTools.postJsonReturnCode
						(ConstantValue.HttpRoot+"UploadBeautyInfo", beautyInfo,null);	
				System.out.println("上传beauty信息返回码："+statusCode);
			}
		} catch (Exception e) {	
			
			e.printStackTrace();
			/**
			 * 当捕获到异常的时候，需要休眠30秒，再查询数据库，看是否
			 * 插入成功，是的话不管界面，否则告诉客户端上传失败
			 * 
			 * 算了，跟微信一样，直接说发送失败就好了
			 */
			return ConstantValue.SocketTimeoutException;
		}
		System.out.println("statusCode "+statusCode);
		return statusCode;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * @description 与上面的方法只有url不一样
	 * @param originFile
	 * @param originFileName
	 * @param thumbnail
	 * @param thumbnailName
	 * @param photo
	 * @return
	 */
	public static int UploadOneBeautyPhoto(File originFile,String originFileName,
			File thumbnail,String thumbnailName,
			HashMap<String,String>photo){
		int statusCode=-1;//操作状态码 当所有操作成功时为1
		int uploadOriginCode=-1;//上传原有照片是否成功
		int uploadThumbCode=-1;//上传缩略图是否成功
		try {
			uploadOriginCode=uploadPhoto(originFile, originFileName);
			uploadThumbCode=uploadPhoto(thumbnail, thumbnailName);
			if(uploadOriginCode==ConstantValue.operateSuccess
					&&uploadThumbCode==ConstantValue.operateSuccess){
				statusCode=HttpConnectTools.getReturnCode(ConstantValue.HttpRoot+"UploadOneBeautyPhoto", photo,null);
				
			}
		} catch (Exception e) {	
			e.printStackTrace();
		}
		return statusCode;
	}
	
	public static int  UploadOneBeautyPhotoPost(File originFile,String originFileName,
			File thumbnail,String thumbnailName,
			Photo photo){
		int statusCode=-1;//操作状态码 当所有操作成功时为才认为成功
		int uploadOriginCode=-1;//上传原有照片是否成功
		int uploadThumbCode=-1;//上传缩略图是否成功
		try {
			uploadOriginCode=uploadPhoto(originFile, originFileName);
			uploadThumbCode=uploadPhoto(thumbnail, thumbnailName);
			if(uploadOriginCode==ConstantValue.operateSuccess
					&&uploadThumbCode==ConstantValue.operateSuccess){//只有在上传成功两张图片的时候才插入信息到数据库
				statusCode=HttpConnectTools.postJsonReturnCode
						(ConstantValue.HttpRoot+"UploadOneBeautyPhoto", photo,null);	
				System.out.println("上传photo信息返回码："+statusCode);
			}
		} catch (Exception e) {	
			e.printStackTrace();
		}
		return statusCode;
	}
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param praise  一个赞的信息
	 * @param beautyId  由于要更新beauty总的赞数，所以需要传入beautyId
	 * @throws Exception
	 */
	public static void UploadPraiseGet(Praise praise,String beautyId)throws Exception{
		HashMap<String  ,String> values= new HashMap<String,String>();
		values.put("photoId",praise.getPhotoId()+"");
		values.put("userPhoneNumber",praise.getUserPhoneNumber()+"");
		values.put("praiseTime",praise.getPraiseTime().toGMTString());
		values.put("beautyId",beautyId);
		System.out.println("in upload praise");
		HttpConnectTools.postJsonReturnCode(ConstantValue.HttpRoot+"UploadPraise", values,null);
	}
	public static int UploadPraisePost(Praise praise)throws Exception{
		return HttpConnectTools.postJsonReturnCode(ConstantValue.HttpRoot+"UploadPraise", praise,null);
	}
	
	

	
	
	
	
	
	
	
	/**
	 * @author hogachen
	 * @description upload praise using the get method
	 * @param comment
	 * @throws Exception
	 */
	public static void UploadOneCommentGet(Comment comment)throws Exception{
		HashMap<String  ,String>commentValues= new HashMap<String,String>();
		commentValues.put("photoId",comment.getPhotoID()+"");
		commentValues.put("userPhoneNumber",comment.getUserPhoneNumber()+"");
		commentValues.put("commentTime",comment.getCommentTime().toString());
		commentValues.put("commentContent",comment.getCommentContent());
		HttpConnectTools.getReturnJsonData(ConstantValue.HttpRoot+"UploadOneComment", commentValues,null);
	}
	public static int UploadOneCommentPost(Comment comment)throws Exception{
		return HttpConnectTools.postJsonReturnCode(ConstantValue.HttpRoot+"UploadOneComment", comment,null);

	}
	
	
	
	
	
	
	
	
	
	
	
	
	/******************************************************************/
	//以下是一些上传文件通用的方法，上面是直接对应到服务器端的servlet方法
	
	/**
	 * 
	 * @param FILENAME   上传到服务器的图片url
	 * @param PhotoName  生成的图片id，用来保存在服务器端
	 * @return  int 自定义成功与否的状态码
	 * @throws Exception
	 */
	
	public static int uploadPhoto(File photo,String PhotoName) throws Exception {
		System.out.println("in post photo method");
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

		HttpPost httppost = new HttpPost(ConstantValue.HttpRoot+"UploadPhotoHttpclient"
						+ "?PhotoName=" + PhotoName);

		FileEntity reqEntity = new FileEntity(photo, "binary/octet-stream");

		httppost.setEntity(reqEntity);
		reqEntity.setContentType("binary/octet-stream");
		System.out.println("executing request " + httppost.getRequestLine());
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();

		System.out.println(response.getStatusLine());
		//这里不用自定义的statusCode，因为上传失败的原因很多，只有当系统返回正常信息时才认为上传成功
		if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
			return ConstantValue.operateSuccess;//上传成功
		else return ConstantValue.uploadPhotoFailed;//上传失败
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
	
	/**
	 * 
	* @Description: TODO  测试是否数据库可链接
	* @author hogachen   
	* @date 2014年12月14日 下午8:43:21 
	* @version V1.0  
	* @param data
	* @return
	 */
	public static int TestIfUploadSuccess(){
		int code =-1;
		String url = ConstantValue.HttpRoot+"IfServerOk";
		try {
			code = HttpConnectTools.getReturnCodeShortTime(url, null, null, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ConstantValue.ReUploadFailed;
		}
		return code;
		
	}
	
	
	public static int sendFeedbackMsg(String feedbackMsg,String userPhoneNumber){
		String url = ConstantValue.HttpRoot+""
				+ "sendFeedbackMsg?FeedbackMsg="+feedbackMsg+"&userPhoneNumber="+userPhoneNumber;
		int code=-1;
		try {
			code  = HttpConnectTools.getReturnCode(url, null, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return code;
	}
}
