package org.wheat.ranking.loader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;

import org.wheat.ranking.httptools.UploadPhotoHttpclientClient;
import org.wheat.ranking.entity.BeautyIntroduction;
import org.wheat.ranking.entity.BeautyIntroductionList;
import org.wheat.ranking.entity.Comment;
import org.wheat.ranking.entity.ConstantValue;
import org.wheat.ranking.entity.Praise;
import org.wheat.ranking.entity.json.BeautyIntroductionListJson;
import org.wheat.ranking.entity.json.CommentJson;
public class HttpUploadTools {

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
			uploadOriginCode=UploadPhotoHttpclientClient.PostPhoto(originFile, originFileName);
			uploadThumbCode=UploadPhotoHttpclientClient.PostPhoto(thumbnail, thumbnailName);
			if(uploadOriginCode==1&&uploadThumbCode==1){
				if(HttpLoader.getData(ConstantValue.HttpRoot+"UploadBeautyInfo", beautyInfo,null)!=null)
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
			uploadOriginCode=UploadPhotoHttpclientClient.PostPhoto(originFile, originFileName);
			uploadThumbCode=UploadPhotoHttpclientClient.PostPhoto(thumbnail, thumbnailName);
			if(uploadOriginCode==1&&uploadThumbCode==1){
				if(HttpLoader.getData(ConstantValue.HttpRoot+"UploadOneBeautyPhoto", photo,null)!=null)
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
		HttpLoader.getData(ConstantValue.HttpRoot+"UploadPraise", values,null);
	}

	public static void UploadOneComment(Comment comment)throws Exception{
		HashMap<String  ,String>commentValues= new HashMap<String,String>();
		commentValues.put("photoId",comment.getPhotoID()+"");
		commentValues.put("userPhoneNumber",comment.getUserPhoneNumber()+"");
		commentValues.put("commentTime",comment.getCommentTime());
		commentValues.put("commentContent",comment.getCommentContent());
		HttpLoader.getData(ConstantValue.HttpRoot+"UploadOneComment", commentValues,null);
	}
	
	
}
