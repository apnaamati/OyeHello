package com.apnaamati.oyehellochat.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.service.autofill.TextValueSanitizer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.apnaamati.oyehellochat.Homepage
import com.apnaamati.oyehellochat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var edtLEmail: EditText
    private lateinit var edtLpass: EditText
    private lateinit var btnLogin: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_login, container, false)
        val signUp:TextView = view.findViewById(R.id.txtSignUp)
        auth = Firebase.auth
        edtLEmail = view.findViewById(R.id.edtLEmail)
        edtLpass = view.findViewById(R.id.edtLPass)
        btnLogin = view.findViewById(R.id.btnLogin)

        signUp.setOnClickListener{
            var fr = getFragmentManager()?.beginTransaction()
            fr?.replace(R.id.frame, RegisterFragment())
            fr?.commit()
        }

        btnLogin.setOnClickListener {
            signIn() }
        return view
    }
    private fun signIn() {
        val email = edtLEmail.text.toString()
        val password = edtLpass.text.toString()
        val progressDialog = ProgressDialog(context)
        if(email == ""){
            edtLEmail.error = "Email Required"
        }
        if(password == ""){
            edtLpass.error = "Password Required"
        }
        else {
            progressDialog.setTitle("Welcome Back")
            progressDialog.setMessage("Please wait, Signing You...")
            progressDialog.show()
            progressDialog.setCancelable(false)
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    reload()
                    progressDialog.dismiss()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressDialog.dismiss()
                }}
            }
    }
        public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }

     fun reload() {
        val intent = Intent (getActivity(), Homepage::class.java)
        getActivity()?.startActivity(intent)
    }
}