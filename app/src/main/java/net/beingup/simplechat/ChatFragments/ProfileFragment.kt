package net.beingup.simplechat.ChatFragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

import net.beingup.simplechat.R
import net.beingup.simplechat.pojo.SingleUserInfo


// Download From : https://nulledsourcecode.com/
//Contact us For Reskin And Making Money Earning Apps

class ProfileFragment : Fragment() {

    private lateinit var profile_big: CircleImageView
    private lateinit var change_profile: ImageView
    private lateinit var profile_name: TextView
    private lateinit var s_username: TextView
    private lateinit var about_me: TextView

    private val IMAGE_REQUEST = 1234
    private lateinit var myId: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profile_big = view.findViewById(R.id.profile_big) as CircleImageView
        change_profile = view.findViewById(R.id.change_profile) as ImageView
        profile_name = view.findViewById(R.id.profile_name) as TextView
        s_username = view.findViewById(R.id.s_username) as TextView
        about_me = view.findViewById(R.id.about_me) as TextView

        myId = FirebaseAuth.getInstance().currentUser!!.uid

        readProfileData()
        uploadImage()

        return view
    }

    /**
     * This function read current user information and show.
     */
    private fun readProfileData(){
        val me = FirebaseAuth.getInstance().currentUser
        val myRef = FirebaseDatabase.getInstance().reference.child(getString(R.string.f_users)).child(me!!.uid)
        myRef.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            @SuppressLint("SetTextI18n")
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val sui = p0.getValue(SingleUserInfo::class.java)
                    if (sui != null){
                        if (sui.profile_image == getString(R.string.f_default))
                            profile_big.setImageResource(R.drawable.jack)
                        else Picasso.get().load(sui.profile_image).into(profile_big)

                        profile_name.text = sui.username
                        val firstWord = sui.username.split(" ")
                        s_username.text = "@${firstWord[0]}"
                        about_me.text = "About ${sui.username}"
                    }
                    myRef.removeEventListener(this)
                }
            }

        })
    }

    /**
     * This function use for open activity and select new profile picture.
     */
    private fun uploadImage(){
        change_profile.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST)
        }
    }

    /**
     * Receive new profile image and invoke upload function.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try{
            if ((requestCode == IMAGE_REQUEST) and (data != null) and (data!!.data != null)){
                val uri = data.data
                try{
                    val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, uri)
                    profile_big.setImageBitmap(bitmap)
                    if (uri != null) {
                        uploadingImage(uri)
                    }
                }
                catch(e: Exception){
                    toast("Try again.")
                }
            }
            else toast("Image not found.")
        }
        catch(e: Exception){}
    }

    /**
     * This is the main function for upload new profile image to server.
     */
    private fun uploadingImage(imgUri: Uri){
        val pd = ProgressDialog(context)
        pd.setMessage("Uploading...")
        pd.show()

        val sr = FirebaseStorage.getInstance().reference.child("${getString(R.string.f_images)}/$myId")
        sr.putFile(imgUri)
            .addOnSuccessListener {
                sr.downloadUrl.addOnSuccessListener {link->
                    pd.dismiss()

                    val database = FirebaseDatabase.getInstance().reference.child(getString(R.string.f_users)).child(myId).child(getString(R.string.f_profile))
                    database.addValueEventListener(object: ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                            pd.dismiss()
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if(p0.exists()){
                                database.setValue(link.toString()).addOnCompleteListener{task->
                                    if (task.isSuccessful) pd.dismiss()
                                    else{
                                        pd.dismiss()
                                        toast("Upload not success. Try Again")
                                    }
                                }
                            }
                        }

                    })
                }
                    .addOnFailureListener {
                        pd.dismiss()
                        toast("Upload not success. Try Again")
                    }
            }
            .addOnFailureListener{
                pd.dismiss()
                toast("Upload not success. Try Again")
            }
            .addOnProgressListener {task->
                val progress = ((100.0*task.bytesTransferred)/task.totalByteCount).toInt()
                pd.setMessage("Uploading $progress%")
            }
    }

    private fun toast(s: String){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

}
