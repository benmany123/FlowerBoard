package com.example.flowerboard.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.flowerboard.activity.ProductListAdminActivity
import com.example.flowerboard.databinding.RowCategoryBinding
import com.example.flowerboard.model.modelCategory
import com.google.firebase.database.FirebaseDatabase

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        //Bind layout file
        binding= RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderCategory(binding.root)
    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {

        //Get the category data
        val model = categoryArrayList[position]
        val id = model.id
        val category = model.category

        //Set the category info to the layout element
        holder.categoryTv.text = category
        holder.deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete category")
                .setMessage("Do you want to delete this category?")
                .setPositiveButton("Yes"){ _, _->
                    Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show()
                    deleteCat(model)
                }
                .setNegativeButton("No"){a,_ ->
                    a.dismiss()
                }
                .show()
        }

        //set the product list click action
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductListAdminActivity::class.java)
            intent.putExtra("categoryId", id)
            intent.putExtra("category", category)
            context.startActivity(intent)
        }
    }

    private fun deleteCat(model: modelCategory){

        //Category id
        val id = model.id
        //Get the category data from firebase
        val cat = FirebaseDatabase.getInstance().getReference("Categories")
        cat.child(id).removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Deleted category success...", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Unable to delete", Toast.LENGTH_SHORT).show()
            }
    }

    //Get number of products records
    override fun getItemCount(): Int {
        return categoryArrayList.size
    }

    //ViewHolder for layout file
    inner class HolderCategory(itemView: View): RecyclerView.ViewHolder(itemView){
        //Initialize layout components of row_category
        var categoryTv: TextView = binding.categoryTv
        var deleteButton: ImageButton = binding.deleteButton
    }
}