package com.example.jobx.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobx.R
import com.example.jobx.database.Job
import com.example.jobx.database.JobDetails
import com.example.jobx.database.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.recycle_info_list.*

class CompanyJobList : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private var listCompany: List<Job>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recycle_info_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        setData()

        swipe_refresher.setOnRefreshListener {
            setData()

        }
    }

    private fun setData() {
        recycleView.visibility = View.GONE
        shimmer.visibility = View.VISIBLE
        shimmer.startShimmer()

        FirebaseFirestore.getInstance().collection("jobs")
            .whereEqualTo("company_id", mAuth.currentUser?.uid)
            .whereEqualTo("job_status", "true").get().addOnSuccessListener { company ->
                listCompany = company.toObjects(Job::class.java)

                recycleView.apply {
                    layoutManager = LinearLayoutManager(this.context)
                    adapter = CompanyJobRecycleViewAdapter(
                        this.context,
                        listCompany.orEmpty() as MutableList<Job>
                    )
                }

                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
                recycleView.visibility = View.VISIBLE
                swipe_refresher.isRefreshing = false
            }
    }
}

