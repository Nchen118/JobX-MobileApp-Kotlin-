package com.example.jobx.jobseeker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jobx.R
import com.example.jobx.database.User
import com.google.firebase.storage.FirebaseStorage

class CompanyListRecyclerAdapter(private val mContext: Context, private val mData: List<User>) :
    RecyclerView.Adapter<CompanyListRecyclerAdapter.MyViewHolder>() {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.relativeLayout1.animation =
            AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation)
        holder.img.animation =
            AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation)
        holder.tvJobname.text = mData.elementAt(position).name
        holder.tvJobdesc.text = mData.elementAt(position).desc
        FirebaseStorage.getInstance().reference.child("pics/${mData.elementAt(position).id}")
            .downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this.mContext).load(task.result).into(holder.img)
            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.element_company, parent, false)
        return MyViewHolder(view)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJobname: TextView = itemView.findViewById(R.id.name)
        val tvJobdesc: TextView = itemView.findViewById(R.id.desc)
        val relativeLayout1: RelativeLayout = itemView.findViewById(R.id.relativeLayout)
        var img: ImageView = itemView.findViewById(R.id.profile_img)
    }
}