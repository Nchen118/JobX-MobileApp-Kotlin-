package com.example.jobx.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jobx.InfoWall
import com.example.jobx.LogoutPage
import com.example.jobx.R
import com.example.jobx.library.Adapter
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val fragmentAdapter = Adapter(supportFragmentManager)
        fragmentAdapter.addFragment(AdminMainFragment(), "Manage")
        fragmentAdapter.addFragment(LogoutPage(), "Logout")
        admin_page.adapter = fragmentAdapter
        admin_tab.setupWithViewPager(admin_page)
    }
}