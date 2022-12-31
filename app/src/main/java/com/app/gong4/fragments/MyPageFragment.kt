package com.app.gong4.fragments

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.gong4.R
import com.app.gong4.model.Ranking
import com.app.gong4.model.UserInfo
import com.app.gong4.adapter.StudyRankingAdapter
import com.app.gong4.databinding.FragmentMyPageBinding
import com.app.gong4.utils.CommonService
import com.app.gong4.utils.NetworkResult
import com.app.gong4.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(FragmentMyPageBinding::inflate) {
    private lateinit var userInfo : UserInfo
    private val userViewModel : UserViewModel by activityViewModels()

    override fun initView() {
        getMyPageInfo()
        myRankInfo()
        clickQnaButton()
        clickSettingButton()
    }

    fun clickQnaButton(){
        binding.profileQnaButton.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageFragmentToMyPageQnaFragment(userInfo = userInfo)
            findNavController().navigate(action)
        }
    }

    fun clickSettingButton(){
        binding.profileSettingButton.setOnClickListener {
            findNavController().navigate(R.id.action_myPageFragment_to_settingFragment)
        }
    }

    fun myRankInfo(){
        userViewModel.rankListLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    setAdapter(it.data!!)
                }
                else -> {
                    showToastMessage(it.msg.toString())
                }
            }
        })
        userViewModel.myStudyGroupRanking()
    }

    fun setAdapter(list: List<Ranking>) {
        val adapter = StudyRankingAdapter(list as ArrayList<Ranking>)
        binding.profileRankRecyclerview.adapter = adapter
        binding.profileRankRecyclerview.layoutManager = LinearLayoutManager(context)
        adapter.notifyDataSetChanged()
        binding.profileRankRecyclerview.setHasFixedSize(true)
    }

    fun getMyPageInfo(){
        userInfo = userViewModel.userInfoRes.value!!.data!!

        val imgPath = CommonService.getImageGlide(userInfo.imgPath)
        Glide.with(requireContext()).load(imgPath).error(R.drawable.error_image).into(binding.profileImageview)

        val studyHour = userInfo.totalStudyTime.substring(0,2)
        val studyMinute = userInfo.totalStudyTime.substring(3,5)
        binding.profileNameTextview.text = userInfo.nickname
        binding.profileTimeTextview.text = String.format(resources.getString(R.string.mypage_study_time),studyHour,studyMinute)
        binding.profileLevelTextview.text = String.format(resources.getString(R.string.mypage_study_level),userInfo.level)
        binding.profilePercentageTextview.text = String.format(resources.getString(R.string.mypage_study_percentage),userInfo.percentage.toInt())

    }

}