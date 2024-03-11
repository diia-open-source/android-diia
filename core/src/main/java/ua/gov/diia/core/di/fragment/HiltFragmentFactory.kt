package ua.gov.diia.core.di.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject
import javax.inject.Provider

@FragmentScoped
class HiltFragmentFactory @Inject constructor(
    private val providerMap: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragmentClass = loadFragmentClass(classLoader, className)

        val creator = providerMap[fragmentClass] ?: providerMap.entries.firstOrNull {
            fragmentClass.isAssignableFrom(it.key)
        }?.value

        return creator?.get() ?: super.instantiate(classLoader, className)
    }
}
