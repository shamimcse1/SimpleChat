package net.beingup.simplechat.ServiceClasses

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import net.beingup.simplechat.HelperClasses.SystemMethods
import net.beingup.simplechat.R
import net.beingup.simplechat.pojo.MessagePojo

class NotiService : Service() {
    private lateinit var database: DatabaseReference
    private lateinit var system: SystemMethods

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        system = SystemMethods(applicationContext)
        val user = FirebaseAuth.getInstance().currentUser!!
        database = FirebaseDatabase.getInstance().reference.child(getString(R.string.f_users)).child(user.uid).child(getString(R.string.f_noti))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        checkNotiMessage()
        return Service.START_STICKY
    }

    /**
     * This function check notification message and send notification.
     * After send notification it also delete this message from notification message list.
     */
    private fun checkNotiMessage(){
        database.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    for (message in p0.children){
                        val singleMess = message.getValue(MessagePojo::class.java)!!
                        system.notification(singleMess.senderName, singleMess.message)
                        system.notificationTune()

                        // delete after notification
                        database.child(message.key!!).removeValue()
                    }
                }
            }
        })
    }
}
