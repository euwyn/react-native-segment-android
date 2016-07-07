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
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class SegmentAndroidModule extends ReactContextBaseJavaModule {

  private Activity mActivity = null;
  private ReactApplicationContext mContext = null;

  public SegmentAndroidModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.mActivity = getCurrentActivity();
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
      Map<String, Object> val = recursivelyDeconstructReadableMap(map);
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
      Map<String, Object> val = recursivelyDeconstructReadableMap(map);
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
      Map<String, Object> val = recursivelyDeconstructReadableMap(map);
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
      Map<String, Object> val = recursivelyDeconstructReadableMap(map);
      properties.putValue(key,val);
    }
    Analytics.with(this.mContext).screen(category, name, properties);
  }

  //https://segment.com/docs/libraries/android/#alias
  @ReactMethod
  public void alias(String newUserID) {
    Analytics.with(this.mContext).alias(newUserID, null);
  }

  //Thanks to @ajwhite for this.
  //https://github.com/facebook/react-native/issues/4655
  private Map<String, Object> recursivelyDeconstructReadableMap(ReadableMap readableMap) {
    ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
    Map<String, Object> deconstructedMap = new HashMap<>();
    while (iterator.hasNextKey()) {
      String key = iterator.nextKey();
      ReadableType type = readableMap.getType(key);
      switch (type) {
        case Null:
          deconstructedMap.put(key, null);
          break;
        case Boolean:
          deconstructedMap.put(key, readableMap.getBoolean(key));
          break;
        case Number:
          deconstructedMap.put(key, readableMap.getDouble(key));
          break;
        case String:
          deconstructedMap.put(key, readableMap.getString(key));
          break;
        case Map:
          deconstructedMap.put(key, recursivelyDeconstructReadableMap(readableMap.getMap(key)));
          break;
        case Array:
          deconstructedMap.put(key, recursivelyDeconstructReadableArray(readableMap.getArray(key)));
          break;
        default:
          throw new IllegalArgumentException("Could not convert object with key: " + key + ".");
      }

    }
    return deconstructedMap;
  }

  //Thanks to @ajwhite for this.
  //https://github.com/facebook/react-native/issues/4655
  private List<Object> recursivelyDeconstructReadableArray(ReadableArray readableArray) {
    List<Object> deconstructedList = new ArrayList<>(readableArray.size());
    for (int i = 0; i < readableArray.size(); i++) {
      ReadableType indexType = readableArray.getType(i);
      switch(indexType) {
        case Null:
          deconstructedList.add(i, null);
          break;
        case Boolean:
          deconstructedList.add(i, readableArray.getBoolean(i));
          break;
        case Number:
          deconstructedList.add(i, readableArray.getDouble(i));
          break;
        case String:
          deconstructedList.add(i, readableArray.getString(i));
          break;
        case Map:
          deconstructedList.add(i, recursivelyDeconstructReadableMap(readableArray.getMap(i)));
          break;
        case Array:
          deconstructedList.add(i, recursivelyDeconstructReadableArray(readableArray.getArray(i)));
          break;
        default:
          throw new IllegalArgumentException("Could not convert object at index " + i + ".");
      }
    }
    return deconstructedList;
  }

}
