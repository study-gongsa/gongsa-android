package com.app.gong4.fragments

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gong4.databinding.FragmentCompleteStudygroupBinding

class CompleteStudygroupFragment : BaseFragment<FragmentCompleteStudygroupBinding>(FragmentCompleteStudygroupBinding::inflate) {

    private val args by navArgs<CompleteStudygroupFragmentArgs>()

    override fun initView() {
        gotoMyStudyGroup()
    }


    private fun gotoMyStudyGroup(){
        binding.moveButton.setOnClickListener {
            val action = CompleteStudygroupFragmentDirections.actionCompleteStudygroupFragmentToStudyGroupFragment(args.groupUID)
            findNavController().navigate(action)
        }
    }

}