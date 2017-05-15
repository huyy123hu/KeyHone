package com.amap.safone.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.safone.R;
//import com.amap.map3d.demo.util.AMapUtil;
//import com.amap.map3d.demo.util.ToastUtil;
import com.amap.safone.util.AMapUtil;
import com.amap.safone.util.ListViewChat;
import com.amap.safone.util.MyLog;
import com.amap.safone.util.ToastUtil;
import com.amap.safone.util.Utils;

/**
 * AMapV1地图中简单介绍poisearch搜索
 */
public class PoiKeywordSearchActivity extends Activity implements
		OnMarkerClickListener, InfoWindowAdapter, TextWatcher,
		OnPoiSearchListener, OnClickListener, InputtipsListener {
	//private AMap aMap;
	private AutoCompleteTextView searchText;// 输入搜索关键字
	private String keyWord = "";// 要输入的poi搜索关键字
	private ProgressDialog progDialog = null;// 搜索时进度条
	//private EditText editCity;// 要输入的城市名字或者城市区号
	private PoiResult poiResult; // poi返回的结果
	private int currentPage = 0;// 当前页面，从0开始计数
	private PoiSearch.Query query;// Poi查询条件类
	private PoiSearch poiSearch;// POI搜索
	private ListView listview;
	//private SimpleAdapter adapter = null;
	ListViewChat lv_chatlist;
	Context context;
	boolean flagsearch = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
		setContentView(R.layout.poikeywordsearch_activity);
		context = getApplicationContext();
	//	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.back_title);
		TextView	titleText = (TextView)findViewById(R.id.titleTextView);
		titleText.setText(R.string.home_seek);
		ImageView titleBackBtn = (ImageView) findViewById(R.id.titleBackImageBtn);
	
		titleBackBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		//if (aMap == null) {
		//	aMap = ((SupportMapFragment) getSupportFragmentManager()
		//			.findFragmentById(R.id.map)).getMap();
			setUpMap();
		//}
		lv_chatlist = (ListViewChat)findViewById(R.id.lv_chatlist);        
        lv_chatlist.setOnItemClickListener( //设置选项被单击的监听器
	        new OnItemClickListener(){	
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					HashMap<String, Object> map = (HashMap<String, Object>) lv_chatlist.getItem(position);
				
        			String addr = (String) map.get("addr");
        			double lat = Double.parseDouble((String)map.get("lat"));
        			double lng = Double.parseDouble((String)map.get("lng"));
        		//	double la1 = poiResult.getPois().get(position).getLatLonPoint().getLatitude();
        		//	double lg1 = poiResult.getPois().get(position).getLatLonPoint().getLongitude();
        			MyLog.i("hu","点中 lat="+lat+" lng="+lng);
        		//			+" la1="+la1+" lg1="+lg1);
        			if(flagsearch)
        				addAddress(addr,lat,lng);  
        			else
        			{
        				Utils.setDoubleValue("endlat", lat, context);
        		        Utils.setDoubleValue("endlng", lng, context);
        			}
        			Intent intent = new Intent();
                	PoiKeywordSearchActivity.this.setResult(RESULT_OK, intent);
                	PoiKeywordSearchActivity.this.finish();
				}        	
        });
        setList();
	}

	public void addAddress(String address,double lat,double lng){
		boolean flag = false;
		StringBuffer buf = new StringBuffer();
		String s1 = Utils.getSPValue("addr", context);
		if(s1.length() != 0){
			String[] str = s1.split("@");
			int len = str.length;
			//只保存8个地址
			for(int j=0;j<len;j++)
			{
				String[] s2 = str[j].split("-");
				if(s2[0].equals(address))
					flag = true;				
			}
			if(flag)
				return;
			if(len > 10)
			{
				for(int i=len-8;i<len;i++)
				{
					buf.append(str[i]);
					buf.append("@");
				}
			}else 
				buf.append(s1+"@");
		}
		
		buf.append(address);
		buf.append("-"+String.valueOf(lat));
		buf.append("-"+String.valueOf(lng));
		
		Utils.setSPValue("addr", buf.toString(), context);
		Utils.setDoubleValue("endlat", lat, context);
        Utils.setDoubleValue("endlng", lng, context);
	}
	/**
	 * 设置页面监听
	 */
	private void setUpMap() {
		Button searButton = (Button) findViewById(R.id.searchButton);
		searButton.setOnClickListener(this);
		Button nextButton = (Button) findViewById(R.id.nextButton);
		nextButton.setOnClickListener(this);
		searchText = (AutoCompleteTextView) findViewById(R.id.keyWord);
		searchText.addTextChangedListener(this);// 添加文本输入框监听事件
	//	editCity = (EditText) findViewById(R.id.city);
	//	aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
	//	aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
	}

	/**
	 * 点击搜索按钮
	 */
	public void searchButton() {
		keyWord = AMapUtil.checkEditText(searchText);
		if ("".equals(keyWord)) {
			ToastUtil.show(PoiKeywordSearchActivity.this, "请输入搜索关键字");
			return;
		} else {
			doSearchQuery();
		}
	}

	/**
	 * 点击下一页按钮
	 */
	public void nextButton() {
		if (query != null && poiSearch != null && poiResult != null) {
			if (poiResult.getPageCount() - 1 > currentPage) {
				currentPage++;
				query.setPageNum(currentPage);// 设置查后一页
				poiSearch.searchPOIAsyn();
			} else {
				ToastUtil.show(PoiKeywordSearchActivity.this,
						R.string.no_result);
			}
		}
	}

	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(false);
		progDialog.setMessage("正在搜索:\n" + keyWord);
		progDialog.show();
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 开始进行poi搜索
	 */
	protected void doSearchQuery() {
		showProgressDialog();// 显示进度框
		currentPage = 0;
		//query = new PoiSearch.Query(keyWord, "", editCity.getText().toString());// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query = new PoiSearch.Query(keyWord, "", "");
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页

		poiSearch = new PoiSearch(this, query);
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		marker.showInfoWindow();
		return false;
	}

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	@Override
	public View getInfoWindow(final Marker marker) {
		View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri,
				null);
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(marker.getTitle());

		TextView snippet = (TextView) view.findViewById(R.id.snippet);
		snippet.setText(marker.getSnippet());
		ImageButton button = (ImageButton) view
				.findViewById(R.id.start_amap_app);
		// 调起高德地图app
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startAMapNavi(marker);
			}
		});
		return view;
	}

	/**
	 * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
	 */
	public void startAMapNavi(Marker marker) {
		// 构造导航参数
		NaviPara naviPara = new NaviPara();
		// 设置终点位置
		naviPara.setTargetPoint(marker.getPosition());
		// 设置导航策略，这里是避免拥堵
		naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);

		// 调起高德地图导航
		try {
			AMapUtils.openAMapNavi(naviPara, getApplicationContext());
		} catch (AMapException e) {

			// 如果没安装会进入异常，调起下载页面
			AMapUtils.getLatestAMapApp(getApplicationContext());

		}

	}

	/**
	 * 判断高德地图app是否已经安装
	 */
	public boolean getAppIn() {
		PackageInfo packageInfo = null;
		try {
			packageInfo = this.getPackageManager().getPackageInfo(
					"com.autonavi.minimap", 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		// 本手机没有安装高德地图app
		if (packageInfo != null) {
			return true;
		}
		// 本手机成功安装有高德地图app
		else {
			return false;
		}
	}

	/**
	 * 获取当前app的应用名字
	 */
	public String getApplicationName() {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(
					getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager
				.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	/**
	 * poi没有搜索到数据，返回一些推荐城市的信息
	 */
	private void showSuggestCity(List<SuggestionCity> cities) {
		String infomation = "推荐城市\n";
		for (int i = 0; i < cities.size(); i++) {
			infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
					+ cities.get(i).getCityCode() + "城市编码:"
					+ cities.get(i).getAdCode() + "\n";
		}
		ToastUtil.show(PoiKeywordSearchActivity.this, infomation);

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String newText = s.toString().trim();
		if (!AMapUtil.IsEmptyOrNullString(newText)) {
		   // InputtipsQuery inputquery = new InputtipsQuery(newText, editCity.getText().toString());
		    InputtipsQuery inputquery = new InputtipsQuery(newText, "");
		    Inputtips inputTips = new Inputtips(PoiKeywordSearchActivity.this, inputquery);
		    inputTips.setInputtipsListener(this);
		    inputTips.requestInputtipsAsyn();
		}
	}


	/**
	 * POI信息查询回调方法
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		//可以在回调中解析result，获取POI信息
		//result.getPois()可以获取到PoiItem列表，Poi详细信息可参考PoiItem类
		//若当前城市查询不到所需Poi信息，可以通过result.getSearchSuggestionCitys()获取当前Poi搜索的建议城市
		//如果搜索关键字明显为误输入，则可通过result.getSearchSuggestionKeywords()方法得到搜索关键词建议
		//返回结果成功或者失败的响应码。0为成功，其他为失败（详细信息参见网站开发指南-错误码对照表）
		dissmissProgressDialog();// 隐藏对话框
		if (rCode == 1000) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条
					poiResult = result;
					// 取得搜索到的poiitems有多少页
					List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

					if (poiItems != null && poiItems.size() > 0) {
						Log.i("hu","********444 ");
						flagsearch = true;
						int len = poiItems.size();
						for(int i=0;i<len;i++)
						{
							Log.i("hu","!!!i="+i+" "+poiItems.get(i).toString()
									+" lat="+poiItems.get(i).getLatLonPoint().getLatitude()
									+" lng="+poiItems.get(i).getLatLonPoint().getLongitude());
						}
						Log.i("hu","********555 ");
					/*	aMap.clear();// 清理之前的图标
						PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
						poiOverlay.removeFromMap();
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();*/
						setList(poiItems);
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
					//	showSuggestCity(suggestionCities);
						ToastUtil.show(PoiKeywordSearchActivity.this,
								"请输入更详细的地址！");
					} else {
						ToastUtil.show(PoiKeywordSearchActivity.this,
								R.string.no_result);
					}
				}
			} else {
				ToastUtil.show(PoiKeywordSearchActivity.this,
						R.string.no_result);
			}
		} else {
			ToastUtil.showerror(this, rCode);
		}

	}	
	
	private void setList(List<PoiItem> listpoi){
		lv_chatlist.reBulid();
		int len = listpoi.size();		
		for(int i=0;i<len;i++)
		{	
			double lat = listpoi.get(i).getLatLonPoint().getLatitude();
			double lng = listpoi.get(i).getLatLonPoint().getLongitude();
			lv_chatlist.addItem(i,listpoi.get(i).toString(),lat,lng);
		}
		if(len > 0)
			lv_chatlist.notifyData();		
    }
	
	private void setList(){
		lv_chatlist.reBulid();
		String address = Utils.getSPValue("addr", context);
		MyLog.i("","*********取 addr="+address);
		if(address == null || address.length()==0)
			return;
		String[] str = address.split("@");
		int len = str.length;
		int i=0,j=0;
		if(len>8)
			i=len-8;
		for(;i<len;i++,j++)
		{	
			String[] strings = str[i].split("-");
			lv_chatlist.addItem(j,strings[0],Double.parseDouble(strings[1]),Double.parseDouble(strings[2]));
		}
		if(len > 0)
			lv_chatlist.notifyData();		
    }
	
	@Override
	public void onPoiItemSearched(PoiItem item, int rCode) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Button点击事件回调方法
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/**
		 * 点击搜索按钮
		 */
		case R.id.searchButton:
			searchButton();
			break;
		/**
		 * 点击下一页按钮
		 */
		case R.id.nextButton:
			nextButton();
			break;
		default:
			break;
		}
	}



	@Override
	public void onGetInputtips(List<Tip> tipList, int rCode) {
		if (rCode == 1000) {// 正确返回
			List<String> listString = new ArrayList<String>();
			for (int i = 0; i < tipList.size(); i++) {
				listString.add(tipList.get(i).getName());
			}
			ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
					getApplicationContext(),
					R.layout.route_inputs, listString);
			searchText.setAdapter(aAdapter);
			aAdapter.notifyDataSetChanged();
		} else {
			ToastUtil.showerror(this, rCode);
		}
		
	}
	
	
}
