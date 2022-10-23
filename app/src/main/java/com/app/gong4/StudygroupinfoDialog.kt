package com.app.gong4

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.app.gong4.DTO.StduyGroupDetailItem
import com.app.gong4.DTO.StduyGroupItem
import com.app.gong4.DTO.StudyCategory
import com.app.gong4.databinding.StudygroupinfoDialogBinding
import com.google.android.material.chip.Chip

class StudygroupinfoDialog(private val data: StduyGroupDetailItem) : DialogFragment() {

    private var _binding: StudygroupinfoDialogBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = StudygroupinfoDialogBinding.inflate(inflater,container,false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.iscamTextview.text = if(data.isCam) resources.getString(R.string.main_study_info_isCam_true) else resources.getString(R.string.main_study_info_isCam_false)

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
            print("가입하기 구현하기")

        }

        return view
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