package org.wheat.ranking.entity.json;

import java.io.Serializable;

import org.wheat.ranking.entity.ErrMsg;
import org.wheat.ranking.loader.HttpDataLoader;

import com.google.gson.annotations.SerializedName;
/**
 * @author wheat
 * @Date 14-9-14
 * @Time ÏÂÎç21:29
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
	    	return HttpDataLoader.toJson(this);
	    }
}
