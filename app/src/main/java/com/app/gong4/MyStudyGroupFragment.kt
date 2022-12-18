package com.app.gong4

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.gong4.DTO.ResponseGroupItemBody
import com.app.gong4.DTO.ResponseStudygroupinfoBody
import com.app.gong4.DTO.StduyGroupItem
import com.app.gong4.R
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentMainBinding
import com.app.gong4.databinding.FragmentMyStudyGroupBinding
import com.app.gong4.util.MainApplication
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class MyStudyGroupFragment : Fragment() {

    private lateinit var binding: FragmentMyStudyGroupBinding
    private lateinit var dataList : ArrayList<StduyGroupItem>
    private lateinit var dataAllList : ArrayList<StduyGroupItem>
    private lateinit var mAdapter : MyStudyGroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyStudyGroupBinding.inflate(inflater, container, false)

        getMyStudyGroup()

        return binding.root
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

    fun showStudyInfoDialog(groupId :Int){
        RequestServer.studyGroupService.getStudygroupInfo(groupId).enqueue(object :
            Callback<ResponseStudygroupinfoBody>{
            override fun onResponse(
                call: Call<ResponseStudygroupinfoBody>,
                response: Response<ResponseStudygroupinfoBody>
            ) {
                Log.d("studyinfo 응답 결과 : ", response.toString())
                val data = response.body()!!.data
                StudygroupinfoDialog(data).show(parentFragmentManager,"InfoDialog")
            }

            override fun onFailure(call: Call<ResponseStudygroupinfoBody>, t: Throwable) {
                Log.d("스터디 정보 결과 - onFailure", t.toString())
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

class MyStudyGroupAdapter(private val context: MyStudyGroupFragment, val dataSet: ArrayList<StduyGroupItem>)
    :RecyclerView.Adapter<MyStudyGroupAdapter.ViewHolder>() {

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
            val action = MyStudyGroupFragmentDirections.actionMyStudyGroupFragmentToStudyGroupFragment(dataSet[position].studyGroupUID)
            it.findNavController().navigate(action)
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