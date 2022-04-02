package net.beingup.simplechat.Activities

import android.content.Intent

import android.os.Bundle

import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_messenger.*
import kotlinx.android.synthetic.main.chat_toolbar.*
import net.beingup.simplechat.Adapter.MessageAdapter
import net.beingup.simplechat.HelperClasses.ArrayAndList
import net.beingup.simplechat.ServiceClasses.NotiService
import net.beingup.simplechat.R
import net.beingup.simplechat.pippo.Pippo
import net.beingup.simplechat.pojo.MessagePojo
import net.beingup.simplechat.pojo.SingleUserInfo
import org.json.JSONArray
import kotlin.collections.HashMap

class Messenger : AppCompatActivity() {


    private lateinit var pippo: Pippo
    private lateinit var dbRef: DatabaseReference
    private lateinit var user: FirebaseUser
    private lateinit var myId: String
    private lateinit var myName: String
    private lateinit var myDatabase: DatabaseReference

    // the targetUser is current user's friend ID
    private lateinit var targetUser: String
    private var isTargetOnline = false
    private lateinit var targetUserDb: DatabaseReference

    private var chatTable = "unknown"
    private lateinit var getChat: DatabaseReference
    private lateinit var chatDb: ValueEventListener

    // ArrayList for collect all messages.
    private lateinit var arrayAndList: ArrayAndList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger)

        start()
        getInformation()
    }

    override fun onResume() {
        super.onResume()
        status(getString(R.string.f_on))
        // stop notification service
        stopService(Intent(this@Messenger, NotiService::class.java))
    }

    override fun onPause() {
        super.onPause()
        status(getString(R.string.f_off))
        // start notification service
        startService(Intent(this@Messenger, NotiService::class.java))
        // stop getting message
        getChat.removeEventListener(chatDb)
    }

    /**
     * This is function init all variables.
     */
    private fun start(){
        setSupportActionBar(chat_toolbar_id)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        arrayAndList = ArrayAndList()

        pippo = Pippo(this@Messenger, true, false, false)

        // firebase user
        user = FirebaseAuth.getInstance().currentUser!!
        myId = user.uid

        // Store my name into pippo database
        pippo.Default("unknown")
        myName = pippo.getString("myName")

        targetUser = intent.getStringExtra("target_user").toString()
        dbRef = FirebaseDatabase.getInstance().reference
        targetUserDb = FirebaseDatabase.getInstance().reference.child(getString(R.string.f_users)).child(targetUser)
        myDatabase = FirebaseDatabase.getInstance().reference.child(getString(R.string.f_users)).child(myId)

        // chat field
        chat_field.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        chat_field.layoutManager = linearLayoutManager

        // methods
        sendClickAction(user.uid, targetUser)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * This function is working for get all information about current user's friend
     */
    private fun getInformation(){
        targetUserDb.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sui = snapshot.getValue(SingleUserInfo::class.java)!!
                isTargetOnline = sui.status == getString(R.string.f_on)
                my_name.text = sui.username.toUpperCase()

                if (sui.profile_image == getString(R.string.f_default)) top_profile.setImageResource(R.drawable.jack)
                else Picasso.get().load(sui.profile_image).into(top_profile)

                getChatTable(targetUser, sui.profile_image)
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    /**
     * This function return chat table name
     */
    private fun getChatTable(targetUser: String, friendProfile: String){
        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                val tab1 = "${user.uid}_table_$targetUser"
                val tab2 = "${targetUser}_table_${user.uid}"
                chatTable = if (p0.child(getString(R.string.f_chats)).child(getString(R.string.f_table)).child(tab1).exists()) tab1 else tab2

                // update current user friend list
                val myFriendList = p0.child(getString(R.string.f_users)).child(user.uid).getValue(SingleUserInfo::class.java)
                val myList = JSONArray(myFriendList!!.friends)
                if (!arrayAndList.isFound(targetUser, myList)){
                    myList.put(targetUser)
                    dbRef.child(getString(R.string.f_users)).child(user.uid).child(getString(R.string.f_friends)).setValue(myList.toString())
                }

                // update target user friend list
                val targetFriendList = p0.child(getString(R.string.f_users)).child(targetUser).getValue(SingleUserInfo::class.java)
                val targetList = JSONArray(targetFriendList!!.friends)
                if (!arrayAndList.isFound(user.uid, targetList)){
                    targetList.put(user.uid)
                    dbRef.child(getString(R.string.f_users)).child(targetUser).child(getString(R.string.f_friends)).setValue(targetList.toString())
                }

                // show old messages
                readMessage(friendProfile)

                dbRef.removeEventListener(this)
            }
        })
    }

    /**
     * This function invoke when user click message send button.
     */
    private fun sendClickAction(sender: String, receiver: String){
        action_send.setOnClickListener {
            val text = send_text.text.toString().trim()
            if (text.isNotEmpty()) {
                val success = sendMessage(chatTable, sender, receiver, text)
                if (success) send_text.setText("")
            }
            else Toast.makeText(this, "write something to send", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * This is the main function for send text message to server.
     */
    private fun sendMessage(table: String, sender: String, receiver: String, message: String): Boolean{
        val hashMap = HashMap<String, Any>()
        hashMap["sender"] = sender
        hashMap["senderName"] = myName
        hashMap["receiver"] = receiver
        hashMap["message"] = message
        hashMap["isSeen"] = "false"

        // if message table name is not 'unknown'
        return if (table != "unknown") {
            dbRef.child(getString(R.string.f_chats)).child(getString(R.string.f_table)).child(table).push().setValue(hashMap)

            // if user offline send message also for notification.
            if (!isTargetOnline) dbRef.child(getString(R.string.f_users)).child(targetUser).child(getString(R.string.f_noti)).push().setValue(hashMap)
            true
        }
        else {
            Toast.makeText(this@Messenger, "Wait a moment", Toast.LENGTH_SHORT).show()
            false
        }
    }

    /**
     * This function read user old messages and show those.
     */
    private fun readMessage(profile: String){
        val mChat: MutableList<MessagePojo> = mutableListOf()
        getChat = dbRef.child(getString(R.string.f_chats)).child(getString(R.string.f_table)).child(chatTable)

        chatDb = getChat.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                mChat.clear()
                if (p0.exists()){
                    for(ds in p0.children){
                        val data = ds.getValue(MessagePojo::class.java)

                        // mark as seen
                        if (data!!.sender != user.uid){
                            val hashMap = HashMap<String, Any>()
                            hashMap["isSeen"] = "true"
                            ds.ref.updateChildren(hashMap)
                        }

                        // show message
                        mChat.add(data)
                        chat_field.adapter = MessageAdapter(this@Messenger, mChat, profile, myId)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }


    /**
     * This function update user's online and offline status.
     */
    private fun status(status: String){
        val hashMap = HashMap<String, Any>()
        hashMap["status"] = status
        myDatabase.updateChildren(hashMap)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@Messenger, StartPage::class.java))
        finish()
    }
}
