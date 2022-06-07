package com.example.flowerboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flowerboard.MyApplication
import com.example.flowerboard.databinding.RowPdfUserBinding
import com.example.flowerboard.model.modelPdf

class UserPdfAdapter : RecyclerView.Adapter<UserPdfAdapter.HolderPdfUser> {

    //context, get using constructor
    private var context: Context
    //arraylist to hold pdfs, get user constructor
    private var pdfArrayList: ArrayList<modelPdf>//to access in filter class, make public
    //view binding row_pdf_user.xml => RowPdfUserBinding
    private lateinit var binding: RowPdfUserBinding

    constructor(context: Context, pdfArrayList: ArrayList<modelPdf>) {
        this.context = context
        this.pdfArrayList = pdfArrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPdfUser {
        //inflate/bind layout row_pdf_user.xml
        binding = RowPdfUserBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderPdfUser(binding.root)
    }

    override fun onBindViewHolder(holder: HolderPdfUser, position: Int) {
        //Get data, set data, handle click etc

        //get data
        val model = pdfArrayList[position]
        //val productId = model.id
        val categoryId = model.categoryId
        val title = model.title
        val description = model.description
        //val uid = model.uid
        val url = model.url
        val timestamp = model.timestamp
        val price = model.price
        //convert time
        val date = MyApplication.formatTimeStamp(timestamp)

        //set data
        holder.titleTv.text = title
        holder.descriptionTv.text = description
        holder.dateTv.text = date
        holder.priceTv.text = price

        MyApplication.loadPdf(url, title, holder.imageView, holder.progressBar, null)

        MyApplication.loadCategory(categoryId, holder.categoryTv)

    }

    override fun getItemCount(): Int {
        return pdfArrayList.size // return number of records
    }

    //View holder class row_pdf_user.xml
    inner class HolderPdfUser(itemView: View): RecyclerView.ViewHolder(itemView){
        //init UI components of row_pdf_user.xml
        var imageView = binding.imageView
        var progressBar = binding.progressBar
        var titleTv = binding.titleTv
        var descriptionTv = binding.descriptionTv
        var categoryTv = binding.categoryTv
        var dateTv = binding.dateTv
        var priceTv = binding.priceTv
    }

}