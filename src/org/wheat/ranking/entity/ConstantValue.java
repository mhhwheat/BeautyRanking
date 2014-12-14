package org.wheat.ranking.entity;

public class ConstantValue {
	public static String HttpRoot="http://192.168.202.197:8080/BeautyRankingServer/";
	public static int ClientParameterErr=1001;//客户端数据请求错误
	public static int ServerDataNotGet=1000;//服务器数据库出错
	public static int InsertDbFailed=1002;//数据库插入失败
	public static int InsertDbSuccess=1003;
	public static int uploadPhotoFailed=1004;
	public static int uploadPhotoSuccess=1005;
	public static int updateCommentCountSuccess=1008;
	public static int updateCommentCountFailed=1009;
	public static int updateFailed=1006;
	public static int updateSuccess=1007;
	public static int getDataFailed=1010;
	public static int deletePhotoSuccess=1011;
	public static int deletePhotoFailed=1012;
}
