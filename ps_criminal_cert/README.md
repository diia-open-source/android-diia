# Description

This is module responsible for criminal certificate public service.

# How to install
1. Copy module folder to your project and add module to gradle dependency like this:

```groovy
implementation project(':ps:ps_criminal_cert')
```

2. Module requires next modules to work
```groovy
implementation project(path: ':libs:ui_base')
implementation project(path: ':libs:core')
implementation project(path: ':ps:publicservice')
implementation project(path: ':features:address_search')
implementation project(path: ':features:search')
```

3. nav_id file describe all ids that this module requires. Entry point should implement all those ids.

`./src/main/res/values/nav_ids.xml`

4. Enter point should implement next interfaces and provide them through Hilt DI:

`./src/main/java/ua/gov/diia/ps_criminal_cert/helper/PSCriminalCertHelper.kt`

5. Add next nav graphs to main navigation graph
```xml
<include app:graph="@navigation/ps_criminal_cert" />
```
6. The following action should be added into the root navigation graph
```xml
<action
    android:id="@+id/action_global_to_criminal_cert"
    app:destination="@+id/nav_criminal_cert"
    app:enterAnim="@anim/slide_in_right"
    app:exitAnim="@anim/slide_out_left"
    app:popEnterAnim="@anim/slide_in_left"
    app:popExitAnim="@anim/slide_out_right">
    <argument
        android:name="contextMenu"
        android:defaultValue="@null"
        app:argType="ua.gov.diia.core.models.ContextMenuField[]"
        app:nullable="true" />
    <argument
        android:name="certId"
        android:defaultValue="@null"
        app:argType="string"
        app:nullable="true" />
</action>
```