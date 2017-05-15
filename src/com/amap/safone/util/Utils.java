package com.amap.safone.util;

import com.amap.api.navi.model.NaviLatLng;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Utils {
    public static final String DAY_NIGHT_MODE = "daynightmode";
    public static final String DEVIATION = "deviationrecalculation";
    public static final String JAM = "jamrecalculation";
    public static final String TRAFFIC = "trafficbroadcast";
    public static final String CAMERA = "camerabroadcast";
    public static final String SCREEN = "screenon";
    public static final String THEME = "theme";
    public static final String ISEMULATOR = "isemulator";


    public static final String ACTIVITYINDEX = "activityindex";

    public static final int SIMPLEHUDNAVIE = 0;
    public static final int EMULATORNAVI = 1;
    public static final int SIMPLEGPSNAVI = 2;
    public static final int SIMPLEROUTENAVI = 3;


    public static final boolean DAY_MODE = false;
    public static final boolean NIGHT_MODE = true;
    public static final boolean YES_MODE = true;
    public static final boolean NO_MODE = false;
    public static final boolean OPEN_MODE = true;
    public static final boolean CLOSE_MODE = false;

    
    public static final String getSPValue(String sparams,Context context) {
       SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
       String svalues  = localSharedPreferences.getString(sparams, "");
	    if (svalues == null) {  
	    	return "";
	    }
	    return svalues;
    }    
    
    public static final void setSPValue(String sparams,String svalue,Context context) {
        SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        localSharedPreferences.edit().putString(sparams, svalue).commit(); 
    }
    
    public static final double getDoubleValue(String sparams,Context context) {    	
    	String svalues  = getSPValue(sparams,context);
 	    if (svalues == null || svalues.length() == 0) {  
 	    	return 0.0d;
 	    }   
		  	   
 	    return Double.parseDouble(svalues);
     }    
     
     public static final void setDoubleValue(String sparams,double svalue,Context context) {
        String val = String.valueOf(svalue);
        setSPValue(sparams,val,context);
     }
}
