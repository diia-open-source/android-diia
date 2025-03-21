# Description

This is module responsible user verification. It provides base functionality for such verification 
flows as authorization, session prolongation, etc.

# How to install

1. Copy module folder to your project and add module to gradle dependency like this:

```groovy
implementation project(':features:verification')
```

2. Module requires next modules to work

```groovy
implementation project(':libs:core')
implementation project(':libs:diia_storage')
```

3. Add next nav graphs to main navigation graph

```xml
<include app:graph="@navigation/nav_verification" />
```

4. nav_ids file describe all ids that this module requires. Entry point should implement all those ids.

`./src/main/res/values/nav_ids.xml`

## Implementing verification methods

To create a new verification method that will be responsible for user verification you have to
implement the `VerificationMethod` abstract class and provide it via di:

```kotlin
@Binds
@IntoMap
@StringKey(METHOD_NAME)
fun bindVerificationMethodImpl(method: VerificationMethodImpl): VerificationMethod
```

You can provide multiple verification methods in this way.