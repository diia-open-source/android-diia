package ua.gov.diia.documents.ui.actions

import android.content.res.Resources
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.ManualDocs
import ua.gov.diia.ui_base.components.organism.list.ContextIconMenuOrgData

interface DocActionsProvider {

    /**
     * @return composable context menu list for specific document
     */
    fun provideActions(
        document: DiiaDocument,
        manualDocs: ManualDocs?,
        enableStackActions: Boolean,
        resources: Resources
    ): ContextIconMenuOrgData

}