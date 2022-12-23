package com.app.gong4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.gong4.DTO.Ranking
import com.app.gong4.DTO.ResponseMyPageInfoBody
import com.app.gong4.DTO.ResponseMyStudyGroupRankingBody
import com.app.gong4.DTO.UserInfo
import com.app.gong4.adapter.StudyRankingAdapter
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentMyPageBinding
import com.app.gong4.util.CommonService
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
    }

    fun clickQnaButton(){
        binding.profileQnaButton.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageFragmentToMyPageQnaFragment(userInfo = userInfo)
            findNavController().navigate(action)
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
        val imgPath = CommonService().getImageGlide(info.imgPath)
        Glide.with(requireContext()).load(imgPath).into(binding.profileImageview)

        val studyHour = info.totalStudyTime.substring(0,2)
        val studyMinute = info.totalStudyTime.substring(3,5)
        binding.profileNameTextview.text = info.nickname
        binding.profileTimeTextview.text = String.format(resources.getString(R.string.mypage_study_time),studyHour,studyMinute)
        binding.profileLevelTextview.text = String.format(resources.getString(R.string.mypage_study_level),info.level)
        binding.profilePercentageTextview.text = String.format(resources.getString(R.string.mypage_study_percentage),info.percentage.toInt())

    }

}