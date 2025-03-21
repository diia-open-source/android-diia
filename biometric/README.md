# Description

This is module responsible for biometric authorization.

# How to install

1. Copy module folder to your project and add module to gradle dependency like this:

```groovy
implementation project(':libs:biometric')
```

2. Module requires next modules to work

```groovy
implementation project(':libs:core')
implementation project(':libs:ui_base')
implementation project(':libs:diia_storage')
```

3. Add next nav graphs to main navigation graph

```xml
<include app:graph="@navigation/nav_biometric" />
```

4. The following action should be added into the root navigation graph

```xml
<action
    android:id="@+id/action_global_to_setupBiometric"
    app:destination="@id/nav_biometric">
    <argument
        android:name="resultDestinationId"
        app:argType="integer" />
    <argument
        android:name="resultKey"
        app:argType="string" />
    <argument
        android:name="pin"
        app:argType="string" />
</action>
```
