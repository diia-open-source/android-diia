# Description

This is module responsible for public services receiving and representation.

# How to install
1. Copy module folder to your project and add module to gradle dependency like this:

```groovy
implementation project(':publicservice')
```

2. Module requires next modules to work
```groovy
implementation project(path: ':ui_base')
implementation project(path: ':core')
implementation project(path: ':diia_storage')
```

3. Enter point should implement next interfaces and provide them through Hilt DI:

```
./src/main/java/ua/gov/diia/publicservice/helper/PSNavigationHelper.kt
./src/main/java/ua/gov/diia/publicservice/helper/PublicServiceHelper.kt
```

4. Add next nav graph to nav_home_children navigation graph
```xml
<include app:graph="@navigation/nav_public_service_categories" />
```
5. The following action should be added into the nav_home_children navigation graph

```xml
    <action
        android:id="@+id/global_to_publicServicesFCompose"
        app:destination="@id/nav_public_service_categories" />
```

6. Add next nav graphs to root navigation graph

```xml
<include app:graph="@navigation/nav_public_service_category_details" />
<include app:graph="@navigation/nav_public_service_search" />
```

7. The following actions should be added into the root navigation graph

```xml
    <action
    android:id="@+id/action_global_destination_categoryDetailsCompose"
    app:destination="@id/nav_public_service_category_details"
    app:enterAnim="@anim/slide_in_right"
    app:exitAnim="@anim/slide_out_left"
    app:popEnterAnim="@anim/slide_in_left"
    app:popExitAnim="@anim/slide_out_right">
    <argument
        android:name="category"
        app:argType="ua.gov.diia.publicservice.models.PublicServiceCategory" />
    <argument
        android:name="resultDestinationId"
        app:argType="integer" />
</action>
```
```xml
    <action
        android:id="@+id/action_global_destination_psSearchCompose"
        app:destination="@id/nav_public_service_search"
        app:enterAnim="@anim/slide_in_right"
        app:exitAnim="@anim/slide_out_left"
        app:popEnterAnim="@anim/slide_in_left"
        app:popExitAnim="@anim/slide_out_right">
        <argument
            android:name="arbitraryDestinationId"
            app:argType="integer" />
        <argument
            android:name="categories"
            app:argType="ua.gov.diia.publicservice.models.PublicServiceCategory[]" />
    </action>
```