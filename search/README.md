# Description

This module is responsible search screen and logic around it

# How to install

1. Copy module folder to your project and add module to gradle dependency like
   this:

```groovy
implementation project(':search')
```

2. Module requires next modules to work

```groovy
implementation project(':core')
implementation project(':ui_base')
```

3. Add next nav graphs to main navigation graph

```xml
<include app:graph="@navigation/nav_search" />
<include app:graph="@navigation/nav_search_bullet" />
```

4. The following actions should be added into the root navigation graph

 ```xml

<action 
    android:id="@+id/action_itemHomeF_to_destination_searchF"
    app:destination="@id/nav_search">
    <argument 
        android:name="key" 
        app:argType="string" />
    <argument 
        android:name="searchableList" 
        android:defaultValue="@null"
        app:argType="ua.gov.diia.search.models.SearchableItem[]"
        app:nullable="true" />
</action>
```

 ```xml

<action app:enterAnim="@anim/slide_in_right" 
    app:exitAnim="@anim/slide_out_left"
    app:popEnterAnim="@anim/slide_in_left"
    app:popExitAnim="@anim/slide_out_right"
    android:id="@+id/action_destination_itemSearch_to_destination_searchBulletF"
    app:destination="@id/nav_search_bullet">
    <argument 
        android:name="screenHeader" 
        app:argType="string" />
    <argument 
        android:name="contentTitle" 
        app:argType="string" />
    <argument 
        android:name="resultKey" 
        app:argType="string" />
    <argument 
        android:name="data"
        app:argType="ua.gov.diia.search.models.SearchableBullet[]" />
</action>
```
