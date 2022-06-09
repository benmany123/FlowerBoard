package com.example.flowerboard

import android.app.Application
import android.text.format.DateFormat
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class MyApplication:Application() {

    companion object{

        //Convert timestamp to dd/mm/yyyy date format
        fun formatTimeStamp(timestamp: Long) : String{

            val date = Calendar.getInstance(Locale.ENGLISH)
            date.timeInMillis = timestamp
            return DateFormat.format("dd/MM/yyyy", date).toString()
        }
        fun loadProduct(url: String, preview: PDFView, progressBar: ProgressBar){

            //Get the product url from firebase
            val product = FirebaseStorage.getInstance().getReferenceFromUrl(url)
            product.getBytes(10000000)
                .addOnSuccessListener {bytes->
                    //Set to product pdf preview
                    preview.fromBytes(bytes).pages(0)
                        .onLoad{
                            //After loading, make the progress bar invisible
                            progressBar.visibility = View.INVISIBLE
                        }
                        .load()
                }
                .addOnFailureListener {
                }
        }
        fun loadCategory(categoryId: String, categoryTv:TextView){

            //Get the category data from firebase
            val categories = FirebaseDatabase.getInstance().getReference("Categories")
            categories.child(categoryId)
                .addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        //Obtain the category value
                        val category = "${snapshot.child("category").value}"
                        //Place the category to the category text view
                        categoryTv.text = category
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }
}