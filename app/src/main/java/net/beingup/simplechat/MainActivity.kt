package net.beingup.simplechat

import android.content.Intent

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import net.beingup.simplechat.Activities.StartPage
import net.beingup.simplechat.Activities.UserSignin

class MainActivity : AppCompatActivity() {

    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_activity, R.anim.alpha_out_activity)
        setContentView(R.layout.activity_main)

        user = FirebaseAuth.getInstance().currentUser

        val handler = Handler()
        val runnable = Runnable {
            if (user != null){
                val anim = Intent(this@MainActivity, StartPage::class.java)
                anim.putExtra("anim", true)
                startActivity(anim)
                finish()
            }
            else{
                val anim = Intent(this@MainActivity, UserSignin::class.java)
                anim.putExtra("anim", true)
                startActivity(anim)
                finish()
            }
        }
        handler.postDelayed(runnable, 3000)
    }
}
