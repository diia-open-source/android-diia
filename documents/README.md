# Description

This is module responsible for documents and logic around it.

# How to install
1. Copy module folder to your project and add module to gradle dependency like this:

```groovy
implementation project(':doc:documents')
```

2. Module requires next modules to work
```groovy
implementation project(':libs:core')
implementation project(':libs:diia_storage')
implementation project(':libs:web')
implementation project(path: ':libs:ui_base')
```

3. nav_id file describe all ids that this module requires. Entry point should implement all those ids.

`./src/main/res/values/nav_ids.xml`

4. Enter point should implement next interfaces and provide them through Hilt DI:
`./src/main/java/ua/gov/diia/documents/data/datasource/local/DocumentsHelper.kt`
`./src/main/java/ua/gov/diia/documents/data/api/ApiDocuments.kt`
`./src/main/java/ua/gov/diia/documents/util/DocNameProvider.kt`
`./src/main/java/ua/gov/diia/documents/ui/WithPdfCertificate.kt`
`./src/main/java/ua/gov/diia/documents/ui/gallery/DocGalleryNavigationHelper.kt`
`./src/main/java/ua/gov/diia/documents/ui/WithRemoveDocument.kt`
`./src/main/java/ua/gov/diia/documents/ui/actions/DocActionsNavigationHandler.kt`
`./src/main/java/ua/gov/diia/documents/ui/DocumentComposeMapper.kt`

5. To provide custom update behaviour for new document group implement next interface and provide it through Hilt DI:
`./src/main/java/ua/gov/diia/documents/data/datasource/local/DocGroupUpdateBehavior.kt`
6. To customize documents before saving to store implement next interface and provide it through Hilt DI:
`./src/main/java/ua/gov/diia/documents/data/datasource/local/DocumentsTransformation.kt`
7. To provide custom action before docs become published implement next interface and provide it through Hilt DI:
`./src/main/java/ua/gov/diia/documents/data/repository/BeforePublishAction.kt`


8. Add next nav graphs to root navigation graph
```xml
<include app:graph="@navigation/nav_doc_actions" />
<include app:graph="@navigation/nav_doc_full_info" />
<include app:graph="@navigation/nav_doc_stack" />
<include app:graph="@navigation/nav_stack_order" />
```
9. The following actions should be added into the root navigation graph
```xml
<action
    android:id="@+id/action_global_destination_docActions"
    app:destination="@id/nav_doc_actions" >
    <argument
        android:name="doc"
        app:argType="android.os.Parcelable" />
    <argument
        android:name="position"
        android:defaultValue="-1"
        app:argType="integer" />
    <argument
        android:name="enableStackActions"
        android:defaultValue="true"
        app:argType="boolean" />
    <argument
        android:name="currentlyDisplayedOdcTypes"
        android:defaultValue="*"
        app:argType="string" />
    <argument
        android:name="manualDocs"
        android:defaultValue="@null"
        app:argType="ua.gov.diia.documents.models.ManualDocs"
        app:nullable="true" />
    <argument
        android:name="resultDestinationId"
        app:argType="integer" />
</action>
```
```xml
<action
    android:id="@+id/destination_fullDocInfoF"
    app:destination="@id/nav_doc_full_info" />
```
```xml
<action
    android:id="@+id/action_global_to_StackFCompose"
    app:destination="@id/nav_doc_stack"
    app:enterAnim="@anim/anim_fade_in"
    app:exitAnim="@anim/anim_fade_out"
    app:popEnterAnim="@anim/anim_fade_in"
    app:popExitAnim="@anim/anim_fade_out">
    <argument
        android:name="docType"
        app:argType="string"
        app:nullable="false" />
    <argument
        android:name="docColor"
        app:argType="integer"
        app:nullable="false" />
</action>
```
```xml
<action
    android:id="@+id/action_global_to_stack_order"
    app:destination="@id/nav_stack_order"
    app:enterAnim="@anim/anim_fade_in"
    app:exitAnim="@anim/anim_fade_out"
    app:popEnterAnim="@anim/anim_fade_in"
    app:popExitAnim="@anim/anim_fade_out">
    <argument
        android:name="doc"
        android:defaultValue="*"
        app:argType="string" />
</action>
```
10. Add next nav graphs to home_children navigation graph
```xml
<include app:graph="@navigation/nav_doc_gallery" />
```

11. The following actions should be added into the home_children nav graph
```xml
    <action
        android:id="@+id/global_to_gallery"
        app:destination="@id/nav_doc_gallery" />
```


## Add new document type

1. Create new android library module. Module name should start with doc_ prefix

2. Place document model and DocJsonAdapterDelegate implementation inside of it

3. Add following field inside document api response on Enter point layer

```kotlin
@Parcelize
@JsonClass(generateAdapter = true)
data class Docs(
    @Json(name = "newDoc")
    val newDoc: NewDoc?
    )
```
4. Implement response handling inside ApiDocumensWrapper on Enter point layer

```kotlin
    private suspend fun docsToDocumentWithMetadataList(docs: Docs): List<DiiaDocumentWithMetadata> {
    var docsWithMetadata = mutableListOf<DiiaDocumentWithMetadata>()

    docs.newDoc?.let {
        docsWithMetadata.addAll(groupToDocumentsWithMetadata(it, docs))
    }
    
    return docsWithMetadata
}
```
5. Provide DriverLicenseJsonAdapterDelegate in DI inside DocumentsModule on Enter point layer

```kotlin
@Provides
@Singleton
fun provideDocDelegates(): List<DocJsonAdapterDelegate<out DiiaDocument>> {
    return listOf(
        NewDocJsonAdapterDelegate()
    )
}
```
