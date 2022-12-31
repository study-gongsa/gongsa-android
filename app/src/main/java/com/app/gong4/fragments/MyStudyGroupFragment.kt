package com.app.gong4.fragments

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.gong4.model.res.ResponseGroupItemBody
import com.app.gong4.model.StudyGroupItem
import com.app.gong4.adapter.MyStudyGroupAdapter
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentMyStudyGroupBinding
import com.app.gong4.utils.NetworkResult
import com.app.gong4.viewmodel.StudyGroupViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class MyStudyGroupFragment : BaseFragment<FragmentMyStudyGroupBinding>(FragmentMyStudyGroupBinding::inflate) {
    private lateinit var mAdapter : MyStudyGroupAdapter

    private val studyViewModel : StudyGroupViewModel by viewModels()

    override fun initView() {
        getMyStudyGroup()
    }

    private fun getMyStudyGroup() {
        studyViewModel.myStudyGroupLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    setAdapter(it.data!!)
                }
                is NetworkResult.Error -> {
                    showToastMessage(it.msg as String)
                }
                else -> TODO()
            }
        })

        studyViewModel.getMyStudyGroup()
    }

    private fun setAdapter(list: List<StudyGroupItem>) {
        mAdapter = MyStudyGroupAdapter(this, list as ArrayList<StudyGroupItem>)
        binding.myStudyRecyclerView.adapter = mAdapter
        binding.myStudyRecyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter.notifyDataSetChanged()
        binding.myStudyRecyclerView.setHasFixedSize(true)
    }
}