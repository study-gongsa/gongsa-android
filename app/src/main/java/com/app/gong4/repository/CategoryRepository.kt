package com.app.gong4.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.gong4.R
import com.app.gong4.api.UserCategoryService
import com.app.gong4.model.StudyCategory
import com.app.gong4.model.UserCategory
import com.app.gong4.model.res.ResponseStudycategoryBody
import com.app.gong4.utils.NetworkResult
import com.google.gson.Gson
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val userCategoryService: UserCategoryService){
    private val _categoryRes = MutableLiveData<NetworkResult<List<StudyCategory>>>()
    val categoryRes : LiveData<NetworkResult<List<StudyCategory>>>
        get() = _categoryRes

    private val _myCategoryRes = MutableLiveData<NetworkResult<List<UserCategory>>>()
    val myCategoryRes : LiveData<NetworkResult<List<UserCategory>>>
        get() = _myCategoryRes

    suspend fun getCategoryList(){
        val response = userCategoryService.getCategory()
        if(response.isSuccessful && response.body() !=null){
            _categoryRes.postValue(NetworkResult.Success(response.body()!!.data))
        }else if(response.errorBody()!=null){
            val error = response.errorBody()!!.string().trimIndent()
            val result = Gson().fromJson(error, ResponseStudycategoryBody::class.java)
            _categoryRes.value = NetworkResult.Error(result.location,result.msg)
        }else{
            _categoryRes.value = NetworkResult.Error(null, R.string.server_error_msg)
        }
    }

    suspend fun getUserCategory(){
        val response = userCategoryService.getUserCategory()
        if(response.isSuccessful && response.body() !=null){
            _myCategoryRes.postValue(NetworkResult.Success(response.body()!!.data))
        }else if(response.errorBody()!=null){
            val error = response.errorBody()!!.string().trimIndent()
            val result = Gson().fromJson(error, ResponseStudycategoryBody::class.java)
            _myCategoryRes.value = NetworkResult.Error(result.location,result.msg)
        }else{
            _myCategoryRes.value = NetworkResult.Error(null, R.string.server_error_msg)
        }

    }
}