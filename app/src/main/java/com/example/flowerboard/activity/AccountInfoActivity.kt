package com.example.flowerboard.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import com.bumptech.glide.Glide
import com.example.flowerboard.MyApplication
import com.example.flowerboard.R
import com.example.flowerboard.databinding.ActivityAccountInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class AccountInfoActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityAccountInfoBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth =FirebaseAuth.getInstance()
        //loadAccountInfo()

        //handle click, go back
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        //handle click, open edit account information
        binding.editButton.setOnClickListener {
            startActivity(Intent(this, AccountEditActivity::class.java))
        }
    }

    /*private fun loadAccountInfo() {
        //db reference to load account info
        val ref = FirebaseDatabase.getInstance().getReference("")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    // get account information
                    val email = "${snapshot.child("email").value}"
                    val name = "${snapshot.child("name").value}"
                    val profileImage = "${snapshot.child("profileImage").value}"
                    val timestamp = "${snapshot.child("timestamp").value}"
                    val uid = "${snapshot.child("uid").value}"
                    val userType = "${snapshot.child("userType ").value}"

                    //convert timestamp to proper date format
                    //val formattedDate = MyApplication.formatTimeStamp(timestamp.toLong())

                    //set data
                    binding.nameTv.text = name
                    binding.emailTv.text = email
                    //binding.dateTv.text = email
                    binding.memberType.text = userType

                    //set image
                    //add glide library to show image from firebase
                    /*try{
                        Glide.with(this@AccountInfoActivity)
                            .load(profileImage)
                            .placeholder(R.drawable.ic_person2)
                            .into(binding.accountIv)
                    }
                    catch (e:Exception){
*/

                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }*/
}