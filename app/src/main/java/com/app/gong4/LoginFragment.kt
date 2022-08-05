package com.app.gong4

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.app.gong4.databinding.FragmentLoginBinding
import java.util.regex.Pattern

class LoginFragment : Fragment() {

    lateinit var emailEditText : EditText
    lateinit var passwordEditText: EditText
    lateinit var errorEmailTextView : TextView
    lateinit var errorPWTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText

        errorEmailTextView = binding.validEmailTextView
        errorPWTextView = binding.validPasswordTextView

        checkEmail()
        checkPassword()

        return binding.root
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
            }

        })
    }
}