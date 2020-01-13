package com.example.jobx.chat

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import com.example.jobx.R
import com.example.jobx.authentication.AuthActivity
import com.example.jobx.database.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLog : AppCompatActivity() {

    companion object{
        val TAG = "ChatLog"
    }
    val adapter = GroupAdapter<ViewHolder>()
    private lateinit var toUser: chatUser
    private lateinit var currentUser:chatUser
    private lateinit var fStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recyclerview_chat_log.adapter = adapter
        val i = intent
        val b = i.extras
        val id = b?.get(UserList.USER_KEY) as String
        var temp = ""
        Log.d("www",id)
        fStore = FirebaseFirestore.getInstance()
        fStore.collection("users").document(id).get()
            .addOnSuccessListener { documents ->
                temp = documents.get("name").toString()
                supportActionBar?.title = temp
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
        toUser = chatUser(id,temp)

        Log.d(TAG, toUser.id)
        Log.d(TAG, toUser.name)
        Toast.makeText(this,temp,Toast.LENGTH_SHORT).show()

        val temp2 = ""
        if(FirebaseAuth.getInstance().currentUser!=null) {
            FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().uid.toString()).get()
                .addOnSuccessListener {
               temp = it.get("name").toString()
            }
        }else{
            startActivity(Intent(this,AuthActivity::class.java))
        }
        currentUser = chatUser(FirebaseAuth.getInstance().uid.toString(),temp)
        listenForMessages()
        send_button_chat_log.setOnClickListener {
            Log.d(TAG, "Attempt to send message....")
            performSendMessage()
        }


    }
    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.id
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        if(FirebaseAuth.getInstance().currentUser!=null) {
            FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().uid.toString()).get()
                .addOnSuccessListener {
                    //                val user = it.toObject(User::class.java)
                    currentUser = chatUser(FirebaseAuth.getInstance().currentUser!!.uid,it.get("name").toString())
                }
        }else{
            startActivity(Intent(this,AuthActivity::class.java))
        }
        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(Message::class.java)

                if (chatMessage != null) {
                    Log.d(TAG, chatMessage.text)
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatFromItem(baseContext,chatMessage.text, currentUser))
                    } else {
                        adapter.add(ChatToItem(baseContext,chatMessage.text, toUser))
                    }
                }

                recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)

            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })

    }
    private fun performSendMessage(){
        val text = edittext_chat_log.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val i = intent
        val b = i.extras
        val toId = b?.get(UserList.USER_KEY) as String
        if(text.isNullOrEmpty()) return
        if(fromId==null) return
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = Message(reference.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${reference.key}")
                edittext_chat_log.text.clear()
                recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)
            }

        toReference.setValue(chatMessage)

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)
    }
}
