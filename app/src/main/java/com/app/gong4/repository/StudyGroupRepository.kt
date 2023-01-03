package com.app.gong4.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.gong4.R
import com.app.gong4.api.StudyGroupService
import com.app.gong4.model.StudyGroupItem
import com.app.gong4.model.req.RequestCreateStudyGroup
import com.app.gong4.model.req.RequestEnterMember
import com.app.gong4.model.res.*
import com.app.gong4.utils.NetworkResult
import com.app.gong4.utils.SingleLiveEvent
import com.google.gson.Gson
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class StudyGroupRepository @Inject constructor(private val studyGroupService: StudyGroupService){
    private val _myGroupRes = MutableLiveData<NetworkResult<List<StudyGroupItem>>>()
    val myGroupRes : LiveData<NetworkResult<List<StudyGroupItem>>>
    get() = _myGroupRes

    private val _recommendGroupRes = MutableLiveData<NetworkResult<List<StudyGroupItem>>>()
    val recommendGroupRes : LiveData<NetworkResult<List<StudyGroupItem>>>
        get() = _recommendGroupRes

    private val _studyGroupInfoRes = SingleLiveEvent<NetworkResult<ResponseStudygroupinfoBody.StudyGroupDetailItem>>()
    val studyGroupInfoRes : LiveData<NetworkResult<ResponseStudygroupinfoBody.StudyGroupDetailItem>>
        get() = _studyGroupInfoRes

    private val _createStudyGroupRes = SingleLiveEvent<NetworkResult<ResponseCreateStudyGroup>>()
    val createStudyGroupRes : LiveData<NetworkResult<ResponseCreateStudyGroup>>
        get() = _createStudyGroupRes

    private val _leaveStudyGroupRes = SingleLiveEvent<NetworkResult<BaseResponse>>()
    val leaveStudyGroupRes : LiveData<NetworkResult<BaseResponse>>
        get() = _leaveStudyGroupRes

    private val _joinStudyGroupRes = SingleLiveEvent<NetworkResult<BaseResponse>>()
    val joinStudyGroupRes : LiveData<NetworkResult<BaseResponse>>
        get() = _joinStudyGroupRes

    private val _studyMemberRes = SingleLiveEvent<NetworkResult<ResponseStudyMembers.StudyMembers>>()
    val studyMemberRes : LiveData<NetworkResult<ResponseStudyMembers.StudyMembers>>
        get() = _studyMemberRes

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

    suspend fun getRecommendStudyGroup(){
        val response = studyGroupService.recommend(type = "main")
        if(response.isSuccessful && response.body() != null){
            _recommendGroupRes.postValue(NetworkResult.Success(response.body()!!.data.studyGroupList))
        }else if(response.errorBody()!=null){
            val error = response.errorBody()!!.string().trimIndent()
            val result = Gson().fromJson(error, ResponseGroupItemBody::class.java)
            _recommendGroupRes.value = NetworkResult.Error(result.location,result.msg)
        }else{
            _recommendGroupRes.value = NetworkResult.Error(null, R.string.server_error_msg)
        }
    }

    suspend fun getStudyGroupInfo(groupUID:Int){
        val response = studyGroupService.getStudygroupInfo(groupUID)
        if(response.isSuccessful && response.body()!=null){
            _studyGroupInfoRes.postValue(NetworkResult.Success(response.body()!!.data))
        }else if(response.errorBody()!=null){
            val error = response.errorBody()!!.string().trimIndent()
            val result = Gson().fromJson(error, ResponseGroupItemBody::class.java)
            _recommendGroupRes.value = NetworkResult.Error(result.location,result.msg)
        }else{
            _recommendGroupRes.value = NetworkResult.Error(null, R.string.server_error_msg)
        }
    }

    fun createStudyGroup(image: MultipartBody.Part?=null, requestBody: RequestCreateStudyGroup){
       studyGroupService.createStudygroup(image,requestBody).enqueue(object :
           Callback<ResponseCreateStudyGroup> {
           override fun onResponse(
               call: Call<ResponseCreateStudyGroup>,
               response: Response<ResponseCreateStudyGroup>
           ) {
               if(response.isSuccessful && response.body() != null){
                   _createStudyGroupRes.postValue(NetworkResult.Success(response.body()!!))
               }else{
                   val error = response.errorBody()!!.string().trimIndent()
                   val result = Gson().fromJson(error, ResponseCreateStudyGroup::class.java)
                   _createStudyGroupRes.value = NetworkResult.Error(result.location,result.msg)
               }
           }

           override fun onFailure(call: Call<ResponseCreateStudyGroup>, t: Throwable) {

           }
       })
    }

    suspend fun leaveGroup(groupUID: Int){
        val response = studyGroupService.leaveGroup(groupUID)
        if(response.isSuccessful){
            _leaveStudyGroupRes.postValue(NetworkResult.ResultEmpty())
        }else{
            _leaveStudyGroupRes.value = NetworkResult.Error(null)
        }
    }

    suspend fun getStudyGroupCodeInfo(code:String){
        val response = studyGroupService.getStudygroupCodeInfo(code)
        if(response.isSuccessful){
            _studyGroupInfoRes.postValue(NetworkResult.Success(response.body()!!.data))
        }else{
            val error = response.errorBody()!!.string().trimIndent()
            val msg = Gson().fromJson(error, ResponseEnterMember::class.java).msg
            _studyGroupInfoRes.value = NetworkResult.Error("",msg)
        }
    }

    suspend fun joinStudyGroup(enterMemberReq: RequestEnterMember){
        val response = studyGroupService.getStudyEnter(enterMemberReq)
        if(response.isSuccessful){
            _joinStudyGroupRes.postValue(NetworkResult.ResultEmpty())
        }else{
            _joinStudyGroupRes.value = NetworkResult.Error(response.body()!!.location,response.body()!!.msg)
        }
    }

    fun getStudyGroupFilterInfo(align:String?=null,categoryUIDs: List<Int>? =null,isCam:Boolean?=null,word:String?=null){
        studyGroupService.getStudygroupfilterInfo(align,categoryUIDs,isCam,word).enqueue(object :
            Callback<ResponseGroupItemBody>{
            override fun onResponse(
                call: Call<ResponseGroupItemBody>,
                response: Response<ResponseGroupItemBody>
            ) {
                if(response.isSuccessful){
                    _recommendGroupRes.postValue(NetworkResult.Success(response.body()!!.data.studyGroupList))
                }else{
                    _recommendGroupRes.value = NetworkResult.Error(response.body()!!.location,response.body()!!.msg)
                }
            }

            override fun onFailure(call: Call<ResponseGroupItemBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    suspend fun getStudyGroupMember(groupUID: Int){
        val response = studyGroupService.getStudyMembers(groupUID)
        if(response.isSuccessful){
            _studyMemberRes.postValue(NetworkResult.Success(response.body()!!.data))
        }else{
            _studyMemberRes.value =NetworkResult.Error(response.body()!!.location,response.body()!!.msg)
        }
    }
}