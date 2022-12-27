package com.app.gong4.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.gong4.model.Member
import com.app.gong4.R


class RankItemAdapter(val data:ArrayList<Member>) : RecyclerView.Adapter<RankItemAdapter.ViewHolder>(){
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val rankItemTextView : TextView
        init {
            rankItemTextView = view.findViewById(R.id.rank_item_info_textview)
        }

        fun bind(item: Member){
            rankItemTextView.text = "${item.ranking}위 : ${item.nickname}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.studygroup_ranking_member_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return if(data.size >=4) 4 else data.size
    }

}