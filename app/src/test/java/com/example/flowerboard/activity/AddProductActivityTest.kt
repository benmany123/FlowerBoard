package com.example.flowerboard.activity

import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.flowerboard.databinding.ActivityAddProductBinding
import com.example.flowerboard.model.modelCategory
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import org.junit.Assert.*

class AddProductActivity : AppCompatActivity() {

    //View binding
    private lateinit var binding: ActivityAddProductBinding

    //Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //Progress dialog
    private lateinit var progressDialog: ProgressDialog

    //Category array list
    private lateinit var categoryArrayList: ArrayList<modelCategory>

    //Product pdf uri
    private var pdfUri: Uri? = null

    //TAG
    private val TAG ="PDF_ADD_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        loadPdfCategories()

        //Setup progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //Back button
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        //Category button
        binding.categoryTv.setOnClickListener {
            categoryPickDialog()
        }

        //Product image button
        binding.ImageAttach.setOnClickListener {
            pdfPickIntent()
        }

        //Add product to database
        binding.submitButton.setOnClickListener {
            validateData()
        }
    }

    private var title = ""
    private var description = ""
    private var category = ""
    private var price = ""


    private fun validateData() {
        //1. Validate data
        Log.d(TAG, "validateData: Validating data")

        //Capture the user input data from layout view components
        title = binding.titleEdit.text.toString().trim()
        description = binding.descriptionEdit.text.toString().trim()
        category = binding.categoryTv.text.toString().trim()
        price = binding.priceEdit.text.toString().trim()

        //Validate user input data
        if (title.isEmpty()){
            Toast.makeText(this, "Please enter Title", Toast.LENGTH_SHORT).show()
        }
        else if(description.isEmpty()){
            Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show()
        }
        else if (category.isEmpty()){
            Toast.makeText(this, "Please enter category", Toast.LENGTH_SHORT).show()
        }
        else if (pdfUri == null){
            Toast.makeText(this, "Pleas add image", Toast.LENGTH_SHORT).show()
        }
        else{
            //data validated, begin upload
            uploadPdf()
        }
    }

    private fun uploadPdf() {
        //Upload product pdf to firebase storage
        Log.d(TAG, "Uploading product")

        //show progress dialog
        progressDialog.setMessage("Uploading PDF.")
        progressDialog.show()

        //timestamp
        val timestamp = System.currentTimeMillis()
        //path of pdf in firebase storage
        val filePathAndName = "FlowerBoards/$timestamp"

        //storage reference
        val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageReference.putFile(pdfUri!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.d(TAG, "uploadPdf: PDF upload now getting url...")

                //Get url of uploaded pdf
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadPdfUrl = "${uriTask.result}"

                uploadPdfDB(uploadPdfUrl, timestamp)
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to upload")
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to upload", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadPdfDB(uploadPdfUrl: String, timestamp: Long) {
        //4. Upload PDF info to firebase db
        Log.d(TAG, "uploadPdfDB: uploading to db")
        progressDialog.setMessage("Uploading pdf Info...")

        //uid of current user
        val uid = firebaseAuth.uid

        //setup data to upload
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["uid"] = "$uid"
        hashMap["id"] = "$timestamp"
        hashMap["title"] = "$title"
        hashMap["description"] = "$description"
        hashMap["categoryId"] = "$selectedCategoryId"
        hashMap["url"] = "$uploadPdfUrl"
        hashMap["timestamp"] = timestamp
        hashMap["price"] = "$price"

        //db reference DB > Flower Boards > Product ID > Flower Board Info
        val ref = FirebaseDatabase.getInstance().getReference("FlowerBoards")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "uploadPdfDB: Uploaded to db")
                progressDialog.dismiss()
                Toast.makeText(this, "Uploaded...", Toast.LENGTH_SHORT).show()
                pdfUri = null
            }
            .addOnFailureListener {e->
                Log.d(TAG, "uploadPdfDB: failed to upload due to ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to upload due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadPdfCategories() {
        Log.d(TAG, "loadPdfCategories: Loading PDF categories")
        //init arraylist
        categoryArrayList = ArrayList()

        //db reference to load categories DF > Categories
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear list before adding data
                categoryArrayList.clear()
                for(ds in snapshot.children){
                    //get data
                    val model = ds.getValue(modelCategory::class.java)
                    //add to array list
                    categoryArrayList.add(model!!)
                    Log.d(TAG, "onDataChange: ${model.category}")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                //Cancel
            }
        })
    }

    private var selectedCategoryId =""
    private var selectedCategoryTitle =""

    private fun categoryPickDialog(){
        Log.d(TAG, "categoryPickDialog: Showing pdf category pick dialog")

        //get string array of categories from arraylist
        val categoriesArray = arrayOfNulls<String>(categoryArrayList.size)
        for (i in categoryArrayList.indices){
            categoriesArray[i] = categoryArrayList[i].category
        }

        //alert dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Category")
            .setItems(categoriesArray){dialog, which ->
                //handle item click
                //get clicked item
                selectedCategoryTitle = categoryArrayList[which].category
                selectedCategoryId = categoryArrayList[which].id
                //set category to text
                binding.categoryTv.text = selectedCategoryTitle

                Log.d(TAG, "categoryPickDialog: Selected Category ID : $selectedCategoryId")
                Log.d(TAG, "categoryPickDialog: Selected Category Title : $selectedCategoryTitle")
            }
            .show()
    }

    private fun pdfPickIntent(){
        Log.d(TAG, "pdfPickIntent: starting pdf pick intent")

        val intent = Intent()
        //Select pdf file from mobile local storage
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        pdfActivityResultLauncher.launch(intent)

    }

    val pdfActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult>{ result ->
            if(result.resultCode == RESULT_OK){
                Log.d(TAG, "PDF Picked:: ")
                pdfUri = result.data!!.data
            }
            else{
                Log.d(TAG, "PDF Pick cancelled: ")
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }


    )
}