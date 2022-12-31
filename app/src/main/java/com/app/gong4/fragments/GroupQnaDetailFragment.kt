package com.app.gong4.fragments

import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.gong4.dialog.AlertCustomDialog
import com.app.gong4.dialog.AlertEditCustomDialog
import com.app.gong4.R
import com.app.gong4.model.*
import com.app.gong4.adapter.CommentAdapter
import com.app.gong4.databinding.FragmentGroupQnaDetailBinding
import com.app.gong4.model.req.RequestRegisterAnswer
import com.app.gong4.model.req.RequestUpdateAnswer
import com.app.gong4.onActionListener
import com.app.gong4.utils.NetworkResult
import com.app.gong4.viewmodel.QnaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupQnaDetailFragment : BaseFragment<FragmentGroupQnaDetailBinding>(FragmentGroupQnaDetailBinding::inflate) , CommentAdapter.CommentListener{

    private lateinit var mAdapter : CommentAdapter
    private val args by navArgs<GroupQnaDetailFragmentArgs>()
    private val qnaViewModel : QnaViewModel by viewModels()

    var questionUID : Int = 0
    private lateinit var removeDialog : AlertCustomDialog
    private lateinit var editDialog : AlertEditCustomDialog

    override fun initView() {
        questionUID = args.questionID
        getQnaDetail(questionUID)

        registerComment()
    }

    private fun getQnaDetail(questionUID:Int){
        qnaViewModel.qnaDetailLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    setAdapter(it.data!!.answerList as ArrayList<Answer>)
                    binding.qnaTitleTextview.text = it.data!!.title
                    binding.qnaContentTextview.text = it.data!!.content
                }
                is NetworkResult.Error -> {
                    showToastMessage(it.msg.toString())
                }
                else -> TODO()
            }
        })
        qnaViewModel.getQnaDetail(questionUID)
    }

    //댓글 버튼 enter 누르면 답변 등록
    private fun registerComment(){
        binding.commentEdittext.setOnKeyListener{ view, keyCode, keyEvent ->
            var flag = false
            if(keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER){
                postAnswer()
                flag = true
            }
            flag
        }
    }

    private fun postAnswer(){
        val content = binding.commentEdittext.text.toString()
        val requestBody = RequestRegisterAnswer(questionUID,content)

        qnaViewModel.requestAnswerLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success ->{
                    val questionUID = it.data!!.data.questionUID
                    showToastMessage(getString(R.string.comment_save_complete))
                    getQnaDetail(questionUID)
                    binding.commentEdittext.setText("")
                    hideKeyboard(binding.commentEdittext)
                }
                is NetworkResult.Error -> {
                    showToastMessage(it.msg.toString())
                }
                else -> TODO()
            }
        })
        qnaViewModel.requestAnswer(requestBody)
    }

    private fun setAdapter(list: ArrayList<Answer>) {
        mAdapter = CommentAdapter(list,this)
        binding.commentRecyclerview.adapter = mAdapter
        binding.commentRecyclerview.layoutManager = LinearLayoutManager(context)
        mAdapter.notifyDataSetChanged()
        binding.commentRecyclerview.setHasFixedSize(true)
    }

    //답변 삭제
    private fun deleteComment(answerID: Int){
        qnaViewModel.deleteAnswerLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Error -> {
                    showToastMessage(it.msg.toString())
                }
                else -> {
                    showToastMessage(getString(R.string.comment_remove_complete))
                    getQnaDetail(questionUID)
                }
            }
            removeDialog.dismiss()
        })
        qnaViewModel.deleteQnaAnswer(answerID)
    }

    //답변 수정
    private fun patchComment(answerID: Int, content: String){
        val requestBody = RequestUpdateAnswer(answerID,content)
        qnaViewModel.patchAnswerLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    showToastMessage(getString(R.string.comment_update_complete))
                    editDialog.dismiss()
                    getQnaDetail(questionUID)
                }
                is NetworkResult.Error -> {
                    showToastMessage(it.msg.toString())
                }
                else -> TODO()
            }
        })
        qnaViewModel.patchQnaAnswer(requestBody)
    }

    override fun onEditComment(answerID: Int) {
        //dialog show
        editDialog = AlertEditCustomDialog()
        editDialog.setData(resources.getString(R.string.comment_update_dialog_title),resources.getString(
            R.string.comment_update_dialog_content
        ))
        editDialog.setActionListener(object : onActionListener {
            override fun onAction() {
                val content = editDialog.getCotent()

                patchComment(answerID,content)
            }

        })
        editDialog.show(parentFragmentManager,"RemoveDialog")
    }

    override fun onRemoveComment(answerID: Int) {
        //dialog show
        removeDialog = AlertCustomDialog()
        removeDialog.setData(resources.getString(R.string.comment_remove_dialog_title),resources.getString(
            R.string.comment_remove_dialog_content
        ))
        removeDialog.setActionListener(object : onActionListener {
            override fun onAction() {
                deleteComment(answerID)
            }
        })
        removeDialog.show(parentFragmentManager,"RemoveDialog")
    }
}