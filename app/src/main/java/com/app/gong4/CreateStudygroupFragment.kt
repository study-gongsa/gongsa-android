package com.app.gong4

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.app.gong4.DTO.ResponseStudycategoryBody
import com.app.gong4.DTO.StudyCategory
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentCreateStudygroupBinding
import com.app.gong4.util.AppViewModel
import com.google.android.material.chip.Chip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CreateStudygroupFragment : Fragment() {

    private lateinit var binding: FragmentCreateStudygroupBinding
    private lateinit var categories : List<StudyCategory>
    private val viewModel : AppViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categories = viewModel.getCategoryList()
        //getCategories()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(true)
        binding = FragmentCreateStudygroupBinding.inflate(inflater, container, false)

        showCategories()
        showDatePicker()
        showTimePicker()
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

    private fun showDatePicker(){
        val today = Calendar.getInstance()
        binding.startDate.text ="${today.get(Calendar.YEAR)}-${today.get(Calendar.MONTH)+1}-${today.get(Calendar.DATE)}"
        binding.endDate.text ="${today.get(Calendar.YEAR)}-${today.get(Calendar.MONTH)+1}-${today.get(Calendar.DATE)}"

        binding.endDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                binding.endDate.text = "${year}-${month+1}-${day}"

            }
            DatePickerDialog(requireContext(), data, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                .apply { datePicker.minDate = System.currentTimeMillis() }
                .show()
        }
    }

    private fun showTimePicker(){
        binding.timeTextView.text = "01:00"
        binding.timeTextView.setOnClickListener {
            val cal = Calendar.getInstance()
            val time = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
                binding.timeTextView.text = "${hour}:00"
            }
            TimePickerDialog(requireContext(),time,cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),false)
                .show()
        }
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