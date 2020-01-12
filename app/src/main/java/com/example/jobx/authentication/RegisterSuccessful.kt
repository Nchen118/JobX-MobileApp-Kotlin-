package com.example.jobx.authentication

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jobx.LoadingPage
import com.example.jobx.MainPage
import com.example.jobx.R
import kotlinx.android.synthetic.main.activity_register_successful.*

class RegisterSuccessful : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_successful)

        login_btn.setOnClickListener{
            startActivity(
                Intent(this, LoadingPage::class.java),
                ActivityOptions.makeCustomAnimation(
                    this,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                ).toBundle()
            )
        }
    }
}
