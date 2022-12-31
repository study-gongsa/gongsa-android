package com.app.gong4.fragments

import androidx.navigation.fragment.findNavController
import com.app.gong4.databinding.FragmentCompleteStudygroupBinding

class CompleteStudygroupFragment : BaseFragment<FragmentCompleteStudygroupBinding>(FragmentCompleteStudygroupBinding::inflate) {

    override fun initView() {
        gotoMyStudyGroup()
    }

    //TODO : 대기실 화면이 없어 가입한스터디그룹 화면으로 이동
    private fun gotoMyStudyGroup(){
        binding.moveButton.setOnClickListener {
            val action = CompleteStudygroupFragmentDirections.actionCompleteStudygroupFragmentToMyStudyGroupFragment()
            findNavController().navigate(action)
        }
    }

}