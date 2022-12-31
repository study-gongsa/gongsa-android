package com.app.gong4.fragments

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.gong4.model.QnaItem
import com.app.gong4.model.res.ResponseQnaListBody
import com.app.gong4.adapter.QnaListAdapter
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentGroupQnaListBinding
import com.app.gong4.onMoveAdapterListener
import com.app.gong4.utils.NetworkResult
import com.app.gong4.viewmodel.QnaViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class GroupQnaListFragment : BaseFragment<FragmentGroupQnaListBinding>(FragmentGroupQnaListBinding::inflate) {

    private val args by navArgs<GroupQnaListFragmentArgs>()
    private val qnaViewModel : QnaViewModel by viewModels()

    override fun initView() {
        getQnaList(args.pid)
    }

    private fun getQnaList(groupUID:Int){
        qnaViewModel.getQnaList(groupUID)
        qnaViewModel.qnaLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    setAdapter(it.data!!)
                }
                is NetworkResult.Error -> {
                    showToastMessage(it.msg.toString())
                }
                else -> TODO()
            }
        })
    }

    fun setAdapter(list: List<QnaItem>) {
        val adapter = QnaListAdapter(list as ArrayList<QnaItem>, object : onMoveAdapterListener {
            override fun onMoveQnaDetail(id: Int): NavDirections {
                return GroupQnaListFragmentDirections.actionGroupQnaListFragmentToGroupQnaDetailFragment(id)
            }
        })
        binding.peopleRecyclerView.addItemDecoration(DividerItemDecoration(context,1))
        binding.peopleRecyclerView.adapter = adapter
        binding.peopleRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter.notifyDataSetChanged()
        binding.peopleRecyclerView.setHasFixedSize(true)
    }

}


