package ua.gov.diia.core.di.data_source.http

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthorizedClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UnauthorizedClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProlongClient