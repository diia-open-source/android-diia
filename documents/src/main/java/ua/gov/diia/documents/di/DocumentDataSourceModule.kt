package ua.gov.diia.documents.di

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.core.models.document.DocJsonAdapterDelegate
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DocumentDataSourceModule {

    @Provides
    @Singleton
    fun provideDocumentJsonAdapter(docDelegates: List<@JvmSuppressWildcards DocJsonAdapterDelegate<out DiiaDocument>>): JsonAdapter<List<DiiaDocumentWithMetadata>> {
        val moshi = Moshi.Builder().add(
            PolymorphicJsonAdapterFactory.of(DiiaDocument::class.java, "__type")
                .let {
                    return@let registerJsonAdapters(it, docDelegates)
                }
        )
            .add(KotlinJsonAdapterFactory())
            .build()
        return moshi.adapter(
            Types.newParameterizedType(
                MutableList::class.java,
                DiiaDocumentWithMetadata::class.java
            )
        )
    }


    private fun registerJsonAdapters(
        factory: PolymorphicJsonAdapterFactory<DiiaDocument>,
        delegates: List<DocJsonAdapterDelegate<out DiiaDocument>>
    ): PolymorphicJsonAdapterFactory<DiiaDocument> {
        return if (delegates.isEmpty()) factory
        else {
            val delegate = delegates.first()
            val result = factory.withSubtype(delegate.subtype, delegate.label)
            val newList = delegates.filter { it != delegate }
            registerJsonAdapters(result, newList)
        }
    }
}