package com.app.gong4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.gong4.model.res.ResponseLoginBody
import com.app.gong4.repository.CategoryRepository
import com.app.gong4.repository.StudyGroupRepository
import com.app.gong4.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository):ViewModel(){
    val categoryLiveData get() = categoryRepository.categoryRes
    val myCategoryLiveData get() = categoryRepository.myCategoryRes

    fun getCategoryList(){
        viewModelScope.launch {
            categoryRepository.getCategoryList()
        }
    }

    fun getUserCategoryList(){
        viewModelScope.launch{
            categoryRepository.getUserCategory()
        }
    }
}