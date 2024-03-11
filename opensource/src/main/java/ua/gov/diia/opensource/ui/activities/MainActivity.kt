package ua.gov.diia.opensource.ui.activities

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
import ua.gov.diia.opensource.NavMainXmlDirections
import ua.gov.diia.opensource.R
import ua.gov.diia.opensource.di.GlobalActionProlongUser
import ua.gov.diia.opensource.util.ext.navigate
import ua.gov.diia.opensource.util.setUpEdgeToEdge
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

    private val viewModel by viewModels<MainActivityVM>()

    private val navController: NavController
        get() = findNavController(R.id.nav_host)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Diia_NoActionBar)
        setUpEdgeToEdge()
        super.onCreate(savedInstanceState)
        adjustFontScale(resources.configuration)
        setUpAnalytics()
        setContentView(R.layout.activity_main)
        actionLogout.observeUiEvent(this, viewModel::doLogout)
        viewModel.restartApp.observeUiEvent(this, ::restartMainNavGraph)
        viewModel.apply {
            timeoutDestination.observeUiDataEvent(this@MainActivity) {
                restartMainNavGraph()
            }

            deeplinkFlow.flowWithLifecycle(lifecycle)
                .onEach {
                    val data = it?.peekContent() ?: return@onEach

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
                }.launchIn(lifecycleScope)
        }
        actionUserVerification.observeUiDataEvent(this) { template ->
            navigate(
                NavMainXmlDirections.actionGlobalToTemplateDialog(
                    template.copy(key = ActionsConst.KEY_GLOBAL_PROCESSING)
                )
            )
        }
        processIntent(intent)
        viewModel.checkPinCount()
    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        overrideConfiguration(newBase)
    }

    private fun adjustFontScale(configuration: Configuration) {
        if (Build.VERSION.SDK_INT < 27) {
            configuration.fontScale = 1.0f
            val metrics: DisplayMetrics = resources.displayMetrics
            val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
    }

    private fun overrideConfiguration(context: Context?) {
        if (Build.VERSION.SDK_INT >= 27) {
            val newOverride = Configuration(context?.resources?.configuration)
            newOverride.fontScale = 1.0f
            applyOverrideConfiguration(newOverride)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        processIntent(intent)
    }

    private fun restartMainNavGraph(
        skipInitialization: Boolean = true,
        serviceUserUUID: String? = null,
    ) {
        NavMainXmlDirections.actionGlobalToSplashClearStack(
            skipInitialization = skipInitialization,
            uuid4 = serviceUserUUID
        ).let { navController.navigate(it) }
    }

    private fun processIntent(intent: Intent?) {
        val path = intent?.data?.path
        if (path?.startsWith("/auth") == false) {
            //after push is clicked, navigate to HomeF as there we handle all nav logic
            if (viewModel.allowAuthorizedDeepLinks) {
                navController.popBackStack(R.id.homeF, false)
            }
            viewModel.processIntentPath(path)
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