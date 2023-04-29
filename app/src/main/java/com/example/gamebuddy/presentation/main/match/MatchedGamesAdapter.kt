package com.example.gamebuddy.presentation.main.match

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.example.gamebuddy.R

class MatchedGamesAdapter(
    context: Context, games: List<String>
) : ArrayAdapter<String>(context, 0, games) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_character_match, parent, false)
        }

        val txtGame = view?.findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.txt_game)

        val game = getItem(position)

        txtGame?.text = game


        return view!!
    }
}