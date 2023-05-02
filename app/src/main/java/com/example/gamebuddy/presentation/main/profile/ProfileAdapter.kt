package com.example.gamebuddy.presentation.main.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamebuddy.R
import com.example.gamebuddy.domain.model.profile.profilUser
import com.example.gamebuddy.presentation.main.match.MatchedGamesAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class ProfileAdapter(
    context: Context, users: List<profilUser>
) : ArrayAdapter<profilUser>(context, 0, users) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.fragment_profile, parent, false)
        }
        // Get user
        val user = getItem(position)

        val username = view?.findViewById<TextView>(R.id.profile_username)
        val avatar = view?.findViewById<ImageView>(R.id.profile_avatar)
        val friendsCount = view?.findViewById<TextView>(R.id.friends_count)
        val age = view?.findViewById<TextView>(R.id.age_text)
        val communities = view?.findViewById<TextView>(R.id.communies_text)
        val gamesRecyclerView = view?.findViewById<RecyclerView>(R.id.game_profile)
        val characterRecyclerView = view?.findViewById<RecyclerView>(R.id.keyword_profile)

        // Initialize adapters
        var characterAdapter: MatchedGamesAdapter? = null
        var matchedGamesAdapter: MatchedGamesAdapter? = null

        // Set up recycler view for games
        val flexboxLayoutManagerGames = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }

        return view!!
    }
}