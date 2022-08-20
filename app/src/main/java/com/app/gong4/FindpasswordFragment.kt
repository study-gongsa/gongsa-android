package com.app.gong4

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.gong4.databinding.FragmentFindpasswordBinding
import com.app.gong4.databinding.FragmentLoginBinding

class FindpasswordFragment : Fragment() {

    private lateinit var binding: FragmentFindpasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFindpasswordBinding.inflate(inflater, container, false)

        //확인 버튼 비활성화
        binding.confirmButton.isEnabled = false

        checkInput()
        return binding.root
    }

    fun checkInput() {
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if(binding.emailEditText.text.toString()==""){
                    binding.confirmButton.isEnabled = false
                }else{
                    binding.confirmButton.isEnabled = true
                }
            }

        })
    }
}