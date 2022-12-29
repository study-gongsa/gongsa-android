package com.app.gong4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.gong4.model.res.ResponseLoginBody
import com.app.gong4.repository.StudyGroupRepository
import com.app.gong4.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudyGroupViewModel @Inject constructor(private val studyRepository: StudyGroupRepository):ViewModel(){
    val myStudyGroupLiveData get() = studyRepository.myGroupRes

    fun getMyStudyGroup(){
        viewModelScope.launch {
            studyRepository.getMyStudyGroup()
        }
    }
}