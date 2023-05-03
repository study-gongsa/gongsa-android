package com.app.gong4.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.app.gong4.databinding.AlertCustomDialogBinding
import com.app.gong4.databinding.AlertEditCustomDialogBinding
import com.app.gong4.databinding.GroupenterDialogBinding
import com.app.gong4.onActionListener

class AlertEditCustomDialog :BaseDialog<AlertEditCustomDialogBinding>(AlertEditCustomDialogBinding::inflate){
    private lateinit var title:String //타이틀
    private lateinit var hint: String //hint

    override fun initDialog() {
        binding.dialogTitleTextview.text = title
        binding.dialogContentEditview.hint = hint

        //취소버튼
        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        //커스텀 액션
        binding.actionButton.setOnClickListener {
            getActionListener().onAction()
        }
    }

    fun setData(title:String,hint:String){
        this.title = title
        this.hint = hint
    }

    fun getCotent():String{
        return binding.dialogContentEditview.text.toString()
    }
}