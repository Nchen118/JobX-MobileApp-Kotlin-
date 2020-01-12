package com.example.jobx.jobseeker

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

class JobListRecyclerAdapter(private val mContext: Context, private val mData: List<Job>) :
    RecyclerView.Adapter<JobListRecyclerAdapter.MyViewHolder>() {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.relativeLayout1.animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation)
        holder.img.animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation)
        holder.tvComname.text = mData.elementAt(position).company_id
        holder.tvJobname.text = mData.elementAt(position).job_name
        holder.tvJobdesc.text = mData.elementAt(position).job_desc

//        holder.relativeLayout1.setOnClickListener {
//            val intent = Intent(this.mContext, JobDetail::class.java)
//            intent.putExtra("job_id", mData.elementAt(position).job_id)
//            mContext.startActivity(intent)
//        }

        holder.msg.setOnClickListener {
            Toast.makeText(
                mContext, "Message Button Click",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.element_job, parent, false)
        return MyViewHolder(view)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJobname: TextView = itemView.findViewById(R.id.name)
        val tvComname: TextView = itemView.findViewById(R.id.comName)
        val tvJobdesc: TextView = itemView.findViewById(R.id.desc)
        val relativeLayout1: RelativeLayout = itemView.findViewById(R.id.relativeLayout)
        val img: ImageView = itemView.findViewById(R.id.profile_img)
        val msg: ImageView = itemView.findViewById(R.id.msgButton)
    }
}