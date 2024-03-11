package ua.gov.diia.search.models

import android.os.Parcelable

interface SearchableItem : Parcelable {

    fun getDisplayTitle(): String

    fun getQueryString() : String
}