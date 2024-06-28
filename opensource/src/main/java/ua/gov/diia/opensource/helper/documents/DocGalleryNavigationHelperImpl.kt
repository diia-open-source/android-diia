package ua.gov.diia.opensource.helper.documents

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.core.models.ConsumableString
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.extensions.fragment.openLink
import ua.gov.diia.core.util.extensions.fragment.previousDestinationId
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResult
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.docgroups.v2.VerificationAction
import ua.gov.diia.documents.ui.DocVM
import ua.gov.diia.documents.ui.DocsConst
import ua.gov.diia.documents.ui.gallery.DocGalleryNavigationHelper
import ua.gov.diia.documents.ui.gallery.DocGalleryVMCompose
import ua.gov.diia.opensource.R
import ua.gov.diia.opensource.ui.AppConst
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import javax.inject.Inject

class DocGalleryNavigationHelperImpl @Inject constructor(
    private val withCrashlytics: WithCrashlytics,
) :
    DocGalleryNavigationHelper {

    override fun subscribeForNavigationEvents(
        fragment: Fragment,
        viewModel: DocVM
    ) {
        galleryUniqRegistration(fragment, viewModel)
        registerFromKeyRating(fragment, viewModel)
        registerFromRemoveDocument(fragment, viewModel)
        registerFromDocumentRating(fragment, viewModel)
        registerFromOpenLink(fragment)
        registerFromRating(fragment, viewModel)
        registerFromVerificationCode(fragment, viewModel)
        registerFromEANCode(fragment, viewModel)
        registerFromQRCode(fragment, viewModel)
    }

    override fun subscribeForStackNavigationEvents(
        fragment: Fragment,
        viewModel: DocVM
    ) {
        stackUniqRegistration(fragment, viewModel)
        registerFromRemoveDocument(fragment, viewModel)
        registerFromDocumentRating(fragment, viewModel)
        registerFromOpenLink(fragment)
        registerFromRating(fragment, viewModel)
        registerFromVerificationCode(fragment, viewModel)
        registerFromEANCode(fragment, viewModel)
        registerFromQRCode(fragment, viewModel)
    }

    private fun galleryUniqRegistration(
        fragment: Fragment,
        viewModel: DocVM
    ) {
        with(fragment) {
            registerForTemplateDialogNavResult { action ->
                findNavController().popBackStack()
                when (action) {
                    ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                }
            }

            registerForTemplateDialogNavResult(DocGalleryVMCompose.RESUlT_KEY_TEMP_RETRY) { action ->
                findNavController().popBackStack()
                when (action) {
                    ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                    ActionsConst.DIALOG_ACTION_CODE_CLOSE,
                    ActionsConst.ERROR_DIALOG_DEAL_WITH_IT
                    -> findNavController().popBackStack()
                }
            }
        }
    }

    private fun stackUniqRegistration(
        fragment: Fragment,
        viewModel: DocVM
    ) {
        with(fragment) {
            registerForTemplateDialogNavResult { action ->
                findNavController().popBackStack()
                when (action) {
                    ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                }
            }
        }
    }

    private fun registerFromRemoveDocument(fragment: Fragment, viewModel: DocVM) {
        with(fragment) {

            registerForNavigationResult<ConsumableItem>(DocsConst.RESULT_KEY_REMOVE_DOC) { key ->
                key.consumeEvent<DiiaDocument> { doc ->
                    if (previousDestinationId == R.id.homeF) {
                        findNavController().popBackStack()
                    }
                    viewModel.removeDoc(doc)
                }
            }
        }
    }

    private fun registerFromKeyRating(fragment: Fragment, viewModel: DocVM) {
        with(fragment) {
            registerForNavigationResult<ConsumableItem>(DocsConst.RESULT_KEY_RATING) { event ->
                event.consumeEvent<RatingRequest> { rating ->
                    viewModel.sendRatingRequest(
                        rating
                    )
                }
            }
        }
    }

    private fun registerFromDocumentRating(
        fragment: Fragment,
        viewModel: DocVM
    ) {
        with(fragment) {

            registerForNavigationResult<ConsumableItem>(DocsConst.RESULT_KEY_RATE_DOCUMENT) { key ->
                key.consumeEvent<DiiaDocument> { doc ->
                    if (previousDestinationId == R.id.homeF) {
                        findNavController().popBackStack()
                    }
                    viewModel.showRating(doc)
                }
            }
        }
    }

    private fun registerFromOpenLink(fragment: Fragment) {
        with(fragment) {
            registerForNavigationResult<ConsumableString>(AppConst.RESULT_KEY_OPEN_LINK) { key ->
                key.consumeEvent { link ->
                    if (previousDestinationId == R.id.homeF) {
                        findNavController().popBackStack()
                    }
                    openLink(link, withCrashlytics)
                }
            }
        }
    }

    private fun registerFromVerificationCode(fragment: Fragment, viewModel: DocVM) {
        with(fragment) {
            registerForNavigationResult<ConsumableItem>(DocsConst.RESULT_KEY_VERIFICATION_CODE) { key ->
                key.consumeEvent<VerificationAction> { action ->
                    if (previousDestinationId == R.id.homeF) {
                        findNavController().popBackStack()
                    }
                    val event = UIAction(
                        actionKey = action.actionKey,
                        data = action.docName,
                        optionalId = action.position.toString()
                    )
                    viewModel.onUIAction(event)
                }
            }
        }
    }

    private fun registerFromEANCode(fragment: Fragment, viewModel: DocVM) {
        with(fragment) {
            registerForNavigationResult<ConsumableItem>(DocsConst.RESULT_KEY_EAN13_CODE) { key ->
                key.consumeEvent<VerificationAction> { action ->
                    if (previousDestinationId == R.id.homeF) {
                        findNavController().popBackStack()
                    }
                    val event = UIAction(
                        actionKey = action.actionKey,
                        data = action.docName,
                        optionalId = action.position.toString(),
                        optionalType = action.id
                    )
                    viewModel.onUIAction(event)
                }
            }
        }
    }

    private fun registerFromQRCode(fragment: Fragment, viewModel: DocVM) {
        with(fragment) {
            registerForNavigationResult<ConsumableItem>(DocsConst.RESULT_KEY_QR_CODE) { key ->
                key.consumeEvent<VerificationAction> { action ->
                    if (previousDestinationId == R.id.homeF) {
                        findNavController().popBackStack()
                    }
                    val event = UIAction(
                        actionKey = action.actionKey,
                        data = action.docName,
                        optionalId = action.position.toString()
                    )
                    viewModel.onUIAction(event)
                }
            }
        }
    }

    private fun registerFromRating(fragment: Fragment, viewModel: DocVM) {
        with(fragment) {
            registerForNavigationResult<ConsumableItem>(ActionsConst.RATING) { event ->
                event.consumeEvent<RatingRequest> { rating ->
                    viewModel.sendRatingRequest(
                        rating
                    )
                }
            }
        }
    }
}