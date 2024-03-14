package ua.gov.diia.home.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.core.CoreConstants.CHECK_SAFETY_NET
import ua.gov.diia.home.helper.HomeHelper
import ua.gov.diia.ui_base.models.homescreen.HomeMenuItemConstructor
import ua.gov.diia.core.util.delegation.Permission
import ua.gov.diia.core.util.delegation.WithPermission
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.core.util.event.observeUiEvent
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.core.util.navigation.KeepStateNavigator
import ua.gov.diia.home.NavHomeDirections
import ua.gov.diia.home.R
import ua.gov.diia.home.databinding.FragmentHomeBinding
import ua.gov.diia.ui_base.components.infrastructure.screen.TabBarRootContainer
import javax.inject.Inject

@AndroidEntryPoint
class HomeF @Inject constructor(
    private val permission: WithPermission,
    private val homeHelper: HomeHelper
) : Fragment(),
    WithPermission by permission {

    private var _navController: NavController? = null

    private fun obtainNavController(): NavController {
        val controller = _navController

        return if (controller == null) {
            val navHostFragment = childFragmentManager.fragments
                .find { it is NavHostFragment } as NavHostFragment

            navHostFragment.navController.also { _navController = it }
        } else {
            controller
        }
    }

    private val viewModel: HomeVM by viewModels()

    private var binding: FragmentHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super<Fragment>.onCreate(savedInstanceState)
        lifecycle.addObserver(permission)

        lifecycleScope.launchWhenStarted {
            viewModel.handleDeepLinks()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            composeTabBar.apply {
                setContent {
                    TabBarRootContainer(
                        tabBarViews = viewModel.bottomData,
                        onUIAction = {
                            viewModel.onUIAction(it)
                        }
                    )
                }
            }
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        viewModel.checkNotificationsRequested()

        viewModel.showTemplateDialog.observeUiDataEvent(viewLifecycleOwner, ::openTemplateDialog)

        viewModel.allowAuthorizedDeepLinks()

        viewModel.apply {
            notificationsRequested.observeUiDataEvent(viewLifecycleOwner) { requested ->
                if (!requested) {
                    checkNotificationEnabled()
                }
            }

            showTemplate.observeUiDataEvent(viewLifecycleOwner) {
                openTemplateDialog(it)
            }
            processNavigation.observeUiDataEvent(viewLifecycleOwner) {
                navigate(it)
            }
            viewModel.selectedMenuItem.observe(viewLifecycleOwner) {
                onNavigationTransactionComplete(it?.peekContent())
            }
        }
        return binding?.root
    }

    private fun onNavigationTransactionComplete(currentNavItem: HomeMenuItemConstructor?) {
        binding?.apply {
            if (currentNavItem?.position == HomeActions.HOME_DOCUMENTS) {
                gradientBg.visibility = View.VISIBLE
                gradientBg.setAnimation(R.raw.gradient_bg)
                gradientBg.playAnimation()
                gradientBg.scaleType = ImageView.ScaleType.FIT_XY
            } else {
                gradientBg.visibility = View.GONE

            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inflateHomeChildNavigator()

        doOnCameraPermissionGrantedEvent.observeUiEvent(
            viewLifecycleOwner,
            ::navigateToQrScannerDestination
        )
        doOnPostNotificationPermissionGrantedEvent.observeUiEvent(viewLifecycleOwner) {
            viewModel.allowNotifications()
        }
        doOnPermissionDeniedEvent.observeUiEvent(viewLifecycleOwner) {
            viewModel.denyNotifications()
        }

        viewModel.selectedMenuItem.observe(viewLifecycleOwner) {
            it?.peekContent()?.let { menuItem ->
                navigate(homeHelper.getNavDirection(menuItem.position), obtainNavController())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun inflateHomeChildNavigator() {
        val navigator = KeepStateNavigator(
            context = requireContext(),
            manager = childFragmentManager,
            containerId = R.id.home_content
        ) {
            val menuItem = homeHelper.getNavMenuItem(it)
        }

        with(obtainNavController()) {
            navigatorProvider.addNavigator(navigator)

            setGraph(homeHelper.getGraphId())
        }
    }


    override fun onResume() {
        super<Fragment>.onResume()
        LocalBroadcastManager
            .getInstance(requireContext())
            .sendBroadcast(Intent(CHECK_SAFETY_NET))
    }

    private fun navigateToQrScannerDestination() {
        navigate(NavHomeDirections.actionGlobalToQrScanF())
    }

    private fun checkNotificationEnabled() {
        if (NotificationManagerCompat.from(requireContext()).areNotificationsEnabled().not()) {
            approvePermission(Permission.POST_NOTIFICATIONS)
        }
    }

    private companion object {
        const val ACTION_PROMO_DO_NOT_SHOW = "doNotShow"
        const val ACTION_PROMO_SUBSCRIBE = "serviceSubscribe"
    }
}
