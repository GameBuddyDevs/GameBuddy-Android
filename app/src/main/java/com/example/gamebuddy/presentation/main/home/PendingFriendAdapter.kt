package com.example.gamebuddy.presentation.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.ItemPendingBinding
import com.example.gamebuddy.domain.model.Pending.PendingFriends


class PendingFriendAdapter(
    private val onClickListener: OnClickListener? = null
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    interface OnClickListener {

        fun onItemClick(position: Int, item: PendingFriends, accept:Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageViewHolder(
            ItemPendingBinding.inflate(
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
        private val binding: ItemPendingBinding,
        private val requestOptions: RequestOptions,
        private val onClickListener: OnClickListener?
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: PendingFriends){
            binding.apply {
                binding.imageButton.setOnClickListener {
                    onClickListener?.onItemClick(absoluteAdapterPosition,item,true)
                }
                binding.imageButton2.setOnClickListener {
                    onClickListener?.onItemClick(absoluteAdapterPosition,item,false)
                }
                val url = "https://firebasestorage.googleapis.com/v0/b/gamebuddy-a6a7e.appspot.com/o/avatar-images%2Fapex1.jpg?alt=media&token=${item.avatar}"
                Glide.with(binding.root)
                    .load(url)
                    .into(imgPendingUserPhoto)
                txtUsername.text = item.username
                txtUsernameDesc.text = item.country
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

    fun submitList(blogList: List<PendingFriends>?) {
        val newList = blogList?.toMutableList()
        differ.submitList(newList)
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PendingFriends>() {

        override fun areItemsTheSame(oldItem: PendingFriends, newItem: PendingFriends): Boolean {
            return oldItem.username == newItem.username
        }

        override fun areContentsTheSame(oldItem: PendingFriends, newItem: PendingFriends): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(
        BlogRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )
    class BlogRecyclerChangeCallback(private val pendingFriendAdapter: PendingFriendAdapter) : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            pendingFriendAdapter.notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            pendingFriendAdapter.notifyItemRemoved(position)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            pendingFriendAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            pendingFriendAdapter.notifyItemRangeChanged(position, count, payload)
        }
    }
}