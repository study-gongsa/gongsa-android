package com.app.gong4.fragments

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.gong4.MainActivity
import com.app.gong4.R
import com.app.gong4.model.QnaItem
import com.app.gong4.model.res.ResponseQnaListBody
import com.app.gong4.model.UserInfo
import com.app.gong4.adapter.QnaListAdapter
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentMyPageQnaBinding
import com.app.gong4.onMoveAdapterListener
import com.app.gong4.utils.CommonService
import com.app.gong4.utils.NetworkResult
import com.app.gong4.viewmodel.QnaViewModel
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyPageQnaFragment : BaseFragment<FragmentMyPageQnaBinding>(FragmentMyPageQnaBinding::inflate) {
    private val args by navArgs<MyPageQnaFragmentArgs>()

    private val qnaViewModel : QnaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(false)
    }

    override fun initView() {
        getMyPageInfo(args.userInfo)
        getQnaList()
    }

    override fun onStop() {
        super.onStop()
        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(true)
    }

    fun getMyPageInfo(info: UserInfo){
        val imgPath = CommonService.getImageGlide(info.imgPath)
        Glide.with(requireContext()).load(imgPath).error(R.drawable.error_image).into(binding.profileImageview)

        val studyHour = info.totalStudyTime.substring(0,2)
        val studyMinute = info.totalStudyTime.substring(3,5)
        binding.profileNameTextview.text = info.nickname
        binding.profileTimeTextview.text = String.format(resources.getString(R.string.mypage_study_time),studyHour,studyMinute)
        binding.profileLevelTextview.text = String.format(resources.getString(R.string.mypage_study_level),info.level)
        binding.profilePercentageTextview.text = String.format(resources.getString(R.string.mypage_study_percentage),info.percentage.toInt())

    }

    fun getQnaList(){
        qnaViewModel.myQnaLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    setAdapter(it.data!!)
                }
                is NetworkResult.Error -> {

                }
                else -> TODO()
            }
        })
        qnaViewModel.getMyQnaList()
    }

    fun setAdapter(list: List<QnaItem>) {
        val adapter = QnaListAdapter(list as ArrayList<QnaItem>, object : onMoveAdapterListener {
            override fun onMoveQnaDetail(id: Int): NavDirections {
                return MyPageQnaFragmentDirections.actionMyPageQnaFragmentToGroupQnaDetailFragment(id)
            }
        })
        binding.qnaRecylcerview.adapter = adapter
        binding.qnaRecylcerview.layoutManager = LinearLayoutManager(context)
        adapter.notifyDataSetChanged()
        binding.qnaRecylcerview.setHasFixedSize(true)
    }
}