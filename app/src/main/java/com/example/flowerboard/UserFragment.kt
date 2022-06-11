package com.example.flowerboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flowerboard.adapter.UserProductAdapter
import com.example.flowerboard.databinding.FragmentUserBinding
import com.example.flowerboard.model.modelProduct
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UserFragment : Fragment {

    //View binding fragment
    private lateinit var binding: FragmentUserBinding

    public companion object{
        private const val TAG ="PRODUCTS_USER"

        //Collect data from activity to load products
        public fun newInstance(categoryId: String, category: String, uid:String): UserFragment{
            val fragment = UserFragment()
            //put data to bundle intent
            val args = Bundle()
            args.putString("categoryId", categoryId)
            args.putString("category", category)
            args.putString("uid", uid)
            fragment.arguments = args
            return fragment
        }
    }

    private var categoryId = ""
    private var category = ""
    private var uid = ""

    //Array list to hold products information
    private lateinit var productArray: ArrayList<modelProduct>
    private lateinit var adapterPdfUser: UserProductAdapter

    constructor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Get arguments in new Instance method
        val args = arguments
        if( args != null){
            categoryId = args.getString("categoryId")!!
            category = args.getString("category")!!
            uid = args.getString("uid")!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        // Inflate layout for fragment
        binding = FragmentUserBinding.inflate(LayoutInflater.from(context), container, false)

        //Based on category to load product data
        Log.d(TAG, "onCreateView: Category: $category")
        if(category == "All"){
            //Load all products
            loadAll()
        }
        else{
            //Load user selected products
            loadSelected()
        }
        return binding.root
    }

    private fun loadAll() {

        //Initialize the array list
        productArray = ArrayList()
        val cat = FirebaseDatabase.getInstance().getReference("FlowerBoards")
        cat.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                //Clear the product array list
                productArray.clear()
                for (ds in snapshot.children){

                    //Get the value from model product
                    val model = ds.getValue(modelProduct::class.java)

                    //Add value to the array list
                    productArray.add(model!!)
                }

                //Setup the user product adapter
                adapterPdfUser = UserProductAdapter(context!!, productArray)

                //Set adapter to recycle view
                binding.productsRv.adapter = adapterPdfUser
            }

            override fun onCancelled(error: DatabaseError) {
                //Cancel
            }
        })
    }

    private fun loadSelected() {

        //Initialize list
        productArray = ArrayList()
        val cat = FirebaseDatabase.getInstance().getReference("FlowerBoards")
        cat.orderByChild("categoryId").equalTo(categoryId)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    //Clear the product array list
                    productArray.clear()
                    for (ds in snapshot.children){

                        //Get value from model product
                        val model = ds.getValue(modelProduct::class.java)

                        //Add value to the product array list
                        productArray.add(model!!)
                    }

                    //Setup adapter
                    adapterPdfUser = UserProductAdapter(context!!, productArray)

                    //Set adapter to recycle view
                    binding.productsRv.adapter = adapterPdfUser
                }
                override fun onCancelled(error: DatabaseError) {
                    //Cancel
                }
        })
    }

}