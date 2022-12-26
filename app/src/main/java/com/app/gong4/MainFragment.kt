package com.app.gong4

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.gong4.model.*
import com.app.gong4.adapter.StudyGroupListAdapter
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentMainBinding
import com.app.gong4.util.AppViewModel
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private lateinit var category: ArrayList<StudyCategory>
    private lateinit var dataList : ArrayList<StduyGroupItem>
    private lateinit var dataAllList : ArrayList<StduyGroupItem>
    private lateinit var mAdapter : StudyGroupListAdapter
    private val viewModel : AppViewModel by activityViewModels()
    var mRequest : RequestGroupItemBody?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(true)

        getCategories()
    }

    override fun initView() {
        CoroutineScope(Dispatchers.IO).launch {
            goRecommendStudygroup()
            getUserCategory()
        }

        showEnterDialog()
        showStudyRoomDialog()
        searchKeyword()
        cameraToogle()
    }

    private fun getUserCategory() : ArrayList<UserCategory>{
        var userCategory : List<UserCategory> = arrayListOf()
        RequestServer.userCategoryService.getUserCategory().enqueue(object :
            Callback<ResponseUserCategory>{
            override fun onResponse(
                call: Call<ResponseUserCategory>,
                response: Response<ResponseUserCategory>
            ) {
                userCategory = response.body()!!.data
                if(userCategory.isEmpty()){
                    UsercategoryDialog(viewModel.getCategoryList()).show(parentFragmentManager,"UserCategoryDialog")
                }else{
                    viewModel.initUserCategoryList(userCategory as ArrayList<UserCategory>)
                }
            }

            override fun onFailure(call: Call<ResponseUserCategory>, t: Throwable) {
                TODO("Not yet implemented")
            }
       })
        return userCategory as ArrayList<UserCategory>
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
                Log.d("studyinfo 응답 결과 : ", response.toString())
                val data = response.body()!!.data
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
                category = response.body()!!.data as ArrayList<StudyCategory>
                viewModel.initCategoryList(category)
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