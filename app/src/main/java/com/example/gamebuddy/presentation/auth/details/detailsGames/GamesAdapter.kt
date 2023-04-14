package com.example.gamebuddy.presentation.auth.details.detailsGames

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.gamebuddy.R
import com.example.gamebuddy.data.remote.model.details.Games
import com.example.gamebuddy.databinding.ItemGamesBinding

class GamesAdapter (

    private val onClickListener: OnClickListener? = null
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        interface OnClickListener {
            fun onItemClick(position: Int, item: Games)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return GameViewHolder(
                ItemGamesBinding.inflate(
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

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class GameViewHolder constructor(
            private val binding: ItemGamesBinding,
            private val requestOptions: RequestOptions,
            private val onClickListener: OnClickListener?
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(item: Games) {
                binding.apply {

                    root.setOnClickListener {
                        onClickListener?.onItemClick(absoluteAdapterPosition, item)
                    }

                    Glide.with(binding.root)
                        .setDefaultRequestOptions(requestOptions)
                        .load(item.avatar)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imgGames)
                    txtGames.text = item.favoriteGames
                }
            }
        }
}