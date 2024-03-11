package ua.gov.diia.ps_criminal_cert.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertStatus

class CriminalCertHomeTabsAdapter(
    private val contextMenu: Array<ContextMenuField>?,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CriminalCertListF().apply {
                arguments = Bundle().apply {
                    putSerializable("certStatus", CriminalCertStatus.DONE)
                    putSerializable("contextMenu", contextMenu)
                }
            }
            else -> CriminalCertListF().apply {
                arguments = Bundle().apply {
                    putSerializable("certStatus", CriminalCertStatus.PROCESSING)
                    putSerializable("contextMenu", contextMenu)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}