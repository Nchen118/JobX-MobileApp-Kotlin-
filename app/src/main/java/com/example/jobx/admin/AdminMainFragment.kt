package com.example.jobx.admin

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.jobx.R
import kotlinx.android.synthetic.main.fragment_admin_main.*

class AdminMainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_main, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        job_btn.setOnClickListener{view : View ->
            startActivity(
                Intent(this.context, ManageJobFragment::class.java),
                ActivityOptions.makeCustomAnimation(
                    this.context,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                ).toBundle()
            )
        }
        user_btn.setOnClickListener{view : View ->
            view.findNavController().navigate(R.id.action_adminMainFragment_to_manageUserFragment)
        }
    }


}
