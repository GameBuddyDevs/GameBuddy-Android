package com.example.gamebuddy.presentation.main.chatbox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.ItemFriendBinding
import com.example.gamebuddy.domain.model.Friend

class FriendsAdapter(
    private val onClickListener: OnFriendClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnFriendClickListener {
        fun onItemClick(position: Int, item: Friend)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageViewHolder(
            ItemFriendBinding.inflate(
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
        private val binding: ItemFriendBinding,
        private val requestOptions: RequestOptions,
        private val onClickListener: OnFriendClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Friend) {
            binding.apply {

                root.setOnClickListener {
                    onClickListener?.onItemClick(absoluteAdapterPosition, item)
                }

                Glide.with(binding.root)
                    .setDefaultRequestOptions(requestOptions)
                    .load(item.avatar)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imgFriend)
                txtFriendUsername.text = item.username
                txtFriendAge.text = item.age.toString()
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

    fun submitList(blogList: List<Friend>?) {
        val newList = blogList?.toMutableList()
        differ.submitList(newList)
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Friend>() {

        override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
            return oldItem.username == newItem.username
        }

        override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(
        BlogRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )

    class BlogRecyclerChangeCallback(private val friendsAdapter: FriendsAdapter) :
        ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            friendsAdapter.notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            friendsAdapter.notifyItemRemoved(position)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            friendsAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            friendsAdapter.notifyItemRangeChanged(position, count, payload)
        }
    }
}