package com.apnaamati.oyehellochat.fragments

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.apnaamati.oyehellochat.Homepage
import com.apnaamati.oyehellochat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


class RegisterFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var edtREmail: EditText
    private lateinit var edtRName: EditText
    private lateinit var edtRBio: EditText
    private lateinit var edtRPass: EditText
    private lateinit var btnRegister: Button
    private lateinit var database: DatabaseReference
    private lateinit var profileImage: ImageView
    private val pickImage = 100
    private var imageUri: Uri? = null
    lateinit var storage: FirebaseStorage


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      val view = inflater.inflate(R.layout.fragment_register, container, false)
        val signIn: TextView = view.findViewById(R.id.txtSignIn)
        val imageView2: ImageView = view.findViewById(R.id.imageView2)
        auth = Firebase.auth
        edtREmail = view.findViewById(R.id.edtREmail)
        edtRName = view.findViewById(R.id.edtRName)
        edtRBio = view.findViewById(R.id.edtRBio)
        edtRPass = view.findViewById(R.id.edtRPass)
        btnRegister = view.findViewById(R.id.btnRegister)
        database = Firebase.database.reference
        profileImage = view.findViewById(R.id.imgRProfile)
        storage = Firebase.storage

        signIn.setOnClickListener{
            var fr = getFragmentManager()?.beginTransaction()
            fr?.replace(R.id.frame, LoginFragment())
            fr?.commit()
        }

        imageView2.setOnClickListener{
            var fr = getFragmentManager()?.beginTransaction()
            fr?.replace(R.id.frame, LoginFragment())
            fr?.commit()
        }

        btnRegister.setOnClickListener{
            createAccount()
        }

        profileImage.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
        return view
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            profileImage.setImageURI(imageUri)
        }
    }
    private fun createAccount() {
       val email = edtREmail.text.toString()
        val password = edtRPass.text.toString()
        val progressDialog = ProgressDialog(context)
        if(email == ""){
            edtREmail.error = "Email Required"
        }
        if(password == ""){
            edtRPass.error = "Password Required"
        }
        if(edtRBio.text.toString() == ""){
            edtRBio.error = "Bio Required"
        }
        if(edtRName.text.toString() == ""){
            edtRName.error = "Name Required"
        }
        else {
            progressDialog.setTitle("Creating New User")
            progressDialog.setMessage("Please wait, Creating Your Profile...")
            progressDialog.show()
            progressDialog.setCancelable(false)
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    updateUI()
                    progressDialog.dismiss()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(context, "Authentication failed.  " + task.exception.toString(),
                        Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
        }

}
    @IgnoreExtraProperties
    data class User(val username: String? = null, val email: String? = null, val bio: String? = null, val profileUrl: String? = null) {
    }
    private fun updateUI() {
        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Saving New User")
        progressDialog.setMessage("Please wait, Saving Your Profile...")
        progressDialog.show()
        progressDialog.setCancelable(false)
        var Ref = storage.reference.child("profileImages").child(auth.currentUser!!.uid)
        val bitmap = (profileImage.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        Ref.putBytes(data).addOnSuccessListener {
            Ref.downloadUrl.addOnSuccessListener {
                writeNewUser(it.toString())
                val intent = Intent(context,Homepage::class.java)
                startActivity(intent)
                activity?.finishAffinity()
                progressDialog.dismiss()
            }
        }.addOnFailureListener {
            Toast.makeText(context,"Failed to upload image to storage: ${it.message}", Toast.LENGTH_LONG).show()
            progressDialog.dismiss()
        }
    }

   private fun writeNewUser(profileUrl: String) {
       val userId = auth.currentUser!!.uid.toString()
        val user = User(edtRName.text.toString(), edtREmail.text.toString(), edtRBio.text.toString(), profileUrl)
        database.child("users").child(userId).setValue(user)
    }
}
