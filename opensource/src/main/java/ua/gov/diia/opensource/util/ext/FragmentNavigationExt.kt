package ua.gov.diia.opensource.util.ext

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.opensource.NavMainXmlDirections
import ua.gov.diia.opensource.ui.PublicServicesHomeConst
import ua.gov.diia.publicservice.models.PublicService

fun Fragment.navigateToPublicService(service: PublicService) {
    when (service.code) {
        PublicServicesHomeConst.PS_SERVICE_CRIME_CERTIFICATE -> navigate(
            NavMainXmlDirections.actionHomeFToCriminalCert(
                contextMenu = service.menu
            )
        )
    }
}

fun Fragment.isPermissionGraned(permission: String): Boolean =
    ContextCompat.checkSelfPermission(
        requireActivity(),
        permission
    ) == PackageManager.PERMISSION_GRANTED

fun Fragment.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", requireContext().packageName, null)
    }
    startActivity(intent)
}
