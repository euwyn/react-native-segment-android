package com.threedot.reactnativesegment;

import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;
import com.segment.analytics.Traits;

import android.app.Activity;
import android.app.Application;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;

public class SegmentAndroidModule extends ReactContextBaseJavaModule {

  private Activity mActivity = null;
  private Application mApplication = null;
  private ReactApplicationContext mContext = null;
  
  public SegmentAndroidModule(ReactApplicationContext reactContext, Activity mActivity, Application mApplication) {
    super(reactContext);
    this.mActivity = mActivity;
    this.mApplication = mApplication;
    this.mContext = reactContext;
  }
  
  @Override
  public String getName() {
    return "SegmentAndroid";
  }
  
  @ReactMethod
  public void initialize(String apiKey) {
    
    try {
      Analytics analytics = new Analytics.Builder(this.mContext, apiKey).build();
      Analytics.setSingletonInstance(analytics);
    } catch (Exception e ) {}
    
  }
  
  @ReactMethod
  public void logEvent(String identifier) {
    Analytics.with(this.mContext).track(identifier, null);
  }
  
  @ReactMethod
  public void logEventWithProps(String identifier, ReadableMap map) {
    
    ReadableMapKeySetIterator it = map.keySetIterator();
    Properties properties = new Properties();
    
    while (it.hasNextKey()) {
      String key = it.nextKey();
      ReadableType type = map.getType(key);
      String val = map.getString(key);
      properties.putValue(key,val);
    }
    
    Analytics.with(this.mContext).track(identifier, properties);
    
  }
  
  @ReactMethod
  public void setUserId(String id) {
    Analytics.with(this.mContext).identify(id, null, null);
  }
  
  /* 
  
  public static JSONObject convertReadableToJsonObject(ReadableMap map) throws JSONException{
    JSONObject jsonObj = new JSONObject();
    ReadableMapKeySetIterator it = map.keySetIterator();
      
    while (it.hasNextKey()) {
      String key = it.nextKey();
      ReadableType type = map.getType(key);
      switch (type) {
        case Map:
            jsonObj.put(key, convertReadableToJsonObject(map.getMap(key)));
            break;
        case String:
            jsonObj.put(key, map.getString(key));
            break;
        case Number:
            jsonObj.put(key, map.getDouble(key));
            break;
        case Boolean:
            jsonObj.put(key, map.getBoolean(key));
            break;
        case Null:
            jsonObj.put(key, null);
            break;
      }
    }
    return jsonObj;
 }
    
 */
     
}