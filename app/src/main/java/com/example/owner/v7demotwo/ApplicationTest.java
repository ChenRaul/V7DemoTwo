package com.example.owner.v7demotwo;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends Application {
	public RequestQueue mQueue;
	public Context context;
	public static final String TAG="V7Demo";
	private static ApplicationTest applicationTest = null;
    public ApplicationTest() {
    }
    
    
    @Override
	public void onCreate() {
		super.onCreate();
		 applicationTest = this; 
		 context =  getApplicationContext();	
		 Log.d(TAG, "this="+this+"---"+getApplicationContext());//这两个值一样
		 //初始化volley队列
		 //equestQueue是一个请求队列对象，它可以缓存所有的HTTP请求，然后按照一定的算法并发地发出这些请求。
		 //RequestQueue内部的设计就是非常合适高并发的，因此我们不必为每一次HTTP请求都创建一个RequestQueue对象，这是非常浪费资源的
         mQueue = Volley.newRequestQueue(getApplicationContext());
	}


	public static ApplicationTest getInApplicationTest(){
    	if(applicationTest == null){
    		applicationTest = new ApplicationTest();
    	}
		return applicationTest;
    }
     
}