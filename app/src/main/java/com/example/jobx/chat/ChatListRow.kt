package com.example.jobx.chat

import android.util.Log
import com.example.jobx.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_list_row.view.*

class ChatListRow(val chatMessage: Message): Item<ViewHolder>() {
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
    }
    override fun getLayout(): Int {
        return R.layout.activity_chat_list_row
    }
}