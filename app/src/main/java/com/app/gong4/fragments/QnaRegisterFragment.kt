package com.app.gong4.fragments

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gong4.MainActivity
import com.app.gong4.R
import com.app.gong4.databinding.FragmentQnaRegisterBinding
import com.app.gong4.model.req.RequestQuestion
import com.app.gong4.utils.CommonTextWatcher
import com.app.gong4.utils.NetworkResult
import com.app.gong4.viewmodel.QnaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QnaRegisterFragment : BaseFragment<FragmentQnaRegisterBinding>(FragmentQnaRegisterBinding::inflate){

    private val args by navArgs<QnaRegisterFragmentArgs>()
    private val qnaViewModel : QnaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(false)
    }

    override fun onStop() {
        super.onStop()
        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(true)
    }

    override fun initView() {
        checkInput()
        saveQuestion(args.groupUID)
    }

    private fun saveQuestion(questionUID : Int){
        binding.registerButton.setOnClickListener {
            val requestBody = RequestQuestion(questionUID,binding.qnaTitleEditText.text.toString(),binding.qnaContentEditText.text.toString())
            qnaViewModel.requestQuestion(requestBody)
            qnaViewModel.requestQuestionLiveData.observe(viewLifecycleOwner, Observer {
                when(it){
                    is NetworkResult.Success -> {
                        val questionUID = it.data!!.questionUID
                        showToastMessage(resources.getString(R.string.study_qna_register_complete))
                        val action = QnaRegisterFragmentDirections.actionQnaRegisterFragmentToGroupQnaDetailFragment(questionUID)
                        findNavController().navigate(action)
                    }
                    else -> {
                        showToastMessage(it.msg.toString())
                    }
                }
            })
        }
    }

    // 입력값 체크
    fun checkInput(){
        binding.qnaTitleEditText.addTextChangedListener(CommonTextWatcher(
            afterChanged = {
                binding.registerButton.isEnabled = binding.qnaTitleEditText.text.isNotEmpty() && binding.qnaContentEditText.text.isNotEmpty()
            }
        ))
        binding.qnaContentEditText.addTextChangedListener(CommonTextWatcher(
            afterChanged = {
                binding.registerButton.isEnabled = binding.qnaTitleEditText.text.isNotEmpty() && binding.qnaContentEditText.text.isNotEmpty()
            }
        ))
    }

}