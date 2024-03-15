package ua.gov.diia.verification.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.create
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.di.data_source.http.ProlongClient
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.core.util.delegation.WithBuildConfig
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepository
import ua.gov.diia.verification.network.ApiVerification

@Module
@InstallIn(SingletonComponent::class)
object VerificationModule {

    @Provides
    @AuthorizedClient
    fun provideApiVerificationAuthorized(
        @AuthorizedClient retrofit: Retrofit
    ): ApiVerification = retrofit.create()

    @Provides
    @UnauthorizedClient
    fun provideApiVerificationUnauthorized(
        @UnauthorizedClient retrofit: Retrofit
    ): ApiVerification = retrofit.create()

    @Provides
    @ProlongClient
    fun provideApiVerificationProlong(
        @ProlongClient retrofit: Retrofit
    ): ApiVerification = retrofit.create()

    @Provides
    @ProviderVerifiedClient
    fun provideApiVerification(
        providerType: VerificationProviderType,
        @AuthorizedClient apiVerificationAuthorized: ApiVerification,
        @UnauthorizedClient apiVerificationUnauthorized: ApiVerification,
        @ProlongClient apiVerificationProlong: ApiVerification
    ): ApiVerification = when (providerType) {
        VerificationProviderType.AUTHORIZED -> apiVerificationAuthorized
        VerificationProviderType.UNAUTHORIZED -> apiVerificationUnauthorized
        VerificationProviderType.PROLONG -> apiVerificationProlong
    }

    @Provides
    fun getVerificationProviderType(
        authorizationRepository: AuthorizationRepository,
        withBuildConfig: WithBuildConfig,
        withCrashlytics: WithCrashlytics,
    ): VerificationProviderType {
        val tokenData = try {
            runBlocking { authorizationRepository.getTokenData() }
        } catch (e: Exception) {
            withCrashlytics.sendNonFatalError(e)
            null
        }

        return if (tokenData != null) {
            if (tokenData.isExpired(withBuildConfig.getTokenLeeway()) && !tokenData.isEmptyToken) {
                VerificationProviderType.PROLONG
            } else {
                if (tokenData.isEmptyToken) {
                    VerificationProviderType.UNAUTHORIZED
                } else {
                    VerificationProviderType.AUTHORIZED
                }
            }
        } else {
            VerificationProviderType.UNAUTHORIZED
        }
    }
}