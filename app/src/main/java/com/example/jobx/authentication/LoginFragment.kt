package com.example.jobx.authentication

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.jobx.LoadingPage
import com.example.jobx.R
import com.example.jobx.database.User
import com.example.jobx.library.Common
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.loading_icon.*
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var fUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        signButton.setOnClickListener {
            var err = false

            if (txtEmail.text.isEmpty()) {
                txtEmail.error = "Please enter the username/email"
                txtEmail.requestFocus()
                err = true
            } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.text.toString()).matches()) {
                txtEmail.error = "Please enter the correct email address format"
                txtEmail.requestFocus()
                err = true
            } else if (txtPassword.text.isEmpty()) {
                txtPassword.error = "Please enter the password"
                txtPassword.requestFocus()
                err = true
            } else if (txtPassword.text.toString().length < 6) {
                txtPassword.error = "Please enter at least 6 digit or character"
                txtPassword.requestFocus()
                err = true
            }

            if (!err) {
                Common.buttonDisable(signButton, this.context!!)
                loading_wrap.visibility = View.VISIBLE
                mAuth.signInWithEmailAndPassword(
                    txtEmail.text.toString(),
                    txtPassword.text.toString()
                )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) signIn()
                        else {
                            Common.buttonEnable(signButton, this.context!!)
                            loading_wrap.visibility = View.GONE
                            Toast.makeText(this.context, "Unable to login", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }
    }

    private fun signIn() {
        if (mAuth.currentUser!!.isEmailVerified) {
            fStore.collection("users").document(mAuth.currentUser!!.uid).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Common.user.id = mAuth.currentUser!!.uid
                        Common.user = task.result?.toObject(User::class.java)!!
                        Toast.makeText(this.context, "Login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(
                            Intent(this.context, LoadingPage::class.java),
                            ActivityOptions.makeCustomAnimation(
                                this.context,
                                R.anim.slide_in_right,
                                R.anim.slide_out_left
                            ).toBundle()
                        )
                        this.activity?.finish()
                    }
                }
        } else {
            fUser = mAuth.currentUser!!
            txtEmail.error = "Account not verify"
            txtEmail.requestFocus()
            resend_email_verify.isVisible = true
            resend_email_verify.setOnClickListener {
                fUser.sendEmailVerification()
                Toast.makeText(this.context, "Email verification sent", Toast.LENGTH_SHORT).show()
            }
            mAuth.signOut()
            Common.buttonEnable(signButton, this.context!!)
            loading_wrap.visibility = View.GONE
        }
    }
}
