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
import com.bumptech.glide.Glide
import com.example.jobx.R
import com.example.jobx.database.Subscriber
import com.example.jobx.database.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class CompanyListRecyclerAdapter(private val mContext: Context, private val mData: List<User>) :
    RecyclerView.Adapter<CompanyListRecyclerAdapter.MyViewHolder>() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private var subscriber: List<Subscriber>? = null

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        mAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

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
        holder.sub.setOnClickListener {
            var count = 0
            var resource: Any? = null
            resource = holder.sub.tag
            val documentReference2 =
                fStore.collection("subscriber")
            documentReference2.get().addOnSuccessListener { documentSnapshot ->
                subscriber = documentSnapshot.toObjects(Subscriber::class.java)
                count = subscriber!!.size

                if (resource == R.drawable.ic_subscriptions_red_24dp) {
                    holder.sub.setImageResource(R.drawable.ic_subscriptions_black_24dp)
                    holder.sub.setTag(R.drawable.ic_subscriptions_black_24dp)

                    val documentReference =
                        fStore.collection("subscriber").whereEqualTo("uid", mAuth.currentUser!!.uid)
                            .whereEqualTo("com_id", mData.elementAt(position).id)
                            .whereEqualTo("status", "true")

                    documentReference.get().addOnSuccessListener { documentSnapshot1 ->
                        subscriber = documentSnapshot1.toObjects(Subscriber::class.java)
                        for (s in subscriber!!) {
                            if (s.status == "true") {
                                fStore.collection("subscriber").document(s.id.toString())
                                    .update("status", "false")
                            }
                        }

                        Toast.makeText(
                            mContext,
                            "Unsubscribe successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {

                    holder.sub.setImageResource(R.drawable.ic_subscriptions_red_24dp)
                    holder.sub.setTag(R.drawable.ic_subscriptions_red_24dp)

                    val documentReference =
                        fStore.collection("subscriber").whereEqualTo("uid", mAuth.currentUser!!.uid)
                            .whereEqualTo("com_id", mData.elementAt(position).id.toString())

                    documentReference.get().addOnSuccessListener { documentSnapshot1 ->
                        subscriber = documentSnapshot1.toObjects(Subscriber::class.java)

                        if (subscriber!!.isNotEmpty()) {
                            for (s in subscriber!!) {
                                if (s.status == "false") {
                                    fStore.collection("subscriber").document(s.id.toString())
                                        .update("status", "true")
                                }
                            }
                            Toast.makeText(
                                mContext,
                                "Subscribe successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            val newSubId: String = "S" + ("%04d".format(count.plus(1)))
                            val documentReference =
                                fStore.collection("subscriber").document(newSubId)
                            documentReference.set(
                                Subscriber(
                                    newSubId,
                                    mAuth.currentUser!!.uid,
                                    mData.elementAt(position).id,
                                    "true"
                                )
                            ).addOnSuccessListener {
                                Toast.makeText(
                                    mContext,
                                    "Subscribe successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }.addOnFailureListener {
                                Toast.makeText(mContext, "Subscribe failure", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
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
        var sub: ImageView = itemView.findViewById(R.id.subButton)
    }
}