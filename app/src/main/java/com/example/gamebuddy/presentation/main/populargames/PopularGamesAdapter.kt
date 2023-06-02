package com.example.gamebuddy.presentation.main.populargames

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.ItemPopularGamesBinding
import com.example.gamebuddy.domain.model.popular.PopularGames
import com.example.gamebuddy.util.loadImageFromUrl

class PopularGamesAdapter(
    private val onClickListener: OnClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener {
        fun onGameClick(gameId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageViewHolder(
            ItemPopularGamesBinding.inflate(
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
        private val binding: ItemPopularGamesBinding,
        private val requestOptions: RequestOptions,
        private val onClickListener: OnClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PopularGames) {
            binding.apply {
                imgGame.loadImageFromUrl(item.gameIcon)
                txtGameName.text = item.gameName
                txtGameVote.text = item.avgVote.toString()
                txtGameCategory.text = item.category
                txtDescription.text = item.description

                root.setOnClickListener {
                    onClickListener?.onGameClick(item.gameId)
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

    fun submitList(blogList: List<PopularGames>?) {
        val newList = blogList?.toMutableList()
        differ.submitList(newList)
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PopularGames>() {

        override fun areItemsTheSame(oldItem: PopularGames, newItem: PopularGames): Boolean {
            return oldItem.gameId == newItem.gameId
        }

        override fun areContentsTheSame(oldItem: PopularGames, newItem: PopularGames): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(
        BlogRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )

    class BlogRecyclerChangeCallback(private val popularGamesAdapter: PopularGamesAdapter) :
        ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            popularGamesAdapter.notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            popularGamesAdapter.notifyItemRemoved(position)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            popularGamesAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            popularGamesAdapter.notifyItemRangeChanged(position, count, payload)
        }
    }
}