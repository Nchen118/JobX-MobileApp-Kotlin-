package com.example.jobx.admin

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin.JobItemView.RecycleViewAdapter
import com.example.jobx.R
import com.example.jobx.database.Job
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_manage_job.*

class ManageJobFragment : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private var listJob: List<Job>? = null
    private lateinit var fStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_manage_job)

        setData()
    }

    private fun setData(){
        mAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        val documentReference =
            fStore.collection("jobs").whereEqualTo("company_id", mAuth.currentUser?.uid)
                .whereEqualTo("job_status", "true")
        documentReference.get().addOnSuccessListener { documentSnapshot ->
            listJob = documentSnapshot.toObjects(Job::class.java)

            if (mAuth.currentUser?.uid != null) {
                jobRV.apply {
                    layoutManager = LinearLayoutManager(this.context)
                    adapter = RecycleViewAdapter(this.context, listJob.orEmpty())

                }
            }
        }
    }

}
