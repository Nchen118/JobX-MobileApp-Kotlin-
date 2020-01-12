package com.example.jobx.chat

import android.content.Context
import com.bumptech.glide.Glide
import com.example.jobx.R
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatFromItem(val context:Context,val text: String, val user: chatUser): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_from_row.text = text


        val target = viewHolder.itemView.imageview_chat_from_row

        FirebaseStorage.getInstance().reference.child("pics/${user.id}")
            .downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(context).load(task.result).into(target)
            }
        }
//        val uri = user.profileImageUrl
//        val targetImageView = viewHolder.itemView.imageview_chat_from_row
//        Picasso.get().load(uri).into(targetImageView)
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(val context:Context,val text: String, val user: chatUser): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_to_row.text = text

        // load our user image into the star
//        val uri = user.profileImageUrl
        val target = viewHolder.itemView.imageview_chat_to_row
//        Picasso.get().load(uri).into(targetImageView)
        FirebaseStorage.getInstance().reference.child("pics/${user.id}")
            .downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(context).load(task.result).into(target)
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}