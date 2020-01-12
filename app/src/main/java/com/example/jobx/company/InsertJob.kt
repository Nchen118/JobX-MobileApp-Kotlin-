package com.example.jobx.company


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction

import com.example.jobx.R
import com.example.jobx.database.Job
import com.example.jobx.database.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_insert_job.*
import kotlin.properties.Delegates

/**
 * A simple [Fragment] subclass.
 */
class InsertJob : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private var count:Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insert_job, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()


        insertRecord.setOnClickListener {
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
            if (txtJobSalary.text.isEmpty()) {
                txtJobSalary.error = "Please enter the salary"
                txtJobSalary.requestFocus()
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
                val documentReference = fStore.collection("jobs")
                documentReference.get().addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val job = document.toObject(Job::class.java)
                        if (job != null) {
                            count+=1
                            Log.i("count1",count.toString())
                        }
                    }
                    if (mAuth.currentUser?.uid != null) {
                        Log.i("count2",count.toString())
                        val newJobId: String = "J" + ("%04d".format(count.plus(1)))
                        fStore.collection("jobs").document(newJobId).set(
                            Job(
                                newJobId,
                                mAuth.currentUser!!.uid,
                                txtJobName.text.toString(),
                                txtJobDesc.text.toString(),
                                txtNumberAvailable.text.toString().toInt(),
                                txtLocation.text.toString(),
                                txtPosition.text.toString(),
                                txtJobSalary.text.toString().toFloat(),
                                txtDate.text.toString(),
                                txtWorkHour.text.toString(),
                                "true"
                            )
                        ).addOnSuccessListener {
                            Toast.makeText(this.context, "Job Insert Successfully", Toast.LENGTH_SHORT)
                                .show()
                            count = 0
                            Log.i("count3",count.toString())

                        }.addOnFailureListener {
                            Toast.makeText(this.context, "Error during insert job", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    clearField()
                }
            }
        }
    }
    private fun clearField(){
        txtJobName.text = null
        txtJobDesc.text = null
        txtNumberAvailable.setText("")
        txtLocation.text = null
        txtPosition.text = null
        txtJobSalary.text = null
        txtDate.text = null
        txtWorkHour.text = null

        txtJobName.requestFocus()
    }
}

