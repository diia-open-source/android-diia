package ua.gov.diia.core.util.file

import java.io.File

interface FileManager {

    /**
     * Save file to working dir
     */
    fun saveFile(filename: String, data: ByteArray): File

    /**
     * Create empty file
     */
    fun createFile(filename: String): File

    /**
     * Get file from current working dir
     */
    fun getFile(filename: String): File

    /**
     * Read file from working dir to byte array
     */
    fun readFileData(filename: String): ByteArray

    /**
     * Remove file from working dir
     */
    fun deleteFile(filename: String)

    /**
     * Clear all files from current working dir
     */
    fun clearPath()

    /**
     * Return current working dir
     */
    fun getDir(): File

    /**
     * Read file as byte array from assets
     */
    fun readAssetsBytes(asset: String): ByteArray

}