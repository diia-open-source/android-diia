package ua.gov.diia.documents.util

interface WithUpdateExpiredDocs {

    /**
     * @return performs update expiration date for specific document
     */
    suspend fun updateExpirationDate(focusDocType: String) : Boolean

    /**
     * @return performs update expiration date for list of documents
     */
    suspend fun updateExpirationDate(types: List<String>)
}