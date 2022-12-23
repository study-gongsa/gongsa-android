package com.app.gong4

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.gong4.DTO.ResponseGroupItemBody
import com.app.gong4.DTO.ResponseStudygroupinfoBody
import com.app.gong4.DTO.StduyGroupItem
import com.app.gong4.adapter.MyStudyGroupAdapter
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentMyStudyGroupBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyStudyGroupFragment : BaseFragment<FragmentMyStudyGroupBinding>(FragmentMyStudyGroupBinding::inflate) {

    private lateinit var dataList : ArrayList<StduyGroupItem>
    private lateinit var dataAllList : ArrayList<StduyGroupItem>
    private lateinit var mAdapter : MyStudyGroupAdapter

    override fun initView() {
        getMyStudyGroup()
    }

    private fun getMyStudyGroup() {
        RequestServer.studyGroupService.getMyStudyGroup().enqueue(object :
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
        mAdapter = MyStudyGroupAdapter(this, list as ArrayList<StduyGroupItem>)
        binding.myStudyRecyclerView.adapter = mAdapter
        binding.myStudyRecyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter.notifyDataSetChanged()
        binding.myStudyRecyclerView.setHasFixedSize(true)
    }
}