package com.example.jobx.authentication

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.jobx.LoadingPage
import com.example.jobx.MainPage
import com.example.jobx.R
import com.example.jobx.admin.AdminActivity
import com.example.jobx.database.User
import com.example.jobx.library.Common
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.loading_icon.*
import kotlinx.android.synthetic.main.register_fragment.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        signUp.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        if (txtEmail.text.isNullOrEmpty()) {
            txtEmail.error = "Please enter your email"
            txtEmail.requestFocus()
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.text.toString()).matches()) {
            txtEmail.error = "Please enter the correct email address format"
            txtEmail.requestFocus()
            return
        }

        if (txtPassword.text.isNullOrEmpty()) {
            txtPassword.error = "Please enter the password"
            txtPassword.requestFocus()
            return
        } else if (txtPassword.text.toString().length < 6) {
            txtPassword.error = "Please enter at least 6 digit or character"
            txtPassword.requestFocus()
            return
        }


        if (txtName.text.isNullOrEmpty()) {
            txtName.error = "Please enter your name"
            txtName.requestFocus()
            return
        } else {
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
                var name = m.appendTail(strBf).toString()
            } else {
                txtName.error = "Please enter a valid name"
                txtName.requestFocus()
                return
            }
        }
        if (txtDesc.text.isNullOrEmpty()) {
            txtDesc.error = "Please key in description"
            txtDesc.requestFocus()
            return
        }

        if (txtPhone.text.isNullOrEmpty()) {
            txtPhone.error = "Please key in phone number"
            txtPhone.requestFocus()
            return
        } else {
            val temp = txtPhone.text.toString().trim()
            var returnPhoneNumber: String
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
        }

        if (txtAddress.text.isNullOrEmpty()) {
            txtAddress.error = "Please enter valid address"
            txtAddress.requestFocus()
            return
        } else {
            var temp: String = txtAddress.text.toString().trim()

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
            } else {
                txtAddress.error = "Please enter valid address"
                txtAddress.requestFocus()
                return
            }
        }

        if (txtCity.text.isNullOrEmpty()) {
            txtCity.error = "Please enter valid city"
            txtCity.requestFocus()
            return
        } else {
            var temp: String = txtCity.text.toString().trim()
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
            } else {
                txtCity.error = "Please enter valid city"
                txtCity.requestFocus()
                return
            }
        }

        if (txtPosCode.text.isNullOrEmpty()) {
            txtPosCode.error = "Please key in valid poscode"
            txtPosCode.requestFocus()
            return
        } else {
            val temp = txtPosCode.text.toString().trim()
            val r = Regex("[0-9]*")
            if (!temp.matches(r)) {
                txtPosCode.error = "Please key in valid poscode"
                txtPosCode.requestFocus()
                return
            }
        }

        Common.buttonDisable(signUp, this.context!!)
        loading_wrap.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(txtEmail.text.toString(), txtPassword.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Common.user = User(
                        mAuth.currentUser!!.uid,
                        txtName.text.toString(),
                        txtDesc.text.toString(),
                        txtPhone.text.toString(),
                        txtAddress.text.toString(),
                        txtCity.text.toString(),
                        txtPosCode.text.toString(),
                        if (selectSwitch.isChecked) "company" else "jobseeker",
                        "true"
                    )
                    fStore.collection("users").document(mAuth.currentUser!!.uid).set(
                        Common.user
                    ).addOnSuccessListener {
                        Toast.makeText(
                            this.context,
                            "Account success register",
                            Toast.LENGTH_SHORT
                        ).show()
                        mAuth.currentUser!!.sendEmailVerification()
                        FirebaseAuth.getInstance().signOut()
                        startActivity(
                            Intent(this.context, RegisterSuccessful::class.java),
                            ActivityOptions.makeCustomAnimation(
                                this.context,
                                R.anim.slide_in_right,
                                R.anim.slide_out_left
                            ).toBundle()
                        )
                        this.activity?.finish()
                    }.addOnFailureListener {
                        Common.buttonEnable(signUp, this.context!!)
                        loading_wrap.visibility = View.GONE
                        Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Common.buttonEnable(signUp, this.context!!)
                    loading_wrap.visibility = View.GONE
                    txtEmail.error = task.exception?.message
                    txtEmail.requestFocus()
                }
            }
    }

    private fun clear() {
        txtEmail.text.clear()
        txtPassword.text.clear()
        txtName.text.clear()
        txtDesc.text.clear()
        txtPhone.text.clear()
        txtAddress.text.clear()
        txtCity.text.clear()
        txtPosCode.text.clear()
        selectSwitch.isChecked = false
    }
}
