package com.app.gong4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.app.gong4.databinding.FragmentStudyGroupBinding

class StudyGroupFragment : Fragment() {

    lateinit var binding : FragmentStudyGroupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyGroupBinding.inflate(inflater, container, false)

        binding.qnaButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_studyGroupFragment_to_groupQnaListFragment)
        }
        return binding.root
    }

    companion object {
    }
}