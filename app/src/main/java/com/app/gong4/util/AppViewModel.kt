package com.app.gong4.util

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.gong4.DTO.StudyCategory

class AppViewModel:ViewModel() {
    private val categoryList = arrayListOf<StudyCategory>()

    fun getCategoryList() : ArrayList<StudyCategory>{
        Log.d("getCategoryList",categoryList.toString());
        return categoryList
    }

    fun initCategoryList(category : ArrayList<StudyCategory>){
        categoryList.addAll(category)
        Log.d("initCategoryList",categoryList.toString());
    }
}