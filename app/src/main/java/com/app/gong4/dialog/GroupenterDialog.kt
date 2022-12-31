package com.app.gong4.dialog

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.gong4.databinding.GroupenterDialogBinding
import com.app.gong4.utils.NetworkResult
import com.app.gong4.viewmodel.StudyGroupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupenterDialog : BaseDialog<GroupenterDialogBinding>(GroupenterDialogBinding::inflate){

    private val studyGroupViewModel: StudyGroupViewModel by viewModels()

    override fun initDialog() {
        binding.joinButton.setOnClickListener {
            val code = binding.codeEditview.text.toString()

            studyGroupViewModel.studyGroupInfoLiveData.observe(viewLifecycleOwner, Observer {
                when(it){
                    is NetworkResult.Success -> {
                        dismiss()
                        StudygroupinfoDialog(it.data!!).show(parentFragmentManager,"InfoDialog")
                    }
                    else -> {
                        Toast.makeText(context,it.msg.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            })
            studyGroupViewModel.getStudyGroupCodeInfo(code)
        }
    }
}
