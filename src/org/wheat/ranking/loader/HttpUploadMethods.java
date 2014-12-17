package org.wheat.ranking.loader;

import java.io.ByteArrayInputStream;
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
import org.wheat.ranking.entity.BeautyIntroduction;
import org.wheat.ranking.entity.BeautyIntroductionList;
import org.wheat.ranking.entity.Comment;
import org.wheat.ranking.entity.ConstantValue;
import org.wheat.ranking.entity.Photo;
import org.wheat.ranking.entity.Praise;
import org.wheat.ranking.entity.json.BeautyIntroductionListJson;
import org.wheat.ranking.entity.json.CommentJson;
import org.wheat.ranking.httptools.HttpConnectTools;
public class HttpUploadMethods {

	/**
	 * @author hogachen
	 * @description  ����ȫ�µ�beauty�����ϴ�һ����Ƭֻ��url��ͬ����
	 * 
	 * @param originFile  ԭͼƬ�ļ�
	 * @param originFileName ԭͼƬ����
	 * @param thumbnail  ����ͼ�ļ�
	 * @param thumbnailName  ����ͼ����
	 * @param beautyInfo  beauty��Ϣ ,��map��ʽ����
	 * @param url �����ַ  HttpRoot+UploadBeautyInfo
	 */
	public static int  UploadBeautyInfoGet(File originFile,String originFileName,
			File thumbnail,String thumbnailName,
			HashMap<String,String> beautyInfo){
		int statusCode=-1;//����״̬�� �����в����ɹ�ʱΪ����Ϊ�ɹ�
		int uploadOriginCode=-1;//�ϴ�ԭ����Ƭ�Ƿ�ɹ�
		int uploadThumbCode=-1;//�ϴ�����ͼ�Ƿ�ɹ�
		try {
			uploadOriginCode=uploadPhoto(originFile, originFileName);
			uploadThumbCode=uploadPhoto(thumbnail, thumbnailName);
			if(uploadOriginCode==ConstantValue.operateSuccess
					&&uploadThumbCode==ConstantValue.operateSuccess){//ֻ�����ϴ��ɹ�����ͼƬ��ʱ��Ų�����Ϣ�����ݿ�
				statusCode=HttpConnectTools.getReturnCode
						(ConstantValue.HttpRoot+"UploadBeautyInfo", beautyInfo,null);	
				System.out.println("�ϴ�beauty��Ϣ�����룺"+statusCode);
			}
		} catch (Exception e) {	
			e.printStackTrace();
		}
		return statusCode;
	}
	public static int  UploadBeautyInfoPost(File originFile,String originFileName,
			File thumbnail,String thumbnailName,
			BeautyDetail beautyInfo){
		int statusCode=-1;//����״̬�� �����в����ɹ�ʱΪ����Ϊ�ɹ�
		int uploadOriginCode=-1;//�ϴ�ԭ����Ƭ�Ƿ�ɹ�
		int uploadThumbCode=-1;//�ϴ�����ͼ�Ƿ�ɹ�
		try {
			uploadOriginCode=uploadPhoto(originFile, originFileName);
			uploadThumbCode=uploadPhoto(thumbnail, thumbnailName);
			if(uploadOriginCode==ConstantValue.operateSuccess
					&&uploadThumbCode==ConstantValue.operateSuccess){//ֻ�����ϴ��ɹ�����ͼƬ��ʱ��Ų�����Ϣ�����ݿ�
				statusCode=HttpConnectTools.postJsonReturnCode
						(ConstantValue.HttpRoot+"UploadBeautyInfo", beautyInfo,null);	
				System.out.println("�ϴ�beauty��Ϣ�����룺"+statusCode);
			}
		} catch (Exception e) {	
			
			e.printStackTrace();
			/**
			 * �������쳣��ʱ����Ҫ����30�룬�ٲ�ѯ���ݿ⣬���Ƿ�
			 * ����ɹ����ǵĻ����ܽ��棬������߿ͻ����ϴ�ʧ��
			 * 
			 * ���ˣ���΢��һ����ֱ��˵����ʧ�ܾͺ���
			 */
			return ConstantValue.SocketTimeoutException;
		}
		System.out.println("statusCode "+statusCode);
		return statusCode;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * @description ������ķ���ֻ��url��һ��
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
		int statusCode=-1;//����״̬�� �����в����ɹ�ʱΪ1
		int uploadOriginCode=-1;//�ϴ�ԭ����Ƭ�Ƿ�ɹ�
		int uploadThumbCode=-1;//�ϴ�����ͼ�Ƿ�ɹ�
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
		int statusCode=-1;//����״̬�� �����в����ɹ�ʱΪ����Ϊ�ɹ�
		int uploadOriginCode=-1;//�ϴ�ԭ����Ƭ�Ƿ�ɹ�
		int uploadThumbCode=-1;//�ϴ�����ͼ�Ƿ�ɹ�
		try {
			uploadOriginCode=uploadPhoto(originFile, originFileName);
			uploadThumbCode=uploadPhoto(thumbnail, thumbnailName);
			if(uploadOriginCode==ConstantValue.operateSuccess
					&&uploadThumbCode==ConstantValue.operateSuccess){//ֻ�����ϴ��ɹ�����ͼƬ��ʱ��Ų�����Ϣ�����ݿ�
				statusCode=HttpConnectTools.postJsonReturnCode
						(ConstantValue.HttpRoot+"UploadOneBeautyPhoto", photo,null);	
				System.out.println("�ϴ�photo��Ϣ�����룺"+statusCode);
			}
		} catch (Exception e) {	
			e.printStackTrace();
		}
		return statusCode;
	}
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param praise  һ���޵���Ϣ
	 * @param beautyId  ����Ҫ����beauty�ܵ�������������Ҫ����beautyId
	 * @throws Exception
	 */
	public static void UploadPraiseGet(Praise praise,String beautyId)throws Exception{
		HashMap<String  ,String>values= new HashMap<String,String>();
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
	 * @deprecated upload praise using the get method
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
	//������һЩ�ϴ��ļ�ͨ�õķ�����������ֱ�Ӷ�Ӧ���������˵�servlet����
	
	/**
	 * 
	 * @param FILENAME   �ϴ�����������ͼƬurl
	 * @param PhotoName  ���ɵ�ͼƬid�����������ڷ�������
	 * @return  int �Զ���ɹ�����״̬��
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
		//���ﲻ���Զ����statusCode����Ϊ�ϴ�ʧ�ܵ�ԭ��ֻܶ࣬�е�ϵͳ����������Ϣʱ����Ϊ�ϴ��ɹ�
		if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
			return ConstantValue.operateSuccess;//�ϴ��ɹ�
		else return ConstantValue.uploadPhotoFailed;//�ϴ�ʧ��
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
	
	/**
	 * 
	* @Description: TODO  �����ڷ���socketTimeOut֮�󣬲�ѯ���ݿ��Ƿ�������ݳɹ�
	* @author hogachen   
	* @date 2014��12��14�� ����8:43:21 
	* @version V1.0  
	* @param data
	* @return
	 */
	public static int TestIfUploadSuccess(HashMap<String ,String>data){
		int code =-1;
		String url = "";
		try {
			code = HttpConnectTools.getReturnCode(url, data, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ConstantValue.ReUploadFailed;
		}
		return code;
		
	}
}
