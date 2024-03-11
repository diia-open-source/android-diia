package ua.gov.diia.opensource.ui.fragments.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ua.gov.diia.core.util.extensions.fragment.currentDestinationId
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResultOnce
import ua.gov.diia.opensource.databinding.FragmentSettingsBinding
import ua.gov.diia.pin.repository.LoginPinRepository
import javax.inject.Inject

@AndroidEntryPoint
class SettingsF : Fragment() {

    private val vm: SettingsFVM by viewModels()
    private var binding: FragmentSettingsBinding? = null

    @Inject
    lateinit var loginPinRepository: LoginPinRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding?.apply {
            vmSettingsFVM = vm
            invalidateAll()
        }
        vm.apply {
            settingsAction.observe(viewLifecycleOwner) {
                if (it !is SettingsFVM.SettingsAction.None) {
                    vm.clearAction()
                }
                when (it) {
                    is SettingsFVM.SettingsAction.PinCodeChangeAction -> {
                        navigateToResetPin()
                    }
                    is SettingsFVM.SettingsAction.CloseSettingsAction -> {
                        findNavController().navigateUp()
                    }
                    is SettingsFVM.SettingsAction.DocStack -> {
                        navigate(SettingsFDirections.actionGlobalToStackOrder())
                    }
                    is SettingsFVM.SettingsAction.OpenSystemNotificationsAction -> {
                        openSystemNotificationSettings()
                    }
                    else -> {
                    }
                }
            }
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerForNavigationResultOnce<String>(RESULT_KEY_PIN) { pin ->
            lifecycleScope.launch {
                loginPinRepository.setUserAuthorized(pin)
            }
        }
    }

    private fun navigateToResetPin() {
        navigate(
            SettingsFDirections.actionSettingsFToDestinationResetPin(
                resultDestination = currentDestinationId ?: return,
                resultKey = RESULT_KEY_PIN
            )
        )
    }

    private fun openSystemNotificationSettings() {
        val intent = Intent(APP_NOTE_SETTINGS)
        intent.putExtra(APP_PACKAGE, requireActivity().packageName)
        intent.putExtra(APP_UID, requireActivity().applicationInfo.uid)
        intent.putExtra(APP_PROVIDER, requireActivity().packageName)

        val packageManager = requireActivity().packageManager
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private companion object {
        const val APP_NOTE_SETTINGS = "android.settings.APP_NOTIFICATION_SETTINGS"
        const val APP_PACKAGE = "app_package"
        const val APP_UID = "app_uid"
        const val APP_PROVIDER = "android.provider.extra.APP_PACKAGE"
        const val RESULT_KEY_PIN = "SettingsF.RESULT_KEY_PIN"
    }
}