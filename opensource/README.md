
# Description

To build you are required to have the dependency [Android Studio](https://developer.android.com/studio) installed. You can then follow these instructions:

1. Clone or download this repository
2. Open the project in Android Studio and run it from there or build an APK directly through Gradle:
   ``` ./gradlew :opensource:assembleGplayDebug```
   *NOTE: Android SDK should be added to PATH environment variable for this to work.*

Deploy to Device/Emulator:
```./gradlew :opensource:installGplayDebug```
*NOTE: You can also replace the "Debug" with "Release" to get an optimized release binary.*

Before building Huawei specific app generate and place agconnect-services.json file in opensource module.

For build Huawei specific APK file use next command:
```./gradlew :opensource:assembleHuaweiDebug```

To deploy to device/emulator for Huawei use this:
```./gradlew :opensource:installHuaweiDebug```