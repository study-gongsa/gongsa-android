package com.app.gong4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.gong4.model.req.RequestQuestion
import com.app.gong4.model.req.RequestRegisterAnswer
import com.app.gong4.model.req.RequestUpdateAnswer
import com.app.gong4.repository.QnaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QnaViewModel @Inject constructor(private val qnaRepository: QnaRepository): ViewModel(){
    val myQnaLiveData get() = qnaRepository.myQnaRes
    val qnaLiveData get() = qnaRepository.qnaRes
    val qnaDetailLiveData get() = qnaRepository.qnaDetailRes
    val requestAnswerLiveData get() = qnaRepository.registerAnswerRes
    val deleteAnswerLiveData get() = qnaRepository.deleteAnswerRes
    val patchAnswerLiveData get() = qnaRepository.patchAnswerRes
    val requestQuestionLiveData get() = qnaRepository.registerQuestionRes

    fun getQnaList(groupID:Int){
        viewModelScope.launch {
            qnaRepository.getQnaList(groupID)
        }
    }

    fun getMyQnaList(){
        viewModelScope.launch {
            qnaRepository.getMyQnaList()
        }
    }

    fun getQnaDetail(questionUID:Int){
        viewModelScope.launch {
            qnaRepository.getQnaDetail(questionUID)
        }
    }

    fun requestAnswer(registerAnswerReq : RequestRegisterAnswer){
        viewModelScope.launch {
            qnaRepository.postQnaAnswer(registerAnswerReq)
        }
    }

    fun deleteQnaAnswer(answerID: Int){
        viewModelScope.launch {
            qnaRepository.deleteQnaAnswer(answerID)
        }
    }

    fun patchQnaAnswer(updateAnswerReq: RequestUpdateAnswer){
        viewModelScope.launch {
            qnaRepository.patchQnaAnswer(updateAnswerReq)
        }
    }

    fun requestQuestion(questionRes: RequestQuestion){
        viewModelScope.launch {
            qnaRepository.registerQuestion(questionRes)
        }
    }
}