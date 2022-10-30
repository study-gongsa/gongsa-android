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
import android.widget.Toast
import androidx.navigation.findNavController
import com.app.gong4.DTO.RequestLoginBody
import com.app.gong4.DTO.ResponseLoginBody
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentLoginBinding
import com.app.gong4.util.MainApplication
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    val requestServer = RequestServer
    private var imm : InputMethodManager ?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationBar(true)

        checkInput()
        goLogin()
        goSignupScreen()
        goFindPasswordScreen()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationBar(false)
    }

    //키보드 내리기
    fun hideKeyboard(v:View){
        if(v!=null){
            imm?.hideSoftInputFromWindow(v.windowToken,0)
        }
    }

    //회원가입으로 이동
    fun goSignupScreen(){
        binding.signupTextView.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    //비밀번호 찾기로 이동
    fun goFindPasswordScreen(){
        binding.findPasswordTextView.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_findpasswordFragment)
        }
    }

    // 로그인
    // TODO : Log 지우기
    fun goLogin(){
        binding.loginButton.setOnClickListener {
            hideKeyboard(it)
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            requestServer.userService.login(RequestLoginBody(email,password)).enqueue(object :
                Callback<ResponseLoginBody>{
                override fun onResponse(
                    call: Call<ResponseLoginBody>,
                    response: Response<ResponseLoginBody>
                ) {
                    Log.d("로그인 코드", response.code().toString())

                    if(response.isSuccessful()){
                        var repos: ResponseLoginBody? = response.body()
                        Log.d("로그인 결과 - 성공", repos.toString())
                        repos.let { it ->
                           val accessToken = it!!.data.accessToken
                            val refreshToken = it!!.data.refreshToken
                            Log.d("로그인 결과 - refreshToken", accessToken.toString())
                            MainApplication.prefs.setData("accessToken",accessToken)
                            MainApplication.prefs.setData("refreshToken",refreshToken)
                        }
                        it.findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                    }else{
                        val error = response.errorBody()!!.string().trimIndent()
                        Log.d("로그인 결과 - tostring", error)
                        val result = Gson().fromJson(error, ResponseLoginBody::class.java)
                        resetLoginErrorMsg()
                        showLoginErrorMsg(result.location,result.msg)
                    }
                }

                override fun onFailure(call: Call<ResponseLoginBody>, t: Throwable) {
                    Log.d("로그인 결과", t.toString())
                    Toast.makeText(context,"서버와의 통신이 원활하지 않습니다.",Toast.LENGTH_SHORT)
                }
            }
            )

        }
    }

    fun resetLoginErrorMsg(){
        binding.validPasswordTextView.text = ""
        binding.validEmailTextView.text = ""
    }

    // 로그인 에러메시지 보여주는 함수
    fun showLoginErrorMsg(location : String?, msg:String?){
        if(location == "passwd"){
            binding.validPasswordTextView.text = msg
        }else if(location == "email"){
            binding.validEmailTextView.text = msg
        }
       binding.loginButton.isEnabled = false
    }

    // 입력값 체크
    fun checkInput(){
        binding.emailEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.validEmailTextView.text = ""

            }

            override fun afterTextChanged(p0: Editable?) {
                binding.loginButton.isEnabled = true
            }

        })
        binding.passwordEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.validPasswordTextView.text = ""

            }

            override fun afterTextChanged(p0: Editable?) {
                binding.loginButton.isEnabled = true
            }

        })

    }

    fun checkEmail(){
        binding.emailEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(p0.toString()).matches())
                {
                    binding.validEmailTextView.text = String.format(resources.getString(R.string.login_fragment_wrong_answer),"이메일 주소")
                    binding.emailEditText.background = context!!.resources.getDrawable(R.drawable.custom_error_input)
                }
                else
                {
                    binding.validEmailTextView.text = ""
                    binding.emailEditText.background = context!!.resources.getDrawable(R.drawable.custom_input)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.loginButton.isEnabled = binding.passwordEditText.text.toString() != "" && binding.validEmailTextView.text == ""
            }

        })
    }
    fun checkPassword(){
        binding.passwordEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,16}$", p0.toString()))
                {
                    binding.validPasswordTextView.text = String.format(resources.getString(R.string.login_fragment_wrong_answer),"비밀번호")
                    binding.passwordEditText.background = context!!.resources.getDrawable(R.drawable.custom_error_input)
                }
                else
                {
                    binding.validPasswordTextView.text = ""
                    binding.passwordEditText.background = context!!.resources.getDrawable(R.drawable.custom_input)

                }
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.loginButton.isEnabled = binding.emailEditText.text.toString() != "" && binding.validPasswordTextView.text == ""
            }

        })
    }

}