package com.example.gamebuddy.presentation.main.match

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gamebuddy.R
import com.example.gamebuddy.domain.model.user.User
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class MatchAdapter(
    context: Context, users: List<User>
) : ArrayAdapter<User>(context, 0, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_match, parent, false)
        }

        // Get user
        val user = getItem(position)

        // Initialize views
        val txtName = view?.findViewById<TextView>(R.id.txt_name)
        val txtAge = view?.findViewById<TextView>(R.id.txt_age)
        val country = view?.findViewById<TextView>(R.id.Country_text)
        val imgView = view?.findViewById<ImageView>(R.id.img_matched_user)
        val gamesRecyclerView = view?.findViewById<RecyclerView>(R.id.rv_favorite_games)
        val characterRecyclerView = view?.findViewById<RecyclerView>(R.id.rv_character)

        // Initialize adapters
        var characterAdapter: MatchedGamesAdapter? = null
        var matchedGamesAdapter: MatchedGamesAdapter? = null

        // Set up recycler view for games
        val flexboxLayoutManagerGames = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }

        val flexboxLayoutManagerKeyword = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }

        gamesRecyclerView?.apply {
            layoutManager = flexboxLayoutManagerGames
            adapter = MatchedGamesAdapter().apply { submitList(user?.games) }
        }

        characterRecyclerView?.apply {
            layoutManager = flexboxLayoutManagerKeyword
            adapter = MatchedGamesAdapter().apply { submitList(user?.keywords) }
        }

        // Load avatar of user
        if (imgView != null) {
            Glide.with(context)
                .load(user?.avatar)
                .into(imgView)
        }

        txtName?.text = user?.gamerUsername
        txtAge?.text = user?.age.toString()
        country?.text = user?.country

        return view!!
    }
}