package ua.gov.diia.notifications.ui.fragments.home.notifications.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.core.di.actions.GlobalActionNotificationsPop
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.core.util.event.observeUiEvent
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.extensions.fragment.findNavControllerById
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.notifications.NavNotificationsDirections
import ua.gov.diia.notifications.R
import ua.gov.diia.notifications.models.notification.pull.MessageIdentification
import ua.gov.diia.ui_base.components.infrastructure.ServiceScreen
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFCompose : Fragment() {

    private var composeView: ComposeView? = null
    val vm: NotificationComposeVM by viewModels()

    @Inject
    @GlobalActionNotificationsPop
    lateinit var actionNotificationsPop: MutableLiveData<UiEvent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.configureTopBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm.configureBody()
        composeView = ComposeView(requireContext())
        return composeView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        composeView?.setContent {
            val topBar = vm.topBarData
            val body = vm.bodyData
            val contentLoaded = vm.contentLoaded.collectAsState(initial = Pair(
                    UIActionKeysCompose.PAGE_LOADING_LINEAR_PAGINATION, true
                )
            )
            vm.navigateTo.collectAsEffect { navigation ->
                navigate(navigation, findNavControllerById(R.id.nav_host))
            }
            vm.navigation.collectAsEffect { navigation ->
                when (navigation) {
                    is BaseNavigation.Back -> {
                        findNavController().popBackStack()
                    }

                    is NotificationsNavigation.NavigateToNotificationsSettings -> {
                        navigateToSettings()
                    }
                }
            }
            actionNotificationsPop.observeUiEvent(this){
                findNavController().popBackStack()
            }
            vm.openResource.collectAsEffect {
                navigateToDirection(it.peekContent())
            }
            vm.onMessageNotificationSelected.collectAsEffect {
                val message = it.peekContent()
                navigate(
                    NavNotificationsDirections.actionToNotificationFull(
                        messageId = MessageIdentification(
                            needAuth = true,
                            resourceId = message.resourceId ?: "",
                            notificationId = message.notificationId ?: ""
                        )
                    ),
                    findNavControllerById(R.id.nav_host)
                )
            }

            ServiceScreen(
                toolbar = topBar,
                body = body,
                contentLoaded = contentLoaded.value,
                onEvent = {
                    vm.onUIAction(it)
                })
        }
    }

    private fun navigateToSettings() {
        navigate(
            NavNotificationsDirections.actionGlobalToNotificationSettingsF(),
            findNavControllerById(R.id.nav_host)
        )
    }

    private fun navigateToDirection(item: PullNotificationItemSelection) {
        vm.navigateToDirection(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }
}