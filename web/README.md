# Description

This module is responsible for webview setup of the project

# How to install
1. Copy module folder to your project and add module to gradle dependency like this:

```groovy
implementation project(':libs:web')
```

2. Module requires next modules to work

```groovy
    implementation project(':libs:core')
    implementation project(':libs:ui_base')
```
3. nav_id file describe all ids that this module requires. Entry point should implement all those ids.

`./src/main/res/values/nav_web.xml`

4. Add next nav graphs to main navigation graph

```xml
<include app:graph="@navigation/nav_splash" />
```

5. The following action should be added into the root navigation graph otherwise navigation actions won't work

```xml
<action
    android:id="@+id/action_global_toWebF"
    app:destination="@id/nav_web">
    <argument
        android:name="url"
        android:defaultValue="<!-- Any default url -->"
        app:argType="string" />
    <argument
        android:name="refresh"
        app:argType="boolean" />
</action>
```

