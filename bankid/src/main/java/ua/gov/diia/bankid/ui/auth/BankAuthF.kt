package ua.gov.diia.bankid.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.bankid.ui.auth.BankAuthConst.PROGRESS_ACTIVE
import ua.gov.diia.bankid.ui.auth.BankAuthConst.PROGRESS_INACTIVE
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.delegation.WithBuildConfig
import ua.gov.diia.core.util.extensions.fragment.hideKeyboard
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.verification.model.VerificationFlowResult
import javax.inject.Inject

@AndroidEntryPoint
internal class BankAuthF : Fragment() {

    private companion object {
        const val DII_APP = "app"
        const val DII_MARKET = "market"
        const val X_DII_HEADER = "x-diia-version"
    }

    private val viewModel: BankAuthVM by viewModels()
    private val args: BankAuthFArgs by navArgs()
    private var composeView: ComposeView? = null

    @Inject
    lateinit var withBuildConfig: WithBuildConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.doInit(args.requestData.bankCode)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        composeView = ComposeView(requireContext())
        return composeView
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        composeView?.setContent {
            with(viewModel) {
                navigation.collectAsEffect { navigation ->
                    when (navigation) {
                        is BankAuthVM.Navigation.CompleteAuth -> {
                            completeAuth(navigation.data)
                        }

                        is BaseNavigation.Back -> {
                            findNavController().popBackStack()
                        }
                    }
                }
            }

            BankAuthScreen(
                dataState = viewModel.uiData,
                configureWebView = {
                    it.webViewClient = bankIdWebClient()
                    it.settings.allowFileAccess = false
                    it.settings.allowFileAccessFromFileURLs = false
                    it.settings.allowUniversalAccessFromFileURLs = false
                    it.settings.allowContentAccess = false
                    it.settings.domStorageEnabled = true
                    it.settings.javaScriptEnabled = true
                    it.loadUrl(args.requestData.authUrl)
                },
                onUIAction = { viewModel.onUIAction(it) }
            )
        }
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun bankIdWebClient() = object : WebViewClient() {

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean =
            if (view != null && url != null) {
                handleUrlOverride(view, url)
            } else {
                false
            }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val url = request?.url?.toString()
            return if (view != null && url != null) {
                handleUrlOverride(view, url)
            } else {
                false
            }
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            viewModel.onUIAction(UIAction(PROGRESS_ACTIVE))
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            viewModel.onUIAction(UIAction(PROGRESS_INACTIVE))
        }
    }

    private fun handleUrlOverride(view: WebView, url: String): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        val activities: List<ResolveInfo> =
            activity?.packageManager?.queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            ) ?: emptyList()

        val hasAppToHandle = activities.isNotEmpty()

        when {
            url.startsWith(withBuildConfig.getBankIdCallbackUrl()) -> {
                viewModel.parseAuthCode(url)
            }

            url.startsWith(DII_APP) -> if (hasAppToHandle) {
                startActivity(intent)
            }

            url.startsWith(DII_MARKET) -> if (!hasAppToHandle) {
                startActivity(intent)
            }

            else -> {
                view.loadUrl(url, getCustomHeaders())
            }
        }
        return true
    }

    private fun getCustomHeaders(): MutableMap<String, String> {
        val headers: MutableMap<String, String> = HashMap()
        headers[X_DII_HEADER] = "Android:${withBuildConfig.getVersionName()}"
        return headers
    }

    private fun completeAuth(result: VerificationFlowResult) {
        setNavigationResult(
            arbitraryDestination = args.resultDestination,
            key = args.resultKey,
            data = result
        )
        findNavController().popBackStack(args.resultDestination, false)
    }

}