package com.app.gong4.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.gong4.api.QnaService
import com.app.gong4.api.StudyGroupService
import com.app.gong4.model.QnaItem
import com.app.gong4.model.QuestionUID
import com.app.gong4.model.StudyCategory
import com.app.gong4.model.req.RequestRegisterAnswer
import com.app.gong4.model.req.RequestUpdateAnswer
import com.app.gong4.model.res.BaseResponse
import com.app.gong4.model.res.ResponseQuestionBody
import com.app.gong4.model.res.ResponseRegisterAnswerBody
import com.app.gong4.utils.NetworkResult
import com.app.gong4.utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class QnaRepository @Inject constructor(private val qnaService: QnaService){
    private val _qnaRes = MutableLiveData<NetworkResult<List<QnaItem>>>()
    val qnaRes : LiveData<NetworkResult<List<QnaItem>>>
        get() = _qnaRes

    private val _myQnaRes = MutableLiveData<NetworkResult<List<QnaItem>>>()
    val myQnaRes : LiveData<NetworkResult<List<QnaItem>>>
        get() = _myQnaRes

    private val _qnaDetailRes = MutableLiveData<NetworkResult<ResponseQuestionBody.Question>>()
    val qnaDetailRes : LiveData<NetworkResult<ResponseQuestionBody.Question>>
        get() = _qnaDetailRes

    private val _registerAnswerRes = MutableLiveData<NetworkResult<ResponseRegisterAnswerBody>>()
    val registerAnswerRes : LiveData<NetworkResult<ResponseRegisterAnswerBody>>
        get() = _registerAnswerRes

    private val _deleteAnswerRes = SingleLiveEvent<NetworkResult<Void>>()
    val deleteAnswerRes : LiveData<NetworkResult<Void>>
        get() = _deleteAnswerRes

    private val _patchAnswerRes = SingleLiveEvent<NetworkResult<QuestionUID>>()
    val patchAnswerRes : LiveData<NetworkResult<QuestionUID>>
        get() = _patchAnswerRes

    suspend fun getQnaList(groupID:Int){
        val response = qnaService.getQnaList(groupID)
        if(response.isSuccessful && response.body() !=null){
            _qnaRes.postValue(NetworkResult.Success(response.body()!!.data.questionList))
        }else if(response.errorBody()!=null){
            _qnaRes.value = NetworkResult.Error(response.body()!!.location,response.body()!!.msg)
        }
    }

    suspend fun getMyQnaList(){
        val response = qnaService.getMyQnaList()
        if(response.isSuccessful && response.body() !=null){
            _myQnaRes.postValue(NetworkResult.Success(response.body()!!.data.questionList))
        }else if(response.errorBody()!=null){
            _myQnaRes.value = NetworkResult.Error(response.body()!!.location,response.body()!!.msg)
        }
    }

    suspend fun getQnaDetail(questionUID:Int){
        val response = qnaService.getQusetionDetail(questionUID)
        if(response.isSuccessful && response.body() !=null){
            _qnaDetailRes.postValue(NetworkResult.Success(response.body()!!.data))
        }else{
            _qnaDetailRes.value = NetworkResult.Error(response.body()!!.location,response.body()!!.msg)
        }
    }

    suspend fun postQnaAnswer(registerAnswerReq : RequestRegisterAnswer){
        val response = qnaService.postAnswer(registerAnswerReq)
        if(response.isSuccessful && response.body() !=null){
            _registerAnswerRes.postValue(NetworkResult.Success(response.body()!!))
        }else{
            _registerAnswerRes.value = NetworkResult.Error(response.body()!!.location,response.body()!!.msg)
        }
    }

    fun deleteQnaAnswer(answerID: Int){
        qnaService.deleteAnswer(answerID).enqueue(object:Callback<BaseResponse>{
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if(response.code()!=204){
                    _deleteAnswerRes.postValue(NetworkResult.Error(response.body()!!.location,response.body()!!.msg))
                }else{
                    _deleteAnswerRes.postValue(NetworkResult.Success(null!!))
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {

            }
        })
    }

    suspend fun patchQnaAnswer(updateAnswerReq: RequestUpdateAnswer){
        val response = qnaService.patchAnswer(updateAnswerReq)
        if(response.isSuccessful && response.body() !=null){
            _patchAnswerRes.postValue(NetworkResult.Success(response.body()!!.data))
        }else{
            _patchAnswerRes.value = NetworkResult.Error(response.body()!!.location,response.body()!!.msg)
        }
    }
}