package ua.gov.diia.documents.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DocTypesAvailableToUsers

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlobalActionUpdateDocument