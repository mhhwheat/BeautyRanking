package org.wheat.ranking.entity.json;

/**
 * @author wheat
 * @Date 14-9-14
 * @Time обнГ21:26
 */
public interface JsonBase <T>
{
//  @SerializedName("c")
//  private int mCode = -1;

  //    @SerializedName("d")
//  private T mData;

  //    @SerializedName("msg")
//  private String mMsg;

//  public T getData() {
//      return mData;
//  }

  public T getData();

  public void setData(T data);

  public int getCode();

  public void setCode(int code);

  public String getMsg();

  public void setMsg(String msg);

  public String toCacheString();
}
