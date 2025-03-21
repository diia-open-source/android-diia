package ua.gov.diia.core.di.fragment

import androidx.fragment.app.Fragment
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentKey(val value: KClass<out Fragment>)
