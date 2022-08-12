package com.app.gong4

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.app.gong4.DTO.RequestLoginBody
import com.app.gong4.DTO.ResponseLoginBody
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class LoginFragment : Fragment() {

    lateinit var emailEditText : EditText
    lateinit var passwordEditText: EditText
    lateinit var errorEmailTextView : TextView
    lateinit var errorPWTextView : TextView
    lateinit var loginButton : Button
    val requestServer = RequestServer
    private var imm : InputMethodManager ?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText
        errorEmailTextView = binding.validEmailTextView
        errorPWTextView = binding.validPasswordTextView
        loginButton = binding.loginButton

        imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        checkEmail()
        checkPassword()
        goLogin()

        return binding.root
    }

    //키보드 내리기
    fun hideKeyboard(v:View){
        if(v!=null){
            imm?.hideSoftInputFromWindow(v.windowToken,0)
        }
    }

    // 로그인
    fun goLogin(){
        loginButton.setOnClickListener {
            hideKeyboard(it)
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            Log.d("로그인","로그인 전달 값 : ${email} ${password}")
            requestServer.userService.login(RequestLoginBody(email,password)).enqueue(object :
                Callback<ResponseLoginBody>{
                override fun onResponse(
                    call: Call<ResponseLoginBody>,
                    response: Response<ResponseLoginBody>
                ) {
                    // 응답받은 데이터를 가지고 처리할 코드 작성
                    val repos:ResponseLoginBody? = response.body()
                    Log.d("성공", repos.toString())
                }

                override fun onFailure(call: Call<ResponseLoginBody>, t: Throwable) {
                    Log.d("로그인 결과", t.toString())
                }
            }
            )

        }
    }

    fun checkEmail(){
        emailEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(p0.toString()).matches())
                {
                    errorEmailTextView.text = String.format(resources.getString(R.string.login_fragment_wrong_answer),"이메일 주소")
                    emailEditText.background = context!!.resources.getDrawable(R.drawable.custom_error_input)
                }
                else
                {
                    errorEmailTextView.text = ""
                    emailEditText.background = context!!.resources.getDrawable(R.drawable.custom_input)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                loginButton.isEnabled = passwordEditText.text.toString() != "" && errorEmailTextView.text == ""
            }

        })
    }

    fun checkPassword(){
        passwordEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,16}$", p0.toString()))
                {
                    errorPWTextView.text = String.format(resources.getString(R.string.login_fragment_wrong_answer),"비밀번호")
                    passwordEditText.background = context!!.resources.getDrawable(R.drawable.custom_error_input)
                }
                else
                {
                    errorPWTextView.text = ""
                    passwordEditText.background = context!!.resources.getDrawable(R.drawable.custom_input)

                }
            }

            override fun afterTextChanged(p0: Editable?) {
                loginButton.isEnabled = emailEditText.text.toString() != "" && errorPWTextView.text == ""
            }

        })
    }
}