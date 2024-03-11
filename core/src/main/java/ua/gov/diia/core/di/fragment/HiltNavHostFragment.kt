package ua.gov.diia.core.di.fragment

import android.content.Context
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HiltNavHostFragment : NavHostFragment(){

    @Inject
    lateinit var hiltFragmentFactory: HiltFragmentFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        childFragmentManager.fragmentFactory = hiltFragmentFactory
    }
}
