package com.example.gamebuddy.presentation.main.chat

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.gamebuddy.R
import com.example.gamebuddy.data.remote.model.message.Conversation
import com.example.gamebuddy.databinding.ItemMessageReceiveBinding
import com.example.gamebuddy.databinding.ItemMessageSendBinding
import com.example.gamebuddy.util.formatDateTime

class ChatAdapter(
    private val userId: String?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_SENT -> SentMessageViewHolder(
                ItemMessageSendBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                requestOptions = RequestOptions.placeholderOf(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground),
            )

            VIEW_TYPE_RECEIVED -> ReceivedMessageViewHolder(
                ItemMessageReceiveBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                requestOptions = RequestOptions.placeholderOf(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground),
            )

            else -> null!!
        }


    class ReceivedMessageViewHolder(
        private val binding: ItemMessageReceiveBinding, requestOptions: RequestOptions
    ) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: Conversation) {
            binding.apply {
                txtMessage.text = item.message
                txtTime.text = item.date.formatDateTime()
            }
        }
    }

    class SentMessageViewHolder(
        private val binding: ItemMessageSendBinding,
        requestOptions: RequestOptions
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: Conversation) {
            binding.apply {
                txtMessage.text = item.message
                txtTime.text = item.date.formatDateTime()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SentMessageViewHolder -> holder.bind(differ.currentList[position])
            is ReceivedMessageViewHolder -> holder.bind(differ.currentList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (differ.currentList[position].sender == userId) VIEW_TYPE_RECEIVED else VIEW_TYPE_SENT
    }

    override fun getItemCount() = differ.currentList.size

    fun submitList(blogList: List<Conversation>?) {
        val newList = blogList?.toMutableList()
        differ.submitList(newList)
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Conversation>() {

        override fun areItemsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(
        BlogRecyclerChangeCallback(this), AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )

    class BlogRecyclerChangeCallback(private val chatAdapter: ChatAdapter) : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            chatAdapter.notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            chatAdapter.notifyItemRemoved(position)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            chatAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            chatAdapter.notifyItemRangeChanged(position, count, payload)
        }
    }

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }
}