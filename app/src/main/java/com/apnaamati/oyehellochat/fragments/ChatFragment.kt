package com.apnaamati.oyehellochat.fragments

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.apnaamati.oyehellochat.Homepage
import com.apnaamati.oyehellochat.R
import com.apnaamati.oyehellochat.SearchUserPage
import com.apnaamati.oyehellochat.adapters.UserAdapter
import com.apnaamati.oyehellochat.models.userlist
import com.bumptech.glide.Glide
import com.firebase.ui.database.ChangeEventListener
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.sample_chat_list.*
import kotlinx.android.synthetic.main.sample_chat_list.view.*
import androidx.recyclerview.widget.RecyclerView.ViewHolder as ViewHolder1


class ChatFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    lateinit var recyclerView: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var userArrayList: ArrayList<userlist>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        auth = Firebase.auth
        database = Firebase.database.reference

        //recyclerView Showing /
        recyclerView = view.findViewById(R.id.chatsPersonList)
        recyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf<userlist>()

        val addPerson: ImageView = view.findViewById(R.id.addPerson)
        addPerson.setOnClickListener{
            val intent = Intent (getActivity(), SearchUserPage::class.java)
            getActivity()?.startActivity(intent)
        }
        fecthdata()

    return view
    }
    private fun fecthdata(){
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    for(userSnap in snapshot.children){
                        val user = userSnap.getValue(userlist::class.java)
                        userArrayList.add(user!!)
                    }
                }
                recyclerView.adapter = UserAdapter(userArrayList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}


