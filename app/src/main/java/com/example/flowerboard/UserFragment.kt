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

    //view binding fragment_products_user.xml => FragmentProductsUserBinding
    private lateinit var binding: FragmentUserBinding

    public companion object{
        private const val TAG ="PRODUCTS_USER_TAG"

        //receive data from activity to load products e.g. categoryId, category, uid
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
    //arraylist to hold pdfs
    private lateinit var pdfArrayList: ArrayList<modelProduct>
    private lateinit var adapterPdfUser: UserProductAdapter

    constructor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //get arguments that we passed in new Instance method
        val args = arguments
        if( args != null){
            categoryId = args.getString("categoryId")!!
            category = args.getString("category")!!
            uid = args.getString("uid")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(LayoutInflater.from(context), container, false)

        //load pdf according to category, this fragment will have new instance to load each category pdfs
        Log.d(TAG, "onCreateView: Category: $category")
        if(category == "All"){
            //load all products
            loadAll()
        }
        else{
            //load selected category products
            loadSelected()
        }

        return binding.root
    }

    private fun loadAll() {
        //init list
        pdfArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("FlowerBoards")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear list before starting adding data into it
                pdfArrayList.clear()
                for (ds in snapshot.children){
                    //get data
                    val model = ds.getValue(modelProduct::class.java)
                    //add to list
                    pdfArrayList.add(model!!)
                }
                //setup adapter
                adapterPdfUser = UserProductAdapter(context!!, pdfArrayList)
                //set adapter to recycle view
                binding.productsRv.adapter = adapterPdfUser
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }
        })
    }

    private fun loadSelected() {
        //init list
        pdfArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("FlowerBoards")
        ref.orderByChild("categoryId").equalTo(categoryId)
            .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear list before starting adding data into it
                pdfArrayList.clear()
                for (ds in snapshot.children){
                    //get data
                    val model = ds.getValue(modelProduct::class.java)
                    //add to list
                    pdfArrayList.add(model!!)
                }
                //setup adapter
                adapterPdfUser = UserProductAdapter(context!!, pdfArrayList)
                //set adapter to recycle view
                binding.productsRv.adapter = adapterPdfUser
            }
            override fun onCancelled(error: DatabaseError) {
                //
            }
        })
    }

}