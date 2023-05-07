package com.example.gamebuddy.presentation.main.market

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import androidx.appcompat.app.AlertDialog
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.ItemMarketBinding
import com.example.gamebuddy.domain.model.market.Market

class MarketAdapter(
    private val onClickListener: OnClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener {
        fun onItemClick(position: Int, avatarId: String)
        fun onBuyClick(position: Int, avatarId: String)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageViewHolder(
            ItemMarketBinding.inflate(
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
        private val binding: ItemMarketBinding,
        private val requestOptions: RequestOptions,
        private val onClickListener: OnClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {
        private val dialogConfirmPurchase = AlertDialog.Builder(binding.root.context)
            .setView(R.layout.confirm_buy)
            .create()

        fun bind(item: Market) {
            binding.apply {

                dialogConfirmPurchase.findViewById<Button>(R.id.btnConfirm)?.setOnClickListener {
                    onClickListener?.onBuyClick(absoluteAdapterPosition, item.id)
                    dialogConfirmPurchase.dismiss()
                }
                btnBuy.setOnClickListener {
                    dialogConfirmPurchase.show()
                }
                dialogConfirmPurchase.findViewById<Button>(R.id.btnCancel)?.setOnClickListener {
                    dialogConfirmPurchase.dismiss()
                }

                Glide.with(binding.root)
                    .setDefaultRequestOptions(requestOptions)
                    .load(item.image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivAvatarDisplayItem)
                txtPrice.text = item.price

            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MessageViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    fun submitList(blogList: List<Market>?) {
        val newList = blogList?.toMutableList()
        differ.submitList(newList)
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Market>() {
        override fun areItemsTheSame(oldItem: Market, newItem: Market): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Market, newItem: Market): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(
        BlogRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )

    class BlogRecyclerChangeCallback(private val marketAdapter: MarketAdapter) :
        ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            marketAdapter.notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            marketAdapter.notifyItemRemoved(position)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            marketAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            marketAdapter.notifyItemRangeChanged(position, count, payload)
        }
    }
}