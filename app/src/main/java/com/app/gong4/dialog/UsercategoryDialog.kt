package com.app.gong4.dialog

import android.R
import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.gong4.model.StudyCategory
import com.app.gong4.databinding.UsercategoryDialogBinding
import com.app.gong4.model.req.RequestSaveUserCateogry
import com.app.gong4.utils.NetworkResult
import com.app.gong4.viewmodel.CategoryViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@AndroidEntryPoint
class UsercategoryDialog(val categories : ArrayList<StudyCategory>) : BaseDialog<UsercategoryDialogBinding>(UsercategoryDialogBinding::inflate) {

    private val categoryViewModel : CategoryViewModel by viewModels()

    override fun initDialog() {
        showCategories()
        saveCategory()
    }

    private fun showCategories(){
        for (c in categories){
            binding.categoryChipGroup.addView(Chip(context).apply {
                text = c.name
                id = c.categoryUID
                isCheckable = true
                isCheckedIconVisible = false
                width = 56
                height = 28
                chipStrokeWidth = 2f
                //  setTextSize(TypedValue.COMPLEX_UNIT_DIP,10f)
                chipStrokeColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-R.attr.state_checked), intArrayOf(R.attr.state_checked)),
                    intArrayOf(Color.parseColor("#2DB57B"),Color.parseColor("#2DB57B"))
                )
                chipBackgroundColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-R.attr.state_checked), intArrayOf(R.attr.state_checked)),
                    intArrayOf(Color.WHITE, Color.parseColor("#2DB57B"))
                )

                setTextColor(
                    ColorStateList(
                        arrayOf(
                            intArrayOf(-R.attr.state_checked), intArrayOf(R.attr.state_checked)),
                        intArrayOf(Color.BLACK, Color.WHITE)
                    )

                )
            })
        }
    }

    private fun saveCategory(){
        binding.saveButton.setOnClickListener {
            val checkedChipList =
                binding.categoryChipGroup.children?.filter { (it as Chip).isChecked }?.map { it.id }?.toList()!!

            val requestSaveUserCateogry = RequestSaveUserCateogry(checkedChipList)

            categoryViewModel.putCategoryLiveData.observe(viewLifecycleOwner, Observer {
                when(it){
                    is NetworkResult.Success -> {
                        val successMsg = resources.getString(com.app.gong4.R.string.main_save_success)
                        Toast.makeText(context,successMsg, Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                    else -> {
                        Toast.makeText(context,it.msg.toString(), Toast.LENGTH_SHORT)
                    }
                }
            })
            categoryViewModel.putCategoryList(requestSaveUserCateogry)
        }
    }



}
