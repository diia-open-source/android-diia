# Description

This module is responsible splash screen and logic around it

# How to install
1. Copy module folder to your project and add module to gradle dependency like this:

```groovy
implementation project(':features:splash')
```

2. Module requires next modules to work

```groovy
    implementation project(':libs:core')
    implementation project(':libs:diia_storage')
    implementation project(':libs:ui_base')
```
3. nav_id file describe all ids that this module requires. Entry point should implement all those ids.

`./src/main/res/values/nav_ids.xml`

4. Add next nav graphs to main navigation graph

```xml
<include app:graph="@navigation/nav_splash" />
```

5. The module requires implementation and delivery of SplashHelper in an entry point

`./src/java/ua/gov/diia/splash/helper/SplashHelper.kt`
