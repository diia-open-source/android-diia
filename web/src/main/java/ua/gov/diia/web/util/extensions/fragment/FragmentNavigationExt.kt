package ua.gov.diia.web.util.extensions.fragment

import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ua.gov.diia.core.R
import ua.gov.diia.core.util.extensions.context.getColorCompat
import ua.gov.diia.core.util.extensions.context.isChromeBrowserExist
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import ua.gov.diia.web.NavWebDirections

fun Fragment.navigateToWebView(url: String) {
    if (url.startsWith("https:")) {
        if (requireContext().isChromeBrowserExist()) {

            val params = CustomTabColorSchemeParams.Builder()
                .setToolbarColor(requireContext().getColorCompat(R.color.colorPrimary))
                .build()

            CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(params)
                .build()
                .launchUrl(requireContext(), url.toUri())
        } else {
            findNavController().navigate(
                NavWebDirections.actionGlobalToWebF(
                    url = url,
                    refresh = false,
                )
            )
        }
    }
}