package com.app.gong4

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.app.gong4.model.res.ResponseLoginBody
import com.app.gong4.model.res.ResponseSignupBody
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentSignupBinding
import com.app.gong4.model.req.RequestSignupBody
import com.app.gong4.utils.CommonTextWatcher
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SignupFragment : BaseFragment<FragmentSignupBinding>(FragmentSignupBinding::inflate) {

    var email: String = ""
    var password: String = ""
    var passwordCheck: String = ""
    var nickname: String = ""
    var button1: Boolean = false
    var button2: Boolean = false
    var button3: Boolean = false

    override fun initView() {
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
    }

    // 이메일 형식 확인
    private fun checkEmail() {
        binding.emailEditText.addTextChangedListener(CommonTextWatcher(
            onChanged = { text,_,_,_ ->
                if(Patterns.EMAIL_ADDRESS.matcher(text).matches()){
                    binding.validEmailTextView.text = ""
                    binding.emailEditText.background = requireContext().resources.getDrawable(R.drawable.custom_input, null)

                } else {
                    binding.validEmailTextView.text = String.format(resources.getString(R.string.signup_wrong_text), "이메일 주소")
                    binding.emailEditText.background = requireContext().resources.getDrawable(R.drawable.custom_error_input, null)
                }
            },
            afterChanged = { text ->
                email = if (binding.validEmailTextView.text == "") text.toString() else ""
                turnSignupButton()
            }
        ))
    }

    // 비밀번호 형식 확인
    private fun checkPassword() {
        binding.passwordEditText.addTextChangedListener(CommonTextWatcher(
            onChanged = { text,_,_,_ ->
                if(Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,16}$", text.toString())){
                    binding.validPasswordTextView.text = ""
                    binding.passwordEditText.background = requireContext().resources.getDrawable(R.drawable.custom_input, null)

                } else {
                    binding.validPasswordTextView.text = String.format(resources.getString(R.string.signup_wrong_text), "비밀번호")
                    binding.passwordEditText.background = requireContext().resources.getDrawable(R.drawable.custom_error_input, null)
                }
            },
            afterChanged = { text ->
                password = if (binding.validPasswordTextView.text == "") text.toString() else ""
                turnSignupButton()
            }
        ))
    }

    // 비밀번호 일치 확인
    private fun confirmPassword() {
        binding.passwordCheckEditText.addTextChangedListener(CommonTextWatcher(
            onChanged = { text,_,_,_ ->
                if(text.toString() == password){
                    binding.validPasswordCheckTextView.text = ""
                    binding.passwordCheckEditText.background = requireContext().resources.getDrawable(R.drawable.custom_input, null)

                } else {
                    binding.validPasswordCheckTextView.text = resources.getString(R.string.signup_wrong_password)
                    binding.passwordCheckEditText.background = requireContext().resources.getDrawable(R.drawable.custom_error_input, null)
                }
            },
            afterChanged = { text ->
                passwordCheck = if (binding.validPasswordCheckTextView.text == "") text.toString() else ""
                turnSignupButton()
            }
        ))
    }

    // 닉네임 형식 확인
    private fun checkNickname() {
        binding.nicknameEditText.addTextChangedListener(CommonTextWatcher(
            onChanged = { text,_,_,_ ->
                if(Pattern.matches("^[a-zA-Zㄱ-힣0-9]{1,10}$", text.toString())){
                    binding.validNicknameTextView.text = ""
                    binding.nicknameEditText.background = requireContext().resources.getDrawable(R.drawable.custom_input, null)

                } else {
                    binding.validNicknameTextView.text = String.format(resources.getString(R.string.signup_wrong_text), "닉네임")
                    binding.nicknameEditText.background = requireContext().resources.getDrawable(R.drawable.custom_error_input, null)
                }
            },
            afterChanged = { text ->
                nickname = if (binding.validNicknameTextView.text == "") text.toString() else ""
                turnSignupButton()
            }
        ))
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
            binding.emailEditText.background = requireContext().resources.getDrawable(R.drawable.custom_error_input, null)
        }else if(location == "nickname"){
            binding.validNicknameTextView.text = msg
            binding.nicknameEditText.background = requireContext().resources.getDrawable(R.drawable.custom_error_input, null)
        }
        binding.signupButton.isEnabled = false
    }
}