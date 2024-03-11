# Description

This is module responsible for user authorization.

# How to install

1. Copy module folder to your project and add module to gradle dependency like this:

```groovy
implementation project(':login')
```

2. Module requires next modules to work

```groovy
implementation project(':core')
implementation project(':web')
implementation project(':pin')
implementation project(':verification')
implementation project(':ui_base')
implementation project(':diia_storage')
```

3. Add next nav graphs to main navigation graph

```xml
<include app:graph="@navigation/nav_login" />
```

4. nav_ids file describe all ids that this module requires. Entry point should implement all those ids.

`./src/main/res/values/nav_ids.xml`

## Implementing login hook (optional)

To create a new action that will be executed after success login you have to implement 
the `PostLoginAction` interface and provide it via di:

```kotlin
@Binds
@IntoSet
fun bindPostLoginAction(
    impl: PostLoginActionImpl
): PostLoginAction
```

You can provide multiple actions in this way.