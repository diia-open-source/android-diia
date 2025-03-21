package ua.gov.diia.opensource.di.data_source.http

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.di.data_source.http.ProlongClient
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.core.util.delegation.WithBuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitClientModule {

    @Provides
    @Singleton
    fun provideMoshiAdapter(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @AuthorizedClient
    @Singleton
    fun provideAuthorizedRetrofitClient(
        moshi: Moshi,
        @AuthorizedClient okHttpClient: OkHttpClient,
        withBuildConfig: WithBuildConfig,
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(withBuildConfig.getServerUrl())
        .client(okHttpClient)
        .build()

    @Provides
    @UnauthorizedClient
    @Singleton
    fun provideUnauthorizedRetrofitClient(
        moshi: Moshi,
        @UnauthorizedClient okHttpClient: OkHttpClient,
        withBuildConfig: WithBuildConfig,
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(withBuildConfig.getServerUrl())
        .client(okHttpClient)
        .build()

    @Provides
    @ProlongClient
    @Singleton
    fun provideProlongRetrofitClient(
        moshi: Moshi,
        @ProlongClient okHttpClient: OkHttpClient,
        withBuildConfig: WithBuildConfig,
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(withBuildConfig.getServerUrl())
        .client(okHttpClient)
        .build()
}