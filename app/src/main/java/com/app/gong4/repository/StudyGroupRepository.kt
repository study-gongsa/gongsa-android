package com.app.gong4.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.gong4.R
import com.app.gong4.api.StudyGroupService
import com.app.gong4.api.UserService
import com.app.gong4.model.StudyGroupItem
import com.app.gong4.model.req.RequestLoginBody
import com.app.gong4.model.res.ResponseGroupItemBody
import com.app.gong4.model.res.ResponseLoginBody
import com.app.gong4.utils.NetworkResult
import com.google.gson.Gson
import javax.inject.Inject

class StudyGroupRepository @Inject constructor(private val studyGroupService: StudyGroupService){
    private val _myGroupRes = MutableLiveData<NetworkResult<List<StudyGroupItem>>>()
    val myGroupRes : LiveData<NetworkResult<List<StudyGroupItem>>>
    get() = _myGroupRes

    suspend fun getMyStudyGroup(){
        val response = studyGroupService.getMyStudyGroup()
        if(response.isSuccessful && response.body() !=null){
            _myGroupRes.postValue(NetworkResult.Success(response.body()!!.data.studyGroupList))
        }else if(response.errorBody()!=null){
            val error = response.errorBody()!!.string().trimIndent()
            val result = Gson().fromJson(error, ResponseGroupItemBody::class.java)
            _myGroupRes.value = NetworkResult.Error(result.location,result.msg)
        }else{
            _myGroupRes.value = NetworkResult.Error(null, R.string.server_error_msg)
        }
    }
}