package ua.gov.diia.search.adapters

import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import ua.gov.diia.search.R
import ua.gov.diia.search.models.SearchableBullet

@BindingAdapter("data")
fun RadioGroup.inflate(buttonsData: List<SearchableBullet>?) {
    if (buttonsData.isNullOrEmpty()) return

    buttonsData.forEachIndexed { position, data ->
        val childButton = RadioButton(context).apply {
            id = position
            typeface = ResourcesCompat.getFont(context,R.font.e_ukraine_regular)
            text = data.getDisplayName(context)
        }

        addView(childButton)
    }
}