package com.example.flowerboard.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.flowerboard.MyApplication
import com.example.flowerboard.R
import com.example.flowerboard.databinding.ActivityAccountEditBinding
import com.example.flowerboard.databinding.ActivityAccountInfoBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception

class AccountEditActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityAccountEditBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //Image uri which we will pick
    private var imageUri: Uri?= null

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        loadAccountInfo()

        //handle click, go back
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        //handle click, pick image from camera/ gallery
        binding.accountInfoTv.setOnClickListener {
            showImagePopup()
        }

        //handle clikc, begin update account information
        binding.updateButton.setOnClickListener {
            validateData()
        }

    }

    private var name = ""

    private fun validateData() {
        //get data
        name = binding.nameEdit.text.toString().trim()

        //validate data
        if(name.isEmpty()){
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show()
        }
        else{
            //name is entered
            if(imageUri == null){
                //update without image
                updateName("")

            }else{
                //update with image
                updateImage()
            }

        }

    }

    private fun updateImage() {
        progressDialog.setMessage("Updating account icon")
        progressDialog.show()

        //image path and name, use uid to replace previous
        val file = "ProfileImages/" + firebaseAuth.uid

        //storage reference
        val reference = FirebaseStorage.getInstance().getReference(file)
        reference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                //image uploaded, get irl; pf uploaded image
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while(!uriTask.isSuccessful);
                val uploadURL= "${uriTask.result}"

                updateName(uploadURL)
            }
            .addOnFailureListener {
                //failed to upload image
                progressDialog.dismiss()
                Toast.makeText(this,"Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateName(update: String) {
        progressDialog.setMessage("Updating account information")

        //setup info to update to db
        val hashMap: HashMap<String, Any> =HashMap()
        hashMap["name"] = "$name"
        if(imageUri != null){
            hashMap["profileImage"] = update
        }

        //update to db
        val reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                //update account information
                progressDialog.dismiss()
                Toast.makeText(this,"Update successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                //failed to upload image
                progressDialog.dismiss()
                Toast.makeText(this,"Failed to update account", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadAccountInfo() {
        //db reference to load account info
        val ref = FirebaseDatabase.getInstance().getReference("")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // get account information
                    //val email = "${snapshot.child("email").value}"
                    val name = "${snapshot.child("name").value}"
                    val profileImage = "${snapshot.child("profileImage").value}"
                    val timestamp = "${snapshot.child("timestamp").value}"
                    //val uid = "${snapshot.child("uid").value}"
                    //val userType = "${snapshot.child("userType ").value}"

                    //convert timestamp to proper date format
                    //val formattedDate = MyApplication.formatTimeStamp(timestamp.toLong())

                    //set data
                    binding.nameEdit.setText(name)

                    //set image
                    //add glide library to show image from firebase
                    try{
                        Glide.with(this@AccountEditActivity)
                            .load(profileImage)
                            .placeholder(R.drawable.ic_person2)
                            .into(binding.accountInfoTv)
                    }
                    catch (e: Exception){


                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun showImagePopup(){
        //Show Popupmenu with options Camera, Gallery ot pick images

        //setup popup menu
        val popup = PopupMenu(this,binding.accountInfoTv)
        popup.menu.add(Menu.NONE, 0, 0, "Camera")
        popup.menu.add(Menu.NONE, 0, 0, "Gallery")
        popup.show()

        //handle popup menu item clicl
        popup.setOnMenuItemClickListener {
            item->
            //get id of clicked item
            val id = item.itemId
            if(id==0){
                //camera click
                camera()
            }
            else if (id==1){
                //Gallery clicked
                gallery()
            }

            true

        }



    }

    private fun gallery() {
        //intent to pick image from camera
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp_Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Description")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivity.launch(intent)
    }

    private fun camera() {
        //intent to pick image from gallery
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivity.launch(intent)
    }
    private val cameraActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> {
            result->
            //used to handle result of camera intent (new way in replacement of start activity results
            //geturi of image
            if(result.resultCode==Activity.RESULT_OK){
                val data = result.data
                //imageUri = data!!.data   no need we already have image in imageUri in camera case

                //set to imageview
                binding.accountInfoTv.setImageURI(imageUri)
            }
            else{
                //cancelled
                Toast.makeText(this,"Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    )

    //used to handle result of gallery intent (new way in replacement of start activity results
    private val galleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback <ActivityResult>{
            result->
            //geturi of image
            if(result.resultCode==Activity.RESULT_OK){
                val data = result.data
                imageUri = data!!.data
                //set to imageview
                binding.accountInfoTv.setImageURI(imageUri)
            }
            else{
                //cancelled
                Toast.makeText(this,"Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    )
}