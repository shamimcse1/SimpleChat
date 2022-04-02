package net.beingup.simplechat.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_signin.*
import net.beingup.simplechat.R

class UserSignin : AppCompatActivity(), View.OnClickListener {

// Download From : https://nulledsourcecode.com/
//Contact us For Reskin And Making Money Earning Apps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_signin)

        start()
    }

    /**
     * This is function init all variables.
     */
    private fun start(){
        this@UserSignin.title = "Sign In"

        showAnimation()

        signin.setOnClickListener(this)
        new_ac.setOnClickListener(this)
    }

    /**
     * This function show activity animation for splash screen.
     */
    private fun showAnimation(){
        val isShow = intent.getBooleanExtra("anim", false)
        if (isShow)
            overridePendingTransition(R.anim.alpha_in_activity, R.anim.slide_out_activity)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id){
            R.id.signin -> signIn()
            R.id.new_ac -> startActivity(Intent(this@UserSignin, UserSignup::class.java))
        }
    }

    /**
     * This function check mail and password and sign in user.
     */
    private fun signIn(){
        val eMail = mail.text.toString().trim()
        val pass = password.text.toString().trim()

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
        FirebaseAuth.getInstance().signInWithEmailAndPassword(eMail, pass).addOnCompleteListener { task ->
            loading.visibility = View.GONE
            if (task.isSuccessful) {
                finish()
                startActivity(Intent(this@UserSignin, StartPage::class.java))
            }
            else Toast.makeText(applicationContext, getString(R.string.error0), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
