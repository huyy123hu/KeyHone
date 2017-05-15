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
import com.amap.api.navi.enums.NaviMode;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.safone.R;
import com.amap.safone.util.MyLog;
import com.amap.safone.util.TTSController;
import com.amap.safone.util.Utils;
import com.autonavi.tbt.TrafficFacilityInfo;

/**
 * 创建时间：16/1/7 11:59
 * 项目名称：newNaviDemo
 *
 * @author lingxiang.wang
 * @email lingxiang.wang@alibaba-inc.com
 * 类说明：
 */

public class BasicWalkNaviActivity1 extends Activity implements AMapNaviListener, AMapNaviViewListener {
    AMapNaviView mAMapNaviView;
    AMapNavi mAMapNavi;
    TTSController mTtsManager;
	NaviLatLng start,end;
	Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);      
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_basic_navi);
       
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
        
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this); 
        
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
        mAMapNavi.addAMapNaviListener(mTtsManager);
        mAMapNavi.setEmulatorNaviSpeed(150);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
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
    public void onInitNaviSuccess() {
    	MyLog.i("hu","导航初始化成功！  onInitNaviSuccess()");
    	MyLog.i("hu","计算步行路径  onInitNaviSuccess()");
    	//mAMapNavi.calculateWalkRoute(start, end);
    }
    
    @Override
    public void onInitNaviFailure() {
    	MyLog.i("hu","导航初始化失败！  onInitNaviFailure()");
        Toast.makeText(this, "init navi Failed", Toast.LENGTH_SHORT).show();
    }

	@Override
	public void onLockMap(boolean arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onNaviBackClick() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onNaviCancel() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onNaviMapMode(int arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onNaviSetting() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onNaviTurnClick() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onNaviViewLoaded() {
		// TODO Auto-generated method stub
		MyLog.i("wlx", "导航页面加载成功");
        MyLog.i("wlx", "请不要使用AMapNaviView.getMap().setOnMapLoadedListener();会overwrite导航SDK内部画线逻辑");
        mAMapNavi.calculateWalkRoute(start, end);
	}


	@Override
	public void onNextRoadClick() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onScanViewButtonClick() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void OnUpdateTrafficFacility(TrafficFacilityInfo arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void hideCross() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void hideLaneInfo() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void notifyParallelRoad(int arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onArriveDestination() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onArrivedWayPoint(int arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onCalculateMultipleRoutesSuccess(int[] arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onCalculateRouteFailure(int arg0) {
		// TODO Auto-generated method stub
		MyLog.i("hu","模拟导航失败！333  onCalculateRouteFailure()");
	}


	@Override
    public void onCalculateRouteSuccess() {
    	MyLog.i("hu","开始模拟导航成功！222  onCalculateRouteSuccess()");
        mAMapNavi.startNavi(AMapNavi.EmulatorNaviMode);
    }


	@Override
	public void onEndEmulatorNavi() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onGetNavigationText(int arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onGpsOpenStatus(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onNaviInfoUpdate(NaviInfo arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onReCalculateRouteForTrafficJam() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onReCalculateRouteForYaw() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStartNavi(int arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTrafficStatusUpdate() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void showCross(AMapNaviCross arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void showLaneInfo(AMapLaneInfo[] arg0, byte[] arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		
	}
}
