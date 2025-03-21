package ua.gov.diia.opensource.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ua.gov.diia.core.di.actions.GlobalActionLogout
import ua.gov.diia.core.models.deeplink.DeepLinkActionViewMessage
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.core.util.event.observeUiEvent
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.notifications.NavNotificationsDirections
import ua.gov.diia.notifications.models.notification.pull.MessageIdentification
import ua.gov.diia.opensource.NavMainDirections
import ua.gov.diia.opensource.R
import ua.gov.diia.opensource.di.actions.GlobalActionProlongUser
import ua.gov.diia.opensource.util.extensions.activity.adjustFontScale
import ua.gov.diia.opensource.util.extensions.activity.navigate
import ua.gov.diia.opensource.util.extensions.activity.overrideConfiguration
import ua.gov.diia.opensource.util.extensions.activity.setUpEdgeToEdge
import javax.inject.Inject

@AndroidEntryPoint
abstract class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var diiaStorage: DiiaStorage

    @Inject
    lateinit var crashlytics: WithCrashlytics

    @Inject
    @GlobalActionLogout
    lateinit var actionLogout: MutableLiveData<UiEvent>

    @Inject
    @GlobalActionProlongUser
    lateinit var actionUserVerification: MutableLiveData<UiDataEvent<TemplateDialogModel>>

    private val vm by viewModels<MainActivityVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Diia_NoActionBar)
        setUpEdgeToEdge()
        adjustFontScale(resources.configuration)

        setUpAnalytics()
        setContentView(R.layout.activity_main)

        processIntent(intent)

        actionLogout.observeUiEvent(this, vm::doLogout)

        vm.apply {

            lifecycleScope.launchWhenStarted {
                deeplinkFlow.collectLatest {
                    val data = it?.peekContent() ?: return@collectLatest
                    //unauthorized message processing authorized is in HomeF
                    if (data is DeepLinkActionViewMessage) {
                        if (!data.needAuth) {
                            navigate(
                                NavNotificationsDirections.actionToNotificationFull(
                                    messageId = MessageIdentification(
                                        needAuth = data.needAuth,
                                        notificationId = data.resourceId,
                                        resourceId = data.resourceId,
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }

        vm.restartApp.observeUiEvent(this, ::restartMainNavGraph)

        actionUserVerification.observeUiDataEvent(this) { template ->
            navigate(
                NavMainDirections.actionGlobalToTemplateDialog(
                    template.copy(key = ActionsConst.KEY_GLOBAL_PROCESSING)
                )
            )
        }
        vm.checkPinCount()
    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        overrideConfiguration(newBase)
    }

    private fun restartMainNavGraph(
        skipInitialization: Boolean = true,
        serviceUserUUID: String? = null
    ) {
        NavMainDirections.actionGlobalToSplashClearStack(
            skipInitialization = skipInitialization,
            uuid4 = serviceUserUUID
        ).let { navController.navigate(it) }
    }

    private val navController: NavController
        get() = findNavController(R.id.nav_host)

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        processIntent(intent)
    }

    private fun processIntent(intent: Intent?) {
        val path = intent?.data?.path
        if (path?.startsWith("/auth") == false) {
            //after push is clicked, navigate to HomeF as there we handle all nav logic
            if (vm.allowAuthorizedDeepLinks) {
                navController.popBackStack(R.id.homeF, false)
            }
            vm.processIntentPath(path)
        }
    }

    abstract fun setUpAnalytics()

    override fun onDestroy() {
        super.onDestroy()
        try {
            WorkManager.getInstance(this).cancelUniqueWork("checkVersionWork")
        } catch (e: Exception) {
            crashlytics.sendNonFatalError(e)
        }
    }
}