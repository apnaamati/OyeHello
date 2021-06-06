package com.apnaamati.oyehellochat.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.apnaamati.oyehellochat.Homepage
import com.apnaamati.oyehellochat.MainActivity
import com.apnaamati.oyehellochat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    lateinit var btnSign: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        auth = Firebase.auth
        btnSign = view.findViewById(R.id.btnSignOut)

        btnSign.setOnClickListener{
            auth.signOut()
            val intent = Intent (getActivity(), MainActivity::class.java)
            getActivity()?.startActivity(intent)
        }

        return view
    }

}