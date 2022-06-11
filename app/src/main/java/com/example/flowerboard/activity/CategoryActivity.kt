package com.example.flowerboard.activity
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.flowerboard.databinding.ActivityCategoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CategoryActivity : AppCompatActivity() {

    //View binding
    private lateinit var binding: ActivityCategoryBinding

    //Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //Progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //Progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Processing...")
        progressDialog.setCanceledOnTouchOutside(false)

        //Back button
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        //Submit button
        binding.submitButton.setOnClickListener {
            validateData()
        }
    }

    private var category =""
    private fun validateData() {

        //Capture the user input data from layout view components
        category = binding.categoryEdit.text.toString().trim()

        //Check empty category input
        if(category.isEmpty()){
            Toast.makeText(this, "Please enter category", Toast.LENGTH_SHORT).show()
        }
        else{
            //Add data to firebase
            addCategoryFirebase()
        }
    }

    private fun addCategoryFirebase() {
        //show progress
        progressDialog.show()

        //Timestamp
        val timestamp = System.currentTimeMillis()

        //Setup data to add in firebase db
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$timestamp"
        hashMap["category"] = category
        hashMap["timestamp"] = timestamp
        hashMap["uid"] = "${firebaseAuth.uid}"

        //add to firebase db : Database Root > Categories > categoryID > Info
        val cat = FirebaseDatabase.getInstance().getReference("Categories")
        cat.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                //Successful
                progressDialog.dismiss()
                Toast.makeText(this, "Category added Successfully...", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                //Failed
                progressDialog.dismiss()
                Toast.makeText(this, "Category failed to add", Toast.LENGTH_SHORT).show()

            }

    }
}