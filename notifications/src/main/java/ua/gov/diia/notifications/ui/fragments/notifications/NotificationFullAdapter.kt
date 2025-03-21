package ua.gov.diia.notifications.ui.fragments.notifications

import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import ua.gov.diia.core.models.notification.pull.message.MessageActions
import ua.gov.diia.core.models.notification.pull.message.MessageTypes
import ua.gov.diia.core.models.notification.pull.message.NotificationMessagesBody
import ua.gov.diia.notifications.R
import ua.gov.diia.notifications.databinding.ItemNotificationDividerBinding
import ua.gov.diia.notifications.databinding.ItemNotificationDownloadArrowedLinkBinding
import ua.gov.diia.notifications.databinding.ItemNotificationImageBinding
import ua.gov.diia.notifications.databinding.ItemNotificationInternalArrowedLinkBinding
import ua.gov.diia.notifications.databinding.ItemNotificationTextBinding
import ua.gov.diia.notifications.databinding.ItemNotificationVideoBinding

class NotificationFullAdapter(private val linkSelected: (MessageActions, String) -> Unit) :
    ListAdapter<NotificationMessagesBody, NotificationFullAdapter.ViewHolder>(DiffCallback) {

    var itemsVhs: HashSet<NotificationVideoVH> = hashSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType) {
            R.layout.item_notification_text -> {
                NotificationTextVH(ItemNotificationTextBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false),  linkSelected )
            }

            R.layout.item_notification_image -> {
                NotificationImageVH(
                    ItemNotificationImageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            }

            R.layout.item_notification_video -> {
                NotificationVideoVH(
                    ItemNotificationVideoBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false),
                )
            }

            R.layout.item_notification_divider -> {
                NotificationSeparatorVH(ItemNotificationDividerBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false ))
            }

            R.layout.item_notification_download_arrowed_link -> {
                NotificationDownloadArrowedLinkVH(
                    ItemNotificationDownloadArrowedLinkBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false ), linkSelected)
            }

            R.layout.item_notification_internal_arrowed_link -> {
                NotificationInternalArrowedLinkVH(
                    ItemNotificationInternalArrowedLinkBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false ), linkSelected)
            }

            else -> {
                NotificationTextVH( ItemNotificationTextBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false ), linkSelected )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position).type) {
            MessageTypes.text -> R.layout.item_notification_text
            MessageTypes.image -> R.layout.item_notification_image
            MessageTypes.video -> R.layout.item_notification_video
            MessageTypes.separator -> R.layout.item_notification_divider
            MessageTypes.downloadArrowedLink -> R.layout.item_notification_download_arrowed_link

            MessageTypes.externalArrowedLink,
            MessageTypes.internalArrowedLink -> R.layout.item_notification_internal_arrowed_link

            else -> R.layout.item_notification_text
        }
    }

    abstract inner class ViewHolder(val viewBinding: ViewBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        /** Initial click by item */
        abstract fun bind(notificationMessageBody: NotificationMessagesBody)
    }

    inner class NotificationTextVH(
        private val binding: ItemNotificationTextBinding,
        private val linkSelected: (MessageActions, String) -> Unit
    ) : ViewHolder(binding) {

            val onLinkClicked: (String) -> Unit = { link ->
                val item = getItem(bindingAdapterPosition)
                linkSelected.invoke(item.data?.action ?: MessageActions.default, link)
            }

        override fun bind(notificationMessageBody: NotificationMessagesBody) {
            binding.message = notificationMessageBody
            binding.adapter = this
        }
    }

    inner class NotificationSeparatorVH(binding: ItemNotificationDividerBinding) : ViewHolder(binding) {
        override fun bind(notificationMessageBody: NotificationMessagesBody) {
            /* Logic implemented with dataBinding */
        }
    }

    inner class NotificationDownloadArrowedLinkVH(
        private val binding: ItemNotificationDownloadArrowedLinkBinding,
        private val linkSelected: (MessageActions, String) -> Unit ) : ViewHolder(binding) {

        init {
            viewBinding.root.setOnClickListener {
                val item = getItem(bindingAdapterPosition)
                linkSelected.invoke(item.data?.action ?: MessageActions.default, item.data?.link ?: "")
            }
        }

        override fun bind(notificationMessageBody: NotificationMessagesBody) {
            binding.message = notificationMessageBody
        }
    }

    inner class NotificationInternalArrowedLinkVH(
        private val binding: ItemNotificationInternalArrowedLinkBinding,
        private val linkSelected: (MessageActions, String) -> Unit ) : ViewHolder(binding) {

        init {
            viewBinding.root.setOnClickListener {
                val item = getItem(bindingAdapterPosition)
                linkSelected.invoke(item.data?.action ?: MessageActions.default, item.data?.link ?: "")
            }
        }

        override fun bind(notificationMessageBody: NotificationMessagesBody) {
            binding.message = notificationMessageBody
        }
    }

    inner class NotificationImageVH( private val binding: ItemNotificationImageBinding ) : ViewHolder(binding) {
        override fun bind(notificationMessageBody: NotificationMessagesBody) {
            val image = notificationMessageBody.data?.link ?: ""
            val requestOption = RequestOptions.bitmapTransform(RoundedCorners(32))
            with(binding) {
                Glide.with(binding.root.context)
                    .load(image)
                    .apply(requestOption)
                    .into(ivMessagesImage)
            }
        }
    }

    inner class NotificationVideoVH(binding: ItemNotificationVideoBinding) : ViewHolder(binding) {
        /** PLAYER */
        var exoPlayer: ExoPlayer? = null

        init {
            exoPlayer = ExoPlayer.Builder(binding.root.context).build()

            /* Makes rounded corners */
            with(binding) {
                tvPlayer.player = exoPlayer
                tvPlayer.outlineProvider = object:ViewOutlineProvider(){
                    override fun getOutline(view: View?, outline: Outline?) {
                        outline?.setRoundRect(
                            0, 0,view?.width ?: 0,view?.height ?: 0,32F)
                    }
                }
                tvPlayer.clipToOutline = true
            }

            binding.tvPlayer.player = exoPlayer
            itemsVhs.add(this)
        }

        override fun bind(notificationMessageBody: NotificationMessagesBody) {
            val url = notificationMessageBody.data?.link ?: ""
            val mediaItem = MediaItem.Builder()
                .setUri(url)
                .setMimeType(MimeTypes.VIDEO_MP4)
                .build()
            exoPlayer?.apply {
                setMediaItem(mediaItem)
                prepare()
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<NotificationMessagesBody>() {
        override fun areItemsTheSame(oldItem: NotificationMessagesBody, newItem: NotificationMessagesBody): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NotificationMessagesBody, newItem: NotificationMessagesBody): Boolean {
            return if (newItem.type == MessageTypes.downloadArrowedLink) {
                false
            } else {
                oldItem == newItem
            }
        }
    }

    fun pauseVideoPlayer(pause: Boolean) {
        itemsVhs.forEach { vh ->
            if (pause) { vh.exoPlayer?.pause() }
            else { vh.exoPlayer?.play() }
        }
    }

    fun releaseVideoPlayer() {
        itemsVhs.forEach { vh ->
            vh.exoPlayer?.stop()
            vh.exoPlayer?.release()
        }
    }
}