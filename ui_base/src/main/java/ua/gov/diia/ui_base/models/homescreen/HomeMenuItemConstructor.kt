package ua.gov.diia.ui_base.models.homescreen

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

data class HomeMenuItemConstructor(
    val position: Int,
    @ColorRes val topBarColor: Int,
    @ColorRes val bottomBarColor: Int,
    @StringRes val headerText: Int,
)