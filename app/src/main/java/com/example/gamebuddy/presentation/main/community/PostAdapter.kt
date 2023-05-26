package com.example.gamebuddy.presentation.main.community

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.gamebuddy.R
import com.example.gamebuddy.data.remote.model.post.Post
import com.example.gamebuddy.databinding.ItemPostBinding
import com.example.gamebuddy.util.loadImageFromUrl
import timber.log.Timber

class PostAdapter(
    private val onClickListener: OnClickListener? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener {
        fun onLikePostClick(postId: String)
        fun onCommentClick(postId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageViewHolder(
            ItemPostBinding.inflate(
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
        private val binding: ItemPostBinding,
        private val requestOptions: RequestOptions,
        private val onClickListener: OnClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Post) {
            binding.apply {
                imgPost.loadImageFromUrl(item.picture)
                imgUser.loadImageFromUrl(item.avatar)
                txtUsername.text = item.username
                txtLikeCount.text = item.likeCount.toString()
                txtCommentCount.text = item.commentCount.toString()
                (if (item.likeCount > 0) "${item.likeCount} people like this" else "Be the first one to like this photo").also { likeText ->
                    txtLikeCount.text = likeText
                }
                when {
                    item.commentCount > 1 -> "See all ${item.commentCount} comments"
                    item.commentCount == 1 -> "See the comment"
                    else -> "There are no comments"
                }.also { commentText -> txtCommentCount.text = commentText }
                if (item.isLiked) imgLikePost.setImageResource(R.drawable.ic_thumb_up_filled_pink_500_24) else imgLikePost.setImageResource(
                    R.drawable.ic_thumb_up_pink_500_24
                )

                val spannableStringBuilder = SpannableStringBuilder()

                // Add username with bold font and larger text size
                val usernameSpan = SpannableString("${item.username}  ").apply {
                    setSpan(StyleSpan(Typeface.BOLD), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    setSpan(AbsoluteSizeSpan(14, true), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                spannableStringBuilder.append(usernameSpan)

                // Add description with regular font and smaller text size
                val descriptionSpan = SpannableString(item.title).apply {
                    setSpan(AbsoluteSizeSpan(13, true), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                spannableStringBuilder.append(descriptionSpan)

                // Set the formatted text to the TextView
                txtPostDetail.text = spannableStringBuilder


                imgLikePost.setOnClickListener {
                    onClickListener?.onLikePostClick(item.postId)

                    val pinkThumbUpDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.ic_thumb_up_pink_500_24)
                    val filledPinkThumbUpDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.ic_thumb_up_filled_pink_500_24)

                    if (imgLikePost.drawable.constantState == pinkThumbUpDrawable?.constantState) {
                        imgLikePost.setImageDrawable(filledPinkThumbUpDrawable)
                    } else {
                        imgLikePost.setImageDrawable(pinkThumbUpDrawable)
                    }

                    onClickListener?.onLikePostClick(item.postId)
                }

                imgComment.setOnClickListener {
                    onClickListener?.onCommentClick(item.postId)
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

    class BlogRecyclerChangeCallback(private val postAdapter: PostAdapter) :
        ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            postAdapter.notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            postAdapter.notifyItemRemoved(position)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            postAdapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            postAdapter.notifyItemRangeChanged(position, count, payload)
        }
    }
}