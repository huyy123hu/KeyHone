package com.amap.safone.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.safone.util.MyLog;
import com.amap.safone.util.TTSController;
import com.amap.safone.util.Utils;
import com.autonavi.tbt.TrafficFacilityInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：11/11/15 11:02
 * 项目名称：newNaviDemo
 *
 * @author lingxiang.wang
 * @email lingxiang.wang@alibaba-inc.com
 * 类说明：
 */

public class BaseActivity extends Activity implements AMapNaviListener, AMapNaviViewListener {

    AMapNaviView mAMapNaviView;
    AMapNavi mAMapNavi;
    TTSController mTtsManager;
  //  NaviLatLng mEndLatlng = new NaviLatLng(39.925846, 116.432765);
  //  NaviLatLng mStartLatlng = new NaviLatLng(39.925041, 116.437901);
  //  List<NaviLatLng> mStartList = new ArrayList<NaviLatLng>();
   // List<NaviLatLng> mEndList = new ArrayList<NaviLatLng>();
   // List<NaviLatLng> mWayPointList;
    NaviLatLng start,end;
	Context context;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        context = getApplicationContext();   
        double slat = Utils.getDoubleValue("startlat", context);
        double slng = Utils.getDoubleValue("startlng", context);
        start = new NaviLatLng(slat,slng);
        double elat = Utils.getDoubleValue("endlat", context);
        double elng = Utils.getDoubleValue("endlng", context);
        end = new NaviLatLng(elat,elng);
        MyLog.i("hu", "BasicWalkNaviActivity.java slat="+slat
        		+" slng="+slng+" elat="+elat+" elng="+elng);

        mTtsManager = TTSController.getInstance(getApplicationContext());
        mTtsManager.init();
        mTtsManager.startSpeaking();

        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
        mAMapNavi.addAMapNaviListener(mTtsManager);
        mAMapNavi.setEmulatorNaviSpeed(150);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
       // mStartList.add(mStartLatlng);
      //  mEndList.add(mEndLatlng);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();

//        仅仅是停止你当前在说的这句话，一会到新的路口还是会再说的
        mTtsManager.stopSpeaking();
//
//        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
//        mAMapNavi.stopNavi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
        //since 1.6.0
        //不再在naviview destroy的时候自动执行AMapNavi.stopNavi();
        //请自行执行
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
        mTtsManager.destroy();
    }

    @Override
    public void onInitNaviFailure() {
    	MyLog.i("hu","111导航初始化失败！  onInitNaviFailure()");
        Toast.makeText(this, "init navi Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInitNaviSuccess() {
    	MyLog.i("hu","111导航初始化成功！计算步行路径  onInitNaviSuccess()");
    	//mAMapNavi.calculateWalkRoute(start, end);
    }

    @Override
    public void onStartNavi(int type) {
    	MyLog.i("hu","****111******onStartNavi()");
    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation location) {
    	MyLog.i("hu","****111******onLocationChange()");
    }

    @Override
    public void onGetNavigationText(int type, String text) {
    	MyLog.i("hu","****111******onGetNavigationText()");
    }

    @Override
    public void onEndEmulatorNavi() {
    	MyLog.i("hu","****111******onEndEmulatorNavi()");
    }

    @Override
    public void onArriveDestination() {
    	MyLog.i("hu","****111******onArriveDestination()");
    }

    @Override
    public void onCalculateRouteSuccess() {
    	MyLog.i("hu","开始模拟导航！111  onCalculateRouteSuccess()");
        mAMapNavi.startNavi(AMapNavi.EmulatorNaviMode);
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {
    	MyLog.i("hu","模拟导航失败！111  onCalculateRouteFailure()");
    }

    @Override
    public void onReCalculateRouteForYaw() {
    	MyLog.i("hu","****111******onReCalculateRouteForYaw()");
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
    	MyLog.i("hu","****111******onReCalculateRouteForTrafficJam()");
    }

    @Override
    public void onArrivedWayPoint(int wayID) {
    	MyLog.i("hu","****111******onArrivedWayPoint()");
    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {
    }

    @Override
    public void onNaviSetting() {
    	MyLog.i("hu","****111******onNaviSetting()");
    }

    @Override
    public void onNaviMapMode(int isLock) {
    	MyLog.i("hu","****111******onNaviMapMode()");
    }

    @Override
    public void onNaviCancel() {
        finish();
    }


    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }


    @Override
    public void onScanViewButtonClick() {
    }

    @Deprecated
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

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }


    @Override
    public void onLockMap(boolean isLock) {
    }

    @Override
    public void onNaviViewLoaded() {
    	MyLog.i("wlx", "导航页面加载成功");
        MyLog.i("wlx", "请不要使用AMapNaviView.getMap().setOnMapLoadedListener();会overwrite导航SDK内部画线逻辑");
     //   if(end.getLatitude()!=0.0d && end.getLatitude()!=0.0d)
     //   	mAMapNavi.calculateWalkRoute(start, end);
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }


}
