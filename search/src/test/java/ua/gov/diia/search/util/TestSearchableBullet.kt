package ua.gov.diia.search.util

import android.content.Context
import kotlinx.parcelize.Parcelize
import ua.gov.diia.search.models.SearchableBullet

@Parcelize
data class TestSearchableBullet(
    private val displayName: String
) : SearchableBullet {

    override fun getDisplayName(context: Context): String = displayName
}
