package com.app.gong4.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.app.gong4.R
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.StudygroupinfoDialogBinding
import com.app.gong4.model.req.RequestEnterMember
import com.app.gong4.model.res.ResponseEnterMember
import com.app.gong4.model.res.ResponseStudygroupinfoBody
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudygroupinfoDialog(private val data: ResponseStudygroupinfoBody.StudyGroupDetailItem) : BaseDialog<StudygroupinfoDialogBinding>(StudygroupinfoDialogBinding::inflate) {

    @SuppressLint("ResourceAsColor")
    override fun initDialog() {
        binding.studynameTextview.text = data.name
        binding.iscamTextview.text = if(data.isCam) resources.getString(R.string.main_study_info_isCam_true) else resources.getString(
            R.string.main_study_info_isCam_false
        )

        binding.categoriesTextview.text = StringBuilder().apply {
            data.categories.forEach {
                append(it.name)
                append(", ")
            }
            deleteCharAt(lastIndexOf(", "))
        }.toString()

        binding.peoplecntTextview.text = String.format(resources.getString(R.string.main_study_info_pepole_cnt),data.maxMember)

        if(data.maxMember == data.currentMember){
            binding.remainpeopleTextview.text = resources.getString(R.string.main_study_info_pepole_cnt_err_msg)
            binding.joinButton.setBackgroundColor(R.color.black02)
            binding.joinButton.isClickable = false
            binding.buttonText.setTextColor(R.color.black03)
        }else{
            binding.remainpeopleTextview.text = String.format(resources.getString(R.string.main_study_info_pepole_cnt_msg),data.maxMember - data.currentMember)
        }

        binding.joinButton.setOnClickListener{
            RequestServer.studyGroupService.getStudyEnter(RequestEnterMember(data.groupUID)).enqueue(object :
                Callback<ResponseEnterMember>{
                override fun onResponse(
                    call: Call<ResponseEnterMember>,
                    response: Response<ResponseEnterMember>
                ) {
                    if(response.isSuccessful) {
                        val successMsg = resources.getString(R.string.main_join_success)
                        Toast.makeText(context,successMsg, Toast.LENGTH_SHORT).show()
                        dismiss()
                    }else{
                        val error = response.errorBody()!!.string().trimIndent()
                        val msg = Gson().fromJson(error, ResponseEnterMember::class.java).msg
                        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                }

                override fun onFailure(call: Call<ResponseEnterMember>, t: Throwable) {
                    Toast.makeText(context,"서버와의 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}