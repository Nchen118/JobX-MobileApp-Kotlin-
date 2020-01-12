package com.example.jobx.chat

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideContext
import com.example.jobx.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_list_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatListRow(val context: Context,val chatMessage: Message): Item<ViewHolder>() {
      var chatPartnerUser:chatUser ?= null

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.message_textview_latest_message.text = chatMessage.text

        val chatPartnerId: String
        if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
            chatPartnerId = chatMessage.toId
        } else {
            chatPartnerId = chatMessage.fromId
        }

        var name:String = ""
        var id :String = ""
        val ref = FirebaseFirestore.getInstance().collection("users").document(chatPartnerId)
       ref.get().addOnCompleteListener{
           if(it.isSuccessful){
               name = it.result?.get("name") as String
               id = chatPartnerId
               viewHolder.itemView.username_textview_latest_message.text = name
               chatPartnerUser = chatUser(id,name)
           }
       }
        val target = viewHolder.itemView.imageview_latest_message

        FirebaseStorage.getInstance().reference.child("pics/${id}")
            .downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(context).load(task.result).into(target)
            }
        }
    }
    override fun getLayout(): Int {
        return R.layout.activity_chat_list_row
    }
}