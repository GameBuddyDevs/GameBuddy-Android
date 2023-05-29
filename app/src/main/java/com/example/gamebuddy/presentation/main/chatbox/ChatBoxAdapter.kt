package com.example.gamebuddy.presentation.main.chatbox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.gamebuddy.R
import com.example.gamebuddy.data.remote.model.chatbox.Inbox
import com.example.gamebuddy.databinding.ItemMessageBinding
import com.example.gamebuddy.util.formatDateTime
import timber.log.Timber

class ChatBoxAdapter(
    private val onClickListener: OnClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener {
        fun onItemClick(item: Inbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageViewHolder(
            ItemMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            requestOptions = RequestOptions
                .placeholderOf(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground),
            onClickListener = onClickListener
        )
    }

    class MessageViewHolder constructor(
        private val binding: ItemMessageBinding,
        private val requestOptions: RequestOptions,
        private val onClickListener: OnClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Inbox) {
            binding.apply {

                root.setOnClickListener {
                    onClickListener?.onItemClick(item)
                }

                Timber.d("bind: ${item.username}")

                Glide.with(binding.root)
                    .setDefaultRequestOptions(requestOptions)
                    .load(item.avatar)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imgChatUsername)
                txtChatUsername.text = item.username
                txtLastMessage.text = item.lastMessage
                txtTime.text = item.lastMessageTime.formatDateTime()
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MessageViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    fun submitList(blogList: List<Inbox>?) {
        val newList = blogList?.toMutableList()
        differ.submitList(newList)
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Inbox>() {

        override fun areItemsTheSame(oldItem: Inbox, newItem: Inbox): Boolean {
            return oldItem.username == newItem.username
        }

        override fun areContentsTheSame(oldItem: Inbox, newItem: Inbox): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(
        BlogRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )

    class BlogRecyclerChangeCallback(private val chatBoxAdapter: ChatBoxAdapter) : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            chatBoxAdapter.notifyItemInserted(position)
        }

        override fun onRemoved(position: Int, count: Int) {
            chatBoxAdapter.notifyItemRemoved(position)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            chatBoxAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            chatBoxAdapter.notifyItemRangeChanged(position, count, payload)
        }
    }
}