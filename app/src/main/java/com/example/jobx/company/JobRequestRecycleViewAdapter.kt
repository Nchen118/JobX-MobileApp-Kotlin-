package com.example.jobx.company

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobx.database.Job
import com.example.jobx.R
import com.example.jobx.database.JobDetails
import com.example.jobx.database.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class JobRequestRecycleViewAdapter(val mContext: Context, val mData: MutableList<JobDetails>) :
    RecyclerView.Adapter<JobRequestRecycleViewAdapter.MyViewHolder>() {
    private lateinit var mAuth:FirebaseAuth
    private lateinit var fStore:FirebaseFirestore
    private var user: User? = null
    private var job: Job? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_request, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        mAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        holder.relativeLayout3.animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation)

        holder.img.animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation)

       val documentReference = fStore.collection("users").document(mData.elementAt(position).user_id.toString())
        documentReference.get().addOnSuccessListener { documentSnapshot ->
           user = documentSnapshot.toObject(User::class.java)

            val documentReference1 = fStore.collection("jobs").document(mData.elementAt(position).job_id.toString())
            documentReference1.get().addOnSuccessListener { documentSnapshot1 ->
                job = documentSnapshot1.toObject(Job::class.java)

                holder.tv_userName.setText(user!!.name)
                holder.tv_jobName.setText(job!!.job_name)
                holder.tv_jobdesc.setText(job!!.job_desc)
            }
        }

        holder.acceptimg.setOnClickListener {
            val documentReference1 = fStore.collection("jobDetails").document(mData.elementAt(position).id.toString())
            documentReference1.update("requestStatus","approved")
            mData.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,itemCount)

        }

        holder.rejectimg.setOnClickListener {
            val documentReference2 = fStore.collection("jobDetails").document(mData.elementAt(position).id.toString())
            documentReference2.update("requestStatus","rejected")
            mData.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,itemCount)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tv_jobName: TextView = itemView.findViewById(R.id.jobName)
        val tv_userName: TextView = itemView.findViewById(R.id.userName)
        val tv_jobdesc: TextView = itemView.findViewById(R.id.jobDesc)
        val relativeLayout3: RelativeLayout = itemView.findViewById(R.id.relativeLayout3)
        val img: ImageView = itemView.findViewById(R.id.user_img)
        val acceptimg: ImageView = itemView.findViewById(R.id.accept)
        val rejectimg: ImageView = itemView.findViewById(R.id.reject)

    }
}