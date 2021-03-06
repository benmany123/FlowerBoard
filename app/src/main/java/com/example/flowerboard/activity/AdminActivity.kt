package com.example.flowerboard.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.flowerboard.adapter.CategoryAdapter
import com.example.flowerboard.databinding.ActivityAdminBinding
import com.example.flowerboard.model.modelCategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminActivity : AppCompatActivity() {

    //View binding
    private lateinit var binding: ActivityAdminBinding

    //Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth


    //Category array list
    private lateinit var categoryArray: ArrayList<modelCategory>
    //Adapter
    private lateinit var adapterCat: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        loadCategories()

        //Logout button
        binding.logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

        //Add category button
        binding.addCategoryButton.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }

        //Add product
        binding.addProductButton.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }
    }

    private fun loadCategories() {
        //Initialize arraylist
        categoryArray = ArrayList()

        //Get categories from firebase
        val cat = FirebaseDatabase.getInstance().getReference("Categories")
        cat.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                //Clear the category array list
                categoryArray.clear()
                for(ds in snapshot.children){
                    //Get value from model category
                    val model = ds.getValue(modelCategory::class.java)

                    //Add to arraylist
                    categoryArray.add(model!!)
                }
                //Setup category adapter
                adapterCat = CategoryAdapter(this@AdminActivity, categoryArray)
                //Set adapter to recylerview
                binding.categoriesRv.adapter = adapterCat
            }
            override fun onCancelled(error: DatabaseError) {
                //Cancel
            }
        })
    }

    private fun checkUser() {

        //Get current user
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null){

            //Without login, move to main activity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else{
            //With login, set the layout to admin email account
            val email =firebaseUser.email
            binding.adminEmail.text=email
        }
    }
}