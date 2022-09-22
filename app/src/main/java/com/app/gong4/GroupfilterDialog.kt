package com.app.gong4

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment

import com.app.gong4.DTO.StudyCategory
import com.app.gong4.databinding.FragmentMainBinding
import com.app.gong4.databinding.GroupfilterDialogBinding
import com.google.android.material.chip.Chip

class GroupfilterDialog(private val categories:List<StudyCategory>) : DialogFragment() {

    private var _binding: GroupfilterDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GroupfilterDialogBinding.inflate(inflater,container,false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        for (c in categories){
            binding.categoryChipGroup.addView(Chip(context).apply {
                text = c.name
                id = c.categoryUID
            })
        }

        binding.enterButton.setOnClickListener{
            print("저장 구현하기")
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}