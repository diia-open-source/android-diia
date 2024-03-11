package ua.gov.diia.bankid.ui.auth

import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlc
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderCircularEclipse23Subatomic


@Composable
internal fun BankAuthScreen(
    modifier: Modifier = Modifier,
    dataState: State<BankAuthScreenData>,
    configureWebView: (WebView) -> Unit,
    onUIAction: (UIAction) -> Unit
) {
    val data = dataState.value
    Box(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = modifier.fillMaxSize()) {
            NavigationPanelMlc(
                data = NavigationPanelMlcData(isContextMenuExist = false),
                onUIAction = onUIAction
            )
            AndroidView(
                modifier = modifier.fillMaxSize(),
                factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        configureWebView(this)
                    }
                }
            )
        }
        if (data.progressLoadState) {
            LoaderCircularEclipse23Subatomic(modifier = Modifier.size(24.dp))
        }
    }
}

internal data class BankAuthScreenData(val progressLoadState: Boolean = false)

@Composable
@Preview
internal fun BankAuthScreenPreview() {
    val state = remember { mutableStateOf(BankAuthScreenData(progressLoadState = true)) }
    BankAuthScreen(dataState = state, configureWebView = { }, onUIAction = {})
}