package com.example.jobx

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.jobx.admin.AdminActivity
import com.example.jobx.jobseeker.CompanyList
import com.example.jobx.jobseeker.JobList
import com.example.jobx.library.Adapter
import com.example.jobx.library.Common
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main_page.*

class MainPage : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        setSupportActionBar(toolbar)

        val tabs = findViewById<TabLayout>(R.id.tab)
        val viewPage = findViewById<ViewPager>(R.id.pager)
        val fragmentAdapter = Adapter(supportFragmentManager)

        if (Common.user?.position == "jobseeker") {
            fragmentAdapter.addFragment(InfoWall(), "Page 1")
            fragmentAdapter.addFragment(JobList(), "Job")
            fragmentAdapter.addFragment(CompanyList(), "Company")
            fragmentAdapter.addFragment(LogoutPage(), "Logout")
        } else if (Common.user?.position == "company") {
            fragmentAdapter.addFragment(InfoWall(), "Page 1")
            fragmentAdapter.addFragment(LogoutPage(), "Logout")
        } else if (Common.user?.position == "admin") {
            startActivity(
                Intent(this, AdminActivity::class.java),
                ActivityOptions.makeCustomAnimation(
                    this,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                ).toBundle()
            )
            finish()
        } else {

        }

        viewPage.adapter = fragmentAdapter
        tabs.setupWithViewPager(viewPage)

        name.text = Common.user.name
    }


    override fun onStart() {
        super.onStart()

        if (Common.user?.position == "jobseeker") {

        } else if (Common.user?.position == "company") {

        } else {

        }

    }

}