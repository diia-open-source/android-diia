package ua.gov.diia.core.models.acquirer

enum class AcquirerServiceType(val id: String) {
    DOCUMENT_GENERATION_BARCODE("documentsGeneration"),
    DOCUMENT_GENERATION_LINK("documentsGeneration"),
    IDENTITY_CHECK("identityCheck"),
    FILE_HASH_SIGNING("hashedFilesSigningDiiaId"),
    DIIA_ID_AUTH("authDiiaId"),
    PAY_ADMINISTRATIVE_FEE("administrativeFees"),
    UNKNOWN(""),
}