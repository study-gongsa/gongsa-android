package com.app.gong4.dialog

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.gong4.R
import com.app.gong4.databinding.StudygroupinfoDialogBinding
import com.app.gong4.model.req.RequestEnterMember
import com.app.gong4.model.res.ResponseStudygroupinfoBody
import com.app.gong4.utils.NetworkResult
import com.app.gong4.viewmodel.StudyGroupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudygroupinfoDialog(private val data: ResponseStudygroupinfoBody.StudyGroupDetailItem) : BaseDialog<StudygroupinfoDialogBinding>(StudygroupinfoDialogBinding::inflate) {

    private val studyGroupViewModel : StudyGroupViewModel by viewModels()

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

            studyGroupViewModel.joinLiveData.observe(viewLifecycleOwner, Observer {
                when(it){
                    is NetworkResult.Error -> {
                        Toast.makeText(context,it.msg.toString(), Toast.LENGTH_SHORT).show()
                        dismiss()
                    }else ->{
                        val successMsg = resources.getString(R.string.main_join_success)
                        Toast.makeText(context,successMsg, Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                }
            })
            studyGroupViewModel.joinStudyGroup(RequestEnterMember(data.groupUID))
        }
    }

}