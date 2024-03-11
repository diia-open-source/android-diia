package ua.gov.diia.documents.ui.fullinfo

import androidx.compose.runtime.snapshots.SnapshotStateList
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import javax.inject.Inject

class DocFullInfoComposeMapperImpl @Inject constructor(val mappers: List<@JvmSuppressWildcards BaseFullInfoComposeMapper> ) :
    DocFullInfoComposeMapper {
    override fun mapDocToBody(
        document: DiiaDocument,
        bodyData: SnapshotStateList<UIElementData>
    ) {
        mappers.forEach {
            it.mapDocToBody(document, bodyData)
        }
    }
}