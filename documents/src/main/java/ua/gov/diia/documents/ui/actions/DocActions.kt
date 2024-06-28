package ua.gov.diia.documents.ui.actions

enum class ContextMenuType(val code: String) {
    FULL_DOC("fullDoc"),
    FAQS("faqs"),
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
    RATE_DOC("rateDoc"),
}