package com.app.gong4.dialog

import android.R
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import com.app.gong4.model.res.ResponseGroupItemBody
import com.app.gong4.model.StudyGroupItem
import com.app.gong4.model.StudyCategory
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.GroupfilterDialogBinding
import com.app.gong4.model.req.RequestGroupItemBody
import com.google.android.material.chip.Chip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupfilterDialog(private val categories:List<StudyCategory>) : BaseDialog<GroupfilterDialogBinding>(GroupfilterDialogBinding::inflate) {

    internal lateinit var listener: DialogResult

    override fun initDialog() {
        showCategories()
        saveFilter()
    }

    fun setEventListener(listener: DialogResult){
        this.listener = listener
    }

    private fun saveFilter(){
        var checkedIsCam :Boolean = true
        var checkedCam : Int = 1 // 모두 보기 선택 변수
        var checkedAlign :String = "latest"
        var checkedChipList : List<Int>
        var checkedCategory : ArrayList<StudyCategory> = arrayListOf()

        binding.filterCamRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                binding.filterCamAll.id -> checkedCam = 1
                binding.filterCamTrue.id -> {
                    checkedCam = 0
                    checkedIsCam = true
                }
                binding.filterCamFalse.id -> {
                    checkedCam = 0
                    checkedIsCam = false
                }
            }
        }

        binding.saveButton.setOnClickListener{
            checkedChipList =
                binding.categoryChipGroup.children?.filter { (it as Chip).isChecked }?.map { it.id }?.toList()!!
            binding.categoryChipGroup.children.filter { (it as Chip).isChecked }.map {
                val studyCategory = StudyCategory((it as Chip).id,(it as Chip).text.toString())
                checkedCategory.add(studyCategory)
            }.toList()
            binding.filterPeriodRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
                when(checkedId){
                    binding.filterPeriodOldRadio.id -> checkedAlign = "expire"
                    binding.filterPeriodNewRadio.id -> checkedAlign = "latest"
                }
            }

            //api 호출
            val request = RequestGroupItemBody(checkedAlign,checkedChipList,checkedIsCam,null)
            if(checkedCam == 1){
                RequestServer.studyGroupService.getStudygroupfilterInfo(align = checkedAlign, categoryUIDs = checkedChipList).enqueue(object :
                    Callback<ResponseGroupItemBody>{
                    override fun onResponse(
                        call: Call<ResponseGroupItemBody>,
                        response: Response<ResponseGroupItemBody>
                    ) {
                        val data = response.body()!!.data.studyGroupList
                        // mainfragment에 값 변경하기
                        listener?.result(request,checkedCategory,data)
                        dismiss()
                    }

                    override fun onFailure(call: Call<ResponseGroupItemBody>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            }else{
                RequestServer.studyGroupService.getStudygroupfilterInfo(
                    align = checkedAlign, categoryUIDs = checkedChipList,
                    isCam =checkedIsCam).enqueue(object :
                    Callback<ResponseGroupItemBody>{
                    override fun onResponse(
                        call: Call<ResponseGroupItemBody>,
                        response: Response<ResponseGroupItemBody>
                    ) {
                        val data = response.body()!!.data.studyGroupList
                        // mainfragment에 값 변경하기
                        listener?.result(request,checkedCategory,data)
                        dismiss()
                    }

                    override fun onFailure(call: Call<ResponseGroupItemBody>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            }

        }
    }

    private fun showCategories(){
        for (c in categories){
            binding.categoryChipGroup.addView(Chip(context).apply {
                text = c.name
                id = c.categoryUID
                isCheckable = true
                isCheckedIconVisible = false
                width = 56
                height = 28
                chipStrokeWidth = 2f
              //  setTextSize(TypedValue.COMPLEX_UNIT_DIP,10f)
                chipStrokeColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-R.attr.state_checked), intArrayOf(R.attr.state_checked)),
                    intArrayOf(Color.parseColor("#2DB57B"),Color.parseColor("#2DB57B"))
                )
                chipBackgroundColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-R.attr.state_checked), intArrayOf(R.attr.state_checked)),
                    intArrayOf(Color.WHITE, Color.parseColor("#2DB57B"))
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

interface DialogResult{
    fun result(request:RequestGroupItemBody,category:List<StudyCategory>,Data:List<StudyGroupItem>)
}
