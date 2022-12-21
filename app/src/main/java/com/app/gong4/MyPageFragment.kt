package com.app.gong4

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.gong4.DTO.*
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentMyPageBinding
import com.app.gong4.util.CommonService
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageFragment : Fragment() {
    private lateinit var binding: FragmentMyPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(inflater, container, false)
        serverMyPageInfo()
        myRankInfo()
        return binding.root
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
                val data = response.body()!!.data
                Log.d("data",data.toString())
                getMyPageInfo(data)
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
class StudyRankingAdapter(val data:ArrayList<Ranking>) :
    RecyclerView.Adapter<StudyRankingAdapter.ViewHolder>() {

    inner class ViewHolder(view:View): RecyclerView.ViewHolder(view) {
        private val groupNameTextview : TextView
        private val groupRecyclerView : RecyclerView
        init {
            groupNameTextview = view.findViewById(R.id.group_item_name_textview)
            groupRecyclerView = view.findViewById(R.id.group_item_rank_recyclerview)
        }

        fun bind(ranking: Ranking){
            groupNameTextview.text = "${ranking.name} 스터디 그룹 내 랭킹 정보"
            groupRecyclerView.apply {
                adapter = RankItemAdapter(ranking.members as ArrayList<Member>)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.studygroup_ranking_group_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}

class RankItemAdapter(val data:ArrayList<Member>) : RecyclerView.Adapter<RankItemAdapter.ViewHolder>(){
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        private val rankItemTextView : TextView
        init {
            rankItemTextView = view.findViewById(R.id.rank_item_info_textview)
        }

        fun bind(item:Member){
            rankItemTextView.text = "${item.ranking}위 : ${item.nickname}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.studygroup_ranking_member_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}