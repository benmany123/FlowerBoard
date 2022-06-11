package com.example.flowerboard

import android.text.format.DateFormat
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase
import org.junit.Assert.*
import org.junit.Test
import java.util.*


class MyApplicationTest{

    @Test
    fun formatTimeStamp(){
        val date = Calendar.getInstance(Locale.ENGLISH)
        date.timeInMillis = 1654780712771
        assertEquals("11/06/2022", DateFormat.format("dd/MM/yyyy", date).toString())
    }

    @Test
    fun loadProduct(){
        val productUrl=""
        val url = FirebaseDatabase.getInstance().getReference(productUrl)
        assertEquals("https://firebasestorage.googleapis.com/v0/b/flowerbroad-35a0a.appspot.com/o/FlowerBoards%2F1654531492000?alt=media&token=8c3f45e4-1f9b-4de5-b7c8-e941d0cec37a", url)
    }

    @Test
    fun loadCategory(categoryId: String, categoryTv: TextView){
        val categories = FirebaseDatabase.getInstance().getReference("Categories")
        categories.child(categoryId)
        assertEquals("Weddings", categories)
    }
}