package ua.gov.diia.opensource.di.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.publicservice.models.PublicServicesCategories

@Module
@InstallIn(SingletonComponent::class)
object MoshiAdaptersModule {

    @Provides
    @MoshiAdapterPublicServiceCategories
    fun provideAdapterPublicServiceCategory(): JsonAdapter<PublicServicesCategories> =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .adapter(PublicServicesCategories::class.java)


    @Provides
    @MoshiAdapterList
    fun provideAdapterStringList(): JsonAdapter<List<String>> {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .adapter(Types.newParameterizedType(List::class.java, String::class.java))
    }





}