package com.example.jobx

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jobx.authentication.AuthActivity
import com.example.jobx.database.User
import com.example.jobx.library.Common
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoadingPage : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth    // Firebase Authentication
    private lateinit var fStore: FirebaseFirestore  // Firebase Firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_activity)
    }

    override fun onStart() {
        super.onStart()

        // Initial Firebase
        mAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        // Delay
        Handler().postDelayed({
            if (mAuth.currentUser != null) {
                fStore.collection("users").document(mAuth.currentUser!!.uid).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Common.user = task.result!!.toObject(User::class.java)!!
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            startActivity(
                                Intent(this, MainPage::class.java),
                                ActivityOptions.makeCustomAnimation(
                                    this,
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_left
                                ).toBundle()
                            )
                            finish()
                        }
                    }
            } else {
                startActivity(
                    Intent(this, AuthActivity::class.java),
                    ActivityOptions.makeCustomAnimation(
                        this,
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                    ).toBundle()
                )
                finish()
            }
        }, 1000)    // Wait for 1 second
    }
}
