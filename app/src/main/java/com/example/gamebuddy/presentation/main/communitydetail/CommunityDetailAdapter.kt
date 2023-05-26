package com.example.gamebuddy.presentation.main.communitydetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.gamebuddy.R
import com.example.gamebuddy.data.remote.model.post.Post
import com.example.gamebuddy.databinding.ItemCommunityDetailBinding
import com.example.gamebuddy.util.loadImageFromUrl

class CommunityDetailAdapter(
    private val onClickListener: OnClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener {
        fun onPostClick(postId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageViewHolder(
            ItemCommunityDetailBinding.inflate(
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
        private val binding: ItemCommunityDetailBinding,
        private val requestOptions: RequestOptions,
        private val onClickListener: OnClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Post) {
            binding.apply {
                imgPost.loadImageFromUrl(item.picture)

                root.setOnClickListener {
                    onClickListener?.onPostClick(item.postId)
                }
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

    fun submitList(blogList: List<Post>?) {
        val newList = blogList?.toMutableList()
        differ.submitList(newList)
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {

        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.username == newItem.username
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(
        BlogRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )

    class BlogRecyclerChangeCallback(private val communityDetailAdapter: CommunityDetailAdapter) :
        ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            communityDetailAdapter.notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            communityDetailAdapter.notifyItemRemoved(position)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            communityDetailAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            communityDetailAdapter.notifyItemRangeChanged(position, count, payload)
        }
    }
}