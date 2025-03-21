package ua.gov.diia.core.util.activity_result

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContract
import ua.gov.diia.core.models.media.MediaFileType
import ua.gov.diia.core.models.media.MediaItem

class TakeListMediaContentItemActivityResult(
    private val context: Context,
    private val media: MediaFileType? = null,
    private val allowedFormats: List<String> = listOf(),
    private val isMultipleAllowed : Boolean = true
) : ActivityResultContract<Unit, List<MediaItem>?>() {

    override fun createIntent(context: Context, input: Unit): Intent =
        Intent(Intent.ACTION_PICK).apply {
            type = media?.ime ?: "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultipleAllowed)
            if (media == null) {
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
            }
        }

    override fun parseResult(resultCode: Int, intent: Intent?): List<MediaItem>? {
        val mediaItemList = mutableListOf<MediaItem>()
        if (intent?.data != null) {
            val uri = intent.data ?: return null
            val type = uri.getMediaContentType() ?: return null
            val name = getFileName(uri, context) ?: "unknown"
            mediaItemList.add(MediaItem(name = name, uri = uri, type = type))
        } else {
            val itemsCount = intent?.clipData?.itemCount ?: 0
            for (i in 0 until itemsCount) {
                intent?.clipData?.getItemAt(i)?.let {
                    val uri = it.uri ?: return null
                    val type = uri.getMediaContentType() ?: return null
                    val name = getFileName(uri, context) ?: "unknown"
                    mediaItemList.add(MediaItem(name = name, uri = uri, type = type))
                }
            }
        }
        return mediaItemList
    }

    private fun Uri.getMediaContentType(): MediaFileType? {
        val mimeType = context.contentResolver.getType(this)
        if (mimeType == null) {
            return null
        }
        val format = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        if (allowedFormats.isNotEmpty() && !allowedFormats.contains(format)) {
            return null
        }
        val uriString = this.toString()
        return when {
            uriString.contains("images") -> MediaFileType.IMAGE
            uriString.contains("Screenshot") -> MediaFileType.IMAGE
            uriString.contains("IMG") -> MediaFileType.IMAGE
            uriString.contains("image") -> MediaFileType.IMAGE
            uriString.contains("jpeg") -> MediaFileType.IMAGE
            uriString.contains("jpg") -> MediaFileType.IMAGE
            uriString.contains("bmp") -> MediaFileType.IMAGE
            uriString.contains("png") -> MediaFileType.IMAGE
            uriString.contains("webp") -> MediaFileType.IMAGE
            uriString.contains("avif") -> MediaFileType.IMAGE
            uriString.contains("raw") -> MediaFileType.IMAGE
            uriString.contains("DCIM") -> MediaFileType.IMAGE
            uriString.contains("tiff") -> MediaFileType.IMAGE
            uriString.contains("heic") -> MediaFileType.IMAGE

            uriString.contains("video") -> MediaFileType.VIDEO
            else -> null
        }
    }

    private fun getFileName(uri: Uri, context: Context): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            context.contentResolver.query(
                uri,
                null,
                null,
                null,
                null
            )
                .use { cursor ->
                    if (cursor != null && cursor.moveToFirst()) {
                        val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        result = cursor.getString(index)
                    }
                }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/') ?: -1
            if (cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result
    }
}