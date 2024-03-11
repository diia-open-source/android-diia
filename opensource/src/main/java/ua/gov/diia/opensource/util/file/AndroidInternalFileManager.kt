package ua.gov.diia.opensource.util.file

import android.content.Context
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

open class AndroidInternalFileManager(
    private val context: Context,
    private val subDir: String,
) : FileManager {

    override fun saveFile(filename: String, data: ByteArray): File {
        val file = File(getDir(), filename)
        file.writeBytes(data)
        return file
    }

    override fun getFile(filename: String): File {
        return File(getDir(), filename)
    }

    override fun readFileData(filename: String): ByteArray {
        val file = File(getDir(), filename)
        val size = file.length().toInt()
        val bytes = ByteArray(size)
        val buf = BufferedInputStream(FileInputStream(file))
        buf.read(bytes, 0, bytes.size)
        buf.close()
        return bytes
    }

    override fun deleteFile(filename: String) {
        File(getDir(), filename).delete()
    }

    override fun clearPath() {
        getDir().deleteRecursively()
    }

    override fun getDir(): File {
        val appDir: File = context.filesDir
        val subDir = File(appDir, subDir)
        if (!subDir.exists()) subDir.mkdir()
        return subDir
    }

    override fun readAssetsBytes(asset: String): ByteArray {
        var inputStream: InputStream? = null
        try {
            inputStream = context.assets.open(asset)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            if (size != inputStream.read(buffer)) {
                throw IllegalStateException()
            }
            return buffer
        } finally {
            inputStream?.close()
        }
    }
}
