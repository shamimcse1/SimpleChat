package net.beingup.simplechat.Activities


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import kotlinx.android.synthetic.main.chat_toolbar.*
import net.beingup.simplechat.R
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_start_page.*
import net.beingup.simplechat.ChatFragments.ChatFragment
import net.beingup.simplechat.ChatFragments.ProfileFragment
import net.beingup.simplechat.ChatFragments.UserFragment
import net.beingup.simplechat.ChatFragments.ViewPagerAdapter

import net.beingup.simplechat.HelperClasses.ServiceControler
import net.beingup.simplechat.MainActivity
import net.beingup.simplechat.ServiceClasses.NotiService
import net.beingup.simplechat.pippo.Pippo
import net.beingup.simplechat.pojo.SingleUserInfo

class StartPage : AppCompatActivity() {

    private lateinit var user: FirebaseUser
    private lateinit var database: DatabaseReference

    // The Pippo is a native Non SQL database [that I create myself]
    private lateinit var pippo: Pippo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_page)

        start()
        getInformation()
    }

    override fun onResume(){
        super.onResume()
        status(getString(R.string.f_on))
        // start service if stop
        val sc = ServiceControler(this@StartPage)
        // if notification service is close then start this.
        if (!sc.isServiceRunning(NotiService::class.java))
            startService(Intent(this@StartPage, NotiService::class.java))

    }

    override fun onPause() {
        super.onPause()
        status(getString(R.string.f_off))
    }

    /**
     * This is function init all variables.
     */
    private fun start(){
        setSupportActionBar(chat_toolbar_id)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        showAnimation()

        pippo = Pippo(this@StartPage, true, false, false)
        user = FirebaseAuth.getInstance().currentUser!!
        database = FirebaseDatabase.getInstance().reference.child(getString(R.string.f_users)).child(user.uid)

    }


    private fun showAnimation(){
        val isShow = intent.getBooleanExtra("anim", false)
        if (isShow)
            overridePendingTransition(R.anim.alpha_in_activity, R.anim.slide_out_activity)
    }

    // menu ----->
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.signout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.signout -> {
                FirebaseAuth.getInstance().signOut()
                // stop service
                stopService(Intent(this@StartPage, NotiService::class.java))
                finish()
                startActivity(Intent(this@StartPage, MainActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
    // menu <-----

    /**
     * This function use for get current user's information
     */
    private fun getInformation(){
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val sui = p0.getValue(SingleUserInfo::class.java)

                my_name.text = sui!!.username.toUpperCase()
                pippo.keep("myName", sui.username)
                if (sui.profile_image == "default") top_profile.setImageResource(R.drawable.jack)
                else Picasso.get().load(sui.profile_image).into(top_profile)
            }

            override fun onCancelled(p0: DatabaseError) {}
        })

        // code for fragment
        val viewPageAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPageAdapter.addFragment(ChatFragment(), "Chats")
        viewPageAdapter.addFragment(UserFragment(), "Users")
        viewPageAdapter.addFragment(ProfileFragment(), "Profile")

        view_pager.adapter = viewPageAdapter
        tab_layout.setupWithViewPager(view_pager)

    }

    /**
     * This function update user's online and offline status.
     */
    private fun status(status: String){
        val hashMap = HashMap<String, Any>()
        hashMap["status"] = status

        database.updateChildren(hashMap)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
