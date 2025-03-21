package ua.gov.diia.opensource.di.actions

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlobalActionProlongUser

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlobalActionForceAppUpdate

