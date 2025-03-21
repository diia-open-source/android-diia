package ua.gov.diia.opensource.util.documents

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.core.models.ConsumableString
import ua.gov.diia.core.models.common.BackStackEvent
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.docgroups.v2.VerificationAction
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.extensions.fragment.openLink
import ua.gov.diia.core.util.extensions.fragment.previousDestinationId
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResult
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.documents.models.DocumentAction
import ua.gov.diia.documents.navigation.ConfirmRemoveDocBackStackResult
import ua.gov.diia.documents.navigation.DocUserActionResult
import ua.gov.diia.documents.navigation.DownloadPdfBackStackResult
import ua.gov.diia.documents.navigation.Earn13CodeBackStackResult
import ua.gov.diia.documents.navigation.QRCodeBackStackResult
import ua.gov.diia.documents.navigation.RateDocumentBackStackResult
import ua.gov.diia.documents.navigation.ShareDocBackStackResult
import ua.gov.diia.documents.navigation.ShowRemoveDocBackStackResult
import ua.gov.diia.documents.navigation.UpdateDocBackStackResult
import ua.gov.diia.documents.navigation.VerificationCodeBackStackResult
import ua.gov.diia.documents.ui.DocsConst
import ua.gov.diia.documents.ui.gallery.DocGalleryNavigationHelper
import ua.gov.diia.documents.ui.gallery.DocGalleryVMCompose
import ua.gov.diia.opensource.R
import ua.gov.diia.opensource.ui.AppConst
import javax.inject.Inject

class DocGalleryNavigationHelperImpl @Inject constructor(
    private val withCrashlytics: WithCrashlytics,
) : DocGalleryNavigationHelper {

    override fun subscribeForNavigationEvents(
        fragment: Fragment,
        navigationBackStackEventFlow: MutableSharedFlow<BackStackEvent>,
    ) {
        with(fragment) {
            galleryUniqRegistration(navigationBackStackEventFlow)
            registerFromKeyRating(navigationBackStackEventFlow)
            registerForDocNavigationResult()
            registerForDownloadPDF(navigationBackStackEventFlow)
            registerFromRemoveDocument(navigationBackStackEventFlow)
            registerFromRemoveDoc(navigationBackStackEventFlow)
            registerFromShareAward(navigationBackStackEventFlow)
            registerFromDocumentRating(navigationBackStackEventFlow)
            registerFromOpenLink()
            registerFromRating(navigationBackStackEventFlow)
            registerFromVerificationCode(navigationBackStackEventFlow)
            registerFromEANCode(navigationBackStackEventFlow)
            registerFromQRCode(navigationBackStackEventFlow)
            registerUpdateDocument(navigationBackStackEventFlow)
        }
    }

    override fun subscribeForStackNavigationEvents(
        fragment: Fragment,
        navigationBackStackEventFlow: MutableSharedFlow<BackStackEvent>,
    ) {
        with(fragment) {
            stackUniqRegistration(navigationBackStackEventFlow)
            registerForDocNavigationResult()
            registerForDownloadPDF(navigationBackStackEventFlow)
            registerFromRemoveDocument(navigationBackStackEventFlow)
            registerFromRemoveDoc(navigationBackStackEventFlow)
            registerFromShareAward(navigationBackStackEventFlow)
            registerFromDocumentRating(navigationBackStackEventFlow)
            registerFromOpenLink()
            registerFromRating(navigationBackStackEventFlow)
            registerFromVerificationCode(navigationBackStackEventFlow)
            registerFromEANCode(navigationBackStackEventFlow)
            registerFromQRCode(navigationBackStackEventFlow)
        }
    }

    private fun Fragment.galleryUniqRegistration(navigationBackStackEventFlow: MutableSharedFlow<BackStackEvent>) {
        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()

            val docUserActionResult = when (action) {

                else -> null
            }

            val event = if (docUserActionResult != null) {
                val docUserActionResult1 = DocUserActionResult(ConsumableItem(docUserActionResult))
                docUserActionResult1
            } else {
                BackStackEvent.UserActionResult(ConsumableString(action))
            }

            navigationBackStackEventFlow.tryEmit(event)
        }

        registerForTemplateDialogNavResult(DocGalleryVMCompose.RESUlT_KEY_TEMP_RETRY) { action ->
            findNavController().popBackStack()
            navigationBackStackEventFlow.tryEmit(
                BackStackEvent.TemplateRetryResult(ConsumableString(action))
            )
        }
    }

    private fun Fragment.stackUniqRegistration(
        navigationBackStackEventFlow: MutableSharedFlow<BackStackEvent>,
    ) {
        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            navigationBackStackEventFlow.tryEmit(
                BackStackEvent.UserActionResult(ConsumableString(action))
            )
        }
    }

    private fun Fragment.registerForDocNavigationResult() {
        registerForNavigationResult<ConsumableString>(ActionsConst.RESULT_KEY_NAVIGATION) { key ->
            key.consumeEvent { action ->
                when (action) {

                    else -> {
                    }
                }
            }
        }
    }

    private fun Fragment.registerForDownloadPDF(
        navigationBackStackEventFlow: MutableSharedFlow<BackStackEvent>
    ) {
        registerForNavigationResult<ConsumableItem>(DocsConst.RESULT_KEY_DOWNLOAD_PDF) { key ->
            navigationBackStackEventFlow.tryEmit(
                DownloadPdfBackStackResult(ConsumableItem(key.item))
            )
        }
    }

    private fun Fragment.registerFromRemoveDocument(
        navigationBackStackEventFlow: MutableSharedFlow<BackStackEvent>
    ) {
        registerForNavigationResult<ConsumableItem>(DocsConst.RESULT_KEY_REMOVE_DOCUMENT) { key ->
            key.consumeEvent<DiiaDocument> { doc ->
                if (previousDestinationId == R.id.homeF) {
                    findNavController().popBackStack()
                }
                navigationBackStackEventFlow.tryEmit(
                    ConfirmRemoveDocBackStackResult(ConsumableItem(doc))
                )
            }
        }
    }

    private fun Fragment.registerFromRemoveDoc(
        navigationBackStackEventFlow: MutableSharedFlow<BackStackEvent>
    ) {
        registerForNavigationResult<ConsumableItem>(DocsConst.RESULT_KEY_REMOVE_DOC) { key ->
            key.consumeEvent<DiiaDocument> { action ->
                if (previousDestinationId == R.id.homeF) {
                    findNavController().popBackStack()
                }
                navigationBackStackEventFlow.tryEmit(
                    ShowRemoveDocBackStackResult(ConsumableItem(action))
                )
            }
        }
    }

    private fun Fragment.registerFromShareAward(
        navigationBackStackEventFlow: MutableSharedFlow<BackStackEvent>
    ) {
        registerForNavigationResult<ConsumableItem>(DocsConst.RESULT_KEY_SHARE_DOC) { key ->
            key.consumeEvent<DiiaDocument> { doc ->
                if (previousDestinationId == R.id.homeF) {
                    findNavController().popBackStack()
                }
                navigationBackStackEventFlow.tryEmit(ShareDocBackStackResult(ConsumableItem(doc)))
            }
        }
    }

    private fun Fragment.registerFromKeyRating(
        navigationBackStackEventFlow: MutableSharedFlow<BackStackEvent>
    ) {
        registerForNavigationResult<ConsumableItem>(DocsConst.RESULT_KEY_RATING) { event ->
            navigationBackStackEventFlow.tryEmit(BackStackEvent.RatingResult(event))
        }
    }

    private fun Fragment.registerFromDocumentRating(
        navigationBackStackEventFlow: MutableSharedFlow<BackStackEvent>
    ) {

        registerForNavigationResult<ConsumableItem>(DocsConst.RESULT_KEY_RATE_DOCUMENT) { key ->
            key.consumeEvent<DiiaDocument> { doc ->
                if (previousDestinationId == R.id.homeF) {
                    findNavController().popBackStack()
                }
                navigationBackStackEventFlow.tryEmit(
                    RateDocumentBackStackResult(ConsumableItem(doc))
                )
            }
        }
    }

    private fun Fragment.registerFromOpenLink() {
        registerForNavigationResult<ConsumableString>(AppConst.RESULT_KEY_OPEN_LINK) { key ->
            key.consumeEvent { link ->
                if (previousDestinationId == R.id.homeF) {
                    findNavController().popBackStack()
                }
                openLink(link, withCrashlytics)
            }
        }
    }

    private fun Fragment.registerUpdateDocument(
        navigationBackStackEventFlow: MutableSharedFlow<BackStackEvent>
    ) {
        registerForNavigationResult<ConsumableItem>(DocsConst.RESULT_KEY_UPDATE_DOCUMENT) { key ->
            key.consumeEvent<DiiaDocument> { doc ->
                if (previousDestinationId == R.id.homeF) {
                    findNavController().popBackStack()
                }
                navigationBackStackEventFlow.tryEmit(UpdateDocBackStackResult(ConsumableItem(doc)))
            }
        }
    }

    private fun Fragment.registerFromVerificationCode(
        navigationBackStackEventFlow: MutableSharedFlow<BackStackEvent>
    ) {
        registerForNavigationResult<ConsumableItem>(DocsConst.RESULT_KEY_VERIFICATION_CODE) { key ->
            key.consumeEvent<VerificationAction> { action ->
                if (previousDestinationId == R.id.homeF) {
                    findNavController().popBackStack()
                }
                navigationBackStackEventFlow.tryEmit(
                    VerificationCodeBackStackResult(
                        ConsumableItem(
                            DocumentAction(
                                actionKey = action.actionKey,
                                docType = action.docName,
                                position = action.position.toString(),
                                id = null
                            )
                        )
                    )
                )
            }
        }
    }

    private fun Fragment.registerFromEANCode(
        navigationBackStackEventFlow: MutableSharedFlow<BackStackEvent>
    ) {
        registerForNavigationResult<ConsumableItem>(DocsConst.RESULT_KEY_EAN13_CODE) { key ->
            key.consumeEvent<VerificationAction> { action ->
                if (previousDestinationId == R.id.homeF) {
                    findNavController().popBackStack()
                }
                navigationBackStackEventFlow.tryEmit(
                    Earn13CodeBackStackResult(
                        ConsumableItem(
                            DocumentAction(
                                actionKey = action.actionKey,
                                docType = action.docName,
                                position = action.position.toString(),
                                id = action.id
                            )
                        )
                    )
                )
            }
        }
    }

    private fun Fragment.registerFromQRCode(
        navigationBackStackEventFlow: MutableSharedFlow<BackStackEvent>
    ) {
        registerForNavigationResult<ConsumableItem>(DocsConst.RESULT_KEY_QR_CODE) { key ->
            key.consumeEvent<VerificationAction> { action ->
                if (previousDestinationId == R.id.homeF) {
                    findNavController().popBackStack()
                }
                navigationBackStackEventFlow.tryEmit(
                    QRCodeBackStackResult(
                        ConsumableItem(
                            DocumentAction(
                                actionKey = action.actionKey,
                                docType = action.docName,
                                position = action.position.toString(),
                                id = null
                            )
                        )
                    )
                )
            }
        }
    }

    private fun Fragment.registerFromRating(
        navigationBackStackEventFlow: MutableSharedFlow<BackStackEvent>
    ) {
        registerForNavigationResult<ConsumableItem>(ActionsConst.RATING) { event ->
            navigationBackStackEventFlow.tryEmit(BackStackEvent.RatingResult(event))
        }
    }
}