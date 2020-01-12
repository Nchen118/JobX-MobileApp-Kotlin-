package com.example.jobx.jobseeker

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.jobx.R
import com.example.jobx.database.Job
import com.example.jobx.database.JobDetails
import com.example.jobx.database.User
import com.example.jobx.databinding.ActivityJobDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_job_detail.toolbar

class JobDetail : AppCompatActivity() {

    private lateinit var job_id: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private var job: Job? = null
    private var user: User? = null
    private var imageUri: Uri? = null
    private var count:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        val binding = DataBindingUtil.setContentView<ActivityJobDetailBinding>(
            this,
            R.layout.activity_job_detail
        )

        job_id = intent.getStringExtra("job_id").orEmpty()

        val documentReference1 =
            fStore.collection("jobs").document(job_id)
        documentReference1.get().addOnSuccessListener { documentSnapshot1 ->
            job = documentSnapshot1.toObject(Job::class.java)

            binding.JobName.text = job!!.job_name
            binding.jobDesc.text = job!!.job_desc

            val storageRef = FirebaseStorage.getInstance()
                .reference.child("pics/${job!!.company_id}")

            storageRef.downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    imageUri = task.result
                    this.let {
                        Glide.with(this)
                            .load(imageUri)
                            .into(binding.imageView2)
                    }

                }
            }.addOnSuccessListener {
                Toast.makeText(
                    this, "Profile pic updated successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
            setSupportActionBar(toolbar)
            val documentReference2 = fStore.collection("users").document(job!!.company_id.toString())

            documentReference2.get().addOnSuccessListener { documentSnapshot2 ->
                user = documentSnapshot2.toObject(User::class.java)
                binding.CompanyName.text = user!!.name
            }
        }
        binding.btnApply.setOnClickListener {
            val documentReference3 = fStore.collection("jobDetails")

            documentReference3.get().addOnSuccessListener { documentSnapshot3 ->

                this.count = documentSnapshot3.size()
                val newJobDetailsID: String = "JD" + ("%04d".format(count.plus(1)))

                if(mAuth.currentUser?.uid != null){
                    documentReference3.document(newJobDetailsID).set(
                        JobDetails(
                            newJobDetailsID,
                            job_id,
                            job?.company_id,
                            mAuth.currentUser!!.uid,
                            "true",
                            "pending"
                        )
                    ).addOnCompleteListener{
                        Toast.makeText(
                            this, "Job Request Apply Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener{
                        Toast.makeText(
                            this, "Job Request Apply Failure",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }
}
