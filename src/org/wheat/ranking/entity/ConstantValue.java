package org.wheat.ranking.entity;
/**
 * 
* @ClassName: ConstantValue 
* @Description: TODO 
获取数据失败三种：

1.数据库操作出错
2.数据库不存在数据
3.链接失败

上传/更新数据失败两种
1.数据库操作出错
2.链接失败
* @author hogachen
* @date 2014年12月12日 下午6:30:01 
*
 */
public class ConstantValue {
	public static String HttpRoot="http://192.168.235.107:8080/BeautyRankingServer/";
	/**
	 * 失败的人生有千百种，成功的人生只有一种（更新失败）
	 */
	//失败状态
	public static int ClientParameterErr=1001;//客户端数据请求错误

	public static int InsertDbFailed=1002;//数据库插入失败

	public static int uploadPhotoFailed=1004;

	public static int updateCommentCountFailed=1009;
	
	public static int dataNotInDBCannotUpdate=1006;

	public static int getDataFailed=1010;

	public static int deletePhotoFailed=1012;
	public static int deleteBeautyFailed=1014;
	//成功状态
	public static int operateSuccess=1000;
	
	
	
	/**
	 * 获取数据失败就这两种
	 */
	//失败状态
	public static int DBException=1013;//数据库执行SQL语句出项错误
	
	public static int DataNotFoundInDB=1014;//数据库没有相关数据
}
