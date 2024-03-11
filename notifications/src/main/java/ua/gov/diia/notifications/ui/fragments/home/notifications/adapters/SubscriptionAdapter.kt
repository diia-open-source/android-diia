package ua.gov.diia.notifications.ui.fragments.home.notifications.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.gov.diia.notifications.databinding.ItemSubscriptionBinding
import ua.gov.diia.notifications.models.notification.Subscription

class SubscriptionAdapter(private val checkedListener: (String, Boolean) -> Unit) :
    ListAdapter<Subscription, SubscriptionAdapter.SubscriptionVH>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Subscription>() {
        override fun areItemsTheSame(oldItem: Subscription, newItem: Subscription): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: Subscription, newItem: Subscription): Boolean {
            return oldItem == newItem
        }
    }

    class SubscriptionVH(private var binding: ItemSubscriptionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(s: Subscription, checkedListener: (String, Boolean) -> Unit) {
            with(binding){
                subscription = s
                switchState.setOnCheckedChangeListener { buttonView, isChecked ->
                    checkedListener(s.code, binding.switchState.isChecked)
                    switchState.isChecked = s.switchState
                }
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionVH {
        return SubscriptionVH(ItemSubscriptionBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: SubscriptionVH, position: Int) {
        val subscription = getItem(position)
        holder.bind(subscription, checkedListener)
    }

}