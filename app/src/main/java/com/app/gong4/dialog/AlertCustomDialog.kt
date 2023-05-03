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
import com.app.gong4.onActionListener

class AlertCustomDialog : BaseDialog<AlertCustomDialogBinding>(AlertCustomDialogBinding::inflate){
    private lateinit var title:String //타이틀
    private lateinit var content:String //내용

    override fun initDialog() {
        binding.dialogTitleTextview.text = title
        binding.dialogContentTextview.text = content

        //취소버튼
        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        //커스텀 액션
        binding.actionButton.setOnClickListener {
            getActionListener().onAction()
        }
    }

    fun setData(title:String,content:String){
        this.title = title
        this.content = content
    }

    fun setActionButtonText(text:String){
        binding.actionButton.text = text
    }

}