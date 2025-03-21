package ua.gov.diia.core.util.extensions.fragment

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.extensions.context.getStringSafe

fun Fragment.openLink(link: String, withCrashlytics: WithCrashlytics) {
    context?.openLink(link, withCrashlytics)
}

fun Context.openLink(link: String, withCrashlytics: WithCrashlytics) {
    if (link.startsWith("https:")) {
        val uri = try {
            link.toUri()
        } catch (e: Exception) {
            withCrashlytics.sendNonFatalError(e)
            null
        }
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = uri
        }
        startActivity(intent)
    }
}

fun Context.openPlayMarket(withCrashlytics: WithCrashlytics) {
    val uri = Uri.parse("market://details?id=ua.gov.diia.app")
    val goToMarket = Intent(Intent.ACTION_VIEW, uri).apply {
        addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY
                    or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
                    or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
    }
    try {
        startActivity(goToMarket)
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=ua.gov.diia.app")
            )
        )
        withCrashlytics.sendNonFatalError(e)
    }
}

fun Context.handlePhoneIntent(data: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", data, null)) ;
    startActivity(intent)
}

fun Fragment.shareLink(
    link: String,
    @StringRes chooserTitle: Int? = null
) {
    val chooserTitleDisplayString = requireContext().getStringSafe(chooserTitle)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, link)
    }
    startActivity(Intent.createChooser(intent, chooserTitleDisplayString))
}