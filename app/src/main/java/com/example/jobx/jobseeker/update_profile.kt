package com.example.jobx.jobseeker

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.jobx.LoadingPage
import com.example.jobx.R
import com.example.jobx.database.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_update_profile.*
import kotlinx.android.synthetic.main.register_fragment.txtAddress
import kotlinx.android.synthetic.main.register_fragment.txtCity
import kotlinx.android.synthetic.main.register_fragment.txtDesc
import kotlinx.android.synthetic.main.register_fragment.txtEmail
import kotlinx.android.synthetic.main.register_fragment.txtName
import kotlinx.android.synthetic.main.register_fragment.txtPhone
import kotlinx.android.synthetic.main.register_fragment.txtPosCode
import java.util.regex.Matcher
import java.util.regex.Pattern


class update_profile : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var user:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        if(firebaseAuth.currentUser==null){
            startActivity(
                Intent(this, LoadingPage::class.java),
                ActivityOptions.makeCustomAnimation(
                    this,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                ).toBundle()
            )
        }

        var profile = firebaseFirestore.collection("users").document(firebaseAuth.currentUser!!.uid)
        profile.get().addOnSuccessListener {
            if(it!=null){
                user = it.toObject(User::class.java)!!
                txtEmail.setText(firebaseAuth.currentUser!!.email.toString())
                txtName.setText(user.name)
                txtDesc.setText(user.desc)
                txtPhone.setText(user.phone)
                txtAddress.setText(user.address)
                txtCity.setText(user.city)
                txtPosCode.setText(user.code)
//                txtEmail.setText(it)
            }else{
                Toast.makeText(this,"The user not found",Toast.LENGTH_SHORT).show()
                //GO back
//
            }
        }
        updateBtn.setOnClickListener {
            validation()
        }


    }
    private fun validation(){
        val name:String
        val desc:String
        val phone:String
        val address:String
        val city:String
        val code:String

        if (txtName.text.isNullOrEmpty()) {
            name = user.name.toString()
        }else{
            val temp = txtName.text.toString().trim()
            val p1 = Pattern.compile("[a-zA-Z/. ]*")
            val match = p1.matcher(temp)
            if (match.matches()) {
                val strBf = StringBuffer()
                val m = Pattern.compile(
                    "([a-z])([a-z]*)",
                    Pattern.CASE_INSENSITIVE
                ).matcher(temp)
                while (m.find()) m.appendReplacement(
                    strBf,
                    m.group(1).toUpperCase() + m.group(2).toLowerCase()
                )
                name = m.appendTail(strBf).toString()
            } else {
                txtName.error = "Please enter a valid name"
                txtName.requestFocus()
                return
            }
        }
        if(txtDesc.text.isNullOrEmpty()){
            desc = user.desc.toString()
        }else{
            desc = txtDesc.text.toString().trim()
        }
        if(txtPhone.text.isNullOrEmpty()){
            phone = user.phone.toString()
        }else{
            val temp = txtPhone.text.toString().trim()
            var returnPhoneNumber:String = ""
            val p1: Pattern = Pattern.compile("([0][1][1])-(\\d{8})") // 011-34567898

            val p2: Pattern = Pattern.compile("([0][1][02-9])-(\\d{7})") // 012-3456789

            val p3: Pattern = Pattern.compile("([0][1][1])(\\d{8})") // 01134567898

            val p4: Pattern = Pattern.compile("([0][1][02-9])(\\d{7})") // 0123456789

            val p5: Pattern = Pattern.compile("([0][1][1]) (\\d{8})") // 011 12121212

            val p6: Pattern = Pattern.compile("([0][1][02-9]) (\\d{7})") // 012 1212121


            val strBf = StringBuffer()
            // Match the phone number with the format of phone number in Malaysia
            val match1: Matcher = p1.matcher(temp)
            val match2: Matcher = p2.matcher(temp)
            val match3: Matcher = p3.matcher(temp)
            val match4: Matcher = p4.matcher(temp)
            val match5: Matcher = p5.matcher(temp)
            val match6: Matcher = p6.matcher(temp)
            val b1: Boolean = match1.matches()
            val b2: Boolean = match2.matches()
            val b3: Boolean = match3.matches()
            val b4: Boolean = match4.matches()
            val b5: Boolean = match5.matches()
            val b6: Boolean = match6.matches()

            // adding the '-'
            // adding the '-'
            if (b1 || b2 && (!b3 || !b4 || !b5 || !b6)) {
                returnPhoneNumber = temp
            } else if ((!b1 || !b2 || !b4 || !b5 || !b6) && b3) {
                match3.appendReplacement(strBf, match3.group(1).toString() + "-" + match3.group(2))
                returnPhoneNumber = match3.appendTail(strBf).toString()
            } else if ((!b1 || !b2 || !b3 || !b5 || !b6) && b4) {
                match4.appendReplacement(strBf, match4.group(1).toString() + "-" + match4.group(2))
                returnPhoneNumber = match4.appendTail(strBf).toString()
            } else if ((!b1 || !b2 || !b3 || !b4 || !b6) && b5) {
                match5.appendReplacement(strBf, match5.group(1).toString() + "-" + match5.group(2))
                returnPhoneNumber = match5.appendTail(strBf).toString()
            } else if ((!b1 || !b2 || !b3 || !b4 || !b5) && b6) {
                match6.appendReplacement(strBf, match6.group(1).toString() + "-" + match6.group(2))
                returnPhoneNumber = match6.appendTail(strBf).toString()
            } else {
                txtPhone.error = "Please enter a valid phone number"
                txtPhone.requestFocus()
                return
            }
            phone = returnPhoneNumber
        }
        if(txtAddress.text.isNullOrEmpty()){
            address = user.address.toString()
        }else{
            var temp:String = txtAddress.text.toString().trim()

            val p1 = Pattern.compile("[a-zA-Z/.,0-9 ]*")
            val match = p1.matcher(temp)
            if (match.matches()) {
                val strBf = StringBuffer()
                val m = Pattern.compile(
                    "([a-z])([a-z]*)",
                    Pattern.CASE_INSENSITIVE
                ).matcher(temp)
                while (m.find()) m.appendReplacement(
                    strBf,
                    m.group(1).toUpperCase() + m.group(2).toLowerCase()
                )
                address = m.appendTail(strBf).toString()
            } else {
                txtAddress.error = "Please enter valid address"
                txtAddress.requestFocus()
                return
            }
        }
        if(txtCity.text.isNullOrEmpty()){
            city = user.city.toString()
        }else{
            var temp:String = txtCity.text.toString().trim()
            val p1 = Pattern.compile("[a-zA-Z/. ]*")
            val match = p1.matcher(temp)
            if (match.matches()) {
                val strBf = StringBuffer()
                val m = Pattern.compile(
                    "([a-z])([a-z]*)",
                    Pattern.CASE_INSENSITIVE
                ).matcher(temp)
                while (m.find()) m.appendReplacement(
                    strBf,
                    m.group(1).toUpperCase() + m.group(2).toLowerCase()
                )
                city = m.appendTail(strBf).toString()
            } else {
                txtCity.error = "Please enter valid city"
                txtCity.requestFocus()
                return
            }
        }
        if(txtPosCode.text.isNullOrEmpty()){
            code = user.code.toString()
        }else{
            val temp = txtPosCode.text.toString().trim()
            val r = Regex("[0-9]*")
            if(!temp.matches(r)){
                txtPosCode.error = "Please key in valid poscode"
                txtPosCode.requestFocus()
                return
            }
            else{
                code = temp
            }
        }
        val user = firebaseFirestore.collection("users").document(firebaseAuth.currentUser!!.uid)

        user.update("address", address,"city",city,"code",code,
            "desc",desc,"name",name,"phone",phone)
            .addOnSuccessListener {
                txtEmail.setText(firebaseAuth.currentUser!!.email.toString())
                txtName.setText(name)
                txtDesc.setText(desc)
                txtPhone.setText(phone)
                txtAddress.setText(address)
                txtCity.setText(city)
                txtPosCode.setText(code)
                Toast.makeText(this,"Information is updated!!",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this,"Something wrong",Toast.LENGTH_SHORT).show()
            }
    }


}
