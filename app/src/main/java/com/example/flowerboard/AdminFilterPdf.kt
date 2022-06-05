package com.example.flowerboard

import android.widget.Filter

//Filter data from recycleview | search pdf from pdf list in recycleview
class AdminFilterPdf: Filter {
    //search array list
    var filterList: ArrayList<modelPdf>
    //adapter in which filter need to be implemented
    var AdminPdfAdapter:  AdminPdfAdapter

    //constructor
    constructor(filterList: ArrayList<modelPdf>, AdminPdfAdapter: AdminPdfAdapter) {
        this.filterList = filterList
        this.AdminPdfAdapter = AdminPdfAdapter
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint:CharSequence? = constraint //value to search
        var results = FilterResults()
        //value to be searched should not be null and not empty
        if(constraint != null && constraint.isNotEmpty()){
            //change to upper case, or lower case to avoid case sensitivity
            constraint = constraint.toString().lowercase()
            var filterModels = ArrayList<modelPdf>()
            for (i in filterList.indices){
                //validate if match
                if(filterList[i].title.lowercase().contains(constraint)){
                    //searched value is sumilar to value in list , add to filtered list
                    filterModels.add(filterList[i])
                }
            }
            results.count = filterModels.size
            results.values = filterModels
        }
        else{
            //searched value is either null or empty, return all data
            results.count = filterList.size
            results.values = filterList
        }

        return results
    }

    override fun publishResults(constraint: CharSequence, results: FilterResults) {
        //apply filter changes
        AdminPdfAdapter.pdfArrayList = results.values as ArrayList<modelPdf> /* = java.util.ArrayList<com.example.flowerboard.modelPdf> */

        //notify changes
        AdminPdfAdapter.notifyDataSetChanged()

    }
}