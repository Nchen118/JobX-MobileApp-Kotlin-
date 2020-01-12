package com.example.jobx.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobx.R
import com.example.jobx.database.User

class AdminCompanyRecyclerAdapter (private val mContext: Context, private val mData : List<User>):
    RecyclerView.Adapter<AdminCompanyRecyclerAdapter.MyViewHolder>() {

    override fun onBindViewHolder(holder: AdminCompanyRecyclerAdapter.MyViewHolder, position: Int) {
        holder.tv_Name.text = mData.elementAt(position).name
        holder.tv_Phone.text = mData.elementAt(position).phone
        holder.tv_Address.text = mData.elementAt(position).address
    }


    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.admin_company_item, parent, false)
        return MyViewHolder(view)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_Name: TextView = itemView.findViewById(R.id.Name_text)
        val tv_Phone: TextView = itemView.findViewById(R.id.Phone_text)
        val tv_Address: TextView = itemView.findViewById(R.id.Address_text)
        val relativeLayout: RelativeLayout = itemView.findViewById(R.id.relativeLayout)
    }
}