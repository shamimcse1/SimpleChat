package net.beingup.simplechat.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_user_signup.*
import kotlinx.android.synthetic.main.toolbar2.*
import net.beingup.simplechat.MainActivity
import net.beingup.simplechat.R
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class UserSignup : AppCompatActivity(), View.OnClickListener {


    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_signup)

        start()
    }

    /**
     * This is function init all variables.
     */
    private fun start(){
        setSupportActionBar(toolbar_id)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // up button color
        val drawable = toolbar_id.navigationIcon
        drawable!!.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)

        auth = FirebaseAuth.getInstance()
        signup.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id){
            R.id.signup -> signUp()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * This function store new user info into database and create new account.
     */
    private fun signUp(){
        val userName = username.text.toString().trim()
        val eMail = mail.text.toString().trim()
        val pass = password.text.toString().trim()

        if (userName.isEmpty()){
            username.error = getString(R.string.error1)
            username.requestFocus()
            return
        }
        if (userName.length<6){
            username.error = getString(R.string.error2)
            username.requestFocus()
            return
        }

        if (eMail.isEmpty()){
            mail.error = getString(R.string.error3)
            mail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(eMail).matches()){
            mail.error = getString(R.string.error4)
            mail.requestFocus()
            return
        }

        if (pass.isEmpty()){
            password.error = getString(R.string.error1)
            password.requestFocus()
            return
        }
        if (pass.length<6){
            password.error = getString(R.string.error2)
            password.requestFocus()
            return
        }

        loading.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(eMail, pass).addOnCompleteListener { task ->
            loading.visibility = View.GONE
            when {
                task.isSuccessful -> {
                    val userid = auth.currentUser!!.uid
                    val database = FirebaseDatabase.getInstance().getReference("users").child(userid)

                    val hasMap = HashMap<String, String>()
                    hasMap["id"] = userid
                    hasMap["username"] = userName
                    hasMap["profile_image"] = "default"
                    hasMap["friends"] = "[]"

                    database.setValue(hasMap).addOnCompleteListener{task->
                        when{
                            task.isSuccessful -> {
                                startActivity(Intent(this@UserSignup, MainActivity::class.java))
                                finish()
                            }
                        }
                    }
                }

                task.exception is FirebaseAuthUserCollisionException -> Toast.makeText(applicationContext, "You are already registered", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(applicationContext, getString(R.string.error0), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
