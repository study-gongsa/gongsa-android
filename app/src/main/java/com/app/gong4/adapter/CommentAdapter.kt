package com.app.gong4.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.gong4.model.Answer
import com.app.gong4.R
import com.app.gong4.util.CommonService

class CommentAdapter(val list:ArrayList<Answer>, private var listener: CommentListener) : RecyclerView.Adapter<CommentAdapter.ViewHolder>(){

    interface CommentListener{
        fun onEditComment(answerID:Int)
        fun onRemoveComment(answerID:Int)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val comment_author_textview : TextView = view.findViewById(R.id.comment_author_textview)
        val comment_content_textview : TextView = view.findViewById(R.id.comment_content_textview)
        val comment_date_textview : TextView = view.findViewById(R.id.comment_date_textview)
        val comment_edit_button : ImageButton = view.findViewById(R.id.comment_edit_button)
        val comment_remove_button : ImageButton = view.findViewById(R.id.comment_remove_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.comment_author_textview.text = item.nickname
        holder.comment_content_textview.text = item.answer
        holder.comment_date_textview.text = CommonService.convertTimestampToDate(item.createdAt)

        //답변 수정 버튼
        holder.comment_edit_button.setOnClickListener {
            editCommentButton(item.answerUID)
        }

        //답변 삭제 버튼
        holder.comment_remove_button.setOnClickListener {
            removeCommentButton(item.answerUID)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    private fun editCommentButton(answerID:Int){
        listener.onEditComment(answerID)
    }

    private fun removeCommentButton(answerID:Int){
        listener.onRemoveComment(answerID)
    }

}