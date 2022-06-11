package com.example.flowerboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flowerboard.databinding.RowCategoryBinding
import com.example.flowerboard.model.modelCategory
import com.google.firebase.database.FirebaseDatabase
import org.junit.Assert.*
import org.junit.Test

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.HolderCategory>{//, Filterable{

    //Context
    private val context: Context
    //Arraylist to hold categories
    private var categoryArrayList: ArrayList<modelCategory>
    private lateinit var binding: RowCategoryBinding

    //constructor
    constructor(context: Context, categoryArrayList: ArrayList<modelCategory>) {
        this.context = context
        this.categoryArrayList = categoryArrayList
    }

    @Test
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        //Bind layout file
        binding= RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderCategory(binding.root)
        assertTrue(HolderCategory(binding.root).isRecyclable)
    }

    @Test
    override fun onBindViewHolder(holder: HolderCategory, position: Int) {

        //Get the category data
        val model = categoryArrayList[position]
        val id = model.id
        val category = model.category

        assertEquals("Weddings",category)
        assertEquals("1654531492000",id)
    }

    @Test
    private fun deleteCat(model: modelCategory){
        //Category id
        val id = model.id
        //Get the category data from firebase
        val cat = FirebaseDatabase.getInstance().getReference("Categories")
        cat.child(id).removeValue()
        assertTrue(cat.child(id).removeValue().isSuccessful)
    }

    @Test
    //Get number of products records
    override fun getItemCount(): Int {
        return categoryArrayList.size
        assertTrue(categoryArrayList.isEmpty())
    }

    //ViewHolder for layout file
    inner class HolderCategory(itemView: View): RecyclerView.ViewHolder(itemView){

    }
}