package com.example.jobx.authentication

import android.app.ActivityOptions
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.auth_activity.*
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var fUser: FirebaseUser
    private lateinit var callbackManager: CallbackManager

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

        callbackManager = CallbackManager.Factory.create()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.iidd))
            .requestEmail()
            .build()

        var mGoogleSignInClient = GoogleSignIn.getClient(this.activity!!, gso)


        buttonFacebookLogin.setReadPermissions("email", "public_profile")
        buttonFacebookLogin.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
                // ...
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
                // ...
            }
        })

        google_btn.setOnClickListener{
            val signInIntent = mGoogleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, 1)
        }

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
                this.activity!!.loading_wrap.visibility = View.VISIBLE
                mAuth.signInWithEmailAndPassword(
                    txtEmail.text.toString(),
                    txtPassword.text.toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) signIn()
                    else {
                        Common.buttonEnable(signButton, this.context!!)
                        this.activity!!.loading_wrap.visibility = View.GONE
                        Toast.makeText(this.context, "Unable to login", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login", "Google sign in failed", e)
            }

        }
    }

    private fun signIn() {
        fUser = mAuth.currentUser!!

        if (mAuth.currentUser!!.isEmailVerified) {
            fStore.collection("users").document(mAuth.currentUser!!.uid).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Common.user = task.result?.toObject(User::class.java)!!
                        Common.user.id = fUser.uid
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
            txtEmail.error = "Account not verify"
            txtEmail.requestFocus()
            resend_email_verify.isVisible = true
            resend_email_verify.setOnClickListener {
                fUser.sendEmailVerification()
                Toast.makeText(this.context, "Email verification sent", Toast.LENGTH_SHORT).show()
            }
            mAuth.signOut()
            Common.buttonEnable(signButton, this.context!!)
            this.activity!!.loading_wrap.visibility = View.GONE
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Toast.makeText(this.context, "Logged in with Google.", Toast.LENGTH_SHORT).show()
                signIn()
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(this.context, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = mAuth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this.context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }

                // ...
            }
    }
}
