package org.wheat.ranking.entity.json;

import java.io.Serializable;

import org.wheat.ranking.entity.ErrMsg;
import org.wheat.ranking.httptools.JsonTools;
import org.wheat.ranking.loader.LoginAndRegister;

import com.google.gson.annotations.SerializedName;
/**
 * @author wheat
 * @description 所有请求，最终都要返回一个对象
 * 分为以下几种情况：
 * 1.请求链接失败，返回http自带的状态码
 * 2.请求链接成功
 * 	A: 请求数据成功，返回相应对象（对于上传数据的请求，也要返回一个成功的对象ReturnMessage，设置成功代码）
 *  B： 请求数据失败，返回相应对象，同时设置错误代码和信息（对于上传数据的请求，也要返回一个失败的对象ReturnMessage，设置失败代码）
 * @Date 14-9-14
 * @Time 下午21:29
 * @extend 只当作是没有继承这个jsonBase接口
 */
public class JsonBaseImpl <T> implements JsonBase<T>,Serializable
{

	  	@SerializedName("c")
	  	private int mCode = -1;

	  	@SerializedName("d")
	  	private T mData;

	  	@SerializedName("err")
	  	private ErrMsg mErrMsg;

	  	@Override
	  	public int getCode() {
	        return mCode;
	    }

	    @Override
	    public void setCode(int code) {
	        mCode = code;
	    }

	    @Override
	    public T getData() {
	        return mData;
	    }

	    @Override
	    public void setData(T data) {
	        mData = data;
	    }

	    public ErrMsg getErrMsg() {
	        return mErrMsg;
	    }

	    public void setErrMsg(ErrMsg errMsg) {
	        mErrMsg = errMsg;
	    }

	    @Override
	    public String getMsg() {
	        if (mErrMsg != null) {
	            return mErrMsg.getMsg();
	        }
	        return "";
	    }

	    @Override
	    public void setMsg(String msg) {
	        if (mErrMsg != null) {
	            mErrMsg.setMsg(msg);
	        }
	    }

	    @Override
	    public String toCacheString() 
	    {
	    	return JsonTools.toJson(this);
	    }
}
