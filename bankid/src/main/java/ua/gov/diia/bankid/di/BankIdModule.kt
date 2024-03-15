package ua.gov.diia.bankid.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import retrofit2.Retrofit
import retrofit2.create
import ua.gov.diia.bankid.BankIdConst
import ua.gov.diia.bankid.network.ApiBankId
import ua.gov.diia.bankid.ui.VerificationMethodBankId
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.di.data_source.http.ProlongClient
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.verification.di.ProviderVerifiedClient
import ua.gov.diia.verification.di.VerificationProviderType
import ua.gov.diia.verification.ui.methods.VerificationMethod

@Module
@InstallIn(SingletonComponent::class)
interface BankIdModule {

    companion object {

        @Provides
        @UnauthorizedClient
        fun provideApiBankIdUnauthorized(
            @UnauthorizedClient retrofit: Retrofit
        ): ApiBankId = retrofit.create()


        @Provides
        @ProlongClient
        fun provideApiBankIdProlong(
            @ProlongClient retrofit: Retrofit
        ): ApiBankId = retrofit.create()

        @Provides
        @AuthorizedClient
        fun provideApiBankIdAuthorized(
            @AuthorizedClient retrofit: Retrofit
        ): ApiBankId = retrofit.create()

        @Provides
        @ProviderVerifiedClient
        fun provideApiBankId(
            providerType: VerificationProviderType,
            @AuthorizedClient apiBankIdAuthorized: ApiBankId,
            @UnauthorizedClient apiBankIdUnauthorized: ApiBankId,
            @ProlongClient apiBankIdProlong: ApiBankId
        ): ApiBankId = when (providerType) {
            VerificationProviderType.AUTHORIZED -> apiBankIdAuthorized
            VerificationProviderType.UNAUTHORIZED -> apiBankIdUnauthorized
            VerificationProviderType.PROLONG -> apiBankIdProlong
        }
    }

    @Binds
    @IntoMap
    @StringKey(BankIdConst.METHOD_BANK_ID)
    fun bindBankIdVerificationMethod(method: VerificationMethodBankId): VerificationMethod
}