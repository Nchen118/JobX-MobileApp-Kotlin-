package com.example.jobx.jobseeker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobx.R
import com.example.jobx.database.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.recycle_info_list.*

class CompanyList : Fragment() {
    private var listJob: List<User>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recycle_info_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()

        swipe_refresher.setOnRefreshListener {
            setData()
        }
    }

    private fun setData() {
        recycleView.visibility = View.GONE
        shimmer.visibility = View.VISIBLE
        shimmer.startShimmer()

        FirebaseFirestore.getInstance().collection("users")
            .whereEqualTo("position", "company").get().addOnSuccessListener { documentSnapshot ->
                listJob = documentSnapshot.toObjects(User::class.java)

                recycleView.apply {
                    layoutManager = LinearLayoutManager(this.context)
                    adapter = CompanyListRecyclerAdapter(this.context, listJob.orEmpty())
                }

                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
                recycleView.visibility = View.VISIBLE
                swipe_refresher.isRefreshing = false
            }
    }
}
