package com.example.gamebuddy.presentation.auth.details.keywords

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.gamebuddy.R
import com.example.gamebuddy.data.remote.model.keyword.Keyword
import com.example.gamebuddy.databinding.ItemKeywordBinding

class KeywordAdapter(
    private val onClickListener: OnClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener {
        fun onItemClick(position: Int, keywordId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageViewHolder(
            ItemKeywordBinding.inflate(
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
        private val binding: ItemKeywordBinding,
        private val requestOptions: RequestOptions,
        private val onClickListener: OnClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Keyword) {
            binding.apply {

                binding.selectKeywordRB.setOnClickListener {
                    onClickListener?.onItemClick(absoluteAdapterPosition, item.id)
                }
                txtKeyword.text = item.keywordName



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

    fun submitList(blogList: List<Keyword>?) {
        val newList = blogList?.toMutableList()
        differ.submitList(newList)
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Keyword>() {

        override fun areItemsTheSame(oldItem: Keyword, newItem: Keyword): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Keyword, newItem: Keyword): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(
        BlogRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )

    class BlogRecyclerChangeCallback(private val keywordAdapter: KeywordAdapter) :
        ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            keywordAdapter.notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            keywordAdapter.notifyItemRemoved(position)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            keywordAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            keywordAdapter.notifyItemRangeChanged(position, count, payload)
        }
    }
}