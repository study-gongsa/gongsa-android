package com.app.gong4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.gong4.model.req.RequestCreateStudyGroup
import com.app.gong4.model.req.RequestEnterMember
import com.app.gong4.model.res.ResponseLoginBody
import com.app.gong4.repository.StudyGroupRepository
import com.app.gong4.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class StudyGroupViewModel @Inject constructor(private val studyRepository: StudyGroupRepository):ViewModel(){
    val myStudyGroupLiveData get() = studyRepository.myGroupRes
    val recommendGroupLiveData get() = studyRepository.recommendGroupRes
    val studyGroupInfoLiveData get() = studyRepository.studyGroupInfoRes
    val createStudyGroupLiveData get() = studyRepository.createStudyGroupRes
    val leaveStudyGroupLiveData get() = studyRepository.leaveStudyGroupRes
    val joinLiveData get() = studyRepository.joinStudyGroupRes
    val memberLiveData get() = studyRepository.studyMemberRes

    fun getMyStudyGroup(){
        viewModelScope.launch {
            studyRepository.getMyStudyGroup()
        }
    }

    fun getRecommendStudyGroup(){
        viewModelScope.launch {
            studyRepository.getRecommendStudyGroup()
        }
    }

    fun getStudyGroupInfo(groupUID:Int){
        viewModelScope.launch {
            studyRepository.getStudyGroupInfo(groupUID)
        }
    }

    fun getStudyGroupCodeInfo(code:String){
        viewModelScope.launch {
            studyRepository.getStudyGroupCodeInfo(code)
        }
    }

    fun createStudygroup(image: MultipartBody.Part, requestBody: RequestCreateStudyGroup){
        viewModelScope.launch {
            studyRepository.createStudyGroup(image,requestBody)
        }
    }

    fun leaveStudyGroup(groupUID: Int){
        viewModelScope.launch {
            studyRepository.leaveGroup(groupUID)
        }
    }

    fun joinStudyGroup(enterMemberReq: RequestEnterMember){
        viewModelScope.launch {
            studyRepository.joinStudyGroup(enterMemberReq)
        }
    }

    fun getStudyGroupFilterInfo(align:String?=null,categoryUIDs: List<Int>? =null,isCam:Boolean?=null,word:String?=null){
        viewModelScope.launch{
            studyRepository.getStudyGroupFilterInfo(align,categoryUIDs,isCam,word)
        }
    }

    fun getStudyGroupMember(groupUID: Int){
        viewModelScope.launch {
            studyRepository.getStudyGroupMember(groupUID)
        }
    }

}