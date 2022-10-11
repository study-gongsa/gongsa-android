package com.app.gong4

import android.content.Context
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.app.gong4.DTO.ResponseStudygroupinfoBody
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.GroupenterDialogBinding
import com.app.gong4.databinding.GroupfilterDialogBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupenterDialog() : DialogFragment() {

    private var _binding: GroupenterDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GroupenterDialogBinding.inflate(inflater,container,false)
        val view = binding.root
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        moveToStudyGroupInfo()

        return view
    }

    private fun moveToStudyGroupInfo(){
        binding.joinButton.setOnClickListener {
            val code = binding.codeEditview.text.toString()

            RequestServer.studyGroupService.getStudygroupCodeInfo(code).enqueue(object :
                Callback<ResponseStudygroupinfoBody>{
                override fun onResponse(
                    call: Call<ResponseStudygroupinfoBody>,
                    response: Response<ResponseStudygroupinfoBody>
                ) {
                    dismiss()
                    //TODO : studyinfo 띄우기
                    val data = response.body()!!.data
                    Log.d("studyinfo 응답 결과 : ", data.toString())
                    StudygroupinfoDialog(data).show(parentFragmentManager,"InfoDialog")
                }

                override fun onFailure(call: Call<ResponseStudygroupinfoBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            }
            )
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

    fun WindowManager.currentWindowMetricsPointCompat():Point{
        return if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R){
            val windowInsets = currentWindowMetrics.windowInsets
            var insets:Insets = windowInsets.getInsets(WindowInsets.Type.navigationBars())
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
