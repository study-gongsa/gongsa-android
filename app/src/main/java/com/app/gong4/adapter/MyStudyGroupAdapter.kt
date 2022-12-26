package com.app.gong4.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.gong4.model.StduyGroupItem
import com.app.gong4.MyStudyGroupFragment
import com.app.gong4.MyStudyGroupFragmentDirections
import com.app.gong4.R
import com.app.gong4.util.CommonService
import com.bumptech.glide.Glide

class MyStudyGroupAdapter(private val context: MyStudyGroupFragment, val dataSet: ArrayList<StduyGroupItem>)
    : RecyclerView.Adapter<MyStudyGroupAdapter.ViewHolder>() {

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
        val start_date = CommonService.convertTimestampToDate(dataSet[position].createdAt)
        val end_date = CommonService.convertTimestampToDate(dataSet[position].expiredAt)

        holder.group_title_textView.text = dataSet[position].name
        holder.group_date_textView.text = "$start_date ~ $end_date"

        val url = CommonService.getImageGlide(dataSet[position].imgPath)
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
}