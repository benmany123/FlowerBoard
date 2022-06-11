package com.example.flowerboard.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.flowerboard.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    //View binding layout file
    private lateinit var binding: ActivityLoginBinding

    //Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //Initialize progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait a second")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click, not have ac
        binding.signUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        //handle click, begin login
        binding.loginButton.setOnClickListener {
            validateData()
        }


    }

    private var email=""
    private var password =""

    private fun validateData() {
        //Capture the user input data from layout view components
        email= binding.emailEt.text.toString().trim()
        password=binding.passwordEt.text.toString().trim()

        //Validate the email format
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Please enter a valid email format", Toast.LENGTH_SHORT).show()
        }
        else if(password.isEmpty()){
            Toast.makeText(this,"Please enter password", Toast.LENGTH_SHORT).show()
        }
        else{
            loginUser()
        }
    }

    private fun loginUser() {
        //Progress Dialog
        progressDialog.setMessage("Processing login")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //Success login
                checkUser()
            }
            .addOnFailureListener {
                //Failed login
                progressDialog.dismiss()
                Toast.makeText(this,"Login failed", Toast.LENGTH_SHORT).show()
            }

    }

    private fun checkUser() {

        //Checking message
        progressDialog.setMessage("Checking User...")

        val firebaseUser = firebaseAuth.currentUser!!
        val ref=FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()

                    //Get user type
                    val userType =snapshot.child("userType").value

                    // If the user type is user, the page will move to user activity layout
                    if(userType =="user"){
                        startActivity(Intent(this@LoginActivity, UserActivity::class.java))
                        finish()
                    }

                    // If the user type is admin, the page will move to admin activity layout
                    else if (userType=="admin"){
                        startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
                        finish()
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}