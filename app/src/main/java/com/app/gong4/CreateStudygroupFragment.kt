package com.app.gong4

import android.R
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.app.gong4.DTO.ResponseStudycategoryBody
import com.app.gong4.DTO.StudyCategory
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentCreateStudygroupBinding
import com.google.android.material.chip.Chip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateStudygroupFragment : Fragment() {

    private lateinit var binding: FragmentCreateStudygroupBinding
    private lateinit var categories : List<StudyCategory>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("CreateStudygroup","onCreate")
        getCategories()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(true)

        binding = FragmentCreateStudygroupBinding.inflate(inflater, container, false)

        Log.d("CreateStudygroup","onCreateView")

        //getCategories()
        showCategories()
        createStudyGroup()

        return binding.root
    }

    private fun createStudyGroup(){
        binding.createButton.setOnClickListener {
            //방제목
            //카메라 여부
            //채팅
            //제한인원
            //공개여부
            //벌점
            //스터디 재진입
            //스터디목표시간
        }
    }
    private fun getCategories(){
        categories = arrayListOf()
        RequestServer.studyGroupService.getCategory().enqueue(object :
            Callback<ResponseStudycategoryBody> {
            override fun onResponse(
                call: Call<ResponseStudycategoryBody>,
                response: Response<ResponseStudycategoryBody>
            ) {
                categories = response.body()!!.data
                Log.d("categories :",categories.toString())
            }

            override fun onFailure(call: Call<ResponseStudycategoryBody>, t: Throwable) {
                Toast.makeText(context,"서버와의 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT)
            }

        })
    }

    private fun showCategories(){
        for (c in categories){
            binding.categoriyChipGroup.addView(Chip(context).apply {
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

}