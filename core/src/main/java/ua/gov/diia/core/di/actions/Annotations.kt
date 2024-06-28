package ua.gov.diia.core.di.actions

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlobalActionLogout

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlobalActionDeeplink

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlobalActionAllowAuthorizedLinks

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlobalActionDocLoadingIndicator

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlobalActionDocumentBackground

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlobalActionConfirmDocumentRemoval

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlobalActionFocusOnDocument

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlobalActionSelectedMenuItem

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlobalActionLazy

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlobalActionNetworkState

//TODO potential move to notficiation package
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlobalActionNotificationRead

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlobalActionNotificationReceived

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlobalActionNotificationsPop