package ua.gov.diia.opensource.di.delegate

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SaveFilesBase64ExternalStorage

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SaveFilesBase64InternalStorage

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ShareEmailBase64Attachments

