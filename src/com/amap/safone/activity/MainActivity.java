package com.amap.safone.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.safone.R;
import com.amap.safone.util.MyLog;
import com.amap.safone.util.TTSController;
import com.amap.safone.util.Utils;
import com.autonavi.tbt.TrafficFacilityInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 创建时间：15/12/7 18:11
 * 项目名称：newNaviDemo
 *
 * @author lingxiang.wang
 * @email lingxiang.wang@alibaba-inc.com
 * 类说明：
 */

public class MainActivity extends Activity implements LocationSource,
	AMapLocationListener,AMapNaviListener {

    NaviLatLng endLatlng = new NaviLatLng(39.955846, 116.352765);
    NaviLatLng startLatlng = new NaviLatLng(39.925041, 116.437901);
    List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    private MapView mapView;
    private AMap amap;
    private AMapNavi aMapNavi;
    private HashMap<Integer, RouteOverLay> routeOverlays = new HashMap<Integer, RouteOverLay>();
    private int routeIndex;
    private int[] routeIds;
    private TTSController ttsManager;
    private boolean chooseRouteSuccess;
    private boolean mapClickStartReady;
    private boolean mapClickEndReady;
    private Marker mStartMarker;
    private Marker mWayMarker;
    private Marker mEndMarker;
    private boolean calculateSuccess;
    private double mMyLon;
	private double mMyLat;
	private String mMyAddress = "";
	private String mMyType = "";
	private int mMyState=0;	//-1: FAIL; 0: NOT FINISH; 1: SUCCESS
	private Marker mMyMarker;
	
	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;
	Context context;
	AMapNaviPath path;
    RouteOverLay routeOverLay = null;   
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_route_layout);
        context = getApplicationContext();
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initLocation();        
        
        amap = mapView.getMap();
  /*      
    //    amap.setOnMapLoadedListener(this);
        amap.setLocationSource(this);
        amap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        amap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);//设置定位类型
   //     amap.setOnInfoWindowClickListener(mInfoWindowClickListener);
        amap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		amap.getUiSettings().setZoomControlsEnabled(false);		
	//	amap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);	// 设置定位的类型为 跟随模式,定位、移动到地图中心点并跟随		
	//	amap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);// 设置定位的类型为根据地图面向方向旋转,定位、移动到地图中心点，跟踪并根据面向方向旋转地图
*/
		
/*	// 自定义系统定位蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		// 自定义定位蓝点图标
		 myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
		        fromResource(R.drawable.location_marker));
		// 自定义精度范围的圆形边框颜色
		 myLocationStyle.strokeColor(0xffff0000);//Color.BLACK
		//自定义精度范围的圆形边框宽度
		myLocationStyle.strokeWidth(5);
		// 将自定义的 myLocationStyle 对象添加到地图上
		amap.setMyLocationStyle(myLocationStyle);
		// 构造 LocationManagerProxy 对象
	//	mAMapLocationManager = LocationManagerProxy.getInstance(LocationSourceActivity.this);
		//设置定位资源。如果不设置此定位资源则定位按钮不可点击。
		amap.setLocationSource(this);
		//设置默认定位按钮是否显示
		amap.getUiSettings().setMyLocationButtonEnabled(true);
		// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		amap.setMyLocationEnabled(true);
		*/
		
        aMapNavi = AMapNavi.getInstance(getApplicationContext());
        aMapNavi.addAMapNaviListener(this);
        aMapNavi.startGPS();

        ttsManager = TTSController.getInstance(getApplicationContext());
        ttsManager.init();
        ttsManager.startSpeaking();

        // 初始化Marker添加到地图
        mStartMarker = amap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.start))));
        mWayMarker = amap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.way))));
        mEndMarker = amap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.end))));


        amap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                for (RouteOverLay routeOverlay : routeOverlays.values()
                        ) {
                    routeOverlay.removeFromMap();
                }


                if (mapClickStartReady) {
                    startLatlng = new NaviLatLng(latLng.latitude, latLng.longitude);
                    mStartMarker.setPosition(latLng);
                  //  startList.clear();
                  //  startList.add(startLatlng);
                }


                if (mapClickEndReady) {
                    endLatlng = new NaviLatLng(latLng.latitude, latLng.longitude);
                    mEndMarker.setPosition(latLng);
                  // endList.clear();
                  //  endList.add(endLatlng);
                    MyLog.i("hu", "click end!**********");
                    Utils.setDoubleValue("endlat", latLng.latitude, context);
                    Utils.setDoubleValue("endlng", latLng.longitude, context);
                    mStartMarker.remove();
                    mEndMarker.remove();
                    calculateRoute(null);
                }

                mapClickEndReady = false;
                mapClickStartReady = false;
            }
        });

        locationDialog();
    }
    
    private void initLocation(){
    	locationClient = new AMapLocationClient(this.getApplicationContext());
		locationOption = new AMapLocationClientOption();
		// 设置定位模式,Hight_Accuracy为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		locationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		//设置是否返回地址信息（默认返回地址信息）
		locationOption.setNeedAddress(true);
		//设置是否只定位一次,默认为false
		locationOption.setOnceLocation(true);
		//设置是否强制刷新WIFI，默认为强制刷新
		locationOption.setWifiActiveScan(true);
		//设置是否允许模拟位置,默认为false，不允许模拟位置
		locationOption.setMockEnable(false);
		//设置定位间隔,单位毫秒,默认为2000ms
		locationOption.setInterval(10000);
		locationClient.setLocationOption(locationOption);
		
		// 设置定位监听
		locationClient.setLocationListener(this);
		
		
		// 启动定位
		locationClient.startLocation();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        aMapNavi.stopNavi();
        ttsManager.destroy();
        locationClient.onDestroy();
        aMapNavi.destroy();        
    }

    public void calculateRoute(View view) {
    	MyLog.i("hu", "*44***********calculateRoute()");
      //  startList.add(startLatlng);
     //   endList.add(endLatlng);
    //    aMapNavi.calculateDriveRoute(startList, endList, null, PathPlanningStrategy.DRIVING_MULTIPLE_ROUTES);
    	MyLog.i("hu"," startLatlng.getLatitude()="+startLatlng.getLatitude()+" startLatlng.getLongitude()="+startLatlng.getLongitude()+
    			" endLatlng.getLatitude()="+endLatlng.getLatitude()+" endLatlng.getLongitude()="+endLatlng.getLongitude());			
    	aMapNavi.calculateWalkRoute(startLatlng, endLatlng);       
    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] routeIds) {

        //当且仅当，使用策略AMapNavi.DrivingMultipleRoutes时回调
        //单路径算路依然回调onCalculateRouteSuccess，不回调这个

    	MyLog.i("hu", "*44************onCalculateMultipleRoutesSuccess() routeIds.length="+routeIds.length);
        //你会获取路径ID数组
        this.routeIds = routeIds;
        for (int i = 0; i < routeIds.length; i++) {
            //你可以通过对应的路径ID获得一条道路路径AMapNaviPath
            AMapNaviPath path = (aMapNavi.getNaviPaths()).get(routeIds[i]);

            //你可以通过这个AMapNaviPath生成一个RouteOverLay用于加在地图上
            RouteOverLay routeOverLay = new RouteOverLay(amap, path, this);
            routeOverLay.setTrafficLine(true);
            routeOverLay.addToMap();

            routeOverlays.put(routeIds[i], routeOverLay);
        }

        routeOverlays.get(routeIds[0]).zoomToSpan();
        calculateSuccess = true;
    }

    @Override
    public void notifyParallelRoad(int i) {

    }


    public void changeRoute(View view) {
        if (!calculateSuccess) {
            Toast.makeText(this, "请先算路", Toast.LENGTH_SHORT).show();
            return;
        }

        if (routeIndex >= routeIds.length)
            routeIndex = 0;

        //突出选择的那条路
        for (RouteOverLay routeOverLay : routeOverlays.values()) {
            routeOverLay.setTransparency(0.7f);
        }
        routeOverlays.get(routeIds[routeIndex]).setTransparency(0);


        //必须告诉AMapNavi 你最后选择的哪条路
        aMapNavi.selectRouteId(routeIds[routeIndex]);
        Toast.makeText(this, "导航距离:" + (aMapNavi.getNaviPaths()).get(routeIds[routeIndex]).getAllLength() + "m" + "\n" + "导航时间:" + (aMapNavi.getNaviPaths()).get(routeIds[routeIndex]).getAllTime() + "s", Toast.LENGTH_SHORT).show();
        routeIndex++;

        chooseRouteSuccess = true;
    }

    public void goToEmulateActivity(View view) {
    /*    if (chooseRouteSuccess && calculateSuccess) {
            //SimpleNaviActivity非常简单，就是startNavi而已（因为导航道路已在这个activity生成好）
            Intent intent = new Intent(this, SimpleNaviActivity.class);
            intent.putExtra("gps", false);
            startActivity(intent);
        } else {*/
        	//步行导航
        //	Intent intent = new Intent(this, BasicWalkNaviActivity.class);
        //    startActivity(intent);
           // Toast.makeText(this, "请先算路，选路", Toast.LENGTH_SHORT).show();
	    	Intent intent = new Intent(this, SimpleNaviActivity.class);
	        intent.putExtra("gps", false);
	        startActivity(intent);         
    //    }
    }

    @Override
    public void onInitNaviFailure() {
    	MyLog.i("hu", "*44***********onInitNaviFailure()");
    }

    @Override
    public void onInitNaviSuccess() {
    	MyLog.i("hu", "*44***********onInitNaviSuccess()");
    }

    @Override
    public void onStartNavi(int type) {
    	MyLog.i("hu", "*44***********onStartNavi()");
    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation location) {
    	MyLog.i("hu", "*44***********onLocationChange");
    }

    @Override
    public void onGetNavigationText(int type, String text) {
    	MyLog.i("hu", "*44***********onGetNavigationText()");
    }

    @Override
    public void onEndEmulatorNavi() {
    	MyLog.i("hu", "*44***********onEndEmulatorNavi()");
    }

    @Override
    public void onArriveDestination() {
    	MyLog.i("hu", "*44***********onArriveDestination()");
    }

    @Override
    public void onCalculateRouteSuccess() {
    	MyLog.i("hu", "*44***********onCalculateRouteSuccess()");
    	//aMapNavi.getNaviPath();
    	// aMapNavi.selectRouteId(0);
    	if(routeOverLay != null)
    		routeOverLay.removeFromMap();
    	path = aMapNavi.getNaviPath();
        routeOverLay = new RouteOverLay(amap, path, this);
      //  routeOverLay.setTrafficLine(true);//开启交通线
        routeOverLay.addToMap();
        routeOverLay.zoomToSpan();
    	 MyLog.i("hu", "导航距离:" + (aMapNavi.getNaviPath()).getAllLength() + "m" + "导航时间:" + (aMapNavi.getNaviPath()).getAllTime() + "s");
        
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {
    	MyLog.i("hu", "*44***********onCalculateRouteFailure()");
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int wayID) {

    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    public void chooseStart(View view) {
        Toast.makeText(this, "请在地图上点选起点", Toast.LENGTH_SHORT).show();
        mapClickStartReady = true;
    }

    public void chooseEnd(View view) {
        Toast.makeText(this, "请在地图上点选终点", Toast.LENGTH_SHORT).show();
        mapClickEndReady = true;
    }

    public void goToGPSActivity(View view) {
    /*    if (chooseRouteSuccess && calculateSuccess) {
            //SimpleNaviActivity非常简单，就是startNavi而已（因为导航道路已在这个activity生成好）
            Intent intent = new Intent(this, SimpleNaviActivity.class);
            intent.putExtra("gps", true);
            startActivity(intent);
        } else {*/
        //	Intent intent = new Intent(this, GPSNaviActivity.class);
         //   startActivity(intent);
            Intent intent = new Intent(this, SimpleNaviActivity.class);
            intent.putExtra("gps", true);
            startActivity(intent);
         //   Toast.makeText(this, "请先算路，选路", Toast.LENGTH_SHORT).show();
   //     }
    }
    public void goToPoiActivity(View view) {
    	Intent intent = new Intent(this, PoiKeywordSearchActivity.class);
        this.startActivityForResult(intent,2);
    }
    
    
	@Override
	public void activate(OnLocationChangedListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}
	
	public void locationDialog(){
		String str = Utils.getSPValue("dig", context);
    	if(str != null && str.trim().equals("1"))
    		return;    	
		
    	final AlertDialog dlg = new AlertDialog.Builder(this).create();
    	dlg.show();
    	Window window = dlg.getWindow();
    	        // *** 主要就是在这里实现这种效果的.
    	        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
    	window.setContentView(R.layout.locationdialog);
    	        // 为确认按钮添加事件,执行退出应用操作    	
    	CheckBox cb_tx = (CheckBox)window.findViewById(R.id.cb_tx);	    	
    	cb_tx.setChecked(false);
    	
    	cb_tx.setOnCheckedChangeListener(new OnCheckedChangeListener() {  			
			public void onCheckedChanged(CompoundButton buttonView,   
			boolean isChecked) {   
				if(isChecked)
					Utils.setSPValue("dig", "1", context);
				else
					Utils.setSPValue("dig", "0", context);
			}   
		}); 
    	TextView tv_cancel = (TextView)window.findViewById(R.id.tv_cancel);
    	tv_cancel.setOnClickListener(new View.OnClickListener() {
  		  public void onClick(View v) {
  			dlg.cancel(); 
  		  }
		 });
    	TextView tv_exit = (TextView)window.findViewById(R.id.tv_exit);
    	tv_exit.setOnClickListener(new View.OnClickListener() {
    		  public void onClick(View v) {
    			  dlg.cancel();
    			//判断手机系统的版本  即API大于10 就是3.0或以上版本 
    			  Intent intent;
  				//if(android.os.Build.VERSION.SDK_INT>10){
  				//	intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
				//}else{
				//	intent = new Intent();
                  //  ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                 //   intent.setComponent(component);
                 //   intent.setAction("android.intent.action.VIEW");
					intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        context.startActivity(intent);
                //}
                startActivity(intent);
    		  }
		 });
    }
	
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {	   
         if (amapLocation != null && amapLocation.getErrorCode() == 0) {
           //  mLocationErrText.setVisibility(View.GONE);
           //  mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
        	MyLog.i("hu","******我的地址改变*****onLocationChanged()**");
 			mMyLon = amapLocation.getLongitude();
 			mMyLat = amapLocation.getLatitude();
 			mMyAddress = amapLocation.getAddress();
 			mMyType = amapLocation.getProvider();	
 			MyLog.i("hu"," mMyLon="+mMyLon+" mMyLat="+mMyLat+" mMyAddress="+mMyAddress+" mMyType="+mMyType);
 			mMyState = 1;
 			
 			//显示手机位置
 			LatLngBounds.Builder boundsBuild = new LatLngBounds.Builder();
 			boundsBuild.include(new LatLng(mMyLat,mMyLon));
 			LatLngBounds bounds = boundsBuild.build();
 			amap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
		//	amap.moveCamera(CameraUpdateFactory.zoomTo(17));
 		//	mMyMarker = amap.addMarker(new MarkerOptions()
		//	.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.location_marker)))
		//	.position(new LatLng(mMyLat,mMyLon)));	
 			
 			//设置起始点
 			startLatlng = new NaviLatLng(mMyLat , mMyLon);
         //   mStartMarker.setPosition(new LatLng(mMyLat , mMyLon));
         //   startList.clear();
         //   startList.add(startLatlng);
            
            Utils.setDoubleValue("startlat", mMyLat, context);
            Utils.setDoubleValue("startlng", mMyLon, context);
            
            double lat = Utils.getDoubleValue("endlat", context);
            double lng = Utils.getDoubleValue("endlng", context);
            if(lat == 0.0d || lng == 0.0d)
            {            	
            	mStartMarker.setPosition(new LatLng(mMyLat , mMyLon));                
            }else{
		        endLatlng = new NaviLatLng(lat, lng);
		    //    mEndMarker.setPosition(new LatLng(lat , lng));
		   //     endList.clear();
		   //     endList.add(endLatlng);
		        
		        calculateRoute(null);
            }
         } else {
             String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
             Log.e("AmapErr",errText);
           //  mLocationErrText.setVisibility(View.VISIBLE);
           //  mLocationErrText.setText(errText);
             mMyState = -1;
         }	   
	}
	
	private long lastPressBackKeyTime;
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (true) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastPressBackKeyTime < 2000) {
            		      finish();
                    //    SysUtil.killAppProcess();
                    } else {
                        Toast.makeText(MainActivity.this, R.string.exit_app_tip, Toast.LENGTH_LONG).show();
                        lastPressBackKeyTime = currentTime;
                    }
                }
                return true;	          
        }
        return super.onKeyDown(keyCode, event);
    }
	
	@Override   
    protected void onActivityResult(int requestCode, int resultCode,  Intent data)    
    {
    	  switch (resultCode)
          {
             case RESULT_OK:          /* 确认返回 */   
            	 locationClient.startLocation();
            	 break;
            	 
          }
    }
}
