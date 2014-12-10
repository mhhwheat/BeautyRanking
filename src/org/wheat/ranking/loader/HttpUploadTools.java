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
	 * @deprecated  ����ȫ�µ�beauty�����ϴ�һ����Ƭֻ��url��ͬ����
	 * 
	 * @param originFile  ԭͼƬ�ļ�
	 * @param originFileName ԭͼƬ����
	 * @param thumbnail  ����ͼ�ļ�
	 * @param thumbnailName  ����ͼ����
	 * @param beautyInfo  beauty��Ϣ ,��map��ʽ����
	 * @param url �����ַ  HttpRoot+UploadBeautyInfo
	 */
	public static void UploadBeautyInfo(File originFile,String originFileName,
			File thumbnail,String thumbnailName,
			HashMap<String,String>beautyInfo){
		int statusCode=-1;//����״̬�� �����в����ɹ�ʱΪ1
		int uploadOriginCode=-1;//�ϴ�ԭ����Ƭ�Ƿ�ɹ�
		int uploadThumbCode=-1;//�ϴ�����ͼ�Ƿ�ɹ�
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
		int statusCode=-1;//����״̬�� �����в����ɹ�ʱΪ1
		int uploadOriginCode=-1;//�ϴ�ԭ����Ƭ�Ƿ�ɹ�
		int uploadThumbCode=-1;//�ϴ�����ͼ�Ƿ�ɹ�
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
	 * @param praise  һ���޵���Ϣ
	 * @param beautyId  ����Ҫ����beauty�ܵ�������������Ҫ����beautyId
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
