package com.app.gong4

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.gong4.model.Ranking
import com.app.gong4.model.res.ResponseMyPageInfoBody
import com.app.gong4.model.ResponseMyStudyGroupRankingBody
import com.app.gong4.model.UserInfo
import com.app.gong4.adapter.StudyRankingAdapter
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentMyPageBinding
import com.app.gong4.utils.CommonService
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(FragmentMyPageBinding::inflate) {
    private lateinit var userInfo : UserInfo

    override fun initView() {
        serverMyPageInfo()
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
        RequestServer.userService.myStudyGroupRanking().enqueue(object :Callback<ResponseMyStudyGroupRankingBody>{
            override fun onResponse(
                call: Call<ResponseMyStudyGroupRankingBody>,
                response: Response<ResponseMyStudyGroupRankingBody>
            ) {
                val data = response.body()!!.data
                setAdapter(data.groupRankList)
            }

            override fun onFailure(call: Call<ResponseMyStudyGroupRankingBody>, t: Throwable) {

            }
        })
    }

    fun setAdapter(list: List<Ranking>) {
        val adapter = StudyRankingAdapter(list as ArrayList<Ranking>)
        binding.profileRankRecyclerview.adapter = adapter
        binding.profileRankRecyclerview.layoutManager = LinearLayoutManager(context)
        adapter.notifyDataSetChanged()
        binding.profileRankRecyclerview.setHasFixedSize(true)
    }

    fun serverMyPageInfo(){
        RequestServer.userService.userMyPage().enqueue(object : Callback<ResponseMyPageInfoBody>{
            override fun onResponse(
                call: Call<ResponseMyPageInfoBody>,
                response: Response<ResponseMyPageInfoBody>
            ) {
                userInfo = response.body()!!.data
                getMyPageInfo(userInfo)
            }

            override fun onFailure(call: Call<ResponseMyPageInfoBody>, t: Throwable) {

            }
        })
    }

    fun getMyPageInfo(info:UserInfo){
        val imgPath = CommonService.getImageGlide(info.imgPath)
        Glide.with(requireContext()).load(imgPath).error(R.drawable.error_image).into(binding.profileImageview)

        val studyHour = info.totalStudyTime.substring(0,2)
        val studyMinute = info.totalStudyTime.substring(3,5)
        binding.profileNameTextview.text = info.nickname
        binding.profileTimeTextview.text = String.format(resources.getString(R.string.mypage_study_time),studyHour,studyMinute)
        binding.profileLevelTextview.text = String.format(resources.getString(R.string.mypage_study_level),info.level)
        binding.profilePercentageTextview.text = String.format(resources.getString(R.string.mypage_study_percentage),info.percentage.toInt())

    }

}