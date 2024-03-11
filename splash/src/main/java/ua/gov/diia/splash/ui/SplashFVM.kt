package ua.gov.diia.splash.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.core.models.DiiaError
import ua.gov.diia.core.models.UserType
import ua.gov.diia.core.network.apis.ApiAuth
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.core.util.work.WorkScheduler
import ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepository
import ua.gov.diia.splash.R
import ua.gov.diia.splash.helper.SplashHelper
import ua.gov.diia.splash.model.SplashJob
import ua.gov.diia.splash.ui.compose.SplashScreenData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import javax.inject.Inject

@HiltViewModel
class SplashFVM @Inject constructor(
    @UnauthorizedClient private val apiAuth: ApiAuth,
    private val authorizationRepository: AuthorizationRepository,
    private val splashHelper: SplashHelper,
    private val workManager: WorkManager,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val retryLastAction: WithRetryLastAction,
    private val crashlytics: WithCrashlytics,
    private val worksToSchedule: Set<@JvmSuppressWildcards WorkScheduler>,
) : ViewModel(),
    WithRetryLastAction by retryLastAction,
    WithErrorHandlingOnFlow by errorHandling {

    private val _navigation = MutableSharedFlow<NavigationPath>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigation = _navigation.asSharedFlow()
    val uiData = mutableStateOf(
        SplashScreenData(
            UiText.StringResource(R.string.splash_screen_title_text),
            ANIMATION_END_KEY
        )
    )

    private var serviceUserUuid: String? = null
    private var serviceUserToken: String? = null

    private val isAuthServiceUserFlow: Boolean
        get() = serviceUserUuid != null

    private val splashActor: SendChannel<ActorEvent> = viewModelScope.splashActor()

    private fun CoroutineScope.splashActor() = actor<ActorEvent> {

        val jobsToComplete = mutableListOf(SplashJob.ANIMATION)

        consumeEach { event ->
            when (event) {
                is ActorEvent.AddJob -> {
                    jobsToComplete.add(event.job)
                }

                is ActorEvent.MarkAsComplete -> {
                    jobsToComplete.remove(event.job)
                    processJobs(jobsToComplete)
                }

                ActorEvent.ContinueWithJobs -> processJobs(jobsToComplete)
            }
        }
    }

    fun doInit(
        skipInitialization: Boolean,
        serviceUserUuid: String?
    ) {
        this.serviceUserUuid = serviceUserUuid

        if (serviceUserUuid != null) {
            startLoginServiceUserFlow()
        } else {
            if (!skipInitialization) {
                setupAppVersionVerificationServices()
            }
        }
    }

    private fun setupAppVersionVerificationServices() {
        viewModelScope.launch {
            splashActor.send(ActorEvent.AddJob(SplashJob.APP_CHECK))
            scheduleWorkers()
            splashActor.send(ActorEvent.MarkAsComplete(SplashJob.APP_CHECK))
        }
    }

    private fun startLoginServiceUserFlow() {
        viewModelScope.launch {
            splashActor.send(ActorEvent.AddJob(SplashJob.SERVICE_USER_AUTHORIZATION))
            loginAsServiceUser()
            setupAppVersionVerificationServices()
        }
    }

    private fun loginAsServiceUser() {
        executeActionOnFlow {
            val uuid = serviceUserUuid ?: return@executeActionOnFlow
            val mobileUuid = authorizationRepository.getMobileUuid()
            val tokenData = apiAuth.getServiceAccountToken(uuid, mobileUuid)

            if (tokenData.template == null) {
                serviceUserToken = tokenData.token
                splashActor.send(ActorEvent.MarkAsComplete(SplashJob.SERVICE_USER_AUTHORIZATION))
            } else {
                crashlytics.sendNonFatalError(IllegalStateException("Service user token is null"))
            }
        }
    }

    /**
     * Used to resume the Splash screen navigation when the user came back after
     * [AlreadyAuthorizedError] has been shown.
     */
    fun resumeSplashJobs() {
        viewModelScope.launch {
            splashActor.send(ActorEvent.ContinueWithJobs)
        }
    }

    private fun markSplashJobComplete(job: SplashJob) {
        viewModelScope.launch {
            splashActor.send(ActorEvent.MarkAsComplete(job))
        }
    }

    private suspend fun processJobs(jobs: List<SplashJob>) {
        if (jobs.isEmpty()) {
            executeActionOnFlow {
                if (isAuthServiceUserFlow) {
                    _navigation.tryEmit(Navigation.ToPinCreation)
                } else {
                    viewModelScope.launch {
                        when (authorizationRepository.getUserType()) {
                            UserType.PRIMARY_USER -> resolvePrimaryUserNavigation()
                            UserType.SERVICE_USER -> {
                                _navigation.tryEmit(Navigation.ToProtection)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Util function which encapsulates complex primary user navigation logic for readability
     * purpose
     */
    private fun resolvePrimaryUserNavigation() {
        executeActionOnFlow {
            if (splashHelper.isProtectionExists()) {
                _navigation.tryEmit(Navigation.ToProtection)
            } else {
                _navigation.tryEmit(Navigation.ToLogin)
            }
        }
    }

    fun setServiceUserPin(pin: String) {
        val authToken = serviceUserToken ?: return
        executeActionOnFlow {
            authorizeServiceUser(pin, authToken)
            _navigation.tryEmit(Navigation.ToQrScanner)
        }
    }

    private suspend fun authorizeServiceUser(pin: String, authToken: String) {
        //splits execution to separate coroutines to speed up execution time
        coroutineScope {
            launch { authorizationRepository.setToken(authToken) }
            launch { authorizationRepository.setIsServiceUser(true) }
            launch { splashHelper.setUserAuthorized(pin) }
        }
    }

    private fun scheduleWorkers() {
        worksToSchedule.forEach {
            it.enqueue(workManager)
        }
    }

    override fun onCleared() {
        super.onCleared()
        splashActor.close()
    }

    fun onUIAction(uiAction: UIAction) {
        when (uiAction.actionKey) {
            ANIMATION_END_KEY -> {
                markSplashJobComplete(SplashJob.ANIMATION)
            }
        }
    }

    sealed class Navigation : NavigationPath {
        data class ToErrorDialog(val diiaError: DiiaError) : Navigation()
        object ToLogin : Navigation()
        object ToProtection : Navigation()
        object ToQrScanner : Navigation()
        object ToPinCreation : Navigation()
    }

    private sealed class ActorEvent {
        data class AddJob(val job: SplashJob) : ActorEvent()
        data class MarkAsComplete(val job: SplashJob) : ActorEvent()
        object ContinueWithJobs : ActorEvent()
    }

    private companion object {
        const val ANIMATION_END_KEY = "animation_end_key"
    }
}
