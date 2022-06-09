package com.example.flowerboard.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.flowerboard.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    //View binding
    private lateinit var binding: ActivityRegisterBinding

    //Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //Progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //Initialize progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //Back button
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        //Register button
        binding.registerButton.setOnClickListener {
            validation()
        }
    }

    private var name=""
    private var email=""
    private var password=""

    private fun validation() {

        //Capture the user input data from layout view components
        name = binding.nameEdit.text.toString().trim()
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEdit.text.toString().trim()
        val cPassword = binding.confirmPasswordEt.text.toString().trim()

        //Checking for the user input
        if(name.isEmpty()){
            //Empty user name
            Toast.makeText(this,"Please enter user name", Toast.LENGTH_SHORT).show()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //Invalid email format
            Toast.makeText(this,"Please check the email format", Toast.LENGTH_SHORT).show()
        }
        else if(password.isEmpty()){
            //Empty password
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
        }
        else if(cPassword.isEmpty()){
            //Empty confirm password
            Toast.makeText(this,"Please enter confirm password", Toast.LENGTH_SHORT).show()
        }
        else if (password !=cPassword){
            Toast.makeText(this,"Please make sure password and confirm password is matched", Toast.LENGTH_SHORT).show()
        }
        else{
            createAccount()
        }
    }

    //Create user account and add to firebase
    private fun createAccount() {

        //show progress
        progressDialog.setMessage("Processing...")
        progressDialog.show()


        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                updateUserInfo()
            }
            .addOnFailureListener {
                //Failed creating account
                progressDialog.dismiss()
                Toast.makeText(this,"Failed to created account", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserInfo() {

        progressDialog.setMessage("Rpocessing...")
        val timestamp= System.currentTimeMillis()
        val uid = firebaseAuth.uid
        val hashMap: HashMap<String, Any?> = HashMap()

        hashMap["uid"] = uid
        hashMap["email"] = email
        hashMap["name"] = name

        //Define the account is user/admin
        hashMap["userType"] = "user"
        hashMap["timestamp"] = timestamp

        //Set the user account information to firebase
        val user=FirebaseDatabase.getInstance().getReference("Users")
        user.child(uid!!).setValue(hashMap)
            .addOnSuccessListener {
                //Update data to firebase successful
                progressDialog.dismiss()
                Toast.makeText(this,"Account Created", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, UserActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                //Failed to update data to firebase
                progressDialog.dismiss()
                Toast.makeText(this,"Failed saving user information", Toast.LENGTH_SHORT).show()
            }
    }
}