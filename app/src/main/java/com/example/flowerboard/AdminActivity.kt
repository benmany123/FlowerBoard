package com.example.flowerboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.flowerboard.databinding.ActivityAdminBinding
import com.example.flowerboard.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class AdminActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityAdminBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth


    //arraylist to hold categories
    private lateinit var categoryArrayList: ArrayList<modelCat>
    //adapter
    private lateinit var adapterCat: AdapterCat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        loadCategories()

        //searach
        /*binding.searchEdit.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //called as and when user type anything
                try{
                    adapterCat.filter.filter(s)
                }
                catch (e: Exception){

                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })*/

        //handle click. logout
        binding.logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

        //handle click, start add category page
        binding.addCategoryButton.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }

        //handle click, start add pdf page
        binding.addProductButton.setOnClickListener {
            startActivity(Intent(this, AddPdfActivity::class.java))
        }


    }

    private fun loadCategories() {
        //init arraylist
        categoryArrayList = ArrayList()

        //get all categories from firebase db > categories
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear list before start adding data into it
                categoryArrayList.clear()
                for(ds in snapshot.children){
                    //get data as model
                    val model = ds.getValue(modelCat::class.java)

                        //add to arraylist
                    categoryArrayList.add(model!!)
                }
                //setup adapter
                adapterCat = AdapterCat(this@AdminActivity, categoryArrayList)
                //set adapter to recylerview
                binding.categoriesRv.adapter = adapterCat
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun checkUser() {
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null){
            //not logged in, goto main screen
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else{
            //logged in, get and show user info
            val email =firebaseUser.email
            //set to textview of toolbar
            binding.adminEmail.text=email
        }
    }
}