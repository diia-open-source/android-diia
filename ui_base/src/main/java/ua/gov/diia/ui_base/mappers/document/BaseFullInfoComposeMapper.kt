package ua.gov.diia.ui_base.mappers.document

import androidx.compose.runtime.snapshots.SnapshotStateList
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.ui_base.components.infrastructure.UIElementData

interface BaseFullInfoComposeMapper {

    fun mapDocToBody(
        document: DiiaDocument,
        bodyData: SnapshotStateList<UIElementData>
    )
}