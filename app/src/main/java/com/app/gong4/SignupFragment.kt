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
import androidx.navigation.fragment.findNavController
import com.app.gong4.DTO.RequestLoginBody
import com.app.gong4.DTO.RequestSignupBody
import com.app.gong4.DTO.ResponseLoginBody
import com.app.gong4.DTO.ResponseSignupBody
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentSignupBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SignupFragment : Fragment() {

    lateinit var binding:FragmentSignupBinding
    var email: String = ""
    var password: String = ""
    var passwordCheck: String = ""
    var nickname: String = ""
    var button1: Boolean = false
    var button2: Boolean = false
    var button3: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        checkEmail()
        checkPassword()
        confirmPassword()
        checkNickname()

        binding.checkImageButton1.setOnClickListener {
            button1 = if (!button1) {
                binding.checkImageButton1.setImageResource(R.drawable.ic_baseline_check_circle_24)
                true
            } else {
                binding.checkImageButton1.setImageResource(R.drawable.ic_baseline_check_circle_outline)
                false
            }
            turnSignupButton()
        }

        binding.checkImageButton2.setOnClickListener {
            button2 = if (!button2) {
                binding.checkImageButton2.setImageResource(R.drawable.ic_baseline_check_circle_24)
                true
            } else {
                binding.checkImageButton2.setImageResource(R.drawable.ic_baseline_check_circle_outline)
                false
            }
            turnSignupButton()
        }

        binding.checkImageButton3.setOnClickListener {
            button3 = if (!button3) {
                binding.checkImageButton3.setImageResource(R.drawable.ic_baseline_check_circle_24)
                true
            } else {
                binding.checkImageButton3.setImageResource(R.drawable.ic_baseline_check_circle_outline)
                false
            }
        }

        goSignup()
        return binding.root
    }

    // 이메일 형식 확인
    private fun checkEmail() {
        binding.emailEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(p0).matches()){
                    binding.validEmailTextView.text = ""
                    binding.emailEditText.background = context!!.resources.getDrawable(R.drawable.custom_input, null)

                } else {
                    binding.validEmailTextView.text = String.format(resources.getString(R.string.signup_wrong_text), "이메일 주소")
                    binding.emailEditText.background = context!!.resources.getDrawable(R.drawable.custom_error_input, null)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                email = if (binding.validEmailTextView.text == "") p0.toString() else ""
                turnSignupButton()
            }
        })
    }

    // 비밀번호 형식 확인
    private fun checkPassword() {
        binding.passwordEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if(Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,16}$", p0.toString())){
                    binding.validPasswordTextView.text = ""
                    binding.passwordEditText.background = context!!.resources.getDrawable(R.drawable.custom_input, null)

                } else {
                    binding.validPasswordTextView.text = String.format(resources.getString(R.string.signup_wrong_text), "비밀번호")
                    binding.passwordEditText.background = context!!.resources.getDrawable(R.drawable.custom_error_input, null)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                password = if (binding.validPasswordTextView.text == "") p0.toString() else ""
                turnSignupButton()
            }
        })
    }

    // 비밀번호 일치 확인
    private fun confirmPassword() {
        binding.passwordCheckEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if(p0.toString() == password){
                    binding.validPasswordCheckTextView.text = ""
                    binding.passwordCheckEditText.background = context!!.resources.getDrawable(R.drawable.custom_input, null)

                } else {
                    binding.validPasswordCheckTextView.text = resources.getString(R.string.signup_wrong_password)
                    binding.passwordCheckEditText.background = context!!.resources.getDrawable(R.drawable.custom_error_input, null)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                passwordCheck = if (binding.validPasswordCheckTextView.text == "") p0.toString() else ""
                turnSignupButton()
            }
        })
    }

    // 닉네임 형식 확인
    private fun checkNickname() {
        binding.nicknameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if(Pattern.matches("^[a-zA-Zㄱ-힣0-9]{1,10}$", p0.toString())){
                    binding.validNicknameTextView.text = ""
                    binding.nicknameEditText.background = context!!.resources.getDrawable(R.drawable.custom_input, null)

                } else {
                    binding.validNicknameTextView.text = String.format(resources.getString(R.string.signup_wrong_text), "닉네임")
                    binding.nicknameEditText.background = context!!.resources.getDrawable(R.drawable.custom_error_input, null)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                nickname = if (binding.validNicknameTextView.text == "") p0.toString() else ""
                turnSignupButton()
            }
        })
    }

    private fun turnSignupButton() {
        binding.signupButton.isEnabled = email != "" && password != "" && passwordCheck != "" && nickname != "" && button1 && button2
    }

    // 회원가입
    private fun goSignup(){
        binding.signupButton.setOnClickListener {

            val requestServer = RequestServer

            requestServer.userService.signup(RequestSignupBody(email,nickname,password)).enqueue(object :
                Callback<ResponseSignupBody> {
                override fun onResponse(
                    call: Call<ResponseSignupBody>,
                    response: Response<ResponseSignupBody>
                ) {
                    Log.d("회원가입 코드", response.code().toString())

                    if(response.isSuccessful){
                        val repo: ResponseSignupBody? = response.body()
                        Log.d("회원가입 성공", repo.toString())

                        // 이메일 인증 프레그먼트 이동
                        val action = SignupFragmentDirections.actionSignupFragmentToCertifyEmailFragment(email)
                        findNavController().navigate(action)

                    }else{
                        val error = response.errorBody()!!.string().trimIndent()
                        val result = Gson().fromJson(error, ResponseLoginBody::class.java)
                        showErrorMsg(result.location,result.msg)
                    }
                }

                override fun onFailure(call: Call<ResponseSignupBody>, t: Throwable) {
                    Toast.makeText(context,"회원가입 실패", Toast.LENGTH_SHORT)
                }
            }
            )

        }
    }

    fun showErrorMsg(location : String?, msg:String?){
        if(location == "email"){
            binding.validEmailTextView.text = msg
            binding.emailEditText.background = context!!.resources.getDrawable(R.drawable.custom_error_input, null)
        }else if(location == "nickname"){
            binding.validNicknameTextView.text = msg
            binding.nicknameEditText.background = context!!.resources.getDrawable(R.drawable.custom_error_input, null)
        }
        binding.signupButton.isEnabled = false
    }
}