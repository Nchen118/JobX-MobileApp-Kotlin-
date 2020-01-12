package com.example.jobx.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobx.R
import com.example.jobx.database.JobDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_approve_job_request.*

class ApproveJobRequest : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private var listRequest: List<JobDetails>? = null
    private lateinit var fStore: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_approve_job_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shimmerLayout1.startShimmer()

        setData()
        swiperefresh3.setOnRefreshListener {
            setData()
        }
    }

    private fun setData() {
        recycleView.visibility = View.GONE
        shimmerLayout1.visibility = View.VISIBLE
        shimmerLayout1.startShimmer()

        mAuth = FirebaseAuth.getInstance()
        FirebaseFirestore.getInstance().collection("jobDetails")
            .whereEqualTo("requestStatus", "pending")
            .whereEqualTo("com_id", mAuth.currentUser!!.uid).get()
            .addOnSuccessListener { documentSnapshot ->
                listRequest = documentSnapshot.toObjects(JobDetails::class.java)

                if (mAuth.currentUser?.uid != null) {
                    recycleView.apply {
                        layoutManager = LinearLayoutManager(this.context)
                        adapter = JobRequestRecycleViewAdapter(
                            this.context,
                            listRequest as MutableList<JobDetails>
                        )
                    }

                    shimmerLayout1.stopShimmer()
                    shimmerLayout1.visibility = View.GONE
                    recycleView.visibility = View.VISIBLE
                    swiperefresh3.isRefreshing = false
                }
            }
    }
}
