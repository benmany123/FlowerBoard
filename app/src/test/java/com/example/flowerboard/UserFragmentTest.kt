package com.example.flowerboard

import android.os.Bundle
import com.example.flowerboard.model.modelCategory
import org.junit.Assert.*
import org.junit.Test

class UserFragmentTest{

    @Test
    fun newInstance(){
        val fragment = UserFragment()
        val args = Bundle()
        fragment.arguments = args
        assertEquals("",fragment.arguments)
    }
}