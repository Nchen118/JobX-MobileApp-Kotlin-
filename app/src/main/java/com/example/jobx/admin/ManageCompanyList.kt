package com.example.jobx.admin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobx.R
import com.example.jobx.database.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.recycle_info_list.*

class ManageCompanyList : AppCompatActivity() {
    private var listUser: List<User>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycle_info_list)

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
            .whereEqualTo("position", "company").get()
            .addOnSuccessListener { documentSnapshot ->
                listUser = documentSnapshot.toObjects(User::class.java)
                recycleView.apply {
                    layoutManager = LinearLayoutManager(this.context)
                    adapter = AdminCompanyRecyclerAdapter(this.context, listUser.orEmpty())
                }

                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
                recycleView.visibility = View.VISIBLE
                swipe_refresher.isRefreshing = false
            }
    }

}
