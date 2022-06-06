package com.example.flowerboard

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.flowerboard.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityRegisterBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //init progress dialog, will show while create ac
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle back
        binding.backButton.setOnClickListener {
            onBackPressed() // goto previous screen
        }

        //handle click, begin register
        binding.registerButton.setOnClickListener {
            /*Steps
            1. Input data
            2. validate data
            3. create account - firebase auth
            4. Save user info - firebase realtime database*/
            validateData()
        }

    }

    private var name=""
    private var email=""
    private var password=""

    private fun validateData() {
        //1. Input data
        name = binding.nameEdit.text.toString().trim()
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEdit.text.toString().trim()
        val cPassword = binding.confirmPasswordEt.text.toString().trim()

        //2. Validate data
        if(name.isEmpty()){
            //empty name..
            Toast.makeText(this,"Enter your name", Toast.LENGTH_SHORT).show()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email pattern
            Toast.makeText(this,"Invalid Email Pattern...", Toast.LENGTH_SHORT).show()
        }
        else if(password.isEmpty()){
            //empty pw
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
        }
        else if(cPassword.isEmpty()){
            //empty password
            Toast.makeText(this,"Confirm password...", Toast.LENGTH_SHORT).show()
        }
        else if (password !=cPassword){
            Toast.makeText(this,"Password doesn't match...", Toast.LENGTH_SHORT).show()
        }
        else{
            createUserAccount()
        }
    }

    private fun createUserAccount() {
       //3. Create account

        //show progress
        progressDialog.setMessage("Creating account...")
        progressDialog.show()

        //create user in firebase auth
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //account created
                updateUserInfo()

            }
            .addOnFailureListener { e->
                //Failed creating account
                progressDialog.dismiss()
                Toast.makeText(this,"Failed creating account due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserInfo() {
        //4. Save user info - firebase realtime database

        progressDialog.setMessage("Saving user info...")

        //timestamp
        val timestamp= System.currentTimeMillis()

        //get current user uid, since user is registered so we can get it now
        val uid = firebaseAuth.uid

        //setup data to add in db
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["email"] = email
        hashMap["name"] = name
        hashMap["profileImage"] = "" // add empty, will do in profile edit
        hashMap["userType"] = "user" //possible values are user/admin, change in firebase
        hashMap["timestamp"] = timestamp

        //set data to db
        val ref=FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                //user info saved
                progressDialog.dismiss()
                Toast.makeText(this,"Account Created...", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, UserActivity::class.java))
                finish()
            }
            .addOnFailureListener { e->
                //failed add data to db
                progressDialog.dismiss()
                Toast.makeText(this,"Failed saving user info due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}