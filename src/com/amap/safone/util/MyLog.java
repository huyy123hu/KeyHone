package com.amap.safone.util;

import android.util.Log;

public class MyLog {
	public static boolean DEBUG = true;
	private static final String MODULE_NAME = "SafoneKeyHome";
    private static final MyLog INSTANCE = new MyLog();

    private MyLog() {
    }


    public static MyLog getInstance() {
        return INSTANCE;
    }


    public static void e(String tag, String msg) {
    	if(DEBUG)
    		Log.e(MODULE_NAME, tag + ", " + msg);
    }


    public static void e(String tag, String msg, Throwable t) {
    	if(DEBUG)
    		Log.e(MODULE_NAME, tag + ", " + msg, t);
    }


    public static void w(String tag, String msg) {
    	if(DEBUG)
    		Log.w(MODULE_NAME, tag + ", " + msg);
    }

    public static void w(String tag, String msg, Throwable t) {
    	if(DEBUG)
    		Log.w(MODULE_NAME, tag + ", " + msg, t);
    }


    public static void i(String tag, String msg) {
    	if(DEBUG)
    		Log.i(MODULE_NAME, tag + ", " + msg);
    }


    public static void i(String tag, String msg, Throwable t) {
    	if(DEBUG)
    		Log.i(MODULE_NAME, tag + ", " + msg, t);
    }


    public static void d(String tag, String msg) {
    	if(DEBUG)
    		Log.d(MODULE_NAME, tag + ", " + msg);
    }


    public static void d(String tag, String msg, Throwable t) {
    	if(DEBUG)
    		Log.d(MODULE_NAME, tag + ", " + msg, t);
    }

    public static void v(String tag, String msg) {
    	if(DEBUG)
    		Log.v(MODULE_NAME, tag + ", " + msg);
    }


    public static void v(String tag, String msg, Throwable t) {
    	if(DEBUG)
    		Log.v(MODULE_NAME, tag + ", " + msg, t);
    }
}
