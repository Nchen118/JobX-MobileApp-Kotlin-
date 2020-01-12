package com.example.admin.JobItemView

import android.content.Context
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

class RecycleViewAdapter(val mContext: Context, val mData: List<Job>) :
    RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder>() {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tv_jobID.setText(mData.elementAt(position).job_id)
        holder.tv_jobName.setText(mData.elementAt(position).job_name)
        holder.tv_comID.setText(mData.elementAt(position).company_id)

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
    }
}