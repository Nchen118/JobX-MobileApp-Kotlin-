package com.example.jobx.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.Constraints
import androidx.recyclerview.widget.RecyclerView
import com.example.jobx.R
import com.example.jobx.database.User

class UserAdapter(val context: Context, val data:List<User>):
        RecyclerView.Adapter<UserAdapter.Holder>(){
    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.name.setText(data.elementAt(position).name)
        holder.box.setOnClickListener{
//            val intent = Intent(context, ChatLog::class.java)
//            intent.putExtra(USER_KEY, data.elementAt(position))
//            startActivity(intent)
//
//            finish()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_new_message, parent, false)
        return Holder(view)
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.username_textview)
        val box:Constraints = itemView.findViewById(R.id.UserBound)
        //Image
    }
}