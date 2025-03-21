package ua.gov.diia.core.models.document.ui

import ua.gov.diia.core.models.document.DocumentContextMenuAction

enum class DocumentsContextMenuActions(override val action: String) : DocumentContextMenuAction {
    FAQS("faqs"),
    FULL_DOC("fullDoc"),
    DOWNLOAD_CERTIFICATE_PDF("downloadCertificatePdf"),
    DOWNLOAD_DOCUMENT_PDF("downloadDocumentPdf"),
    VERIFICATION_CODE("verificationCode"),
    OPEN_SAME_DOC_TYPE("openSameDocType"),
    CHANGE_DISPLAY_ORDER("changeDisplayOrder"),
    CHANGE_DOC_ORDERING("changeDocOrdering"),
    REMOVE_DOC("removeDoc"),
    TRANSLATE_TO_UA("translateToUa"),
    TRANSLATE_TO_ENG("translateToEng"),
    RATE_DOCUMENT("rating"),
    UPDATE_DOC("updateDoc"),
    SHARE_WITH_FRIENDS("shareWithFriends"),
    REQUEST_DELIVERY("requestDelivery"),
    RATE_AWARD_DOC("rateAwardDoc"),//done maybe change name to RATE_DOC ?
}