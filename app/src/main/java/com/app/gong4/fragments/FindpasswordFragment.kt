package com.app.gong4.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.gong4.MainActivity
import com.app.gong4.R
import com.app.gong4.databinding.FragmentFindpasswordBinding
import com.app.gong4.model.req.RequestFindPwdBody
import com.app.gong4.utils.CommonTextWatcher
import com.app.gong4.utils.NetworkResult
import com.app.gong4.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindpasswordFragment : BaseFragment<FragmentFindpasswordBinding>(FragmentFindpasswordBinding::inflate) {

    private val userViewModel : UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationBar(true)
    }

    override fun initView() {
        //확인 버튼 비활성화
        binding.confirmButton.isEnabled = false

        checkInput()
        goConfirm()
    }

    fun checkInput() {
        binding.emailEditText.addTextChangedListener(CommonTextWatcher(
            afterChanged = { text ->
                binding.confirmButton.isEnabled = binding.emailEditText.text.toString() != ""
            }
        ))
    }

    /* 확인 버튼 */
    fun goConfirm(){
        binding.confirmButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            binding.validEmailTextView.text = ""
            binding.waitingView.visibility = View.VISIBLE

            userViewModel.findPasswordRes.observe(viewLifecycleOwner, Observer {
                binding.waitingView.visibility = View.INVISIBLE
                when(it){
                    is NetworkResult.ResultEmpty -> {
                        findNavController().navigate(R.id.action_findpasswordFragment_to_loginFragment)
                    }
                    is NetworkResult.Error -> {
                        binding.validEmailTextView.text = it.msg.toString()
                        binding.confirmButton.isEnabled = false
                    }
                    else -> {
                        binding.waitingView.visibility = View.INVISIBLE
                        showToastMessage(it.msg.toString())
                    }
                }
            })
            userViewModel.findPassword(RequestFindPwdBody(email))
        }
    }
}