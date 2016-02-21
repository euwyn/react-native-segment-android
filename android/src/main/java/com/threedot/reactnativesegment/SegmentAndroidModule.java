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
  private ReactApplicationContext mContext = null;

  public SegmentAndroidModule(ReactApplicationContext reactContext, Activity mActivity) {
    super(reactContext);
    this.mActivity = mActivity;
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

  //https://segment.com/docs/libraries/android/#identify
  @ReactMethod
  public void identify(String userID) {
    Analytics.with(this.mContext).identify(userID, null, null);
  }

  //https://segment.com/docs/libraries/android/#identify
  @ReactMethod
  public void identifyWithTraits(String userID, ReadableMap map) {
    ReadableMapKeySetIterator it = map.keySetIterator();
    Traits traits = new Traits();
    while (it.hasNextKey()) {
      String key = it.nextKey();
      ReadableType type = map.getType(key);
      String val = map.getString(key);
      traits.putValue(key, val);
    }
    Analytics.with(this.mContext).identify(userID, traits, null);
  }

  //https://segment.com/docs/libraries/android/#group
  @ReactMethod
  public void group(String groupID) {
    Analytics.with(this.mContext).group(groupID, null, null);
  }

  //https://segment.com/docs/libraries/android/#group
  @ReactMethod
  public void groupWithTraits(String groupID, ReadableMap map) {
    ReadableMapKeySetIterator it = map.keySetIterator();
    Traits traits = new Traits();
    while (it.hasNextKey()) {
      String key = it.nextKey();
      ReadableType type = map.getType(key);
      String val = map.getString(key);
      traits.putValue(key, val);
    }
      Analytics.with(this.mContext).group(groupID, traits, null);
  }

  //https://segment.com/docs/libraries/android/#track
  @ReactMethod
  public void track(String name) {
    Analytics.with(this.mContext).track(name, null);
  }

  //https://segment.com/docs/libraries/android/#track
  @ReactMethod
  public void trackWithProps(String name, ReadableMap map) {
    ReadableMapKeySetIterator it = map.keySetIterator();
    Properties properties = new Properties();
    while (it.hasNextKey()) {
      String key = it.nextKey();
      ReadableType type = map.getType(key);
      String val = map.getString(key);
      properties.putValue(key,val);
    }
    Analytics.with(this.mContext).track(name, properties);
  }

  //https://segment.com/docs/libraries/android/#screen
  @ReactMethod
  public void screen(String category, String name, ReadableMap map) {
    ReadableMapKeySetIterator it = map.keySetIterator();
    Properties properties = new Properties();
    while (it.hasNextKey()) {
      String key = it.nextKey();
      ReadableType type = map.getType(key);
      String val = map.getString(key);
      properties.putValue(key,val);
    }
    Analytics.with(this.mContext).screen(category, name, properties);
  }

  //https://segment.com/docs/libraries/android/#alias
  @ReactMethod
  public void alias(String newUserID) {
    Analytics.with(this.mContext).alias(newUserID, null);
  }

}
