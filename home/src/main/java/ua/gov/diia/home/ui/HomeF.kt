package ua.gov.diia.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ua.gov.diia.core.models.ConsumableString
import ua.gov.diia.core.models.common.BackStackEvent
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.delegation.Permission
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.delegation.WithPermission
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.core.util.event.observeUiEvent
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResult
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult
import ua.gov.diia.documents.navigation.DocumentsHomeNavigation
import ua.gov.diia.documents.ui.gallery.ScreenDocGalleryRoute
import ua.gov.diia.documents.ui.gallery.docGalleryGraphBuilder
import ua.gov.diia.feed.ScreenFeedRoute
import ua.gov.diia.feed.feedNavGraph
import ua.gov.diia.feed.navigation.FeedHomeNavigation
import ua.gov.diia.home.NavHomeDirections
import ua.gov.diia.home.R
import ua.gov.diia.home.model.HomeMenuItem
import ua.gov.diia.home.ui.views.HomeBottomBar
import ua.gov.diia.menu.navigation.MenuHomeNavigation
import ua.gov.diia.menu.ui.ScreenMenuRoute
import ua.gov.diia.menu.ui.menuNavGraph
import ua.gov.diia.publicservice.navigation.PublicServiceHomeNavigation
import ua.gov.diia.publicservice.ui.categories.compose.PublicServicesCategoriesRoute
import ua.gov.diia.publicservice.ui.categories.compose.publicServicesCategoriesNavGraph
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.fragments.dialog.system.DiiaSystemDFVM
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import javax.inject.Inject

@AndroidEntryPoint
class HomeF @Inject constructor(
    private val permission: WithPermission
) : Fragment(),
    WithPermission by permission {

    @Inject
    lateinit var withCrashlytics: WithCrashlytics

    private val args: HomeFArgs by navArgs()
    private var composeView: ComposeView? = null

    private val viewModel: HomeVM by viewModels()
    private val systemDialogVM: DiiaSystemDFVM by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super<Fragment>.onCreate(savedInstanceState)
        lifecycle.addObserver(permission)

        lifecycleScope.launchWhenStarted {
            viewModel.handleDeepLinks()
        }
        viewModel.handleNavigationFlowArgs(args.launchFlow)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            processNavigation.observeUiDataEvent(viewLifecycleOwner) { action ->
                action.apply {
                    navigate(action)
                }
            }
            selectedMenuItem.asLiveData().observe(viewLifecycleOwner) { action ->
                action?.peekContent().let {
                    if (it == HomeMenuItem.DOCUMENTS) {
                        //should be called before DocGallery appearing
                        viewModel.fetchDocs()
                    }
                }
            }
        }
        composeView = ComposeView(requireContext())
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeNavigationActionFlow.collectLatest {
                    when (it) {
                        is FeedHomeNavigation -> {
                            handleFeedNavigation(it)
                        }

                        is PublicServiceHomeNavigation -> {
                            handlePublicServiceNavigation(it)
                        }

                        is MenuHomeNavigation -> {
                            handleMenuNavigation(it)
                        }

                        is DocumentsHomeNavigation -> {
                            handleDocumentsNavigation(it)
                        }
                    }
                }
            }
        }

        composeView?.setContent {
            val localDensity = LocalDensity.current
            val bottomSystemNavBarHeight = with(LocalDensity.current) {
                WindowInsets.navigationBars.getBottom(localDensity).toDp()
            }
            val selectedMenuItem = viewModel.selectedMenuItem.collectAsState(initial = null)
            val bottomBar = viewModel.bottomBar.collectAsState()
            val navController = rememberNavController()
            val navBackStackEntry = navController.currentBackStackEntryAsState().value
            val displayDynamicBackground by remember {
                derivedStateOf {
                    (selectedMenuItem.value?.peekContent()?.position == HomeActions.HOME_DOCUMENTS)
                            || navBackStackEntry?.destination?.navigatorName == ScreenDocGalleryRoute.javaClass.name
                }
            }


            Box(modifier = Modifier.fillMaxSize()) {
                HomeBackground(animated = displayDynamicBackground)
                bottomBar.value?.let { data ->
                    Scaffold(modifier = Modifier.background(Color.Transparent),
                        containerColor = Color.Transparent,
                        contentWindowInsets = WindowInsets(0.dp),
                        bottomBar = {
                            HomeBottomBar(
                                modifier = Modifier.height(
                                    bottomSystemNavBarHeight + 70.dp
                                ),
                                data = data,
                                onTabClick = { action ->
                                    viewModel.onUIAction(action)
                                    val tab = action.data
                                    if (tab == HomeActions.HOME_DOCUMENTS.toString()) {
                                        viewModel.fetchDocs()
                                    }
                                })
                        }, content = { padding ->
                            NavHost(
                                modifier = Modifier.padding(padding),
                                navController = navController,
                                enterTransition = {
                                    fadeIn(animationSpec = tween(250))
                                },
                                exitTransition = {
                                    fadeOut(animationSpec = tween(250))
                                },
                                startDestination = ScreenFeedRoute
                            ) {
                                feedNavGraph(
                                    navigationBackStackEventFlow = viewModel.navigationBackStackEventFlow,
                                    homeNavigationActionFlow = viewModel.homeNavigationActionFlow
                                )
                                docGalleryGraphBuilder(
                                    withCrashlytics = withCrashlytics,
                                    navigationBackStackEventFlow = viewModel.navigationBackStackEventFlow,
                                    homeNavigationActionFlow = viewModel.homeNavigationActionFlow
                                )
                                //todo: discuss and maybe add as required to all home tabs
                                publicServicesCategoriesNavGraph(
                                    navigationBackStackEventFlow = viewModel.navigationBackStackEventFlow,
                                    homeNavigationActionFlow = viewModel.homeNavigationActionFlow
                                )
                                menuNavGraph(
                                    systemDFViewModel = systemDialogVM,
                                    navigationBackStackEventFlow = viewModel.navigationBackStackEventFlow,
                                    homeNavigationActionFlow = viewModel.homeNavigationActionFlow
                                )
                            }
                        })

                    //Navigate to selected tab
                    val selectedTabId = data.tabs.find {
                        it.selectionState == UIState.Selection.Selected
                    }?.id
                    if (selectedTabId != null && navController.currentDestination != null) {
                        LaunchedEffect(key1 = selectedTabId) {
                            navigateToTab(navController, selectedTabId)
                        }
                    }
                }
            }
        }

        registerForNavigationResult<ConsumableString>(ActionsConst.ACTION_ITEM_SELECTED) { event ->
            event.consumeEvent { action ->
                when (action) {
                    ActionsConst.ACTION_MENU_FEED -> viewModel.setSelectedMenuItem(HomeMenuItem.FEED)
                    ActionsConst.ACTION_MENU_SERVICES -> viewModel.setSelectedMenuItem(HomeMenuItem.SERVICES)
                    ActionsConst.ACTION_MENU_DOCUMENTS -> viewModel.setSelectedMenuItem(HomeMenuItem.DOCUMENTS)
                    ActionsConst.ACTION_MENU_MENU -> viewModel.setSelectedMenuItem(HomeMenuItem.MENU)
                }
            }
        }

        registerForNavigationResult<ConsumableString>(
            ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
            viewLifecycleOwner
        ) { event ->
            viewModel.navigationBackStackEventFlow.tryEmit(
                BackStackEvent.UserActionResult(event)
            )
        }

        viewModel.navigationSubscriptionHandler.subscribeForNavigationEvents(
            fragment = this,
            navigationBackStackEventFlow = viewModel.navigationBackStackEventFlow
        )
    }

    private fun handleMenuNavigation(navigation: MenuHomeNavigation) {
        when (navigation) {
            is MenuHomeNavigation.ToAppSession -> {
                navigation.consumeEvent {
                    viewModel.navigateToAppSessions(this@HomeF)
                }
            }

            is MenuHomeNavigation.ToDiiaID -> {
                navigation.consumeEvent {
                    viewModel.navigateToDiiaId(this@HomeF)
                }
            }

            is MenuHomeNavigation.ToFAQ -> {
                navigation.consumeEvent {
                    viewModel.navigateToFAQ(this@HomeF)
                }
            }

            is MenuHomeNavigation.ToHelp -> {
                navigation.consumeEvent {
                    viewModel.navigateToHelp(this@HomeF)
                }
            }

            is MenuHomeNavigation.ToLinkDialog -> {
                navigation.consumeEvent {
                    viewModel.navigateToSettingsSystemDialog(this@HomeF)
                }
            }

            is MenuHomeNavigation.ToLogout -> {
                navigation.consumeEvent {
                    viewModel.navigateToLogout(this@HomeF)
                }
            }

            is MenuHomeNavigation.ToNotifications -> {
                navigation.consumeEvent {
                    viewModel.navigateToNotifications(this@HomeF)
                }
            }

            is MenuHomeNavigation.ToSettings -> {
                navigation.consumeEvent {
                    viewModel.navigateToSettings(this@HomeF)
                }
            }

            is MenuHomeNavigation.ToSignHistory -> {
                navigation.consumeEvent {
                    viewModel.navigateToSignHistory(this@HomeF)
                }
            }

            is MenuHomeNavigation.ToSupport -> {
                navigation.consumeEvent {
                    viewModel.navigateToSupport(this@HomeF)
                }
            }

            is MenuHomeNavigation.ToTemplateDialog -> {
                navigation.consumeEvent {
                    openTemplateDialog(navigation.template)
                }
            }

            is MenuHomeNavigation.ToWebView -> {
                navigation.consumeEvent {
                    viewModel.navigateToWebViewUrl(this@HomeF, navigation.link)
                }
            }
        }
    }

    private fun handlePublicServiceNavigation(navigation: PublicServiceHomeNavigation) {
        when (navigation) {
            is PublicServiceHomeNavigation.ToCategory -> {
                navigation.consumeEvent {
                    viewModel.navigateToCategoryServices(this@HomeF, navigation.category)
                }
            }

            is PublicServiceHomeNavigation.ToService -> {
                navigation.consumeEvent {
                    viewModel.navigateToService(this@HomeF, navigation.service)
                }
            }

            is PublicServiceHomeNavigation.ToServiceSearch -> {
                navigation.consumeEvent {
                    viewModel.navigateToServiceSearch(this@HomeF, navigation.data)
                }
            }

            is PublicServiceHomeNavigation.ToTemplateDialog -> {
                navigation.consumeEvent {
                    openTemplateDialog(navigation.template)
                }
            }
        }
    }

    private suspend fun handleFeedNavigation(navigation: FeedHomeNavigation) {
        when (navigation) {
            is FeedHomeNavigation.ToPublicService -> {
                navigation.consumeEvent {
                    viewModel.navigateToPublicService(this@HomeF, navigation.serviceCode)
                }
            }

            is FeedHomeNavigation.OnNotificationSelected -> {
                if (!navigation.isConsumed) {
                    navigation.isConsumed = true
                    handleNotificationSelection(navigation.notification)
                }
            }

            is FeedHomeNavigation.ToCameraRequest -> {
                navigation.consumeEvent {
                    permission.apply {
                        approvePermission(Permission.CAMERA)
                    }
                }
            }

            is FeedHomeNavigation.ToNotification -> {
                navigation.consumeEvent {
                    viewModel.navigateToNotifications(this@HomeF)
                }
            }

            is FeedHomeNavigation.ToTemplateDialog -> {
                navigation.consumeEvent {
                    openTemplateDialog(navigation.template)
                }
            }

            is FeedHomeNavigation.ToStartNewFlow -> {
                navigation.consumeEvent {
                    viewModel.handleStartFlowDeeplink(navigation.deeplink)
                }
            }
        }
    }

    private fun handleDocumentsNavigation(navigation: DocumentsHomeNavigation) {
        when (navigation) {
            is DocumentsHomeNavigation.OnTickerClick -> {
                navigation.consumeEvent {
                    viewModel.onTickerClick(this@HomeF, navigation.doc)
                }
            }

            is DocumentsHomeNavigation.ToDocNotExistTemplate -> {
                viewModel.showDocDoestNotExistTemplate(this, navigation.docType)
            }

            is DocumentsHomeNavigation.ToStartNewFlow -> {
                navigation.consumeEvent {
                    viewModel.handleStartFlowDeeplink(navigation.deeplink)
                }
            }

            is DocumentsHomeNavigation.ToDocActions -> {
                navigation.consumeEvent {
                    viewModel.navigateToDocActions(
                        this@HomeF,
                        navigation.doc,
                        navigation.position,
                        navigation.manualDocs
                    )
                }
            }

            is DocumentsHomeNavigation.ToDocOrder -> {
                navigation.consumeEvent {
                    viewModel.navigateToDocOrder(this@HomeF)
                }
            }

            is DocumentsHomeNavigation.ToRatingService -> {
                navigation.consumeEvent {
                    viewModel.navigateToRatingService(
                        this@HomeF,
                        navigation.docId,
                        navigation.form,
                        navigation.isFromStack
                    )
                }
            }

            is DocumentsHomeNavigation.ToStackDocs -> {
                navigation.consumeEvent {
                    viewModel.navigateToStackDocs(this@HomeF, navigation.doc)
                }
            }

            is DocumentsHomeNavigation.ToTemplateDialog -> {
                navigation.consumeEvent {
                    openTemplateDialog(navigation.template)
                }
            }

            is DocumentsHomeNavigation.ToWebView -> {
                navigation.consumeEvent {
                    viewModel.navigateToWebViewUrl(this@HomeF, navigation.link)
                }
            }
        }
    }

    private suspend fun handleNotificationSelection(notification: PullNotificationItemSelection) {
        viewModel.markNotificationAsRead(notification)
        if (viewModel.isMessageNotification(notification.resourceType)) {
            viewModel.navigateToNotification(this@HomeF, notification)
        } else {
            val direction = viewModel.getNavDirectionForNotification(notification)
            if (direction == null) {
                setNavigationResult(
                    arbitraryDestination = R.id.homeF,
                    key = ActionsConst.ACTION_ITEM_SELECTED,
                    data = ConsumableString(ActionsConst.ACTION_MENU_DOCUMENTS)
                )
            } else {
                viewModel.navigateByNavDirection(
                    fragment = this@HomeF,
                    navDirection = direction
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun navigateToTab(navController: NavHostController, tab: String?) {
        navController.navigate(
            when (tab) {
                HomeActions.HOME_FEED.toString() -> ScreenFeedRoute
                HomeActions.HOME_DOCUMENTS.toString() -> ScreenDocGalleryRoute
                HomeActions.HOME_SERVICES.toString() -> PublicServicesCategoriesRoute
                HomeActions.HOME_MENU.toString() -> ScreenMenuRoute
                else -> ScreenFeedRoute
            }
        ) {
            navController.graph.startDestinationRoute?.let { route ->
                popUpTo(route) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    private fun navigateToQrScannerDestination() {
        navigate(NavHomeDirections.actionGlobalToQrScanF())
    }

    private fun checkNotificationEnabled() {
        if (NotificationManagerCompat.from(requireContext()).areNotificationsEnabled().not()) {
            approvePermission(Permission.POST_NOTIFICATIONS)
        }
    }
}

@Composable
fun HomeBackground(
    animated: Boolean?
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.gradient_bg))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    AnimatedVisibility(
        visible = animated == true,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LottieAnimation(
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.Center,
            contentScale = ContentScale.FillBounds,
            composition = composition,
            progress = { progress },

            )
    }
    AnimatedVisibility(
        visible = animated == null || animated == false,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.bg_blue_yellow_gradient_with_bottom),
            contentDescription = "",
            contentScale = ContentScale.FillBounds
        )
    }
}
