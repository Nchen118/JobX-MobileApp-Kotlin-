package com.example.jobx

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_logout_page.*

class LogoutPage : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logout_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sign_out.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this.context, "Login Successful", Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(this.context, LoadingPage::class.java),
                ActivityOptions.makeCustomAnimation(
                    this.context,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                ).toBundle()
            )
            this.activity!!.finish()
        }
    }
}
