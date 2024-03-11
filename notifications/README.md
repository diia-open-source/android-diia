# Description

This is module responsible for notification receiving and representation. The module implements functionality for google play and hauwey platforms.

# How to install
1. Copy module folder to your project and add module to gradle dependency like this:

```groovy
implementation project(':notifications')
```

2. Module requires next modules to work
3. 
```groovy
    implementation project(':core')
    implementation project(':analytics')
    implementation project(':diia_storage')
    implementation project(path: ':ui_base')
```

3. nav_id file describe all ids that this module requires. Entry point should implement all those ids.

`./src/main/res/values/nav_ids.xml`

4. Enter point should implement next interfaces and provide them through Hilt DI:

`./src/main/java/ua/gov/diia/notifications/helper/NotificationHelper.kt`

5. Add next nav graphs to main navigation graph

```xml
<include app:graph="@navigation/nav_notification_details" />
<include app:graph="@navigation/nav_notifications" />
<include app:graph="@navigation/nav_notification_settings" />
```

6. The following action should be added into the root navigation graph otherwise navigation actions won't work

```xml
<action
    android:id="@+id/action_global_to_notificationSettingsF"
    app:destination="@id/notificationSettingsF" />
```

7. Create PushNotificationActionModule with function that provide all notification types that will be processed by the app. and add all your notification actions into

```kotlin
@Module
@InstallIn(SingletonComponent::class)
class PushNotificationActionModule {

    @Provides
    fun providePushActionTypes(): List<@JvmSuppressWildcards BasePushNotificationAction> {
        return listOf(
            DocumentSharingPushNotificationAction(),
            PushAccessibilityNotificationAction(),
            //Other your actions
        )
    }
}
```

## Add new notification type

To implement a new notification type a class should implement BasePushNotificationAction with notification type passed in constructor as an id

```kotlin
class AppSessionNotificationAction() : BasePushNotificationAction("notification_type") {

    override fun getNavigationDirection(item: PullNotificationItemSelection): NavDirections =
        NavMainDirections
            .actionNavigateSomewhere(ConsumableItem(item))
}
```

