package ua.gov.diia.core.data.data_source.file

import android.net.Uri
import java.io.File

interface PrivateFileDataSource {

    /**
     * Saves base64 file in the specified [directory]
     *
     * @param name file to save name
     * @param directory save directory name
     * @param base64File file in the base64 format
     * @return content//: uri to the saved file source
     */
    suspend fun save(name: String, directory: String, base64File: String): Uri

    /**
     * Saves base64 file in the created "container" file by the Uri link
     *
     * @param containerUri link to the file container
     * @param base64File file in the base64 format
     * @return content//: uri to the saved file source
     */
    suspend fun save(containerUri: Uri, base64File: String): Uri

    /**
     * Deletes file by [name] in the specified [directory]
     *
     * @param name file to delete name
     * @param directory file store directory
     * @return true - if the file was deleted, otherwise - false
     */
    suspend fun delete(name: String, directory: String): Boolean

    /**
     * Retrieves the content//: uri from the [file]
     *
     * @param file to retrieve uri
     * @return true - if the file exists and uri has been retrieved successfully, otherwise - false
     */
    suspend fun getContentUri(file: File): Uri?

    /**
     * Returns or creates (if not found) the sub directory based on the [name]
     * in the app internal storage
     *
     * @param name of the sub directory
     */
    suspend fun createDir(name: String) : File
}