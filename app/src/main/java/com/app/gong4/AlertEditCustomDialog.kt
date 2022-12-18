package com.app.gong4

import android.content.Context
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.app.gong4.databinding.AlertEditCustomDialogBinding

class AlertEditCustomDialog :DialogFragment(){
    private var _binding : AlertEditCustomDialogBinding?=null
    private val binding get() = _binding!!

    private lateinit var listener: onActionListener

    private lateinit var title:String //타이틀
    private lateinit var hint: String //hint

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AlertEditCustomDialogBinding.inflate(inflater,container,false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        showDialogView()

        return binding.root
    }

    fun showDialogView(){
        binding.dialogTitleTextview.text = title
        binding.dialogContentEditview.hint = hint

        //취소버튼
        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        //커스텀 액션
        binding.actionButton.setOnClickListener {
            listener.onAction()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setActionListener(listener: onActionListener){
        this.listener = listener
    }

    fun setData(title:String,hint:String){
        this.title = title
        this.hint = hint
    }

    fun getCotent():String{
        return binding.dialogContentEditview.text.toString()
    }
}