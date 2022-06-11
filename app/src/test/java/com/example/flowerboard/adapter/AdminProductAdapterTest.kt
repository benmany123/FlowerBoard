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

class AdminProductAdapter : RecyclerView.Adapter<AdminProductAdapter.ProductAdminHolder>{

    private var context: Context
    private var productArray: ArrayList<modelProduct>
    private lateinit var binding: RowProductAdminBinding

    constructor(context: Context, productArray: ArrayList<modelProduct>) : super() {
        this.context = context
        this.productArray = productArray
    }

    @Test
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdminHolder {
        binding = RowProductAdminBinding.inflate(LayoutInflater.from(context),parent, false)
        return ProductAdminHolder(binding.root)
        assertTrue(ProductAdminHolder(binding.root).isRecyclable)
    }

    @Test
    override fun onBindViewHolder(holder: ProductAdminHolder, p: Int) {

        //Get the product data
        val model = productArray[p]
        val categoryId = model.categoryId
        val productUrl = model.url

        //Load Category
        MyApplication.loadCategory(categoryId = categoryId, holder.categoryTv)

        //Load product preview
        MyApplication.loadProduct(productUrl, holder.pdfView, holder.progressBar)
        assertEquals("https://firebasestorage.googleapis.com/v0/b/flowerbroad-35a0a.appspot.com/o/FlowerBoards%2F1654531492000?alt=media&token=8c3f45e4-1f9b-4de5-b7c8-e941d0cec37a",productUrl)
        assertEquals("1654312526720",categoryId)
    }

    @Test
    //Get number of products records
    override fun getItemCount(): Int {
        return productArray.size
        assertTrue(productArray.isEmpty())
    }

    //ViewHolder for layout file
    inner class ProductAdminHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //Initialize layout components of row_product_admin
        val pdfView = binding.ImageView
        val progressBar = binding.progressBar
        val categoryTv = binding.categoryTv
    }
}