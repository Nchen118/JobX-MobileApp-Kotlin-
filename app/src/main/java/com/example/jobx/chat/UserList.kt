package com.example.jobx.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jobx.R
import com.example.jobx.database.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserList : AppCompatActivity() {
    private var listUser:List<User>?=null
    companion object {
        val USER_KEY = ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        supportActionBar?.title = "Select User"
//        fetchUser()
//        if(FirebaseAuth.getInstance().currentUser!=null) {
//            val intent = Intent(this, ChatLog::class.java)
//            intent.putExtra(USER_KEY, "xrazZlNvMFc05tUAh2fplo9a0OV2")
//            startActivity(intent)
//        }
    }
    private fun fetchUser(){
        val ref = FirebaseFirestore.getInstance().collection("users").whereEqualTo("status","true")
        ref.get().addOnSuccessListener {
            listUser = it.toObjects(User::class.java)
//            recyclerUser.apply{
//                layoutManager = LinearLayoutManager(this.context)
//                adapter = UserAdapter(this.context,listUser.orEmpty())
//
//            }
        }

    }

}
