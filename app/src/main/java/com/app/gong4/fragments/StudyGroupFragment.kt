package com.app.gong4.fragments

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.gong4.R
import com.app.gong4.model.*
import com.app.gong4.adapter.CategoryAdapter
import com.app.gong4.adapter.PeopleAdapter
import com.app.gong4.databinding.FragmentStudyGroupBinding
import com.app.gong4.dialog.AlertCustomDialog
import com.app.gong4.model.res.ResponseStudygroupinfoBody
import com.app.gong4.onActionListener
import com.app.gong4.utils.CommonService
import com.app.gong4.utils.NetworkResult
import com.app.gong4.viewmodel.StudyGroupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudyGroupFragment : BaseFragment<FragmentStudyGroupBinding>(FragmentStudyGroupBinding::inflate){

    private val args by navArgs<StudyGroupFragmentArgs>()
    private lateinit var cAdapter : CategoryAdapter
    private lateinit var pAdapter : PeopleAdapter

    private val studyViewModel : StudyGroupViewModel by viewModels()

    override fun initView() {
        getStudyGroupInfo(args.pid)
        getPeopleInfo(args.pid)
    }

    override fun onResume() {
        super.onResume()

        clickQnaButton()
        leaveGroup()
    }

    private fun leaveGroup(){
        binding.leaveButton.setOnClickListener {
            val dialog = AlertCustomDialog()
            dialog.setData(resources.getString(R.string.study_qna_delete_dialog_title),resources.getString(
                R.string.study_qna_delete_dialog_content
            ))
            dialog.setActionListener(object : onActionListener {
                override fun onAction() {
                    gotoServer()
                    dialog.dismiss()
                }
            })
            dialog.show(parentFragmentManager,"RemoveDialog")
        }
    }

    private fun gotoServer(){
        studyViewModel.leaveStudyGroupLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Error -> {
                    showToastMessage(it.msg.toString())
                }
                else -> {
                    showToastMessage(resources.getString(R.string.study_qna_delete_dialog_complete_msg))
                    findNavController().navigate(R.id.action_studyGroupFragment_to_myStudyGroupFragment)

                }
            }
        })
        studyViewModel.leaveStudyGroup(args.pid)
    }

    fun clickQnaButton(){
        binding.qnaButton.setOnClickListener {
            val action = StudyGroupFragmentDirections.actionStudyGroupFragmentToGroupQnaListFragment(args.pid)
            it.findNavController().navigate(action)
        }
    }
    private fun getStudyGroupInfo(pid: Int) {
        studyViewModel.getStudyGroupInfo(pid)
        studyViewModel.studyGroupInfoLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    val data: ResponseStudygroupinfoBody.StudyGroupDetailItem = it.data!!

                    binding.studyNameTextView.text = binding.studyNameTextView.text.toString() + " ${data.name}"
                    binding.studyTermTextView.text = binding.studyTermTextView.text.toString() +
                            " ${CommonService.convertTimestampToDate(data.createdAt)} ~ ${CommonService.convertTimestampToDate(data.expiredAt)}"
                    if (data.isCam) {
                        binding.studyCamTextView.text = binding.studyCamTextView.text.toString() + " 필수"
                    } else {
                        binding.studyCamTextView.text = binding.studyCamTextView.text.toString() + " 필수 아님"
                    }
                    //벌점 기준 전송 안됨
                    binding.studyMinTimeTextView.text = binding.studyMinTimeTextView.text.toString() +
                            " 주 ${data.minStudyHour.substring(0, 2)}시간 ${data.minStudyHour.substring(3, 5)}분 이상"

                    setCategoryAdapter(data.categories)
                }
                else -> {
                    showToastMessage(it.msg.toString())
                }
            }
        })
    }

    //사람 정보 불러와서 띄우는 작업 완료 안됨(리스폰스 바디 데이터를 가져올 때 null 포인터가 뜸)
    private fun getPeopleInfo(pid: Int) {
        studyViewModel.memberLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    setPeopleAdapter(it.data!!.members)
                }
                else -> {
                    showToastMessage(it.msg.toString())
                }
            }
        })
        studyViewModel.getStudyGroupMember(pid)
    }

    fun setCategoryAdapter(list: List<StudyCategory>) {
        cAdapter = CategoryAdapter(list as ArrayList<StudyCategory>)
        binding.categoryRecyclerView.adapter = cAdapter
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.categoryRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        val spaceDecoration = VerticalSpaceItemDecoration(8)
        binding.categoryRecyclerView.addItemDecoration(spaceDecoration)
        binding.categoryRecyclerView.setHasFixedSize(true)
    }

    fun setPeopleAdapter(list: List<Member>) {
        pAdapter = PeopleAdapter(this, list as ArrayList<Member>)
        binding.peopleRecyclerView.adapter = pAdapter
        binding.peopleRecyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.peopleRecyclerView.addItemDecoration(GridSpacingItemDecoration(3,12,18))
        binding.peopleRecyclerView.setHasFixedSize(true)
    }

    //recycler view 간격 조절(width)
    inner class VerticalSpaceItemDecoration(private val verticalSpaceWidth: Int) :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.right = verticalSpaceWidth
        }
    }

    inner class GridSpacingItemDecoration(val spanCount: Int, val widthSpacing: Int,val heightSpacing: Int): RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position: Int = parent.getChildAdapterPosition(view)

            //첫번째 행이 아닌 행은 여백 추가
            Log.d("position","${position}")
            if(position >= spanCount){
                outRect.top = heightSpacing
            }
            outRect.right = widthSpacing
        }
    }
}
