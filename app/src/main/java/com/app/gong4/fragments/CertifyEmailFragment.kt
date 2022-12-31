package com.app.gong4.fragments

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gong4.R
import com.app.gong4.databinding.FragmentCertifyEmailBinding
import com.app.gong4.model.req.RequestAuthCodeBody
import com.app.gong4.model.req.RequestCertifyEmailBody
import com.app.gong4.utils.CommonTextWatcher
import com.app.gong4.utils.NetworkResult
import com.app.gong4.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class CertifyEmailFragment : BaseFragment<FragmentCertifyEmailBinding>(FragmentCertifyEmailBinding::inflate) {

    private val args by navArgs<CertifyEmailFragmentArgs>()

    private val userViewModel : UserViewModel by viewModels()

    override fun initView() {
        binding.emailTextView.text = args.email

        sendEmail()
        checkCode()
        goNext()
        goLogin()
    }

    private fun sendEmail() {
        userViewModel.certifyEmailRes.observe(viewLifecycleOwner, Observer {
            binding.waitingView.visibility = View.INVISIBLE
        })
        userViewModel.certifyEmail(RequestCertifyEmailBody(args.email))
    }

    private fun checkCode(){
        binding.codeEditText.addTextChangedListener(CommonTextWatcher(
            beforeChanged = { _,_,_,_ ->
                binding.validCodeTextView.text = ""
            },
            onChanged = { text,_,_,_ ->
                binding.confirmButton.isEnabled =
                    Pattern.matches("^[0-9].{5}$", text.toString())
            }
        ))
    }

    private fun goNext(){
        binding.confirmButton.setOnClickListener {
            val authCode = binding.codeEditText.text.toString()
            userViewModel.confirmCodeRes.observe(viewLifecycleOwner, Observer {
                when(it){
                    is NetworkResult.ResultEmpty -> {
                        binding.waitingView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.INVISIBLE

                        binding.checkImageView.visibility = View.VISIBLE
                        binding.startButton.visibility = View.VISIBLE

                        binding.checkEmailTextView.text = R.string.certify_success.toString()
                        binding.sendCodeTextView.text = R.string.success_msg.toString()
                    }
                    is NetworkResult.Error -> {
                        binding.validCodeTextView.text = it.msg.toString()
                    }
                    else -> {
                        showToastMessage(it.msg.toString())
                    }
                }
            })
            userViewModel.confirmCode(RequestAuthCodeBody(authCode,args.email))
        }
    }

    private fun goLogin() {
        binding.startButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_certifyEmailFragment_to_loginFragment)
        }
    }



}