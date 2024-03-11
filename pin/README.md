# Description

This is module responsible for pin code input, creation and changing.

# How to install

1. Copy module folder to your project and add module to gradle dependency like this:

```groovy
implementation project(':pin')
```

2. Module requires next modules to work

```groovy
implementation project(':core')
implementation project(':ui_base')
implementation project(':diia_storage')
```

3. Add next nav graphs to main navigation graph

```xml
<include app:graph="@navigation/nav_pin_create" />
<include app:graph="@navigation/nav_pin_input" />
<include app:graph="@navigation/nav_pin_reset" />
```

4. nav_ids file describe all ids that this module requires. Entry point should implement all those ids.

`./src/main/res/values/nav_ids.xml`

5. The following actions should be added into the root navigation graph

```xml
<action
    android:id="@+id/action_global_diiaIdPinSetUp"
    app:destination="@+id/diiaIdPinSetUp" />

<action
    android:id="@+id/action_global_destination_pinInput"
    app:destination="@id/destination_pinInput" />
```

6. Entry point should implement next interfaces and provide them through Hilt DI:

`./src/main/java/ua/gov/diia/pin/helper/PinHelper.kt`
