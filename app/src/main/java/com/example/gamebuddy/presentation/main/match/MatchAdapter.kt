package com.example.gamebuddy.presentation.main.match

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.gamebuddy.R
import com.example.gamebuddy.domain.model.user.User

class MatchAdapter(
    context: Context, users: List<User>
) : ArrayAdapter<User>(context, 0, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_match, parent, false)
        }

        val user = getItem(position)

        val txtNameAndAge = view?.findViewById<TextView>(R.id.txt_name_and_age)
        val country = view?.findViewById<TextView>(R.id.Country_text)
        val imgView = view?.findViewById<ImageView>(R.id.img_matched_user)
        if (imgView != null) {
            Glide.with(context)
                .load(user?.avatar)
                .into(imgView)
        }

        txtNameAndAge?.text = String.format(
            "${user?.gamerUsername}",
            "${user?.age?.toString()}"
        ) //"${item.gamerUsername}, ${item.age?.toString()}"
        country?.text = user?.country




        return view!!
    }
}

//class MatchAdapter(
//    private val onClickListener: OnClickListener? = null
//) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    interface OnClickListener {
//        fun onItemClick(position: Int, item: User)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return MessageViewHolder(
//            ItemMatchBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            ),
//            requestOptions = RequestOptions
//                .placeholderOf(R.drawable.ic_launcher_foreground)
//                .error(R.drawable.ic_launcher_foreground),
//            onClickListener = onClickListener
//        )
//    }
//
//    class MessageViewHolder constructor(
//        private val binding: ItemMatchBinding,
//        private val requestOptions: RequestOptions,
//        private val onClickListener: OnClickListener?
//    ) : RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(item: User) {
//            binding.apply {
//
//                root.setOnClickListener {
//                    onClickListener?.onItemClick(absoluteAdapterPosition, item)
//                }
//
//                Glide.with(binding.root)
//                    .setDefaultRequestOptions(requestOptions)
//                    .load(item.avatar)
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .into(imgMatchedUser)
//
//                txtNameAndAge.text = String.format(
//                    "${item.gamerUsername}",
//                    "${item.age?.toString()}"
//                ) //"${item.gamerUsername}, ${item.age?.toString()}"
//
//
//            }
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        when (holder) {
//            is MessageViewHolder -> {
//                holder.bind(differ.currentList[position])
//            }
//        }
//    }
//
//    override fun getItemCount() = differ.currentList.size
//
//    fun submitList(blogList: List<User>?) {
//        val newList = blogList?.toMutableList()
//        differ.submitList(newList)
//    }
//
//    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
//
//        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
//            return oldItem.userId == newItem.userId
//        }
//
//        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
//            return oldItem == newItem
//        }
//
//    }
//    private val differ = AsyncListDiffer(
//        BlogRecyclerChangeCallback(this),
//        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
//    )
//
//    class BlogRecyclerChangeCallback(private val matchAdapter: MatchAdapter) :
//        ListUpdateCallback {
//        override fun onInserted(position: Int, count: Int) {
//            matchAdapter.notifyItemRangeChanged(position, count)
//        }
//
//        override fun onRemoved(position: Int, count: Int) {
//            matchAdapter.notifyItemRemoved(position)
//        }
//
//        override fun onMoved(fromPosition: Int, toPosition: Int) {
//            matchAdapter.notifyItemMoved(fromPosition, toPosition)
//        }
//
//        override fun onChanged(position: Int, count: Int, payload: Any?) {
//            matchAdapter.notifyItemRangeChanged(position, count, payload)
//        }
//    }
//}