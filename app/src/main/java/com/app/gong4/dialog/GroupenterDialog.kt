package com.app.gong4.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import com.app.gong4.model.res.ResponseStudygroupinfoBody
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.GroupenterDialogBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupenterDialog : BaseDialog<GroupenterDialogBinding>(GroupenterDialogBinding::inflate){

    override fun initDialog() {
        binding.joinButton.setOnClickListener {
            val code = binding.codeEditview.text.toString()

            RequestServer.studyGroupService.getStudygroupCodeInfo(code).enqueue(object :
                Callback<ResponseStudygroupinfoBody>{
                override fun onResponse(
                    call: Call<ResponseStudygroupinfoBody>,
                    response: Response<ResponseStudygroupinfoBody>
                ) {
                    dismiss()
                    //TODO : studyinfo 띄우기
                    val data = response.body()!!.data
                    Log.d("studyinfo 응답 결과 : ", data.toString())
                    StudygroupinfoDialog(data).show(parentFragmentManager,"InfoDialog")
                }

                override fun onFailure(call: Call<ResponseStudygroupinfoBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            }
            )
        }
    }
}
