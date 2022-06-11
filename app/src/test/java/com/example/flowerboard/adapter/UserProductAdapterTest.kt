package com.example.flowerboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flowerboard.MyApplication
import com.example.flowerboard.databinding.RowProductAdminBinding
import com.example.flowerboard.model.modelProduct
import org.junit.Assert.*
import org.junit.Test

class UserProductAdapter : RecyclerView.Adapter<UserProductAdapter.ProductUserHolder> {

    private var context: Context
    private var productArray: ArrayList<modelProduct>
    private lateinit var binding: RowProductAdminBinding

    constructor(context: Context, productArray: ArrayList<modelProduct>) {
        this.context = context
        this.productArray = productArray
    }

    @Test
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductUserHolder {
        //Bind layout file
        binding = RowProductAdminBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProductUserHolder(binding.root)
        assertTrue(ProductUserHolder(binding.root).isRecyclable)
    }

    @Test
    override fun onBindViewHolder(holder: ProductUserHolder, p: Int) {

        val model = productArray[p]
        val categoryId = model.categoryId
        val productUrl = model.url

        MyApplication.loadProduct(productUrl, holder.imageView, holder.progressBar)
        MyApplication.loadCategory(categoryId, holder.categoryTv)
        assertEquals("https://firebasestorage.googleapis.com/v0/b/flowerbroad-35a0a.appspot.com/o/FlowerBoards%2F1654531492000?alt=media&token=8c3f45e4-1f9b-4de5-b7c8-e941d0cec37a",productUrl)
        assertEquals("1654312526720",categoryId)
    }

    @Test
    override fun getItemCount(): Int {
        return productArray.size
        assertTrue(productArray.isEmpty())
    }

    inner class ProductUserHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imageView = binding.ImageView
        var progressBar = binding.progressBar
        var categoryTv = binding.categoryTv
    }
}