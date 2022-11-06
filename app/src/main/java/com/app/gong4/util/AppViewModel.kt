package com.app.gong4.util

import android.util.Log
import androidx.lifecycle.ViewModel
import com.app.gong4.DTO.StudyCategory

class AppViewModel:ViewModel() {
    private var categoryList :MutableList<StudyCategory>

    init {
        categoryList = arrayListOf()
    }
    fun getCategoryList() : ArrayList<StudyCategory>{
        return categoryList as ArrayList<StudyCategory>
    }

    fun initCategoryList(category : ArrayList<StudyCategory>){
        categoryList = category
    }
}