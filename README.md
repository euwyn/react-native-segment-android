# react-native-segment-android
Simple React Native wrapper for Segment tracking

## Installation ##

`npm install react-native-segment-android --save`

### In `settings.gradle` add the following lines:

```groovy
include ':SegmentAndroid'
project(':SegmentAndroid').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-segment-android/android')
```

### In `build.gradle` add the following line:

```groovy
compile project(':SegmentAndroid')
```

### In `MainActivity.java` add the following lines:

```java
import com.threedot.reactnativesegment.ReactNativeSegmentPackage;
```

```java
new ReactNativeSegmentPackage(this)
```

## Example usage:

```javascript
import SegmentAndroid from 'react-native-segment-android'
...
//Initialize w/ API KEY
SegmentAndroid.initialize(YOUR_API_KEY)
//Track an event anonymously...
SegmentAndroid.track('Application Loaded')
...
//After user creates an account in your system...
//Alias ties in the previous anonymous session...
SegmentAndroid.alias(userID)
SegmentAndroid.identify(userID)
...
//Track another event
SegmentAndroid.track('Another event...')
```
