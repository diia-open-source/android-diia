package ua.gov.diia.documents.util.view

import android.content.ClipData
import android.view.View
import com.google.android.material.snackbar.Snackbar
import ua.gov.diia.core.util.extensions.context.serviceClipboard
import ua.gov.diia.ui_base.util.view.showTopSnackBar
import ua.gov.diia.documents.R


fun View.showCopyDocIdClipedSnackBar(docId: String, topPadding: Float) {
    val clip = ClipData.newPlainText("docId", docId)

    context.serviceClipboard
        ?.setPrimaryClip(clip)

    showTopSnackBar(R.string.doc_id_copied, Snackbar.LENGTH_LONG, topPadding)
}