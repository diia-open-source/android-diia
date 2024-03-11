# Description

This is module responsible for Driver license document implementation.

# How to install

1. Copy module folder to your project and add module to gradle dependency like this:

```groovy
implementation project(':doc_driver_license')
```

2. Module requires next modules to work

```groovy
implementation project(path: ':ui_base')
implementation project(path: ':core')
implementation project(path: ':documents')
```

3. Add following field inside document api response

```kotlin
@Parcelize
@JsonClass(generateAdapter = true)
data class Docs(
    @Json(name = "driverLicense")
    val driverLicense: DriverLicenseV2?
    )
```
4. Implement response handling inside ApiDocumensWrapper on Enter point layer

```kotlin
    private suspend fun docsToDocumentWithMetadataList(docs: Docs): List<DiiaDocumentWithMetadata> {
    var docsWithMetadata = mutableListOf<DiiaDocumentWithMetadata>()

    docs.driverLicense?.let {
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
        DriverLicenceJsonAdapterDelegate()
    )
}
```
