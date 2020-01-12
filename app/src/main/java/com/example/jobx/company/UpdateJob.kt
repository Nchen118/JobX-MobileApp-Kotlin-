package com.example.jobx.company

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.jobx.R
import com.example.jobx.database.Job
import com.example.jobx.databinding.ActivityJobDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_update_job.*
import kotlinx.android.synthetic.main.activity_update_job.txtDate
import kotlinx.android.synthetic.main.activity_update_job.txtJobDesc
import kotlinx.android.synthetic.main.activity_update_job.txtJobName
import kotlinx.android.synthetic.main.activity_update_job.txtLocation
import kotlinx.android.synthetic.main.activity_update_job.txtNumberAvailable
import kotlinx.android.synthetic.main.activity_update_job.txtPosition
import kotlinx.android.synthetic.main.activity_update_job.txtWorkHour
import kotlinx.android.synthetic.main.fragment_insert_job.*

class UpdateJob : AppCompatActivity() {
    private lateinit var job_id: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private var job: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_job)

        mAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        job_id = intent.getStringExtra("job_id").orEmpty()

        val documentReference = fStore.collection("jobs").document(job_id)
        documentReference.get().addOnSuccessListener { documentSnapshot ->
            job = documentSnapshot.toObject(Job::class.java)

            txtJobName.setText(job!!.job_name)
            txtJobDesc.setText(job!!.job_desc)
            txtNumberAvailable.setText(job!!.job_number.toString())
            txtLocation.setText(job!!.job_location)
            txtPosition.setText(job!!.job_position)
            txtJobsalary.setText(job!!.job_salary.toString())
            txtDate.setText(job!!.job_date)
            txtWorkHour.setText(job!!.job_workHour)

        }

        updateRecord.setOnClickListener {
            var err = false
            if (txtJobName.text.isEmpty()) {
                txtJobName.error = "Please enter job name"
                txtJobName.requestFocus()
                err = true
            }

            if (txtJobDesc.text.isEmpty()) {
                txtJobDesc.error = "Please enter the job desc"
                txtJobDesc.requestFocus()
                err = true
            }

            if (txtNumberAvailable.text.isEmpty()) {
                txtNumberAvailable.error = "Please enter the number available"
                txtJobDesc.requestFocus()
                err = true
            }
            if (txtLocation.text.isEmpty()) {
                txtLocation.error = "Please enter the location"
                txtLocation.requestFocus()
                err = true
            }
            if (txtPosition.text.isEmpty()) {
                txtPosition.error = "Please enter the position"
                txtPosition.requestFocus()
                err = true
            }
            if (txtJobsalary.text.isEmpty()) {
                txtJobsalary.error = "Please enter the salary"
                txtJobsalary.requestFocus()
                err = true
            }
            if (txtDate.text.isEmpty()) {
                txtDate.error = "Please enter the date"
                txtDate.requestFocus()
                err = true
            }
            if (txtWorkHour.text.isEmpty()) {
                txtWorkHour.error = "Please enter the work hour"
                txtWorkHour.requestFocus()
                err = true
            }

            if (!err) {
                if (mAuth.currentUser?.uid != null) {
                    fStore.collection("jobs").document(job_id).set(
                        Job(
                            job_id,
                            mAuth.currentUser!!.uid,
                            txtJobName.text.toString(),
                            txtJobDesc.text.toString(),
                            txtNumberAvailable.text.toString().toInt(),
                            txtLocation.text.toString(),
                            txtPosition.text.toString(),
                            txtJobsalary.text.toString().toFloat(),
                            txtDate.text.toString(),
                            txtWorkHour.text.toString(),
                            "true"
                        )
                    ).addOnSuccessListener {
                        Toast.makeText(this, "Job Update Successfully", Toast.LENGTH_SHORT)
                            .show()

                    }.addOnFailureListener {
                        Toast.makeText(this, "Error during update job", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}
