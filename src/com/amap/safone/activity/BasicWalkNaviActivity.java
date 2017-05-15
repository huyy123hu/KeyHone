package com.amap.safone.activity;

import android.os.Bundle;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.safone.R;
import com.amap.safone.util.MyLog;

/**
 * 创建时间：16/1/7 11:59
 * 项目名称：newNaviDemo
 *
 * @author lingxiang.wang
 * @email lingxiang.wang@alibaba-inc.com
 * 类说明：
 */

public class BasicWalkNaviActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_basic_navi);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
    }
   
	@Override
    public void onCalculateRouteSuccess() {
    	MyLog.i("hu","333  onCalculateRouteSuccess()");
        mAMapNavi.startNavi(AMapNavi.EmulatorNaviMode);
    }

}
