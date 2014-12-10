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
		int statusCode=-1;//����״̬�� �����в����ɹ�ʱΪ1
		int uploadOriginCode=-1;//�ϴ�ԭ����Ƭ�Ƿ�ɹ�
		int uploadThumbCode=-1;//�ϴ�����ͼ�Ƿ�ɹ�
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
	//������һЩ�ϴ��ļ�ͨ�õķ�����������ֱ�Ӷ�Ӧ���������˵�servlet����
	
	/**
	 * 
	 * @param FILENAME   �ϴ�����������ͼƬurl
	 * @param PhotoName  ���ɵ�ͼƬid�����������ڷ�������
	 * @return  int -1 ʧ��  1 �ɹ�
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
			return 1;//�ϴ��ɹ�
		else return -1;//�ϴ�ʧ��
//		if (resEntity != null) {
//			System.out.println(EntityUtils.toString(resEntity));
//		}
//		if (resEntity != null) {
//			resEntity.consumeContent();
//		}
//
//		httpclient.getConnectionManager().shutdown();
//		if (resEntity != null)
//			return "�ϴ��ɹ���";
//		else
//			return "�ϴ�ʧ�ܣ�";
	}
	
}
