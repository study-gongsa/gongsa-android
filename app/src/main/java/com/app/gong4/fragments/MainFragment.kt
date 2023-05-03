package com.app.gong4.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.gong4.*
import com.app.gong4.model.*
import com.app.gong4.adapter.StudyGroupListAdapter
import com.app.gong4.databinding.FragmentMainBinding
import com.app.gong4.dialog.*
import com.app.gong4.dialog.StudygroupinfoDialog
import com.app.gong4.model.req.RequestGroupItemBody
import com.app.gong4.utils.DebounceSearchListener
import com.app.gong4.utils.NetworkResult
import com.app.gong4.viewmodel.CategoryViewModel
import com.app.gong4.viewmodel.StudyGroupViewModel
import com.app.gong4.viewmodel.UserViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private lateinit var dataList : ArrayList<StudyGroupItem>
    private lateinit var dataAllList : ArrayList<StudyGroupItem>
    private lateinit var mAdapter : StudyGroupListAdapter

    private val categoryViewModel : CategoryViewModel by activityViewModels()
    private val studyViewModel : StudyGroupViewModel by viewModels()
    private val userViewModel : UserViewModel by activityViewModels()

    var mRequest : RequestGroupItemBody?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(true)

    }

    override fun initView() {
        getCategories()
        goRecommendStudygroup()
        getUserCategory()

        showEnterDialog()
        showStudyRoomDialog()
        searchKeyword()
        cameraToogle()
        serverMyPageInfo()
    }

    fun serverMyPageInfo(){
        userViewModel.userInfoRes.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    val data = it.data!!
                    MainApplication.tokenManager.saveUserName(data.nickname)
                }
                is NetworkResult.Error -> {
                    showToastMessage(it.msg.toString())
                }
                else -> TODO()
            }
        })
        userViewModel.getUserInfo()
    }

    private fun getUserCategory() {
        categoryViewModel.getUserCategoryList()
        categoryViewModel.myCategoryLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    if(it.data!!.isEmpty()){
                        UsercategoryDialog(it.data!! as ArrayList<StudyCategory>).show(parentFragmentManager,"UserCategoryDialog")
                    }
                }
                is NetworkResult.Error -> {
                    showToastMessage(it.msg.toString())
                }
                else -> TODO()
            }
        })
    }

    private fun cameraToogle(){
        binding.isCam.setOnClickListener{
            filterListByTooggle(true)
        }
        binding.isFalseCam.setOnClickListener{
            filterListByTooggle(false)
        }

    }

    private fun searchKeyword(){
        val debounceSearchListener = DebounceSearchListener(delay = 500L) { query ->
            val sAlign = mRequest?.align
            val sIsCam = mRequest?.isCam
            val sCategory = mRequest?.categoryUIDs

            searchWordApiCall(sAlign, sIsCam, sCategory, query)
        }
        binding.searchView.setOnQueryTextListener(debounceSearchListener)
    }

    private fun searchWordApiCall(sAlign:String?,sIsCam:Boolean?,sCategory:List<Int>?,s:String?){
        // api 호출
        studyViewModel.getStudyGroupFilterInfo(align = sAlign, isCam = sIsCam, categoryUIDs = sCategory, word = s)
        studyViewModel.recommendGroupLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    val data = it.data
                    data.let { it ->
                        dataAllList = it!! as ArrayList<StudyGroupItem>
                        dataList = dataAllList
                        setAdapter(dataList)
                    }
                }
                else -> {
                    showToastMessage(it.msg.toString())
                }
            }
        })
    }

    private fun showEnterDialog(){
        binding.enterButton.setOnClickListener {
            GroupenterDialog().show(parentFragmentManager,"EnterDialog")
        }
    }

    // 스터디 정보 다이얼로그
    fun showStudyInfoDialog(groupId :Int){
        studyViewModel.getStudyGroupInfo(groupId)
        studyViewModel.studyGroupInfoLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    StudygroupinfoDialog(it.data!!).show(parentFragmentManager,"InfoDialog")
                }
                else -> {
                    showToastMessage(it.msg.toString())
                }
            }
        })
    }

    // 필터 다이얼로그 보여주기
    private fun showStudyRoomDialog(){
        binding.filterButton.setOnClickListener {
            val category = categoryViewModel.categoryLiveData.value!!.data!!
            if(category.isEmpty()){
                getCategories()
            }else {
                val mDialog = GroupfilterDialog(category)
                mDialog.setEventListener(object : DialogResult{
                    override fun result(
                        request: RequestGroupItemBody,
                        category: List<StudyCategory>,
                        Data: List<StudyGroupItem>
                    ) {
                        showCategoryChipList(category)
                        refreshData(Data as ArrayList<StudyGroupItem>)
                        binding.cameraSegmentButton.visibility = View.VISIBLE
                    }

                })
                mDialog.show(parentFragmentManager, "filterDialog")
            }
        }
    }

    private fun getCategories(){
        categoryViewModel.getCategoryList()
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
    private fun refreshData(data: ArrayList<StudyGroupItem>){
        dataList.clear()
        dataList.addAll(data)
        mAdapter.notifyDataSetChanged()

    }

    fun filterListByTooggle(isCam : Boolean){
        when(isCam){
            true -> {
                dataList = dataAllList.filter { it.isCam } as ArrayList<StudyGroupItem>
            }
            false -> {
                dataList = dataAllList.filter { !it.isCam } as ArrayList<StudyGroupItem>
            }
        }
        setAdapter(dataList)
    }

    // 추천 스터디 그룹 보여주기
    private fun goRecommendStudygroup() {
        studyViewModel.recommendGroupLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    dataAllList = it!!.data as ArrayList<StudyGroupItem>
                    dataList = dataAllList
                    setAdapter(dataList)
                }
                is NetworkResult.Error -> {
                    showToastMessage(it.msg.toString())
                }
                else -> TODO()
            }
        })
        studyViewModel.getRecommendStudyGroup()
    }

    fun setAdapter(list: List<StudyGroupItem>) {
        mAdapter = StudyGroupListAdapter(this, list as ArrayList<StudyGroupItem>)
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter.notifyDataSetChanged()
        binding.recyclerView.setHasFixedSize(true)
    }
}
