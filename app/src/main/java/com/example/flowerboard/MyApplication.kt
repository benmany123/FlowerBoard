package com.example.flowerboard

import android.app.Application
import android.net.Uri
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import org.w3c.dom.Text
import java.util.*

class MyApplication:Application() {

    override fun onCreate() {
        super.onCreate()
    }
    companion object{
        //convert timestamp to proper date format
        fun formatTimeStamp(timestamp: Long) : String{
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = timestamp
            //format dd/mm/yyyy
            return DateFormat.format("dd/MM/yyyy", cal).toString()
        }

        fun loadPdf(
            pdfUrl:String,
            pdfTitle: String,
            pdfView: PDFView,
            progressBar: ProgressBar,
            pagesTv: TextView?
        ){
            val TAG = "PDF_THUMBNAIL_TAG"

            //using url we can get file and its metadata from firebase storage
            val ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl)
            ref.getBytes(Constants.Max_BYTES_PDF)
                .addOnSuccessListener {bytes->

                    Log.d(TAG, "loadPdfSize: Size Bytes $bytes")

                    //Set to pdfview
                    pdfView.fromBytes(bytes)
                        .pages(0) // show first page only
                        .spacing(0)
                        .swipeHorizontal(false)
                        .enableSwipe(false)
                        .onError { t->
                            progressBar.visibility = View.INVISIBLE
                            Log.d(TAG, "loadPdf: ${t.message}")
                        }
                        .onPageError { page, t ->
                            progressBar.visibility = View.INVISIBLE
                            Log.d(TAG, "loadPdf: ${t.message}")
                        }
                        .onLoad{nbPages ->
                            Log.d(TAG, "loadPdf: Pages $nbPages")
                            //pdf loaded, we can set page count, pdf thumbnail
                            progressBar.visibility = View.INVISIBLE

                            //if pagesTv param is not null then set page numbers
                            if(pagesTv != null){
                                pagesTv.text = "$nbPages"
                            }
                        }
                        .load()
                }
                .addOnFailureListener {e->
                    //failed to get metadata
                    Log.d(TAG, "loadPdfSize: Failed to get metadata due to${e.message}")
                }
        }



        fun loadCategory(categoryId: String, categoryTv:TextView){
            //load category using category id from firebase
            val ref = FirebaseDatabase.getInstance().getReference("Categories")
            ref.child(categoryId)
                .addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        //get category
                        val category = "${snapshot.child("category").value}"

                        //set category
                        categoryTv.text = category
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }
}