package ua.gov.diia.opensource.di.actions

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.gov.diia.core.di.actions.GlobalActionLazy
import ua.gov.diia.core.models.ActionDataLazy
import ua.gov.diia.core.util.event.UiDataEvent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DebugGlobalActionModule {

    @Provides
    @Singleton
    @GlobalActionLazy
    fun provideActionLazy() = MutableSharedFlow<UiDataEvent<ActionDataLazy>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
}