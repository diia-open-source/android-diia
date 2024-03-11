package ua.gov.diia.ps_criminal_cert.ui.steps.type

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertTypes
import ua.gov.diia.ui_base.util.view.inflater
import ua.gov.diia.ps_criminal_cert.databinding.ItemCriminalCertTypeBinding

class CriminalCertTypesAdapter(
    private val onItemClicked: (CriminalCertTypes.Type) -> Unit
) : ListAdapter<CriminalCertTypes.Type, CriminalCertTypesAdapter.ViewHolder>(
    DiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            binding = ItemCriminalCertTypeBinding.inflate(parent.inflater, parent, false),
            onItemClicked = {
                onItemClicked(getItem(it))
            }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).run(holder::bind)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.forEach { list ->
                (list as List<String>).forEach { payload ->
                    if (payload == PAYLOAD_IS_SELECTED) {
                        holder.setSelected(getItem(position).isSelected)
                    }
                }
            }
        }
    }

    class ViewHolder(
        private val binding: ItemCriminalCertTypeBinding,
        private val onItemClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClicked(bindingAdapterPosition)
            }
        }

        fun bind(type: CriminalCertTypes.Type) {
            setSelected(type.isSelected)
            binding.nameTv.text = type.name
            binding.descriptionTv.text = type.description
        }

        fun setSelected(isSelected: Boolean) {
            binding.radioBtn.isChecked = isSelected
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CriminalCertTypes.Type>() {
        override fun areItemsTheSame(
            oldItem: CriminalCertTypes.Type,
            newItem: CriminalCertTypes.Type,
        ): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(
            oldItem: CriminalCertTypes.Type,
            newItem: CriminalCertTypes.Type,
        ): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: CriminalCertTypes.Type, newItem: CriminalCertTypes.Type): Any {
            val payloads = mutableListOf<String>()
            if (oldItem.isSelected != newItem.isSelected) {
                payloads.add(PAYLOAD_IS_SELECTED)
            }
            return payloads
        }

        const val PAYLOAD_IS_SELECTED = "PAYLOAD_IS_SELECTED"
    }
}