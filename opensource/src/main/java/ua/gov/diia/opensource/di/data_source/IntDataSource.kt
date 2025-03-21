package ua.gov.diia.opensource.di.data_source

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import ua.gov.diia.diia_storage.store.datasource.itn.ItnDataRepository
import ua.gov.diia.opensource.util.store.datasource.itn.ItnDataRepositoryImpl
import ua.gov.diia.opensource.util.store.datasource.itn.KeyValueItnDataSource
import ua.gov.diia.opensource.util.store.datasource.itn.NetworkItnDataSource
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IntDataSource {

    @Provides
    @Singleton
    fun bindIntDataSource(
        network: NetworkItnDataSource,
        keyValueInt: KeyValueItnDataSource
    ): ItnDataRepository {
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val applicationScope = CoroutineScope(SupervisorJob() + dispatcher)
        return ItnDataRepositoryImpl(applicationScope, keyValueInt, network)
    }
}