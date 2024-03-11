# Description

This module is responsible for representing home screen and logic around it

# How to install
1. Copy module folder to your project and add module to gradle dependency like this:

```groovy
implementation project(':home')
```

2. Module requires next modules to work

```groovy
    implementation project(':core')
    implementation project(':diia_storage')
    implementation project(':ui_base')
```

3. nav_id file describe all ids that this module requires. Entry point should implement all those ids.

`./src/main/res/values/nav_ids.xml`

4. Add next nav graphs to main navigation graph

```xml
<include app:graph="@navigation/nav_home" />
```

5. The following action should be added into the root navigation graph

```xml
<action
    android:id="@+id/action_global_to_QrScanF"
    app:destination="@id/qrScanF" />
```

6. The module requires implementation of HomeHelper. It should be implemented and provided by an entry point.

`./src/java/ua/gov/diia/core/helper/HomeHelper.kt`
