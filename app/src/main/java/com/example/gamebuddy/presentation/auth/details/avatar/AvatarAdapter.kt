package com.example.gamebuddy.presentation.auth.details.avatar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.ItemAvatarBinding
import com.example.gamebuddy.domain.model.avatar.Avatar
import timber.log.Timber

class AvatarAdapter(
    private val onClickListener: AvatarAdapter.OnClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnClickListener {
        fun onItemClick(position: Int, avatarId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageViewHolder(
            ItemAvatarBinding.inflate(
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
        private val binding: ItemAvatarBinding,
        private val requestOptions: RequestOptions,
        private val onClickListener: AvatarAdapter.OnClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {
        private var isRadioButtonSelected = false
        @SuppressLint("SuspiciousIndentation")
        fun bind(item: Avatar) {
            binding.apply {

                var isItemSelected = false
                val defaultBackground = ContextCompat.getDrawable(itemView.context, R.drawable.border)
                val selectedBackground =
                    ContextCompat.getDrawable(itemView.context, R.drawable.border_custom)

                    binding.ivAvatarDisplayItem.setOnClickListener {
                        onClickListener?.onItemClick(absoluteAdapterPosition, item.id)
                        isItemSelected = !isItemSelected
                        if (isItemSelected) {
                            itemView.background = selectedBackground
                        } else {
                            itemView.background = defaultBackground
                        }
                    }

                Glide.with(binding.root)
                    .setDefaultRequestOptions(requestOptions)
                    .load(item.image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivAvatarDisplayItem)
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

    fun submitList(blogList: List<Avatar>?) {
        val newList = blogList?.toMutableList()
        differ.submitList(newList)
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Avatar>() {

        override fun areItemsTheSame(oldItem: Avatar, newItem: Avatar): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Avatar, newItem: Avatar): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(
        BlogRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )

    class BlogRecyclerChangeCallback(private val messageAdapter: AvatarAdapter) :
        ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            messageAdapter.notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            messageAdapter.notifyItemRemoved(position)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            messageAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            messageAdapter.notifyItemRangeChanged(position, count, payload)
        }
    }

}