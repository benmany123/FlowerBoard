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

    //viewbinding
    private lateinit var binding: ActivityLoginBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //init progress dialog, will show while login user
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click, not have ac
        binding.signUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        //hadnle click, begin login
        binding.loginButton.setOnClickListener {
            /*Steps
            * 1. Input data
            * 2. Validate data
            * 3. Login - firebase auth
            * 4. Check user type - firebase auth
            *   if user - move to user page
            *   if admin - move to admin page*/
            validateData()
        }


    }

    private var email=""
    private var password =""

    private fun validateData() {
        //1. Input data
        email= binding.emailEt.text.toString().trim()
        password=binding.passwordEt.text.toString().trim()

        //2. Validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Invalid email format...", Toast.LENGTH_SHORT).show()
        }
        else if(password.isEmpty()){
            Toast.makeText(this,"Enter password...", Toast.LENGTH_SHORT).show()
        }
        else{
            loginUser()
        }
    }

    private fun loginUser() {
        //3. Login - Firebase Auth

        //show progress
        progressDialog.setMessage("Logging In...")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //login success
                checkUser()
            }
            .addOnFailureListener { e->
                //failed login
                progressDialog.dismiss()
                Toast.makeText(this,"Login Failed ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun checkUser() {
        /* 4. Check user type - firebase auth
        *   if user - move to user page
        *   if admin - move to admin page*/

        progressDialog.setMessage("Checking User...")

        val firebaseUser = firebaseAuth.currentUser!!

        val ref=FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()

                    //get user tpye eg user or admin
                    val userType =snapshot.child("userType").value
                    if(userType =="user"){
                        //simple user, open user page
                        startActivity(Intent(this@LoginActivity, UserActivity::class.java))
                        finish()
                    }
                    else if (userType=="admin"){
                        //admin, open admin page
                        startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}