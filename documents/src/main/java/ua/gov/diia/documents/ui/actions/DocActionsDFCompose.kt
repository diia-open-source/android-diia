package ua.gov.diia.documents.ui.actions

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.LocalizationType
import ua.gov.diia.documents.models.docgroups.v2.VerificationAction
import ua.gov.diia.documents.util.DocNameProvider
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.atom.button.ButtonWhiteLargeAtomData
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.DocAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.organism.list.ActivityViewOrg
import ua.gov.diia.ui_base.components.organism.list.ActivityViewOrgData
import ua.gov.diia.ui_base.fragments.BaseBottomDialog
import javax.inject.Inject

@AndroidEntryPoint
class DocActionsDFCompose : BaseBottomDialog() {

    @Inject
    lateinit var docNameProvider: DocNameProvider

    @Inject
    lateinit var documentsHelper: DocumentsHelper

    @Inject
    lateinit var docActionsNavigationHandler: DocActionsNavigationHandler

    @Inject
    lateinit var diiaResourceIconProvider: DiiaResourceIconProvider
    private val vm: DocActionsVMCompose by viewModels()
    private var composeView: ComposeView? = null
    private val args: DocActionsDFComposeArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        composeView = ComposeView(requireContext())
        return composeView
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        (view?.parent as View).setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        composeView?.setContent {

            vm.apply {
                navigation.collectAsEffect { navigation ->
                    docActionsNavigationHandler.handleNavigation(
                        this@DocActionsDFCompose,
                        navigation,
                        args
                    )
                }
                docAction.collectAsEffect { docAction ->
                    handleAction(
                        this@DocActionsDFCompose,
                        vm,
                        docAction,
                        args
                    )
                }
                dismiss.collectAsEffect {
                    dismiss()
                }
            }

            val contextMenuOrgData = vm.provideActions(
                args.doc as DiiaDocument,
                args.manualDocs,
                args.enableStackActions,
                resources
            )

            val button = ButtonWhiteLargeAtomData(
                title = "Закрити",
                id = "",
                interactionState = UIState.Interaction.Enabled
            )
            val data = ActivityViewOrgData(
                contextMenuOrg = contextMenuOrgData,
                button = button
            )
            ActivityViewOrg(
                modifier = Modifier,
                data = data,
                onUIAction = {
                    vm.onUIAction(it)

                },
                diiaResourceIconProvider = diiaResourceIconProvider,
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    fun handleAction(
        fragment: DocActionsDFCompose,
        vm: DocActionsVMCompose,
        action: DocAction,
        args: DocActionsDFComposeArgs
    ) {
        documentsHelper.handleAction(fragment, action, args)

        with(fragment) {
            when (action) {
                is DocActionsVMCompose.DocActions.RemoveDoc -> {
                    val doc = args.doc
                    if (doc is DiiaDocument) {
                        dismiss()
                        setNavigationResult(
                            arbitraryDestination = args.resultDestinationId,
                            key = ActionsConst.RESULT_KEY_REMOVE_DOCUMENT,
                            data = ConsumableItem(doc)
                        )
                    }
                }

                is DocActionsVMCompose.DocActions.UpdateDoc -> {
                    val doc = args.doc
                    if (doc is DiiaDocument) {
                        dismiss()
                        setNavigationResult(
                            arbitraryDestination = args.resultDestinationId,
                            key = ActionsConst.RESULT_KEY_UPDATE_DOCUMENT,
                            data = ConsumableItem(doc)
                        )
                    }
                }

                is DocActionsVMCompose.DocActions.TranslateToUa -> {
                    val doc = args.doc
                    if (doc is DiiaDocument) {
                        vm.switchLocalization(doc, LocalizationType.ua)
                    }
                    dismiss()
                }

                is DocActionsVMCompose.DocActions.TranslateToEng -> {
                    val doc = args.doc
                    if (doc is DiiaDocument) {
                        vm.switchLocalization(doc, LocalizationType.eng)
                    }
                    dismiss()
                }

                is DocActionsVMCompose.DocActions.RateDocument -> {
                    val doc = args.doc
                    if (doc is DiiaDocument) {
                        dismiss()
                        setNavigationResult(
                            arbitraryDestination = args.resultDestinationId,
                            key = ActionsConst.RESULT_KEY_RATE_DOCUMENT,
                            data = ConsumableItem(doc)
                        )
                    }
                }

                is DocActionsVMCompose.DocActions.OpenVerificationCode -> {
                    dismiss()
                    setNavigationResult(
                        arbitraryDestination = args.resultDestinationId,
                        key = ActionsConst.RESULT_KEY_VERIFICATION_CODE,
                        data = ConsumableItem(
                            VerificationAction(
                                actionKey = UIActionKeysCompose.DOC_CARD_FORCE_FLIP,
                                position = args.position,
                                id = action.id,
                                docName = docNameProvider.getDocumentName(args.doc as DiiaDocument)
                            )
                        )
                    )
                }

                is DocActionsVMCompose.DocActions.OpenQr -> {
                    dismiss()
                    setNavigationResult(
                        arbitraryDestination = args.resultDestinationId,
                        key = ActionsConst.RESULT_KEY_QR_CODE,
                        data = ConsumableItem(
                            VerificationAction(
                                actionKey = UIActionKeysCompose.DOC_CARD_FORCE_FLIP,
                                position = args.position,
                                id = action.id,
                                docName = docNameProvider.getDocumentName(args.doc as DiiaDocument)
                            )
                        )
                    )
                }

                is DocActionsVMCompose.DocActions.OpenEan13 -> {
                    dismiss()
                    setNavigationResult(
                        arbitraryDestination = args.resultDestinationId,
                        key = ActionsConst.RESULT_KEY_EAN13_CODE,
                        data = ConsumableItem(
                            VerificationAction(
                                actionKey = UIActionKeysCompose.DOC_CARD_FORCE_FLIP,
                                position = args.position,
                                id = action.id,
                                docName = docNameProvider.getDocumentName(args.doc as DiiaDocument)
                            )
                        )
                    )
                }

            }

        }
    }
}