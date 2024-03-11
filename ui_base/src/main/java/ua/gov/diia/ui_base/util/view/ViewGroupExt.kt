package ua.gov.diia.ui_base.util.view

import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Creates the [LayoutInflater] from a given [ViewGroup].
 */
val ViewGroup.inflater: LayoutInflater
    get() = LayoutInflater.from(this.context)
