package ua.gov.diia.documents.ui.fullinfo

import androidx.compose.runtime.snapshots.SnapshotStateList
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.ui_base.components.infrastructure.UIElementData

interface BaseFullInfoComposeMapper {

    fun mapDocToBody(
        document: DiiaDocument,
        bodyData: SnapshotStateList<UIElementData>
    )
}