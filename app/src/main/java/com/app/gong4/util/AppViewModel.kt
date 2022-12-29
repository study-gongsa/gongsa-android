package com.app.gong4.util

import androidx.lifecycle.ViewModel
import com.app.gong4.UserCategory
import com.app.gong4.model.StudyCategory
import com.app.gong4.model.UserInfo

class AppViewModel:ViewModel() {
    private val categoryList = arrayListOf<StudyCategory>()
    private val userCategoryList = arrayListOf<UserCategory>()

    fun getCategoryList() : ArrayList<StudyCategory>{
        return categoryList
    }

    fun getUserCategoryList() : ArrayList<UserCategory>{
        return userCategoryList
    }

    fun initCategoryList(category : ArrayList<StudyCategory>){
        categoryList.addAll(category)
    }

    fun initUserCategoryList(category : ArrayList<UserCategory>){
        userCategoryList.addAll(category)
    }
}