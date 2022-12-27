package com.app.gong4

import androidx.navigation.NavDirections
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.gong4.model.QnaItem
import com.app.gong4.model.res.ResponseQnaListBody
import com.app.gong4.adapter.QnaListAdapter
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentGroupQnaListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupQnaListFragment : BaseFragment<FragmentGroupQnaListBinding>(FragmentGroupQnaListBinding::inflate) {

    private val args by navArgs<GroupQnaListFragmentArgs>()
    private var list : ArrayList<QnaItem> = arrayListOf()

    override fun initView() {
        getQnaList(args.pid)
    }

    private fun getQnaList(groupUID:Int){
        RequestServer.qnaService.getQnaList(groupUID).enqueue(object :
            Callback<ResponseQnaListBody> {
            override fun onResponse(
                call: Call<ResponseQnaListBody>,
                response: Response<ResponseQnaListBody>
            ) {
                list.clear()

                list.addAll(response.body()!!.data.questionList)
                setAdapter(list)
            }

            override fun onFailure(call: Call<ResponseQnaListBody>, t: Throwable) {

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


