package com.example.gamebuddy.presentation.main.profile

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
import com.example.gamebuddy.databinding.ItemAllfriendBinding
import com.example.gamebuddy.domain.model.profile.AllFriends

class AllFriendsAdapter(
    private val onClickListener: OnClickListener? = null
): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    interface OnClickListener {

        fun onItemClick(position: Int, item: AllFriends)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageViewHolder(
            ItemAllfriendBinding.inflate(
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
        private val binding: ItemAllfriendBinding,
        private val requestOptions: RequestOptions,
        private val onClickListener: OnClickListener?
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(item:AllFriends){
            binding.apply {
                binding.removeButton.setOnClickListener {
                    onClickListener?.onItemClick(absoluteAdapterPosition,item)
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

    fun submitList(blogList: List<AllFriends>?) {
        val newList = blogList?.toMutableList()
        differ.submitList(newList)
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AllFriends>() {

        override fun areItemsTheSame(oldItem: AllFriends, newItem: AllFriends): Boolean {
            return oldItem.username == newItem.username
        }

        override fun areContentsTheSame(oldItem: AllFriends, newItem: AllFriends): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(
        BlogRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )

    class BlogRecyclerChangeCallback(private val allFriendsAdapter: AllFriendsAdapter) :
        ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            allFriendsAdapter.notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            allFriendsAdapter.notifyItemRemoved(position)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            allFriendsAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            allFriendsAdapter.notifyItemRangeChanged(position, count, payload)
        }
    }
}