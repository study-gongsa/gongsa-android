package com.app.gong4

import android.app.Dialog
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

import com.app.gong4.DTO.StudyCategory
import com.app.gong4.databinding.GroupfilterDialogBinding

class StudyGroupDialog(context : AppCompatActivity, private val categories:List<StudyCategory>){
//    private lateinit var binding : GroupfilterDialogBinding
    private val dlg = Dialog(context)

    fun show(){
      //  binding = GroupfilterDialogBinding.inflate()
//        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dlg.setContentView(binding.root)
//        dlg.setCancelable(false)
    }

}