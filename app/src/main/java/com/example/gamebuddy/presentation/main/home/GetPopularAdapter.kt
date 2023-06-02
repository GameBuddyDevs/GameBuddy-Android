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
import com.example.gamebuddy.databinding.ItemPopularBinding
import com.example.gamebuddy.domain.model.popular.PopularGames

class GetPopularAdapter(
    private val onClickListener: OnClickListener? = null
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnClickListener {
        fun onItemClick(position: Int, item: PopularGames)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageViewHolder(
            ItemPopularBinding.inflate(
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
        private val binding: ItemPopularBinding,
        private val requestOptions: RequestOptions,
        private val onClickListener: OnClickListener?
    ) :RecyclerView.ViewHolder(binding.root){
        fun bind(item: PopularGames){
            binding.apply {
                root.setOnClickListener {
                    onClickListener?.onItemClick(absoluteAdapterPosition, item)
                }
                Glide.with(binding.root)
                    .load(item.gameIcon)
                    .into(popularImage)
                popularTitle.text = item.gameName
                popularAvg.text = item.avgVote.toString()
                popularCategory.text = item.category
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

    fun submitList(blogList: List<PopularGames>?) {
        val newList = blogList?.toMutableList()
        differ.submitList(newList)
    }
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PopularGames>() {

        override fun areItemsTheSame(oldItem: PopularGames, newItem: PopularGames): Boolean {
            return oldItem.gameName == newItem.gameName
        }

        override fun areContentsTheSame(oldItem: PopularGames, newItem: PopularGames): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(
        BlogRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )
    class BlogRecyclerChangeCallback(private val getPopularAdapter: GetPopularAdapter) :
        ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            getPopularAdapter.notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            getPopularAdapter.notifyItemRemoved(position)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            getPopularAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            getPopularAdapter.notifyItemRangeChanged(position, count, payload)
        }
    }
}