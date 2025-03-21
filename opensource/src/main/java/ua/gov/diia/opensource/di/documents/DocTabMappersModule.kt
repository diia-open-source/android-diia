package ua.gov.diia.opensource.di.documents

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.opensource.ui.compose.mappers.DocumentComposeMapperImpl
import ua.gov.diia.ui_base.mappers.document.DocumentComposeMapper

@Module
@InstallIn(SingletonComponent::class)
interface DocTabMappersModule {

    @Binds
    fun bindPreviewComposeMapper(
        impl: DocumentComposeMapperImpl
    ): DocumentComposeMapper

}