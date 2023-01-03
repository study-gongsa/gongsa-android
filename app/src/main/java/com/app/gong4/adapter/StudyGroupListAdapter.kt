package com.app.gong4.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.gong4.model.StudyGroupItem
import com.app.gong4.fragments.MainFragment
import com.app.gong4.R
import com.app.gong4.utils.CommonService
import com.app.gong4.utils.GlideApp
import com.bumptech.glide.Glide

class StudyGroupListAdapter(private val context: MainFragment, val dataSet: ArrayList<StudyGroupItem>)
    : RecyclerView.Adapter<StudyGroupListAdapter.ViewHolder>() {

    var searchList = ArrayList<StudyGroupItem>()
    var searchAllList = ArrayList<StudyGroupItem>()

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.group_title_textView.text = dataSet[position].name
        holder.group_date_textView.text = "${CommonService.convertTimestampToDate(dataSet[position].createdAt)} ~ ${CommonService.convertTimestampToDate(dataSet[position].expiredAt)}"

        val url = CommonService.getImageGlide(dataSet[position].imgPath)
        GlideApp.with(context).load(url).error(R.drawable.error_image).into(holder.group_image_imageView)
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
}