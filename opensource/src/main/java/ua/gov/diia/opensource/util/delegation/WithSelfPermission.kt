package ua.gov.diia.opensource.util.delegation

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import ua.gov.diia.core.models.ConsumableString
import ua.gov.diia.core.models.common.template_dialogs.SystemDialogData
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.delegation.Permission
import ua.gov.diia.core.util.delegation.WithPermission
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResult
import ua.gov.diia.opensource.R
import ua.gov.diia.opensource.util.extensions.fragment.isPermissionGraned
import javax.inject.Inject

class DefaultSelfPermissionBehavior @Inject constructor() : WithPermission {

    private companion object {

        const val RESULT_KEY_SYS_DIALOG_PERMISSION =
            "DefaultSelfPermissionBehavior.RESULT_KEY_SYS_DIALOG_PERMISSION"

        const val RESULT_KEY_SYS_DIALOG_RATIONALE =
            "DefaultSelfPermissionBehavior.RESULT_KEY_SYS_DIALOG_RATIONALE"

        const val RESULT_KEY_PERMISSION =
            "DefaultSelfPermissionBehavior.RESULT_KEY_PERMISSION"
    }

    private val _doOnCameraPermissionGrantedEvent = MutableLiveData<UiEvent>()
    override val doOnCameraPermissionGrantedEvent: LiveData<UiEvent> =
        _doOnCameraPermissionGrantedEvent

    private val _doOnGeoPermissionGrantedEvent = MutableLiveData<UiEvent>()
    override val doOnGeoPermissionGrantedEvent: LiveData<UiEvent> =
        _doOnGeoPermissionGrantedEvent

    private val _doOnPostNotificationPermissionGrantedEvent =
        MutableLiveData<UiEvent>()
    override val doOnPostNotificationPermissionGrantedEvent: LiveData<UiEvent> =
        _doOnPostNotificationPermissionGrantedEvent

    private val _doOnStoragePermissionGrantedEvent = MutableLiveData<UiEvent>()
    override val doOnStoragePermissionGrantedEvent: LiveData<UiEvent> =
        _doOnStoragePermissionGrantedEvent

    private val _doOnPermissionDeniedEvent = MutableLiveData<UiEvent>()
    override val doOnPermissionDeniedEvent: LiveData<UiEvent> =
        _doOnPermissionDeniedEvent

    private var requestedPermission: Permission? = null
    private var permissionLauncher: ActivityResultLauncher<Array<String>>? = null

    override fun <T : Fragment> T.approvePermission(permission: Permission) {
        val granted = permission.value.all { isPermissionGraned(it) }
        if (granted) {
            when (permission) {
                Permission.CAMERA -> _doOnCameraPermissionGrantedEvent.value =
                    UiEvent()

                Permission.LOCATION -> _doOnGeoPermissionGrantedEvent.value =
                    UiEvent()

                Permission.POST_NOTIFICATIONS -> _doOnPostNotificationPermissionGrantedEvent.value =
                    UiEvent()

                Permission.STORAGE_READ -> _doOnStoragePermissionGrantedEvent.value =
                    UiEvent()

                Permission.STORAGE_WRITE -> _doOnStoragePermissionGrantedEvent.value =
                    UiEvent()
            }
        } else {
            requestedPermission = permission
            if (permission.value.size > 1) {
                val shouldShowRationale = permission.value.map {
                    ActivityCompat.shouldShowRequestPermissionRationale(activity as Activity, it)
                }
                val partiallyGranted = permission.value.any { isPermissionGraned(it) }
                if (shouldShowRationale.all { it }) {
                    requestAppSettings(permission.rationaleDialog)
                } else if (partiallyGranted) {
                    requestPermission()
                } else {
                    requestAppPermission(permission.permissionDialog)
                }
            } else {
                if (shouldShowRequestPermissionRationale(permission.value.first())) {
                    requestAppSettings(permission.rationaleDialog)
                } else {
                    requestAppPermission(permission.permissionDialog)
                }
            }
        }
    }

    private fun <T : Fragment> T.obtainPermissionLauncher(permission: Permission): ActivityResultLauncher<Array<String>>? =
        if (permissionLauncher == null) {
            requireActivity().activityResultRegistry.register(
                RESULT_KEY_PERMISSION,
                ActivityResultContracts.RequestMultiplePermissions()
            ) { resultMapping ->
                val granted = resultMapping.any { it.value }
                if (granted) {
                    when (permission) {
                        Permission.CAMERA -> _doOnCameraPermissionGrantedEvent.value =
                            UiEvent()

                        Permission.LOCATION -> _doOnGeoPermissionGrantedEvent.value =
                            UiEvent()

                        Permission.POST_NOTIFICATIONS -> _doOnPostNotificationPermissionGrantedEvent.value =
                            UiEvent()

                        Permission.STORAGE_READ -> _doOnStoragePermissionGrantedEvent.value =
                            UiEvent()

                        Permission.STORAGE_WRITE -> _doOnStoragePermissionGrantedEvent.value =
                            UiEvent()
                    }
                } else {
                    _doOnPermissionDeniedEvent.value = UiEvent()
                }
            }.also { permissionLauncher = it }
        } else {
            permissionLauncher
        }

    private fun <T : Fragment> T.requestPermission() {
        val permission = requestedPermission ?: return
        val launcher = obtainPermissionLauncher(permission) ?: return
        launcher.launch(permission.value)
    }

    private fun <T : Fragment> T.requestAppPermission(dialogData: SystemDialogData) {
        registerForNavigationResult<ConsumableString>(
            RESULT_KEY_SYS_DIALOG_PERMISSION
        ) { dataEvent ->
            dataEvent.consumeEvent { action ->
                when (action) {
                    ActionsConst.SYSTEM_DIALOG_NEGATIVE -> _doOnPermissionDeniedEvent.value =
                        UiEvent()

                    ActionsConst.SYSTEM_DIALOG_POSITIVE -> requestPermission()
                }
            }
        }

        navigateToSystemDialog(
            dialogData = dialogData,
            resultKey = RESULT_KEY_SYS_DIALOG_PERMISSION
        )
    }

    override fun <T : Fragment> T.requestAppSettings(permission: Permission) {
        requestAppSettings(permission.rationaleDialog)
    }

    private fun <T : Fragment> T.requestAppSettings(dialogData: SystemDialogData) {
        registerForNavigationResult<ConsumableString>(
            RESULT_KEY_SYS_DIALOG_RATIONALE
        ) { dataEvent ->
            dataEvent.consumeEvent { action ->
                when (action) {
                    ActionsConst.SYSTEM_DIALOG_NEGATIVE -> _doOnPermissionDeniedEvent.value =
                        UiEvent()
                }
            }
        }

        navigateToSystemDialog(
            dialogData = dialogData,
            resultKey = RESULT_KEY_SYS_DIALOG_RATIONALE
        )
    }

    private fun <T : Fragment> T.navigateToSystemDialog(
        dialogData: SystemDialogData,
        resultKey: String
    ) {
        val args = Bundle().apply {
            putString("resultKey", resultKey)
            putParcelable("dialog", dialogData)
        }
        findNavController().navigate(R.id.destination_systemDialog, args)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        permissionLauncher?.unregister()
    }
}