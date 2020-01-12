package com.example.jobx.company

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.jobx.R
import com.example.jobx.database.Job
import com.example.jobx.jobseeker.JobDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CompanyJobRecycleViewAdapter(private val mContext: Context, private val mData: MutableList<Job>) :
    RecyclerView.Adapter<CompanyJobRecycleViewAdapter.MyViewHolder>() {
    private lateinit var mAuth:FirebaseAuth
    private lateinit var fStore:FirebaseFirestore
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        mAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        holder.relativeLayout.animation =
            AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation)
        holder.img.animation =
            AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation)
        holder.tvComname.text = mData.elementAt(position).company_id
        holder.tvJobname.text = mData.elementAt(position).job_name
        holder.tvJobdesc.text = mData.elementAt(position).job_desc

        holder.dlt.setOnClickListener {
            val documentReference = fStore.collection("jobs").document(mData.elementAt(position).job_id.toString())
            documentReference.update("job_status","false").addOnSuccessListener {
                mData.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position,itemCount)
            }.addOnFailureListener {
                Toast.makeText(mContext, "The archive job is fail", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        holder.upt.setOnClickListener {
            val intent = Intent(this.mContext, UpdateJob::class.java)
            intent.putExtra("job_id",mData.elementAt(position).job_id)
            mContext.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.element_com_job, parent, false)
        return MyViewHolder(view)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJobname: TextView = itemView.findViewById(R.id.name)
        val tvComname: TextView = itemView.findViewById(R.id.comName)
        val tvJobdesc: TextView = itemView.findViewById(R.id.desc)
        val relativeLayout: RelativeLayout = itemView.findViewById(R.id.relativeLayout)
        val img: ImageView = itemView.findViewById(R.id.profile_img)
        val upt: ImageView = itemView.findViewById(R.id.update2)
        val dlt: ImageView = itemView.findViewById(R.id.delete2)
    }
}