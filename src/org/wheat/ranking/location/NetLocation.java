package org.wheat.ranking.location;

import org.wheat.beautyranking.R;
import org.wheat.ranking.testActivity.PicCutDemoActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

public class NetLocation extends Activity implements AMapLocationListener {

	private LocationManagerProxy mLocationManagerProxy;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化
		init();
	}
	
	private void init() {
		// 初始化定位，只采用网络定位
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.setGpsEnable(false);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用destroy()方法
		// 其中如果间隔时间为-1，则定位只定一次,
		//在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, -1, 15, this);
		 
		
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if (amapLocation!=null&&amapLocation.getAMapException().getErrorCode() == 0) {
			// 定位成功回调信息，设置相关消息
			Intent intent = new Intent();   
			intent = intent.setClass(NetLocation.this, PicCutDemoActivity.class);   
			Bundle bundle = new Bundle();   
			bundle.putDouble("lat", amapLocation.getLatitude());
			bundle.putDouble("lng", amapLocation.getLongitude());
			bundle.putString("message", amapLocation.getAddress());
			intent.putExtras(bundle);       
			setResult(RESULT_OK, intent);   //RESULT_OK是返回状态码   
			NetLocation.this.finish(); //会触发onDestroy(); 	
		}
	}
}
