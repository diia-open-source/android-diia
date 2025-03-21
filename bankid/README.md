# Description

This is module responsible for BankID verification method.

# How to install

1. Copy module folder to your project and add module to gradle dependency like this:

```groovy
implementation project(':features:bankid')
```

2. Module requires next modules to work

```groovy
implementation project(':libs:core')
implementation project(':features:verification')
implementation project(':libs:ui_base')
```

3. Add next nav graphs to main navigation graph

```xml
<include app:graph="@navigation/nav_bankid" />
```

4. The following action should be added into the root navigation graph

```xml
<action 
    android:id="@+id/action_global_destination_bankSelection"
    app:destination="@id/nav_bankid" />
```
