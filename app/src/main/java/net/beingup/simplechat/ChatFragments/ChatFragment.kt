package net.beingup.simplechat.ChatFragments

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import net.beingup.simplechat.Adapter.UserAdapter

import net.beingup.simplechat.R
import net.beingup.simplechat.pojo.MessagePojo
import net.beingup.simplechat.pojo.SingleUserInfo
import org.json.JSONArray


// Download From : https://nulledsourcecode.com/
//Contact us For Reskin And Making Money Earning Apps

class ChatFragment : Fragment() {
    lateinit var r_view: RecyclerView
    lateinit var myId: String

    // store friend list
    val user: MutableList<SingleUserInfo> = mutableListOf()

    // store friends last message
    val lastMessages: MutableList<MessagePojo> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view  = inflater.inflate(R.layout.fragment_chat, container, false)
        r_view = view.findViewById(R.id.recycler_view) as RecyclerView
        r_view.setHasFixedSize(true)
        r_view.layoutManager = LinearLayoutManager(context)

        readUser()
        return view
    }

    /**
     * This function read friend list and last message.
     */
    private fun readUser(){
        val me = FirebaseAuth.getInstance().currentUser
        myId = me!!.uid
        val database = FirebaseDatabase.getInstance().reference

        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                user.clear()
                if (p0.child("users").child(myId).exists()){
                    val anUser = p0.child("users").child(myId).getValue(SingleUserInfo::class.java)!!
                    val friends = JSONArray(anUser.friends)

                    if (friends.length()>0){
                        for (id in 0..(friends.length()-1)){
                            try{
                                if(p0.child("users").child(friends.getString(id)).exists()){
                                    val aFriend = p0.child("users").child(friends.getString(id)).getValue(SingleUserInfo::class.java)
                                    if (aFriend != null){
                                        user.add(aFriend)
                                        val tab1 = "${myId}_table_${aFriend.id}"
                                        val tab2 = "${aFriend.id}_table_$myId"
                                        var table: String

                                        val tableSnap = p0.child(getString(R.string.f_chats)).child(getString(R.string.f_table))
                                        table = if (tableSnap.child(tab1).exists()) tab1 else tab2
                                        val emptyMs = MessagePojo()
                                        emptyMs.sender = myId
                                        emptyMs.receiver = myId
                                        emptyMs.message = getString(R.string.f_null)
                                        emptyMs.isSeen = "true"

                                        if (table != "0"){
                                            try{
                                                val lastChild = tableSnap.child(table).children.last()
                                                lastMessages.add(lastChild.getValue(MessagePojo::class.java)!!)
                                            }
                                            catch (e1: Exception){
                                                lastMessages.add(emptyMs)
                                            }
                                        }
                                        else lastMessages.add(emptyMs)
                                    }
                                }
                            }
                            catch (e: Exception){}
                        }
                    }

                    try{
                        val userAdapter = UserAdapter(context!!, user, true, true, myId, lastMessages)
                        r_view.adapter = userAdapter
                    }
                    catch (e: Exception){}
                }
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

}
