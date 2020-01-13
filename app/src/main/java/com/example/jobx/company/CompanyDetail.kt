package com.example.jobx.company

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.jobx.R
import com.example.jobx.database.Job
import com.example.jobx.database.JobDetails
import com.example.jobx.database.User
import com.example.jobx.databinding.ActivityCompanyDetailBinding
import com.example.jobx.databinding.ActivityJobDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_job_detail.*

class CompanyDetail : AppCompatActivity() {

    private lateinit var com_id: String
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
        val binding = DataBindingUtil.setContentView<ActivityCompanyDetailBinding>(
            this,
            R.layout.activity_company_detail
        )

        com_id = intent.getStringExtra("com_id").orEmpty()

        val documentReference1 =
            fStore.collection("users").document(com_id)
        documentReference1.get().addOnSuccessListener { documentSnapshot1 ->
            user = documentSnapshot1.toObject(User::class.java)

            binding.JobName.text = user!!.phone
            binding.jobDesc.text = user!!.desc

            val storageRef = FirebaseStorage.getInstance()
                .reference.child("pics/${user!!.id}")

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
        }
    }
}
