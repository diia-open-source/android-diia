package ua.gov.diia.documents.ui.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.gov.diia.core.models.common.BackStackEvent
import ua.gov.diia.core.models.deeplink.DeepLinkActionStartFlow
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.extensions.activity.getActivity
import ua.gov.diia.core.util.extensions.activity.setWindowBrightness
import ua.gov.diia.core.util.extensions.fragment.collapseApp
import ua.gov.diia.core.util.extensions.fragment.openLink
import ua.gov.diia.core.util.extensions.fragment.sendImage
import ua.gov.diia.core.util.extensions.fragment.sendPdf
import ua.gov.diia.core.util.navigation.HomeNavigation
import ua.gov.diia.diia_storage.AndroidBase64Wrapper
import ua.gov.diia.documents.R
import ua.gov.diia.documents.models.DocumentAction
import ua.gov.diia.documents.navigation.AddDocBackStackResult
import ua.gov.diia.documents.navigation.ConfirmDocShareBackStackResult
import ua.gov.diia.documents.navigation.ConfirmRemoveDocBackStackResult
import ua.gov.diia.documents.navigation.DocUserActionResult
import ua.gov.diia.documents.navigation.DocumentsHomeNavigation
import ua.gov.diia.documents.navigation.DownloadCerfPdfBackStackResult
import ua.gov.diia.documents.navigation.DownloadPdfBackStackResult
import ua.gov.diia.documents.navigation.Earn13CodeBackStackResult
import ua.gov.diia.documents.navigation.QRCodeBackStackResult
import ua.gov.diia.documents.navigation.RateDocumentBackStackResult
import ua.gov.diia.documents.navigation.RemoveDocBackStackResult
import ua.gov.diia.documents.navigation.RemoveDocumentBackStackResult
import ua.gov.diia.documents.navigation.ShareDocBackStackResult
import ua.gov.diia.documents.navigation.ShowRemoveDocBackStackResult
import ua.gov.diia.documents.navigation.StartFlowIfDocExistBackStackResult
import ua.gov.diia.documents.navigation.UpdateDocBackStackResult
import ua.gov.diia.documents.navigation.VerificationCodeBackStackResult
import ua.gov.diia.documents.ui.DocsConst
import ua.gov.diia.documents.util.view.showCopyDocIdClipedSnackBar
import ua.gov.diia.ui_base.components.infrastructure.HomeScreenTab
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.navigation.BaseNavigation

@Composable
fun DocGalleryC(
    modifier: Modifier = Modifier,
    viewModel: DocGalleryVMCompose,
    withCrashlytics: WithCrashlytics,
    navigationBackStackEventFlow: Flow<BackStackEvent>,
    homeNavigationActionFlow: MutableSharedFlow<HomeNavigation>,
) {
    val context = LocalContext.current
    val body = viewModel.bodyData
    val progressIndicator =
        viewModel.progressIndicator.collectAsState(
            initial = Pair(
                "",
                true
            )
        )
    val connectivityState = viewModel.connectivity.collectAsState(true)

    val contentLoaded = viewModel.contentLoaded.collectAsState(
        initial = Pair(
            UIActionKeysCompose.PAGE_LOADING_TRIDENT,
            true
        )
    )

    val certificatePdf = viewModel.certificatePdf.observeAsState()
    val documentPdf = viewModel.documentPdf.observeAsState()
    val buildConfig = viewModel.withBuildConfig
    val navigationBackStackEvent = navigationBackStackEventFlow.collectAsStateWithLifecycle(
        initialValue = BackStackEvent.Empty
    )

    LaunchedEffect(key1 = certificatePdf.value) {
        val event = certificatePdf.value
        event?.getContentIfNotHandled()?.let { e ->
            val bArray = AndroidBase64Wrapper().decode(e.docPDF.toByteArray())
            context.sendPdf(
                bArray,
                event.peekContent().name,
                buildConfig.getApplicationId()
            )
        }
    }

    LaunchedEffect(key1 = documentPdf.value) {
        val event = documentPdf.value
        event?.getContentIfNotHandled()?.let { e ->
            val bArray = AndroidBase64Wrapper().decode(e.docPDF.toByteArray())
            context.sendPdf(
                bArray,
                event.peekContent().name,
                buildConfig.getApplicationId()
            )
        }
    }

    LifecycleResumeEffect(key1 = Unit) {
        onPauseOrDispose {
            context.getActivity()?.setWindowBrightness(true)
        }
    }

    viewModel.apply {
        navigation.collectAsEffect { navigation ->
            when (navigation) {
                is BaseNavigation.Back -> {
                    context.getActivity()?.collapseApp()
                }

                is DocGalleryVMCompose.Navigation.ToDocActions -> {
                    homeNavigationActionFlow.tryEmit(
                        DocumentsHomeNavigation.ToDocActions(
                            navigation.doc,
                            navigation.position,
                            navigation.manualDocs
                        )
                    )
                }

                is DocGalleryVMCompose.Navigation.NavToVehicleInsurance -> {
                    homeNavigationActionFlow.tryEmit(
                        DocumentsHomeNavigation.OnTickerClick(navigation.doc)
                    )
                }

                is DocGalleryVMCompose.Navigation.ToDocStackOrder -> {
                    homeNavigationActionFlow.tryEmit(DocumentsHomeNavigation.ToDocOrder())
                }

                is DocGalleryVMCompose.Navigation.ToDocStack -> {
                    homeNavigationActionFlow.tryEmit(DocumentsHomeNavigation.ToStackDocs(navigation.doc))
                }
            }
        }
        docAction.collectAsEffect { action ->
            when (action) {
                is DocGalleryVMCompose.DocActions.OpenWebView -> {
                    homeNavigationActionFlow.tryEmit(
                        DocumentsHomeNavigation.ToWebView(action.url)
                    )
                }

                is DocGalleryVMCompose.DocActions.OpenNewFlow -> {
                    homeNavigationActionFlow.tryEmit(
                        DocumentsHomeNavigation.ToStartNewFlow(
                            DeepLinkActionStartFlow(flowId = action.code, resId = "")
                        )
                    )
                }

                is DocGalleryVMCompose.DocActions.OpenLink -> {
                    context.openLink(action.link, withCrashlytics)
                }

                is DocGalleryVMCompose.DocActions.DocNumberCopy -> {
                    context.getActivity()?.window?.decorView?.showCopyDocIdClipedSnackBar(
                        action.value,
                        topPadding = 40f
                    )
                }

                is DocGalleryVMCompose.DocActions.ShareImage -> {
                    context.sendImage(
                        action.data.byteArray,
                        action.data.fileName,
                        action.applicationId
                    )
                }

                is DocGalleryVMCompose.DocActions.HighBrightness -> {
                    context.getActivity()?.setWindowBrightness()
                }

                is DocGalleryVMCompose.DocActions.DefaultBrightness -> {
                    context.getActivity()?.setWindowBrightness(true)
                }
            }
        }
    }

    viewModel.showTemplateDialog.collectAsEffect { template ->
        viewModel.stopLoading()
        homeNavigationActionFlow.tryEmit(DocumentsHomeNavigation.ToTemplateDialog(template.peekContent()))
    }

    viewModel.showRatingDialogByUserInitiative.collectAsEffect {
        viewModel.stopLoading()
        homeNavigationActionFlow.tryEmit(
            DocumentsHomeNavigation.ToRatingService(
                viewModel.currentDocId().orEmpty(),
                it.peekContent(),
                false
            )
        )
    }

    handleNavigationBackStackEvent(
        navigationBackStackEvent.value,
        viewModel,
        homeNavigationActionFlow
    )

    HomeScreenTab(
        modifier = modifier,
        progressIndicator = progressIndicator.value,
        contentLoaded = contentLoaded.value,
        connectivityState = connectivityState.value,
        body = body,
        onEvent = {
            viewModel.onUIAction(it)
        })
}

fun handleNavigationBackStackEvent(
    event: BackStackEvent,
    viewModel: DocGalleryVMCompose,
    homeNavigationActionFlow: MutableSharedFlow<HomeNavigation>,
) {
    when (event) {
        is BackStackEvent.UserActionResult -> {
            event.data.consumeEvent { action ->
                when (action) {
                    ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                    ActionsConst.ERROR_DIALOG_DEAL_WITH_IT -> viewModel.stopLoading()

                    DocsConst.RESULT_KEY_CONFIRM_REMOVE_DOC -> {
                        viewModel.showConfirmDeleteTemplateLocal()
                    }

                    DocsConst.DIALOG_ACTION_DOCUMENTS_NEXT_CARD -> {
                        viewModel.invalidateAndRemove()
                    }
                }
            }
        }

        is DocUserActionResult -> {
            event.data.consumeEvent<DocumentAction> { docAction ->
                when (docAction.actionKey) {
                    ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()

                    DocsConst.RESULT_KEY_CONFIRM_REMOVE_DOC -> {
                        viewModel.showConfirmDeleteTemplateLocal()
                    }

                    DocsConst.DOCUMENT_ACTION_SHARE -> {
                        viewModel.loadAndShareDocument(docAction.docType ?: return)
                    }

                    DocsConst.DOCUMENT_ACTION_CONFIRM -> {
                        viewModel.removeDocByType(docAction.docType ?: return)
                    }

                    DocsConst.DOCUMENT_ACTION_START_FLOW -> {
                        val deeplink = DeepLinkActionStartFlow(
                            flowId = docAction.docType.orEmpty(),
                            resId = docAction.data.orEmpty()
                        )
                        homeNavigationActionFlow.tryEmit(
                            DocumentsHomeNavigation.ToStartNewFlow(deeplink)
                        )
                    }

                    DocsConst.DOCUMENT_ACTION_OPEN -> {
                        viewModel.invalidateAndScroll(docAction.docType ?: return)
                    }

                    DocsConst.DIALOG_ACTION_DOCUMENTS_NEXT_CARD -> {
                        viewModel.invalidateAndRemove()
                    }
                }
            }
        }

        is BackStackEvent.TemplateRetryResult -> {
            event.data.consumeEvent { action ->
                when (action) {
                    ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                    ActionsConst.ERROR_DIALOG_DEAL_WITH_IT -> viewModel.stopLoading()
                }
            }
        }

        is BackStackEvent.RatingResult -> {
            event.data.consumeEvent<RatingRequest> { rating ->
                viewModel.sendRatingRequest(rating)
            }
        }

        is DownloadPdfBackStackResult -> {
            event.data.consumeEvent<DiiaDocument> { item ->
                viewModel.getDocumentPdf(item)
            }
        }

        is DownloadCerfPdfBackStackResult -> {
            event.data.consumeEvent<DiiaDocument> { item ->
                viewModel.getCertificatePdf(item)
            }
        }

        is RemoveDocBackStackResult -> {
            event.data.consumeEvent<DiiaDocument> { doc ->
                viewModel.showConfirmDeleteTemplateRemote(doc)
            }
        }

        is RemoveDocumentBackStackResult -> {
            event.data.consumeEvent<DiiaDocument> { doc ->
                viewModel.removeDoc(doc)
            }
        }

        is ConfirmRemoveDocBackStackResult -> {
            event.data.consumeEvent<DiiaDocument> { doc ->
                viewModel.removeDoc(doc)
            }
        }

        is AddDocBackStackResult -> {
            event.data.consumeEvent {
                viewModel.addDocToGallery()
            }
        }

        is QRCodeBackStackResult -> {
            event.data.consumeEvent<DocumentAction> { docAction ->
                viewModel.onUIAction(
                    UIAction(
                        actionKey = docAction.actionKey,
                        data = docAction.docType,
                        optionalId = docAction.position,
                        optionalType = docAction.id
                    )
                )
            }
        }

        is Earn13CodeBackStackResult -> {
            event.data.consumeEvent<DocumentAction> { docAction ->
                viewModel.onUIAction(
                    UIAction(
                        actionKey = docAction.actionKey,
                        data = docAction.docType,
                        optionalId = docAction.position,
                        optionalType = docAction.id
                    )
                )
            }
        }

        is VerificationCodeBackStackResult -> {
            event.data.consumeEvent<DocumentAction> { docAction ->
                viewModel.onUIAction(
                    UIAction(
                        actionKey = docAction.actionKey,
                        data = docAction.docType,
                        optionalId = docAction.position,
                        optionalType = docAction.id
                    )
                )
            }
        }

        is UpdateDocBackStackResult -> {
            event.data.consumeEvent<DiiaDocument> { doc ->
                viewModel.forceUpdateDocument(doc)
            }
        }

        is RateDocumentBackStackResult -> {
            event.data.consumeEvent<DiiaDocument> { doc ->
                viewModel.showRating(doc)
            }
        }

        is ShareDocBackStackResult -> {
            event.data.consumeEvent<DiiaDocument> { doc ->
                viewModel.loadImageAndShare(doc.getItemType(), doc.docId())
            }
        }

        is ShowRemoveDocBackStackResult -> {
            event.data.consumeEvent<DiiaDocument> { doc ->
                viewModel.showRemoveDocDialog(doc)
            }
        }

        is StartFlowIfDocExistBackStackResult -> {
            event.data.consumeEvent<DocumentAction> { docAction ->
                viewModel.validateIsDocExist(docAction.docType ?: return) { docExist ->
                    if (docExist) {
                        homeNavigationActionFlow.tryEmit(
                            DocumentsHomeNavigation.ToDocNotExistTemplate(docAction.docType)
                        )
                    } else {
                        val deeplink = DeepLinkActionStartFlow(
                            flowId = docAction.docType,
                            resId = docAction.data.orEmpty()
                        )
                        homeNavigationActionFlow.tryEmit(
                            DocumentsHomeNavigation.ToStartNewFlow(deeplink)
                        )
                    }
                }
            }
        }

        is ConfirmDocShareBackStackResult -> {
            event.data.consumeEvent { type ->
                viewModel.confirmDocumentShare(type)
            }
        }
    }
}
