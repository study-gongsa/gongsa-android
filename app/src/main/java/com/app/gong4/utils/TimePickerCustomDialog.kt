package com.app.gong4.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.NumberPicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.app.gong4.databinding.TimePickerCustomDialogBinding
import com.app.gong4.onActionListener
import java.util.*


class TimePickerCustomDialog : DialogFragment() {

    private var _binding: TimePickerCustomDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var listener: onActionListener

    private var setHourValue:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TimePickerCustomDialogBinding.inflate(inflater,container,false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        showDialogView()

        return binding.root
    }

    fun showDialogView(){
        val cal = Calendar.getInstance()

        binding.timePicker.setIs24HourView(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.timePicker.hour = cal.get(Calendar.HOUR_OF_DAY)
            binding.timePicker.minute = 0
        }

        setTimePickerInterval(binding.timePicker)

        binding.timePicker.setOnTimeChangedListener { timePicker, hour, minute ->
            this.setHourValue = hour
        }
        binding.dialogYesButton.setOnClickListener {
            listener.onAction()
            dismiss()
        }

        binding.dialogCancelButton.setOnClickListener {
            dismiss()
        }

    }

    private fun setTimePickerInterval(timePicker: TimePicker) {
        try {
            val minutePicker = timePicker.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android")) as NumberPicker
            minutePicker.minValue = 0
            minutePicker.maxValue = 0
//            val displayedValues: MutableList<String> = ArrayList()
//            var i = 0
//            while (i < 60) {
//                displayedValues.add(String.format("%02d", i))
//                i += TIME_PICKER_INTERVAL
//            }
//            minutePicker.displayedValues = displayedValues.toTypedArray()

            val hourPicker = timePicker.findViewById(Resources.getSystem().getIdentifier("hour", "id", "android")) as NumberPicker
            hourPicker.minValue = 1
            hourPicker.maxValue = 24

        } catch (e: Exception) {
            Toast.makeText(context,"error TimePicker",Toast.LENGTH_SHORT).show()
        }
    }

    fun setActionListener(listener: onActionListener){
        this.listener = listener
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

    fun getHour():Int{
        return this.setHourValue
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}