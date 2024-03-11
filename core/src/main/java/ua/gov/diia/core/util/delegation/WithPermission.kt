package ua.gov.diia.core.util.delegation

import android.Manifest
import android.os.Build
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import ua.gov.diia.core.R
import ua.gov.diia.core.models.common.template_dialogs.SystemDialogData
import ua.gov.diia.core.util.event.UiEvent

enum class Permission {
    CAMERA,
    LOCATION,
    STORAGE_READ,
    STORAGE_WRITE,
    POST_NOTIFICATIONS;

    val value: Array<String>
        get() = when (this) {
            CAMERA -> arrayOf(Manifest.permission.CAMERA)
            LOCATION -> arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            STORAGE_READ -> arrayOf("Manifest.permission.READ_EXTERNAL_STORAGE")
            STORAGE_WRITE -> arrayOf("Manifest.permission.WRITE_EXTERNAL_STORAGE")
            POST_NOTIFICATIONS -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    arrayOf("android.permission.POST_NOTIFICATIONS")
                }
            }
        }

    val permissionDialog: SystemDialogData
        get() = SystemDialogData(
            title = dialogTitle,
            message = dialogText,
            positiveButtonTitle = R.string.allow,
            negativeButtonTitle = R.string.deny,
            cancelable = false,
            rationale = rationale,
            rationaleTitle = rationaleTitle
        )

    val rationaleDialog: SystemDialogData
        get() = SystemDialogData(
            title = rationaleDialogTitle,
            message = rationalDialogText,
            positiveButtonTitle = R.string.open_settings,
            negativeButtonTitle = R.string.close,
            cancelable = false,
            rationale = rationale,
            rationaleTitle = rationaleTitle
        )

    private val dialogTitle: Int
        @StringRes
        get() = when (this) {
            CAMERA -> R.string.camera_permission_request_title
            LOCATION -> R.string.location_permission_request_title
            STORAGE_READ,
            STORAGE_WRITE -> R.string.file_permission_request_title

            POST_NOTIFICATIONS -> R.string.notifications_permission_request_title
        }

    private val rationaleDialogTitle: Int
        @StringRes
        get() = when (this) {
            CAMERA -> R.string.camera_permission_request_title_rational
            LOCATION -> R.string.location_permission_request_title
            STORAGE_READ,
            STORAGE_WRITE -> R.string.file_permission_request_title

            POST_NOTIFICATIONS -> R.string.notifications_permission_request_title
        }

    private val dialogText: Int
        @StringRes
        get() = when (this) {
            CAMERA -> R.string.camera_permission_request
            LOCATION -> R.string.location_permission_request
            STORAGE_READ,
            STORAGE_WRITE -> R.string.file_permission_request

            POST_NOTIFICATIONS -> R.string.notifications_permission_request
        }

    private val rationalDialogText: Int
        @StringRes
        get() = when (this) {
            CAMERA -> R.string.camera_permission_request_rational
            LOCATION -> R.string.location_permission_request
            STORAGE_READ,
            STORAGE_WRITE -> R.string.file_permission_request

            POST_NOTIFICATIONS -> R.string.notifications_permission_request
        }

    private val rationaleTitle: Int?
        @StringRes
        get() = when (this) {
            CAMERA -> null
            LOCATION -> R.string.location_permission_rationale_title
            STORAGE_READ,
            STORAGE_WRITE -> R.string.file_permission_rationale_title

            POST_NOTIFICATIONS -> null
        }

    private val rationale: Int?
        @StringRes
        get() = when (this) {
            CAMERA -> R.string.camera_permission_rationale
            LOCATION -> R.string.location_permission_rationale
            STORAGE_READ,
            STORAGE_WRITE -> R.string.file_permission_rationale

            POST_NOTIFICATIONS -> null
        }
}

interface WithPermission : DefaultLifecycleObserver {

    val doOnCameraPermissionGrantedEvent: LiveData<UiEvent>

    val doOnGeoPermissionGrantedEvent: LiveData<UiEvent>

    val doOnPostNotificationPermissionGrantedEvent: LiveData<UiEvent>

    val doOnStoragePermissionGrantedEvent: LiveData<UiEvent>

    val doOnPermissionDeniedEvent: LiveData<UiEvent>

    fun <T : Fragment> T.approvePermission(permission: Permission)

}
