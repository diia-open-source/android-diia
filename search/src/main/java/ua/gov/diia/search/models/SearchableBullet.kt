package ua.gov.diia.search.models

import android.content.Context
import android.os.Parcelable

interface SearchableBullet : Parcelable {

    fun getDisplayName(context: Context) : String
}