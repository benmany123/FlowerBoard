package com.example.flowerboard
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.flowerboard.databinding.ActivityAdminBinding
import com.example.flowerboard.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class AdminActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityAdminBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle click. logout
        binding.logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

        //handle click, start add category page
        binding.addCategoryButton.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }
    }

    private fun checkUser() {
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null){
            //not logged in, goto main screen
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else{
            //logged in, get and show user info
            val email =firebaseUser.email
            //set to textview of toolbar
            binding.subTitleTv.text=email
        }
    }
}