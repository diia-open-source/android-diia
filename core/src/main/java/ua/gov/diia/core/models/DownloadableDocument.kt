package ua.gov.diia.core.models

interface DownloadableDocument {
    val id: String
    val docName: String
    val downloadUrl: String
}