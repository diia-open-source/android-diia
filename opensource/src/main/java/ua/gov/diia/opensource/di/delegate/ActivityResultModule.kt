package ua.gov.diia.opensource.di.delegate

import android.content.Context
import androidx.activity.result.ActivityResultRegistry
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import ua.gov.diia.opensource.ui.activities.MainActivity

@Module
@InstallIn(FragmentComponent::class)
class ActivityResultModule {

    @Provides
    fun provideResult(
        @ActivityContext context: Context
    ): ActivityResultRegistry = (context as MainActivity).activityResultRegistry
}