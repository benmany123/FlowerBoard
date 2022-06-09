package com.example.flowerboard.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.flowerboard.adapter.AdminProductAdapter
import com.example.flowerboard.databinding.ActivityProductListAdminBinding
import com.example.flowerboard.model.modelProduct
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductListAdminActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityProductListAdminBinding

    private companion object{
        const val TAG = "PDF_LIST_ADMIN_TAG"
    }

    //Category information
    private var categoryId =""
    private var category =""

    //Arraylist to hold product
    private lateinit var pdfArrayList: ArrayList<modelProduct>

    //View page adapter
    private lateinit var adapter: AdminProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get the information from intent
        val intent = intent
        categoryId = intent.getStringExtra("categoryId")!!
        category = intent.getStringExtra("category")!!

        //Place the category to the subtitle text view
        binding.subTitleTv.text = category

        //Load products
        loadProductList()
    }

    private fun loadProductList() {
        //Initialize arraylist
        pdfArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("FlowerBoards")
        ref.orderByChild("categoryId").equalTo(categoryId)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //clear list before start adding data into it
                    pdfArrayList.clear()
                    for (ds in snapshot.children){
                        //get data
                        val model = ds.getValue(modelProduct::class.java)
                        //add to list
                        if (model != null) {
                            pdfArrayList.add(model)
                            Log.d(TAG, "onDataChange: ${model.title} ${model.categoryId}")
                        }
                    }
                    //setup adapter
                    adapter = AdminProductAdapter(this@ProductListAdminActivity, pdfArrayList)
                    binding.boardsRv.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            } )
    }
}