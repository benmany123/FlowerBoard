package com.example.flowerboard

import android.widget.Filter

class FilterCat: Filter {

    //array list in which we want to search
    private var filterList: ArrayList<modelCat>

    //adapter in which filter need to be implement
    private var adapterCat: AdapterCat

    //constructor
    constructor(filterList: ArrayList<modelCat>, adapterCat: AdapterCat): super() {
        this.filterList = filterList
        this.adapterCat = adapterCat
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()

        //value should not be null and not empty
        if(constraint != null && constraint.isNotEmpty()){
            //search value is nor null not empty

            //change to upper case, or lower case to avoid case sensitive
            constraint = constraint.toString().uppercase()
            val filteredModel: ArrayList<modelCat> = ArrayList()
            for( i in 0 until filterList.size){
                //validate
                if(filterList[i].category.uppercase().contains(constraint)){
                    //add to filtered list
                    filteredModel.add(filterList[i])
                }
            }
            results.count = filteredModel.size
            results.values = filteredModel

        }
        else{
            //search value is either null or empty
            results.count = filterList.size
            results.values = filterList
        }

        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        // apply filter changes
        adapterCat.categoryArrayList = results.values as ArrayList<modelCat> /* = java.util.ArrayList<com.example.flowerboard.modelCat> */

        // notify changes
        adapterCat.notifyDataSetChanged()
    }


}