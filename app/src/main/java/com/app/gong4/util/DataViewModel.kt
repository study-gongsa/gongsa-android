package com.app.gong4.util

import androidx.lifecycle.ViewModel
import com.app.gong4.DTO.StudyCategory

class DataViewModel : ViewModel() {
    private var _categories = arrayListOf<StudyCategory>()

    fun getCategories(): List<StudyCategory> {
       return _categories
    }

    fun loadCategories(element: List<StudyCategory>) {
       _categories.addAll(element)
    }
}