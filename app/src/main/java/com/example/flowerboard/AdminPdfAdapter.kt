package com.example.flowerboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.flowerboard.databinding.RowPdfAdminBinding

class AdminPdfAdapter :RecyclerView.Adapter<AdminPdfAdapter.AdminPdfHolder>{//, Filterable{

    // context
    private var context: Context
    //array to hold pdfs
    public var pdfArrayList: ArrayList<modelPdf>
    private val filterList: ArrayList<modelPdf>

    //view binding
    private lateinit var binding: RowPdfAdminBinding

    //filter object
    //var filter: AdminFilterPdf? = null

    //constructor
    constructor(context: Context, pdfArrayList: ArrayList<modelPdf>) : super() {
        this.context = context
        this.pdfArrayList = pdfArrayList
        this.filterList = pdfArrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminPdfHolder {
        //bind/inflate layout row_pdf_admin.xml
        binding = RowPdfAdminBinding.inflate(LayoutInflater.from(context),parent, false)

        return AdminPdfHolder(binding.root)
    }

    override fun onBindViewHolder(holder: AdminPdfHolder, position: Int) {
        //Get data, set data, handle click etc

        //get data
        val model = pdfArrayList[position]
        val pdfId = model.id
        val categoryId = model.categoryId
        val title = model.title
        val description = model.description
        val pdfUrl = model.url
        val timestamp = model.timestamp
        //convert timestamp to dd/mm/yy format
        val formattedDate = MyApplication.formatTimeStamp(timestamp)

        //set data
        holder.titleTv.text = title
        holder.descriptionTv.text = description
        holder.dateTv.text = formattedDate

        //load further details like category, pdf from url, pdf size

        //load category id
        MyApplication.loadCategory(categoryId = categoryId, holder.categoryTv)

        //pass null for page number || load pdf thumbnail
        MyApplication.loadPdf(pdfUrl, title, holder.pdfView, holder.progressBar, null)
    }

    override fun getItemCount(): Int {
        return pdfArrayList.size //items count
    }

    //View Holder class for row_pdf_admin.xml
    inner class AdminPdfHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //UI Views of row_pdf_admin.xml
        val pdfView = binding.ImageView
        val progressBar = binding.progressBar
        val titleTv = binding.titleTv
        val descriptionTv = binding.descriptionTv
        val categoryTv = binding.categoryTv
        //val sizeTv = binding.sizeTv
        val dateTv = binding.dateTv
        //val moreBtn = binding.moreButton
    }
}