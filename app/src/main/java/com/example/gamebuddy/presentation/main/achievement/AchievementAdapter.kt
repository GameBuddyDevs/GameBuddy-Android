package com.example.gamebuddy.presentation.main.achievement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import com.bumptech.glide.request.RequestOptions
import com.example.gamebuddy.R
import com.example.gamebuddy.databinding.ItemAchievementBinding
import com.example.gamebuddy.domain.model.achievement.Achievement
import com.example.gamebuddy.util.loadImageFromDrawable

class AchievementAdapter(
    private val onClickListener: OnClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener {
        fun onItemClick(achievementId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AchievementAdapter.MessageViewHolder(
            ItemAchievementBinding.inflate(
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
        private val binding: ItemAchievementBinding,
        private val requestOptions: RequestOptions,
        private val onClickListener: AchievementAdapter.OnClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(achievement: Achievement) {
            binding.apply {
                txtAchievementName.text = achievement.achievementName

                if (achievement.isCollected) {
                    imgAchievement.loadImageFromDrawable(R.drawable.collected_trophy)
                } else if (achievement.isEarned) {
                    imgAchievement.loadImageFromDrawable(R.drawable.earned_trophy)
                } else {
                    imgAchievement.loadImageFromDrawable(R.drawable.trophy)
                }

                txtDescription.text = achievement.description

                root.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION || achievement.isEarned) {
                        onClickListener?.onItemClick(achievement.id)
                    }
                }
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

    fun submitList(blogList: List<Achievement>?) {
        val newList = blogList?.toMutableList()
        differ.submitList(newList)
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Achievement>() {
        override fun areItemsTheSame(oldItem: Achievement, newItem: Achievement): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Achievement, newItem: Achievement): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(
        BlogRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )

    class BlogRecyclerChangeCallback(private val achievementAdapter: AchievementAdapter) :
        ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            achievementAdapter.notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            achievementAdapter.notifyItemRemoved(position)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            achievementAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            achievementAdapter.notifyItemRangeChanged(position, count, payload)
        }
    }
}

