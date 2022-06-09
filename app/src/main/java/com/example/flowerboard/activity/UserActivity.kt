package com.example.flowerboard.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.flowerboard.UserFragment
import com.example.flowerboard.databinding.ActivityUserBinding
import com.example.flowerboard.model.modelCategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserActivity : AppCompatActivity() {

    //View binding
    private lateinit var binding: ActivityUserBinding

    //Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //Arraylist to hold product category
    private lateinit var categoryArrayList: ArrayList<modelCategory>

    //View page adapter
    private lateinit var viewPageAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //Setup Page adapter
        setupPagerAdapter(binding.viewPager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setupPagerAdapter(viewPager: ViewPager){
        viewPageAdapter = ViewPagerAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this)

        //Initialize the category array list
        categoryArrayList = ArrayList()

        //Get the product category url from firebase
        val cat = FirebaseDatabase.getInstance().getReference("Categories")
        cat.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                //Clear the array list
                categoryArrayList.clear()

                //add data to models
                val modelAll = modelCategory("01", "All", 1, "")

                //Add model to the category array list
                categoryArrayList.add(modelAll)

                //Add view pager adapter
                viewPageAdapter.addFragment(
                    UserFragment.newInstance(
                        "${modelAll.id}",
                        "${modelAll.category}",
                        "${modelAll.uid}"
                    ), modelAll.category
                )

                //Make a refresh for the array list
                viewPageAdapter.notifyDataSetChanged()

                for(n in snapshot.children){

                    //Get the category value to model
                    val model = n.getValue(modelCategory::class.java)

                    //Add the data to category list
                    categoryArrayList.add(model!!)

                    //Add fragment to view pager adapter
                    viewPageAdapter.addFragment(
                        UserFragment.newInstance(
                            "${model.id}",
                            "${model.category}",
                            "${model.uid}"
                        ), model.category
                    )

                    //Make a refresh for the array list
                    viewPageAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Cancelled
            }
        })

        //Set the view page adapter to view pager
        viewPager.adapter =viewPageAdapter
    }

    class ViewPagerAdapter(fm: FragmentManager, behavior: Int, context: Context): FragmentPagerAdapter(fm, behavior){

        //Set the list of user fragments
        private val fragmentsList: ArrayList<UserFragment> = ArrayList()

        //Set the list of product title fragments
        private val fragmentTitleList: ArrayList<String> = ArrayList()

        //Set the context
        private val context: Context
        init{this.context = context}

        //Get number of fragments list records
        override fun getCount(): Int {return fragmentsList.size}

        override fun getItem(p: Int): Fragment {return fragmentsList[p]}

        override fun getPageTitle(p: Int): CharSequence{return fragmentTitleList[p]}

        public fun addFragment(fragment: UserFragment, title: String){

            //Add fragment to fragment list
            fragmentsList.add(fragment)
            //Add title to fragment list
            fragmentTitleList.add(title)
        }

    }
}