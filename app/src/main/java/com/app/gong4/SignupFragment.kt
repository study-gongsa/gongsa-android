package com.app.gong4

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.gong4.databinding.FragmentSignupBinding
import java.util.regex.Pattern

class SignupFragment : Fragment() {

    lateinit var binding:FragmentSignupBinding
    var password: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        checkEmail()
        checkPassword()
        confirmPassword()
        checkNickname()

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
                password = p0.toString()
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
            }
        })
    }
}