package com.app.gong4

import android.R
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import com.app.gong4.DTO.ResponseGroupItemBody
import com.app.gong4.DTO.StduyGroupItem
import com.app.gong4.DTO.StudyCategory
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.GroupfilterDialogBinding
import com.google.android.material.chip.Chip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupfilterDialog(private val categories:List<StudyCategory>) : DialogFragment() {

    private var _binding: GroupfilterDialogBinding? = null
    private val binding get() = _binding!!
    internal lateinit var listener: DialogResult

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GroupfilterDialogBinding.inflate(inflater,container,false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        showCategories()
        saveFilter();

        return view
    }

    fun setEventListener(listener:DialogResult){
        this.listener = listener
    }

    private fun saveFilter(){
        var checkedIsCame :Boolean = true
        var checkedAlign :String = "latest"
        var checkedChipList : List<Int>
        var checkedCategory : ArrayList<StudyCategory> = arrayListOf()

        binding.saveButton.setOnClickListener{
            binding.filterCamRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                when(checkedId){
                    binding.filterCamTrue.id -> checkedIsCame = true
                    binding.filterCamFalse.id -> checkedIsCame = false
                }
            }
            checkedChipList =
                binding.categoryChipGroup.children?.filter { (it as Chip).isChecked }?.map { it.id }?.toList()!!
            binding.categoryChipGroup.children.filter { (it as Chip).isChecked }.map {
                val studyCategory = StudyCategory((it as Chip).id,(it as Chip).text.toString())
                checkedCategory.add(studyCategory)
            }.toList()
            binding.filterPeriodRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
                when(checkedId){
                    binding.filterPeriodOldRadio.id -> checkedAlign = "expire"
                    binding.filterPeriodNewRadio.id -> checkedAlign = "latest"
                }
            }
            //api 호출
           RequestServer.studyGroupService.getStudygroupfilterInfo(checkedAlign,checkedChipList,checkedIsCame).enqueue(object :
               Callback<ResponseGroupItemBody>{
               override fun onResponse(
                   call: Call<ResponseGroupItemBody>,
                   response: Response<ResponseGroupItemBody>
               ) {
                   val data = response.body()!!.data.studyGroupList
                    // mainfragment에 값 변경하기
                   listener?.result(checkedCategory,data)
                   dismiss()
               }

               override fun onFailure(call: Call<ResponseGroupItemBody>, t: Throwable) {
                   TODO("Not yet implemented")
               }
           })
        }
    }

    private fun showCategories(){
        for (c in categories){
            binding.categoryChipGroup.addView(Chip(context).apply {
                text = c.name
                id = c.categoryUID
                isCheckable = true
                isCheckedIconVisible = false
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
    override fun onResume() {
        super.onResume()

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val windowManager = activity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = windowManager.currentWindowMetricsPointCompat()
        val deviceWidth = size.x

        params?.width = (deviceWidth * 0.8).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    fun WindowManager.currentWindowMetricsPointCompat(): Point {
        return if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R){
            val windowInsets = currentWindowMetrics.windowInsets
            var insets: Insets = windowInsets.getInsets(WindowInsets.Type.navigationBars())
            windowInsets.displayCutout?.run {
                insets = Insets.max(
                    insets,
                    Insets.of(safeInsetLeft,safeInsetTop,safeInsetRight,safeInsetBottom)
                )
            }
            val insetsWidth = insets.right + insets.left
            val insetsHeight = insets.top + insets.bottom
            Point(
                currentWindowMetrics.bounds.width() - insetsWidth,
                currentWindowMetrics.bounds.height() - insetsHeight
            )
        }else{
            Point().apply {
                defaultDisplay.getSize(this)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

interface DialogResult{
    fun result(category:List<StudyCategory>,Data:List<StduyGroupItem>)
}
