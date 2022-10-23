package com.app.gong4

import android.R
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.app.gong4.DTO.ResponseStudycategoryBody
import com.app.gong4.DTO.StudyCategory
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentCreateStudygroupBinding
import com.app.gong4.databinding.FragmentLoginBinding
import com.app.gong4.databinding.FragmentMainBinding
import com.app.gong4.util.DataViewModel
import com.google.android.material.chip.Chip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateStudygroupFragment : Fragment() {

    private lateinit var binding: FragmentCreateStudygroupBinding
    private lateinit var categories : List<StudyCategory>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateStudygroupBinding.inflate(inflater, container, false)
        categories = DataViewModel().getCategories()

        showCategories()
        return binding.root
    }

    private fun showCategories(){
        for (c in categories){
            binding.chipGroup2.addView(Chip(context).apply {
                text = c.name
                id = c.categoryUID
                isCheckable = true
                isCheckedIconVisible = false
                width = 48
                height = 22
                chipBackgroundColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-R.attr.state_checked), intArrayOf(R.attr.state_checked)),
                    intArrayOf(Color.parseColor("#E8E8E8"), Color.parseColor("#2DB57B"))
                )

                setTextColor(
                    ColorStateList(
                        arrayOf(
                            intArrayOf(-R.attr.state_checked), intArrayOf(R.attr.state_checked)),
                        intArrayOf(Color.BLACK, Color.WHITE)
                    )

                )
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(false)
    }

}