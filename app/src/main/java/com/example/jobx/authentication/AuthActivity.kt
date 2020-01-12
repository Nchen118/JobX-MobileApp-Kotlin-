package com.example.jobx.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jobx.R
import com.example.jobx.library.Adapter
import kotlinx.android.synthetic.main.auth_activity.*

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.auth_activity)

        val fragmentAdapter = Adapter(supportFragmentManager)
        fragmentAdapter.addFragment(LoginFragment(), "Sign In")
        fragmentAdapter.addFragment(RegisterFragment(), "Sign Up")
        page.adapter = fragmentAdapter
        tabs.setupWithViewPager(page)
    }
}