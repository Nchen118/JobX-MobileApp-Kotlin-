package com.example.jobx.admin

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobx.R
import com.example.jobx.database.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.edit_user.*
import kotlinx.android.synthetic.main.recycle_info_list.*
import kotlinx.android.synthetic.main.register_fragment.*

class EditUser : AppCompatActivity(){

    private var user: List<User>? = null

    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_user)

        id = intent.getStringExtra("id").orEmpty()
    }

    override fun onStart() {
        super.onStart()
        if(disableAccount.isChecked){
            FirebaseFirestore.getInstance().collection("users").document(id).update("status", "false")
        }

    }
}