package com.example.gamebuddy.presentation.main.joincommunity

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.gamebuddy.R
import com.example.gamebuddy.data.remote.model.joincommunity.Community
import com.example.gamebuddy.databinding.ItemCommunityBinding
import com.example.gamebuddy.util.loadImageFromUrl
import com.google.android.material.button.MaterialButton
import timber.log.Timber

class JoinCommunityAdapter(
    private val onClickListener: OnClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener {
        fun onCommunityClick(community: Community)
        fun onCommunityJoinClick(communityId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageViewHolder(
            ItemCommunityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClickListener = onClickListener
        )
    }

    class MessageViewHolder constructor(
        private val binding: ItemCommunityBinding,
        private val onClickListener: OnClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, item: Community) {
            binding.apply {
                imgCommunityBackground.loadImageFromUrl(item.wallpaper)
                imgCommunity.loadImageFromUrl(item.communityAvatar)
                txtCommunityName.text = item.name
                txtCommunityDescription.text = item.description
                "${item.memberCount} Member".also { memberCount ->
                    txtCommunityMemberCount.text = memberCount
                }
                if (item.isJoined) {
                    setButtonToLeave(btnJoinStatus, context)
                } else {
                    setButtonToJoin(btnJoinStatus, context)
                }

                btnJoinStatus.setOnClickListener {
                    Timber.d("Join button clicked")
                    onClickListener?.onCommunityJoinClick(item.communityId)
                }

                root.setOnClickListener {
                    onClickListener?.onCommunityClick(item)
                }
            }
        }

        private fun setButtonToLeave(btnJoinStatus: MaterialButton, context: Context) {
            btnJoinStatus.text = context.getString(R.string.leave)
            btnJoinStatus.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFFFF"))
            btnJoinStatus.setTextColor(Color.parseColor("#000000"))
        }

        private fun setButtonToJoin(btnJoinStatus: MaterialButton, context: Context) {
            btnJoinStatus.text = context.getString(R.string.join)
            btnJoinStatus.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF4D67"))
            btnJoinStatus.setTextColor(Color.parseColor("#FFFFFFFF"))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MessageViewHolder -> {
                holder.bind(holder.itemView.context, differ.currentList[position])
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    fun submitList(blogList: List<Community>?) {
        val newList = blogList?.toMutableList()
        differ.submitList(newList)
    }

    fun updateCommunityJoinStatus(communityId: String, isJoined: Boolean) {
        val position = differ.currentList.indexOfFirst { it.communityId == communityId }
        if (position != -1) {
            val community = differ.currentList[position]
            community.isJoined = isJoined
            notifyItemChanged(position)
        }
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Community>() {

        override fun areItemsTheSame(oldItem: Community, newItem: Community): Boolean {
            return oldItem.communityId == newItem.communityId
        }

        override fun areContentsTheSame(oldItem: Community, newItem: Community): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(
        BlogRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )

    class BlogRecyclerChangeCallback(private val joinCommunityAdapter: JoinCommunityAdapter) :
        ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            joinCommunityAdapter.notifyItemInserted(position)
        }

        override fun onRemoved(position: Int, count: Int) {
            joinCommunityAdapter.notifyItemRemoved(position)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            joinCommunityAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            joinCommunityAdapter.notifyItemRangeChanged(position, count, payload)
        }
    }
}