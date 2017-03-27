/**
 * 
 */
package com.shengliedu.teacher.teacher.chat.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.shengliedu.teacher.teacher.R;
import com.shengliedu.teacher.teacher.chat.view.D3View;

/**
 * @author MZH
 *
 */
public class MapActivity extends BaseActivity {
	@D3View(click="onClick") ImageView leftBtn;
	MapView mMapView = null;  
	BaiduMap mBaiduMap ;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		SDKInitializer.initialize(getApplicationContext());  
		setContentView(R.layout.acti_map);
//		double lat = 39.960742;
//		double lon = 116.706243;
		double lat = getIntent().getDoubleExtra("lat", 0.0);
		double lon = getIntent().getDoubleExtra("lon", 0.0);
		
		mMapView = (MapView) findViewById(R.id.bmapView);  
		mBaiduMap =  mMapView.getMap();
		mMapView.showZoomControls(false);
		mBaiduMap.setMyLocationEnabled(true);  
		System.out.println("rttttttt,"+lat+","+lon);
		MyLocationData locData = new MyLocationData.Builder()  
		    .accuracy((float)40.0)  
		    .direction(100).latitude(lat)  
		    .longitude(lon).build();  
		mBaiduMap.setMyLocationData(locData);  
		LatLng ll = new LatLng(lat,lon);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
		mBaiduMap.animateMapStatus(u);
	}
	
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.leftBtn:
			finish();
			break;
		}
	}
	
	 @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        mMapView.onDestroy();  
    }  
    @Override  
    protected void onResume() {  
        super.onResume();  
        mMapView.onResume();  
        }  
    @Override  
    protected void onPause() {  
        super.onPause();  
        mMapView.onPause();  
        }  
}
