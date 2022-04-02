package net.beingup.simplechat.ChatFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import net.beingup.simplechat.Activities.StartPage
import net.beingup.simplechat.Adapter.UserAdapter

import net.beingup.simplechat.R
import net.beingup.simplechat.pojo.SingleUserInfo

// Download From : https://nulledsourcecode.com/
//Contact us For Reskin And Making Money Earning Apps

class UserFragment : Fragment() {
    lateinit var r_view: RecyclerView
    val user: MutableList<SingleUserInfo> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view  = inflater.inflate(R.layout.fragment_user, container, false)
        r_view = view.findViewById(R.id.recycler_view) as RecyclerView
        r_view.setHasFixedSize(true)
        r_view.layoutManager = LinearLayoutManager(context)
        readUser()

        return view
    }

    /**
     * This function read all users and show as a list.
     */
    private fun readUser(){
        val me = FirebaseAuth.getInstance().currentUser
        val myId = me!!.uid
        val database = FirebaseDatabase.getInstance().reference.child("users")

        database.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                user.clear()
                if (p0.exists()){
                    for (ds in p0.children) {
                        val anUser = ds.getValue(SingleUserInfo::class.java)

                        if (anUser!!.id != myId) user.add(anUser)
                    }

                    try{
                        val userAdapter = UserAdapter(context!!, user, true, false, "", mutableListOf())
                        r_view.adapter = userAdapter
                    }
                    catch (e: Exception){}
                }
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }
}
