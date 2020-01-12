package com.example.jobx.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobx.R
import com.example.jobx.database.Job
import com.google.firebase.firestore.FirebaseFirestore

class AdminJobRecyclerAdapter(private val mContext: Context, private val mData: List<Job>) :
    RecyclerView.Adapter<AdminJobRecyclerAdapter.MyViewHolder>() {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv_jobID.text = mData.elementAt(position).job_id
        holder.tv_jobName.text = mData.elementAt(position).job_name
        holder.tv_comID.text = mData.elementAt(position).company_id
        holder.tv_switch.isChecked = !mData.elementAt(position).job_status!!.toBoolean()

        holder.tv_switch.setOnClickListener{
            if (holder.tv_switch.isChecked) {
                FirebaseFirestore.getInstance().collection("jobs")
                    .document(mData.elementAt(position).job_id.toString()).update("job_status", "false")
            } else {
                FirebaseFirestore.getInstance().collection("jobs")
                    .document(mData.elementAt(position).job_id.toString()).update("job_status", "true")
            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.admin_job_item, parent, false)
        return MyViewHolder(view)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_jobID: TextView = itemView.findViewById(R.id.JobID_text)
        val tv_jobName: TextView = itemView.findViewById(R.id.JobName_text)
        val tv_comID: TextView = itemView.findViewById(R.id.CompanyID_text)
        val relativeLayout: RelativeLayout = itemView.findViewById(R.id.relativeLayout)
        val tv_switch: Switch = itemView.findViewById(R.id.switch1)
    }
}