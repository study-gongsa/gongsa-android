package com.app.gong4

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.gong4.DTO.*
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentMainBinding
import com.app.gong4.util.MainApplication
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var category: List<StudyCategory>
    private lateinit var dataList : ArrayList<StduyGroupItem>
    private lateinit var dataAllList : ArrayList<StduyGroupItem>
    private lateinit var mAdapter : StudyGroupListAdapter
    var mRequest : RequestGroupItemBody?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        goRecommendStudygroup()
        getCategories()

        showEnterDialog()
        showStudyRoomDialog()
        searchKeyword()
        cameraToogle()

        return binding.root
    }


    private fun cameraToogle(){
        binding.isCam.setOnClickListener{
           // mAdapter.filter.filter(true.toString())
            filterListByTooggle(true)
        }
        binding.isFalseCam.setOnClickListener{
            filterListByTooggle(false)
        }

    }

    private fun searchKeyword(){
        var searchViewTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                //검색버튼 입력시 호출, 검색버튼이 없으므로 사용하지 않음
                override fun onQueryTextSubmit(s: String): Boolean {

                    val sAlign = mRequest?.align
                    val sIsCam = mRequest?.isCam
                    val sCategory = mRequest?.categoryUIDs

                    // api 호출
                    RequestServer.studyGroupService.getStudygroupfilterInfo(align = sAlign, isCam = sIsCam, categoryUIDs = sCategory, word = s).enqueue(object :
                        Callback<ResponseGroupItemBody>{
                        override fun onResponse(
                            call: Call<ResponseGroupItemBody>,
                            response: Response<ResponseGroupItemBody>
                        ) {
                            if (response.isSuccessful) {
                                val data: ResponseGroupItemBody? = response.body()
                                data.let { it ->
                                    dataAllList = it!!.data.studyGroupList as ArrayList<StduyGroupItem>
                                    dataList = dataAllList
                                    setAdapter(dataList)
                                }
                            } else {
                                val error = response.errorBody()!!.string().trimIndent()
                                val result = Gson().fromJson(error, ResponseGroupItemBody::class.java)
                                Log.d("응답 값 결과 - tostring", result.toString())
                            }

                        }

                        override fun onFailure(call: Call<ResponseGroupItemBody>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
                    return false
                }

                //텍스트 입력/수정시에 호출
                override fun onQueryTextChange(s: String): Boolean {
                    return false
                }
            }
        binding.searchView.setOnQueryTextListener(searchViewTextListener)
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
                val mDialog = GroupfilterDialog(category)
                val listener = object : DialogResult {
                    override fun result(
                        request: RequestGroupItemBody,
                        category: List<StudyCategory>,
                        Data: List<StduyGroupItem>
                    ) {
                        showCategoryChipList(category)
                        refreshData(Data as ArrayList<StduyGroupItem>)
                        binding.cameraSegmentButton.visibility = View.VISIBLE
                    }
                }
                mDialog.setEventListener(listener)
                mDialog.show(parentFragmentManager, "filterDialog")
            }
        }
    }

    fun getCategories(){
        RequestServer.studyGroupService.getCategory().enqueue(object :
            Callback<ResponseStudycategoryBody>{
            override fun onResponse(
                call: Call<ResponseStudycategoryBody>,
                response: Response<ResponseStudycategoryBody>
            ) {
                category = response.body()!!.data
            }

            override fun onFailure(call: Call<ResponseStudycategoryBody>, t: Throwable) {
                Toast.makeText(context,"서버와의 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT)
            }

        })
    }

    // 카테고리 chip 띄우기
    private fun showCategoryChipList(category: List<StudyCategory>){
        binding.checkChipGroup.removeAllViews()
        for (c in category){
            binding.checkChipGroup.addView(Chip(context).apply {
                text = c.name
                id = c.categoryUID
                width = 48
                height = 22
                chipStrokeWidth = 2f
                chipStrokeColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
                    intArrayOf(Color.parseColor("#2DB57B"),Color.parseColor("#2DB57B"))
                )
                chipBackgroundColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
                    intArrayOf(Color.WHITE, Color.parseColor("#2DB57B"))
                )

                setTextColor(
                    ColorStateList(
                        arrayOf(
                            intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
                        intArrayOf(Color.BLACK, Color.WHITE)
                    )

                )
            })
        }
    }

    // 데이터 리스트 새로고침
    private fun refreshData(data: ArrayList<StduyGroupItem>){
        dataList.clear()
        dataList.addAll(data)
        mAdapter.notifyDataSetChanged()

    }

    fun filterListByTooggle(isCam : Boolean){
        when(isCam){
            true -> {
                dataList = dataAllList.filter { it.isCam } as ArrayList<StduyGroupItem>
            }
            false -> {
                dataList = dataAllList.filter { !it.isCam } as ArrayList<StduyGroupItem>
            }
        }
        Log.d("dataList",dataList.toString())
        setAdapter(dataList)
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
                        dataAllList = it!!.data.studyGroupList as ArrayList<StduyGroupItem>
                        dataList = dataAllList
                        setAdapter(dataList)
                    }
                } else {
                    val error = response.errorBody()!!.string().trimIndent()
                    val result = Gson().fromJson(error, ResponseGroupItemBody::class.java)
                    Log.d("스터디그룹 응답- tostring", result.toString())
                }
            }

            override fun onFailure(call: Call<ResponseGroupItemBody>, t: Throwable) {
                Log.d("결과 - onFailure", t.toString())
                Toast.makeText(context,"서버와의 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT)
            }

        })
    }

    fun setAdapter(list: List<StduyGroupItem>) {
        mAdapter = StudyGroupListAdapter(this, list as ArrayList<StduyGroupItem>)
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter.notifyDataSetChanged()
        binding.recyclerView.setHasFixedSize(true)
    }
}

class StudyGroupListAdapter(private val context: MainFragment, val dataSet: ArrayList<StduyGroupItem>)
    : RecyclerView.Adapter<StudyGroupListAdapter.ViewHolder>() {

    var searchList = ArrayList<StduyGroupItem>()
    var searchAllList = ArrayList<StduyGroupItem>()

    init {
        Log.d("searchList", dataSet.toString())
        searchList.addAll(dataSet)
        searchAllList.addAll(dataSet)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val group_title_textView: TextView
        val group_date_textView: TextView
        val group_image_imageView: ImageView
        val group_cam_button: ImageButton
        val group_info_button: ImageButton

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

    fun getImageGlide(imagePath: String): GlideUrl {
        val USER_TOKEN = MainApplication.prefs.getData("accessToken", "")
        val IMAGE_URL = "${RequestServer.BASE_URL}/api/image/" + imagePath
        val glideUrl = GlideUrl(IMAGE_URL) { mapOf(Pair("Authorization", "Bearer $USER_TOKEN")) }
        return glideUrl
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.group_title_textView.text = dataSet[position].name
        holder.group_date_textView.text =
            "${convertTimestampToDate(dataSet[position].createdAt)} ~ ${
                convertTimestampToDate(dataSet[position].expiredAt)
            }"

        val url = getImageGlide(dataSet[position].imgPath)
        Glide.with(context).load(url).into(holder.group_image_imageView)
        if (!dataSet[position].isCam) {
            holder.group_cam_button.setImageResource(R.drawable.ic_camera_off_22)
        } else {
            holder.group_cam_button.setImageResource(R.drawable.ic_baseline_photo_camera_24)
        }
        holder.group_info_button.setOnClickListener {
            context.showStudyInfoDialog(dataSet[position].studyGroupUID)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    private fun convertTimestampToDate(time: Long): String {
        val sdf = SimpleDateFormat("yy.MM.dd")
        val date = sdf.format(time).toString()
        return date
    }

}