package ua.gov.diia.notifications.ui.fragments.home.notificationsettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.notifications.databinding.FragmentNotificationSettingsBinding
import ua.gov.diia.notifications.ui.fragments.home.notifications.adapters.SubscriptionAdapter

@AndroidEntryPoint
class NotificationSettingsF : Fragment() {


    private var binding: FragmentNotificationSettingsBinding? = null

    val vm: NotificationSettingsVM by viewModels()
    val adapter = SubscriptionAdapter { code, isChecked ->
        if (isChecked)
            vm.subscribe(code)
        else
            vm.unsubscribe(code)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationSettingsBinding.inflate(inflater, container, false)

        binding?.apply {
            viewModel = vm
            lifecycleOwner = viewLifecycleOwner
            rvSubscriptions.adapter = adapter
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }

        vm.subscriptions.observe(viewLifecycleOwner) {
            adapter.submitList(it.subscriptions)
        }

        vm.getSubs.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                vm.getSubs()
            }
        }

        vm.error.observeUiDataEvent(viewLifecycleOwner, ::openTemplateDialog)

        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                DIALOG_ACTION_GET_SUBS -> vm.getSubs()
            }
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private companion object {
        const val DIALOG_ACTION_GET_SUBS = "getSubscriptions"
    }
}