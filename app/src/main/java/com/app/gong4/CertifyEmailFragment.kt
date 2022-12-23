package com.app.gong4

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gong4.DTO.RequestAuthCodeBody
import com.app.gong4.DTO.RequestCertifyEmailBody
import com.app.gong4.DTO.ResponseAuthCodeBody
import com.app.gong4.DTO.ResponseCertifyEmailBody
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentCertifyEmailBinding
import com.app.gong4.databinding.FragmentSignupBinding
import com.app.gong4.util.CommonTextWatcher
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import java.util.regex.Pattern
import javax.security.auth.callback.Callback

class CertifyEmailFragment : Fragment() {

    lateinit var binding: FragmentCertifyEmailBinding
    private val args by navArgs<CertifyEmailFragmentArgs>()
    private val requestServer = RequestServer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCertifyEmailBinding.inflate(inflater, container, false)
        binding.emailTextView.text = args.email

        sendEmail()
        checkCode()
        goNext()
        goLogin()

        return binding.root
    }

    private fun sendEmail() {
        requestServer.userService.certifyEmail(RequestCertifyEmailBody(args.email)).enqueue(object :
            retrofit2.Callback<ResponseCertifyEmailBody> {
            override fun onResponse(
                call: Call<ResponseCertifyEmailBody>,
                response: Response<ResponseCertifyEmailBody>
            ) {
                binding.waitingView.visibility = View.INVISIBLE
//                    if(response.isSuccessful) {
//                        binding.waitingView.visibility = View.INVISIBLE
//                    } else {
//                        val error = response.errorBody()!!.string().trimIndent()
//                        val result = Gson().fromJson(error, ResponseCertifyEmailBody::class.java)
//                    }
            }

            override fun onFailure(call: Call<ResponseCertifyEmailBody>, t: Throwable) {
                Toast.makeText(context,"서버와의 통신이 원활하지 않습니다.",Toast.LENGTH_SHORT)
            }
        })
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

            requestServer.userService.confirmCode(RequestAuthCodeBody(authCode, args.email)).enqueue(object :
                retrofit2.Callback<ResponseAuthCodeBody> {
                override fun onResponse(
                    call: Call<ResponseAuthCodeBody>,
                    response: Response<ResponseAuthCodeBody>
                ) {
                    if(response.isSuccessful) {
                        binding.waitingView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.INVISIBLE

                        binding.checkImageView.visibility = View.VISIBLE
                        binding.startButton.visibility = View.VISIBLE

                        binding.checkEmailTextView.text = R.string.certify_success.toString()
                        binding.sendCodeTextView.text = R.string.success_msg.toString()


                    } else {
                        val error = response.errorBody()!!.string().trimIndent()
                        val result = Gson().fromJson(error, ResponseCertifyEmailBody::class.java)
                        binding.validCodeTextView.text = result.msg
                    }
                }

                override fun onFailure(call: Call<ResponseAuthCodeBody>, t: Throwable) {
                    Toast.makeText(context,"서버와의 통신이 원활하지 않습니다.",Toast.LENGTH_SHORT)
                }
            })
        }
    }

    private fun goLogin() {
        binding.startButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_certifyEmailFragment_to_loginFragment)
        }
    }

}