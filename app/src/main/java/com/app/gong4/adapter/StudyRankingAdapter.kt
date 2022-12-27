package com.app.gong4.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.gong4.model.Member
import com.app.gong4.model.Ranking
import com.app.gong4.R

class StudyRankingAdapter(val data:ArrayList<Ranking>) :
    RecyclerView.Adapter<StudyRankingAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val groupNameTextview : TextView
        private val groupRecyclerView : RecyclerView
        init {
            groupNameTextview = view.findViewById(R.id.group_item_name_textview)
            groupRecyclerView = view.findViewById(R.id.group_item_rank_recyclerview)
        }

        fun bind(ranking: Ranking){
            groupNameTextview.text = "${ranking.name} 그룹 내 랭킹 정보"
            groupRecyclerView.apply {
                adapter = RankItemAdapter(ranking.members as ArrayList<Member>)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.studygroup_ranking_group_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}