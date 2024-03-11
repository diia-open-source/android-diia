package ua.gov.diia.web.ui

import android.net.http.SslError
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import ua.gov.diia.web.R
import ua.gov.diia.web.databinding.FragmentWebBinding

class WebF : Fragment() {

    private val args: WebFArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWebBinding.inflate(inflater, container, false)
        binding.apply {
            wvWebF.webViewClient = object : WebViewClient() {
                override fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler,
                    error: SslError
                ) {
                    val message = when (error.primaryError) {
                        SslError.SSL_UNTRUSTED ->
                            R.string.error_ssl_cant_check
                        SslError.SSL_EXPIRED -> R.string.error_ssl_expire
                        SslError.SSL_IDMISMATCH -> R.string.error_ssl_valid_error
                        SslError.SSL_NOTYETVALID -> R.string.error_ssl_not_yet_error
                        else -> R.string.error_ssl_cant_check
                    }
                    val sslErrorMessage = getString(message)

                    AlertDialog.Builder(requireContext())
                        .setTitle(R.string.error_ssl_title)
                        .setMessage(getString(R.string.error_ssl_continue_anyway, sslErrorMessage))
                        .setPositiveButton(R.string.next) { _, _ -> handler.proceed() }
                        .setNegativeButton(R.string.cancel) { _, _ -> handler.cancel() }
                        .show()
                }
            }
            wvWebF.settings.allowFileAccess = false
            wvWebF.settings.allowFileAccessFromFileURLs = false
            wvWebF.settings.allowUniversalAccessFromFileURLs = false
            wvWebF.settings.allowContentAccess = false
            wvWebF.loadUrl(args.url)
            invalidateAll()
        }
        return binding.root
    }
}
