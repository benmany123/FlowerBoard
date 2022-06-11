package com.example.flowerboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flowerboard.MyApplication
import com.example.flowerboard.databinding.RowProductAdminBinding
import com.example.flowerboard.model.modelProduct

class UserProductAdapter : RecyclerView.Adapter<UserProductAdapter.ProductUserHolder> {

    //Context
    private var context: Context
    //Arraylist to hold products
    private var productArray: ArrayList<modelProduct>
    //View binding layout file
    private lateinit var binding: RowProductAdminBinding

    constructor(context: Context, productArray: ArrayList<modelProduct>) {
        this.context = context
        this.productArray = productArray
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductUserHolder {
        //Bind layout file
        binding = RowProductAdminBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProductUserHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ProductUserHolder, p: Int) {

        //get data
        val model = productArray[p]
        val categoryId = model.categoryId
        val productTitle = model.title
        val productDescription = model.description
        val productUrl = model.url
        val timestamp = model.timestamp
        val price = model.price
        val createdDate = MyApplication.formatTimeStamp(timestamp)

        //Set data
        holder.titleTv.text = productTitle
        holder.descriptionTv.text = productDescription
        holder.dateTv.text = createdDate
        holder.priceTv.text = price

        //Load Product info
        MyApplication.loadProduct(productUrl, holder.imageView, holder.progressBar)

        //Load Category
        MyApplication.loadCategory(categoryId, holder.categoryTv)
    }

    //Get number of products records
    override fun getItemCount(): Int {
        return productArray.size
    }

    //ViewHolder for layout file
    inner class ProductUserHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //Initialize layout components of row_product_user
        var imageView = binding.ImageView
        var progressBar = binding.progressBar
        var titleTv = binding.titleTv
        var descriptionTv = binding.descriptionTv
        var categoryTv = binding.categoryTv
        var dateTv = binding.dateTv
        var priceTv = binding.priceTv
    }
}