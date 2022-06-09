package com.example.flowerboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flowerboard.MyApplication
import com.example.flowerboard.databinding.RowProductAdminBinding
import com.example.flowerboard.model.modelProduct

class AdminProductAdapter :RecyclerView.Adapter<AdminProductAdapter.ProductAdminHolder>{

    //Context
    private var context: Context
    //Arraylist to hold products
    private var productArray: ArrayList<modelProduct>
    //View binding layout file
    private lateinit var binding: RowProductAdminBinding

    constructor(context: Context, productArray: ArrayList<modelProduct>) : super() {
        this.context = context
        this.productArray = productArray
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdminHolder {
        //Bind layout file
        binding = RowProductAdminBinding.inflate(LayoutInflater.from(context),parent, false)
        return ProductAdminHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ProductAdminHolder, p: Int) {

        //Get the product data
        val model = productArray[p]
        val categoryId = model.categoryId
        val productTitle = model.title
        val productDescription = model.description
        val productUrl = model.url
        val timestamp = model.timestamp
        val productPrice = model.price
        val createdDate = MyApplication.formatTimeStamp(timestamp)

        //Set the product data to the layout element
        holder.titleTv.text = productTitle
        holder.descriptionTv.text = productDescription
        holder.dateTv.text = createdDate
        holder.priceTv.text = productPrice

        //Load Category
        MyApplication.loadCategory(categoryId = categoryId, holder.categoryTv)

        //Load product preview
        MyApplication.loadProduct(productUrl, holder.pdfView, holder.progressBar)
    }


    //Get number of products records
    override fun getItemCount(): Int {
        return productArray.size
    }

    //ViewHolder for layout file
    inner class ProductAdminHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //Initialize layout components of row_product_admin
        val pdfView = binding.ImageView
        val progressBar = binding.progressBar
        val titleTv = binding.titleTv
        val descriptionTv = binding.descriptionTv
        val categoryTv = binding.categoryTv
        val dateTv = binding.dateTv
        var priceTv = binding.priceTv
    }
}