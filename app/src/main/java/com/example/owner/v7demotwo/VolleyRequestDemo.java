package com.example.owner.v7demotwo;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;


public class VolleyRequestDemo {
	
	private static ImageLoader imageLoader=null;
	
	/*一个简单的http请求*/
	public static void simpleRequest(final String tag){//默认应该是get吧，看源码
		StringRequest stringRequest = new StringRequest("http://222.177.20.197:8888/vsmp/PortalvidiconConfig/getALLListByOrgcode.action?page=0&orgcode=50010800007338323900&loginname=cs5", 
			new Listener<String>() {
				@Override
				public void onResponse(String response) {
					Log.d(tag, response);
				}

			}, new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Log.d(tag, "StringRequest失败"+error.getMessage());
					Toast.makeText(MainActivity.main_this, "访问失败", Toast.LENGTH_SHORT).show();
				}
				
		});
		stringRequest.setTag(tag);//添加标签，以便用来取消
		ApplicationTest.getInApplicationTest().mQueue.add(stringRequest);
	}
	/*post请求，提交键值对数据参数或者其他比如header等信息，并返回数据*/
	public static void postRequest(String url){
		StringRequest stringRequest= new StringRequest(Method.POST, url, new Listener<String>() {
			
			@Override
			public void onResponse(String response) {
				//成功
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				//失败
			}
		}){

			@Override
			protected Map<String, String> getPostParams()
					throws AuthFailureError {
				//在这个地方来添加post需要的参数：
				Map<String, String> map = new HashMap<String, String>();  
		        map.put("params1", "value1");  
		        map.put("params2", "value2"); 
		        
				return map;
			}
			// 重写parseNetworkResponse方法改变返回头参数解决乱码问题  
	        // 主要是看服务器编码，如果服务器编码不是UTF-8的话那么就需要自己转换，反之则不需要  
			 protected final String TYPE_UTF8_CHARSET = "charset=UTF-8";
			@Override
			protected Response<String> parseNetworkResponse(
					NetworkResponse response) {
				try {  
	                String type = response.headers.get(HTTP.CONTENT_TYPE);  
	                if (type == null) {  
	                    type = TYPE_UTF8_CHARSET;  
	                    response.headers.put(HTTP.CONTENT_TYPE, type);  
	                } else if (!type.contains("UTF-8")) {  
	                    type += ";" + TYPE_UTF8_CHARSET;  
	                    response.headers.put(HTTP.CONTENT_TYPE, type);  
	                }  
	            } catch (Exception e) {  
	            }  
				return super.parseNetworkResponse(response);
			}
			
			
		};
		stringRequest.setTag(ApplicationTest.TAG);
		ApplicationTest.getInApplicationTest().mQueue.add(stringRequest);
	}
	/*Json数据请求：JsonRequest是抽象类，无法直接创建实例
	 * JsonRequest有两个直接的子类，JsonObjectRequest和JsonArrayRequest，从名字上你应该能就看出它们的区别了吧？一个是用于请求一段JSON数据的，一个是用于请求一段JSON数组的。
	 * get方式需要将第二个参数赋值为null；
	 * */
	public static void getJsonObjectRequest(){
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://222.177.20.197:8888/vsmp/PortalvidiconConfig/getALLListByOrgcode.action?page=0&orgcode=50010800007338323900&loginname=cs5", null, 
			new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					Log.d(ApplicationTest.TAG, response.toString());
				}
			}, new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Log.d(ApplicationTest.TAG, "JsonObjectRequest失败"+error.getMessage());
				}
			
			});
		jsonObjectRequest.setTag(ApplicationTest.TAG);
		ApplicationTest.getInApplicationTest().mQueue.add(jsonObjectRequest);
	}
	/*get方式获取jsonarray字符串*/
	public static void getJsonArrayRequest(){
		JsonArrayRequest jsonArrayRequest  = new JsonArrayRequest("http://222.177.20.197:8888/vsmp/PortalvidiconConfig/getALLListByOrgcode.action?page=0&orgcode=50010800007338323900&loginname=cs5",new Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				Log.d(ApplicationTest.TAG, response.toString());
			}
		},new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d(ApplicationTest.TAG, "JsonArrayRequest失败"+error.getLocalizedMessage());
			}
		});
		jsonArrayRequest.setTag(ApplicationTest.TAG);
		ApplicationTest.getInApplicationTest().mQueue.add(jsonArrayRequest);
	}
	/*post方式提交json数据给服务器(需要服务器支持)，并返回Json数据,此种post方式只有JsonObjectRequest才有，JsonArrayRequest是没有的*/
	public static void postJsonObjectRequest(String url){
		final Map<String, String> map = new HashMap<String, String>();  
        map.put("user", "user");  
        map.put("password", "password");
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST, url, new JSONObject(map), new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// 成功返回
				
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// 失败处理
				
			}
		});
		jsonObjectRequest.setShouldCache(false);//一般post不缓存，只有图片才有可能缓存
		jsonObjectRequest.setTag(ApplicationTest.TAG);
		ApplicationTest.getInApplicationTest().mQueue.add(jsonObjectRequest);
	}

	/*网络图片加载，带有缓存，如果不带，只需将ImageLoader的第二个参数改为ImageCache，并把复写函数的语句清空；
	 * 此外volley还有NetworkImageView、ImageRequest可以加载网络图片，但是前者是一个View，后者的使用方法跟前面的Request类方法是一样的,具体的可以百度
	 * 
	 * 三种都不能加载本地图片
	 * */
	public static void imageLoader(ImageView imageView,String url){
		/*imageLoader是复用的，不能创建多次*/
		if(imageLoader == null ){
			 imageLoader = new ImageLoader(ApplicationTest.getInApplicationTest().mQueue, new BtiMapCache());
//			imageLoader = new ImageLoader(ApplicationTest.getInApplicationTest().mQueue, new ImageCache() {
//				@Override
//				public void putBitmap(String url, Bitmap bitmap) {
//				}
//				@Override
//				public Bitmap getBitmap(String url) {
//					return null;
//				}
//			}) ;
		 }
		ImageListener listener = ImageLoader.getImageListener(imageView, R.drawable.cheese_3, R.drawable.cheese_3);
		//imageLoader.get(requestUrl, imageListener, maxWidth, maxHeight)//可以设置图片的宽高
		imageLoader.get(url, listener);
	}
	
	public static void getGsonRequset(){
		GsonRequest<Datas> gsonRequest= new GsonRequest<Datas>("", Datas.class, new Listener<Datas>() {
			@Override
			public void onResponse(Datas response) {
				Data data = response.dataList.get(0);
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				
			}
		});
	}
	
	
}
class Datas{
	String title;
	List<Data> dataList;
}
class Data{
	String name;
	String age;
}
/*imageloader需要的图片缓存类*/
class BtiMapCache implements ImageCache{
	private LruCache<String, Bitmap> mLruCache;
	public BtiMapCache() {
		int maxSize=10*1024*1024;//设置缓存的大小10M
		mLruCache = new LruCache<String, Bitmap>(maxSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				
				return value.getRowBytes()*value.getHeight();
			}
			
		};
	}
	@Override
	public Bitmap getBitmap(String url) {
		
		Bitmap temp = mLruCache.get(url);
		Log.e(ApplicationTest.TAG, "获取缓存："+url+"---"+temp);
		return temp;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		Log.e(ApplicationTest.TAG, "缓存："+url+"---"+bitmap);
		mLruCache.put(url, bitmap);
	}
	
}
/*自定义的GSONRequest，使用谷歌的gson来解析json字符串数据，这个要比Volley自带的jsonrequset类要更方便点，因为Gson会自动解析，只要按照Gson的要求数据格式
 * 写好数据字段类就可以了，
 * 其实下面的泛型T就代表json字符串的数据字段类，如果访问成功就返回该泛型类。
 * 实际上就个人理解而言，这个类方法和前面的jsonrequest类差不多，实际上就是用stringrequest类也是一样的，返回的字符串同样可以用Json或者Gson来解析json字符串数据
 * 
 * 当然Gson适合处理小的json字符串，Json适合大文件
 * */
class GsonRequest<T> extends Request<T> {  
	  
    private final Listener<T> mListener;  
  
    private Gson mGson;  
  
    private Class<T> mClass;  
  
    public GsonRequest(int method, String url, Class<T> clazz, Listener<T> listener,  
            ErrorListener errorListener) {  
        super(method, url, errorListener);  
        mGson = new Gson();  
        mClass = clazz;  
        mListener = listener;  
        
    }  
  
    public GsonRequest(String url, Class<T> clazz, Listener<T> listener,  
            ErrorListener errorListener) {  
        this(Method.GET, url, clazz, listener, errorListener);  
    }  
  
    @Override  
    protected Response<T> parseNetworkResponse(NetworkResponse response) {  
        try {  
            String jsonString = new String(response.data,  
                    HttpHeaderParser.parseCharset(response.headers));  
            return Response.success(mGson.fromJson(jsonString, mClass),  
                    HttpHeaderParser.parseCacheHeaders(response));  
        } catch (UnsupportedEncodingException e) {  
            return Response.error(new ParseError(e));  
        }  
    }  
  
    @Override  
    protected void deliverResponse(T response) {  
        mListener.onResponse(response);  
    }  
  
} 
