package ua.gov.diia.opensource.ui.fragments.context

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.util.extensions.context.getColorCompat
import ua.gov.diia.ui_base.util.view.inflater
import ua.gov.diia.opensource.databinding.ItemContextMenuFieldBinding

class ContextMenuListAdapter(
    private val items: List<ContextMenuField>,
    private val onItemSelected: (ContextMenuField) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ContextFieldVH(
        private val binding: ItemContextMenuFieldBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ContextMenuField,
            onItemSelected: (ContextMenuField) -> Unit
        ) {
            with(binding) {
                val context = binding.root.context
                val tintColorCompat = context.getColorCompat(item.getTintColor())
                separator.setBackgroundColor(tintColorCompat)
                text.text = item.getDisplayName(context)
                text.setOnClickListener { onItemSelected(item) }
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ContextFieldVH(
            ItemContextMenuFieldBinding.inflate(parent.inflater, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        with((holder as ContextFieldVH)){
            bind(item,onItemSelected)

        }
    }

    override fun getItemCount(): Int = items.size


}
