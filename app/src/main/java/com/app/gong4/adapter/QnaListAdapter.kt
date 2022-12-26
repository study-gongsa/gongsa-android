package com.app.gong4.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.gong4.model.QnaItem
import com.app.gong4.R
import com.app.gong4.onMoveAdapterListener
import java.text.SimpleDateFormat

class QnaListAdapter(val dataSet: ArrayList<QnaItem>, val listener: onMoveAdapterListener)
    : RecyclerView.Adapter<QnaListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val qna_title_textView: TextView
        val qna_content_textView: TextView
        val qna_date_textView: TextView
        val qna_status_textView: TextView

        init {
            qna_title_textView = view.findViewById(R.id.title_textview)
            qna_content_textView = view.findViewById(R.id.content_textview)
            qna_date_textView = view.findViewById(R.id.date_textview)
            qna_status_textView = view.findViewById(R.id.status_textview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.qna_list_item, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.qna_title_textView.text = dataSet[position].title
        holder.qna_content_textView.text = dataSet[position].content
        holder.qna_date_textView.text = convertTimestampToDate(dataSet[position].createdAt)
        holder.qna_status_textView.text = dataSet[position].answerStatus

        if(dataSet[position].answerStatus == "응답 완료"){
            holder.qna_status_textView.setTextColor(
                ContextCompat.getColor(holder.qna_status_textView.context,
                    R.color.green_03_main))
        }else{
            holder.qna_status_textView.setTextColor(
                ContextCompat.getColor(holder.qna_status_textView.context,
                    R.color.black03))
        }
        holder.itemView.setOnClickListener { view ->
            val id = dataSet[position].questionUID
            view.findNavController().navigate(listener.onMoveQnaDetail(id))
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