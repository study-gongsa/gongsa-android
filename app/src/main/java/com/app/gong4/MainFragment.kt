package com.app.gong4

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.gong4.DTO.*
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentMainBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var category: List<StudyCategory>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(true)

        goRecommendStudygroup()
        getCategories() //livedata 로 카테고리 데이터 관리하는게 맞는지..?

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        showEnterDialog()
        showStudyRoomDialog()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(false)
    }

    private fun showEnterDialog(){
        binding.enterButton.setOnClickListener {
            GroupenterDialog().show(parentFragmentManager,"EnterDialog")
        }
    }

    // 스터디 정보 다이얼로그
    fun showStudyInfoDialog(groupId :Int){
        RequestServer.studyGroupService.getStudygroupInfo(groupId).enqueue(object :
            Callback<ResponseStudygroupinfoBody>{
            override fun onResponse(
                call: Call<ResponseStudygroupinfoBody>,
                response: Response<ResponseStudygroupinfoBody>
            ) {
                val data = response.body()!!.data
                Log.d("studyinfo 응답 결과 : ", data.toString())
                Log.d("categories", category.toString())
                StudygroupinfoDialog(data).show(parentFragmentManager,"InfoDialog")
            }

            override fun onFailure(call: Call<ResponseStudygroupinfoBody>, t: Throwable) {
                Log.d("스터디 정보 결과 - onFailure", t.toString())
                Toast.makeText(context,"서버와의 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT)
            }

        })
    }

    // 필터 다이얼로그 보여주기
    private fun showStudyRoomDialog(){
        binding.filterButton.setOnClickListener {
            if(category.isEmpty()){
                getCategories()
            }else {
                GroupfilterDialog(category).show(parentFragmentManager, "filterDialog")
            }
        }
    }

    private fun getCategories(){
        RequestServer.studyGroupService.getCategory().enqueue(object :
            Callback<ResponseStudycategoryBody>{
            override fun onResponse(
                call: Call<ResponseStudycategoryBody>,
                response: Response<ResponseStudycategoryBody>
            ) {
                Log.d("getCategories 응답 결과 : ", response.body()!!.data.toString())
                category = response.body()!!.data
            }

            override fun onFailure(call: Call<ResponseStudycategoryBody>, t: Throwable) {
                Log.d("로그인 결과 - onFailure", t.toString())
                Toast.makeText(context,"서버와의 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT)
            }

        })
    }

    // 추천 스터디 그룹 보여주기
    private fun goRecommendStudygroup() {
        RequestServer.studyGroupService.recommend(type = "main").enqueue(object :
            Callback<ResponseGroupItemBody> {
            override fun onResponse(
                call: Call<ResponseGroupItemBody>,
                response: Response<ResponseGroupItemBody>
            ) {
                if (response.isSuccessful) {
                    val data: ResponseGroupItemBody? = response.body()
                    data.let { it ->
                        val dataList = it!!.data.studyGroupList
                        Log.d("test", dataList.toString())
                        setAdapter(dataList)
                    }
                } else {
                    val error = response.errorBody()!!.string().trimIndent()
                    Log.d("로그인 결과 - tostring", error)
                    val result = Gson().fromJson(error, ResponseLoginBody::class.java)
                    Log.d("로그인 응답 값 결과 - tostring", result.toString())
                }
            }

            override fun onFailure(call: Call<ResponseGroupItemBody>, t: Throwable) {
                Log.d("로그인 결과 - onFailure", t.toString())
                Toast.makeText(context,"서버와의 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT)
            }

        })
    }

    fun setAdapter(list: List<StduyGroupItem>) {
        val mAdapter = StudyGroupListAdapter(this, list as MutableList<StduyGroupItem>)
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter.notifyDataSetChanged()
        binding.recyclerView.setHasFixedSize(true)
    }
}

class StudyGroupListAdapter(private val context: MainFragment, val dataSet: MutableList<StduyGroupItem>)
    : RecyclerView.Adapter<StudyGroupListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val group_title_textView: TextView
        val group_date_textView: TextView
        val group_image_imageView : ImageView
        val group_cam_button : ImageButton
        val group_info_button : ImageButton

        init {
            group_title_textView = view.findViewById(R.id.group_item_title_textView)
            group_date_textView = view.findViewById(R.id.group_item_date_textView)
            group_image_imageView = view.findViewById(R.id.group_item_image_imageView)
            group_cam_button = view.findViewById(R.id.group_cam_button)
            group_info_button = view.findViewById(R.id.group_info_button)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.studygroup_row_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.group_title_textView.text = dataSet[position].name
        holder.group_date_textView.text = "${convertTimestampToDate(dataSet[position].createdAt)} ~ ${convertTimestampToDate(dataSet[position].expiredAt)}"
        // 아직 이미지 규격이 나온게 없어서 기본이미지 출력하도록 개발
        holder.group_image_imageView.setImageResource(R.drawable.ic_gongsa)
        if(!dataSet[position].isCam){
            holder.group_cam_button.setImageResource(R.drawable.ic_camera_off_22)
        }else{
            holder.group_cam_button.setImageResource(R.drawable.ic_baseline_photo_camera_24)
        }
        holder.group_info_button.setOnClickListener {
            context.showStudyInfoDialog(dataSet[position].studyGroupUID)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    private fun convertTimestampToDate(time: Long) : String {
        val sdf = SimpleDateFormat("yy.MM.dd")
        val date = sdf.format(time).toString()
        return date
    }
}