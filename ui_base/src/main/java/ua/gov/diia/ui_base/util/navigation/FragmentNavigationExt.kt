package ua.gov.diia.ui_base.util.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import ua.gov.diia.ui_base.R
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.util.extensions.fragment.currentDestinationId
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.ui_base.NavTemplateDialogDirections


/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////// Global navigation //////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////

fun Fragment.openTemplateDialog(
    templateDialogModel: TemplateDialogModel,
    @IdRes destinationId: Int = -1
) {
    if (currentDestinationId != R.id.template_dialog) {
        navigate(
            NavTemplateDialogDirections.actionGlobalToTemplateDialog(
                templateDialogModel,
                destinationId
            )
        )
    }
}

