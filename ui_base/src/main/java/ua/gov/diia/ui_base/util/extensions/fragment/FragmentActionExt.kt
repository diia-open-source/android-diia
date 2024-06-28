package ua.gov.diia.ui_base.util.extensions.fragment

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersConstants

fun Fragment.handleTextWithParamsIntent(type: String, data: String) {
    when (type){
        TextWithParametersConstants.TYPE_PHONE -> {
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", data, null)) ;
            context?.startActivity(intent)
        }
        TextWithParametersConstants.TYPE_LINK -> {
            //openLink(data)
        }
        TextWithParametersConstants.TYPE_MAIL -> {
            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                this.type = "text/plain"
                val to = arrayOf(data.replace("mailto:", ""))
                this.putExtra(Intent.EXTRA_EMAIL, to)
            }
            context?.startActivity(Intent.createChooser(emailIntent, ""))
        }
    }

}