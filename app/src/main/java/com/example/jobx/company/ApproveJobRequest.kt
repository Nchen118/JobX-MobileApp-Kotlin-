package com.example.jobx.company


import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.jobx.R
import com.example.jobx.database.JobDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_approve_job_request.*

/**
 * A simple [Fragment] subclass.
 */
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

        Handler().postDelayed({
            shimmerLayout1.stopShimmer()
            shimmerLayout1.visibility = View.GONE

            setData()
        }, 5000)
        swiperefresh3.setOnRefreshListener {
            shimmerLayout1.visibility = View.VISIBLE
            shimmerLayout1.startShimmer()
            recycleView.visibility = View.GONE

            Handler().postDelayed({
                shimmerLayout1.stopShimmer()
                shimmerLayout1.visibility = View.GONE
                recycleView.visibility = View.VISIBLE

                setData()
            }, 5000)
        }
    }

    private fun setData() {
        mAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        val documentReference =
            fStore.collection("jobDetails")
                .whereEqualTo("requestStatus", "pending")
                .whereEqualTo("com_id", mAuth.currentUser!!.uid)
        documentReference.get().addOnSuccessListener { documentSnapshot ->
            listRequest = documentSnapshot.toObjects(JobDetails::class.java)

            if (mAuth.currentUser?.uid != null) {
                recycleView.apply {
                    layoutManager = LinearLayoutManager(this.context)
                    adapter = JobRequestRecycleViewAdapter(this.context,
                        listRequest as MutableList<JobDetails>
                    )
                }
                swiperefresh3.isRefreshing = false
            }
        }
    }
}
