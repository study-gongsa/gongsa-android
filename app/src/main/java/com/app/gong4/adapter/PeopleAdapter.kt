package com.app.gong4.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.gong4.DTO.Member
import com.app.gong4.R
import com.app.gong4.StudyGroupFragment
import com.app.gong4.util.CommonService
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView

class PeopleAdapter(private val context: StudyGroupFragment, private val dataSet: ArrayList<Member>)
    : RecyclerView.Adapter<PeopleAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val timeTextView: TextView = view.findViewById(R.id.timeTextView)
        private val memberCard: MaterialCardView = view.findViewById(R.id.memberCard)
        private val mamberImage : ImageView = view.findViewById(R.id.personImageView)

        fun bind(member: Member) {
            timeTextView.text = "${member.totalStudyTime.substring(0,2)}시간 ${member.totalStudyTime.substring(3,5)}분"
            val imgPath = CommonService().getImageGlide(member.imgPath)
            Glide.with(mamberImage.context).load(imgPath).into(mamberImage)
            changeLayout(member.studyStatus)
        }

        @SuppressLint("ResourceAsColor")
        fun changeLayout(status: String) {
            if (status=="inactive") {
                memberCard.strokeColor = R.color.black01
                timeTextView.setBackgroundResource(R.color.black01)
                timeTextView.setTextColor(Color.BLACK)
            } else {
                memberCard.strokeColor = R.color.green_03_main
                timeTextView.setBackgroundResource(R.color.green_03_main)
                timeTextView.setTextColor(Color.WHITE)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.member_recycler_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}