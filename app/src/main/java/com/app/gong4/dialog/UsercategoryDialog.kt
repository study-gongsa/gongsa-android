package com.app.gong4.dialog

import android.R
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import com.app.gong4.model.StudyCategory
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.UsercategoryDialogBinding
import com.app.gong4.model.req.RequestSaveUserCateogry
import com.app.gong4.model.res.BaseResponse
import com.google.android.material.chip.Chip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class UsercategoryDialog(val categories : ArrayList<StudyCategory>) : BaseDialog<UsercategoryDialogBinding>(UsercategoryDialogBinding::inflate) {

    override fun initDialog() {
        showCategories()
        saveCategory()
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

    private fun saveCategory(){
        binding.saveButton.setOnClickListener {
            val checkedChipList =
                binding.categoryChipGroup.children?.filter { (it as Chip).isChecked }?.map { it.id }?.toList()!!

            val requestSaveUserCateogry = RequestSaveUserCateogry(checkedChipList)
            Log.d("request",requestSaveUserCateogry.toString())
            RequestServer.userCategoryService.putUserCategory(requestSaveUserCateogry).enqueue(object :
                Callback<BaseResponse>{
                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if(response.isSuccessful){
                        val successMsg = resources.getString(com.app.gong4.R.string.main_save_success)
                        Toast.makeText(context,successMsg, Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    Toast.makeText(context,"서버와의 통신이 원활하지 않습니다.",Toast.LENGTH_SHORT)
                }
            })
        }
    }


}
