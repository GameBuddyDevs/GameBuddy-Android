package com.example.gamebuddy.presentation.main.match

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.gamebuddy.R
import com.example.gamebuddy.domain.model.user.User

class MatchAdapter(
    context: Context, users: List<User>
) : ArrayAdapter<User>(context, 0, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        }

        val user = getItem(position)

        val gamerUsername = view?.findViewById<TextView>(R.id.user_name)
        val age = view?.findViewById<TextView>(R.id.Age_text)
        val country = view?.findViewById<TextView>(R.id.Country_text)

        gamerUsername?.text = user?.gamerUsername
        age?.text = user?.age.toString()
        country?.text = user?.country

        return view!!
    }
}