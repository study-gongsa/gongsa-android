package com.app.gong4

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.gong4.DTO.*
import com.app.gong4.api.RequestServer
import com.app.gong4.databinding.FragmentGroupQnaDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GroupQnaDetailFragment : Fragment() ,CommentAdapter.CommentListener{

    private lateinit var binding: FragmentGroupQnaDetailBinding
    private lateinit var mAdapter : CommentAdapter
    private val args by navArgs<GroupQnaDetailFragmentArgs>()

    private var imm : InputMethodManager?=null

    var questionUID : Int = 0
    private lateinit var removeDialog : AlertCustomDialog
    private lateinit var editDialog : AlertEditCustomDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGroupQnaDetailBinding.inflate(inflater, container, false)

        questionUID = args.questionID
        getQnaDetail(questionUID)

        registerComment()
        return binding.root
    }

    private fun getQnaDetail(questionUID:Int){
        RequestServer.qnaService.getQusetionDetail(questionUID).enqueue(object :
            Callback<ResponseQuestionBody>{
            override fun onResponse(
                call: Call<ResponseQuestionBody>,
                response: Response<ResponseQuestionBody>
            ) {
                if(response.isSuccessful){
                    val data = response.body()!!.data
                    Log.d("data",data.toString())
                    setAdapter(data.answerList as ArrayList<Answer>)
                    binding.qnaTitleTextview.text = data.title
                    binding.qnaContentTextview.text = data.content
                }
            }

            override fun onFailure(call: Call<ResponseQuestionBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
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

    //키보드 내리기
    fun hideKeyboard(v:View){
        if(v!=null){
            imm?.hideSoftInputFromWindow(v.windowToken,0)
        }
    }

    private fun postAnswer(){
        val content = binding.commentEdittext.text.toString()
        val requestBody = RequestRegisterAnswer(questionUID,content)
        RequestServer.qnaService.postAnswer(requestBody).enqueue(object :
            Callback<ResponseRegisterAnswerBody>{
            override fun onResponse(
                call: Call<ResponseRegisterAnswerBody>,
                response: Response<ResponseRegisterAnswerBody>
            ) {
                if(response.isSuccessful){
                    val questionUID = response.body()!!.data.questionUID
                    Log.d("complete - questionUID","${questionUID}")
                    Toast.makeText(context,resources.getString(R.string.comment_save_complete),Toast.LENGTH_SHORT).show()
                    getQnaDetail(questionUID)
                    binding.commentEdittext.setText("")
                    hideKeyboard(binding.commentEdittext)

                }
            }

            override fun onFailure(call: Call<ResponseRegisterAnswerBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
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
        RequestServer.qnaService.deleteAnswer(answerID).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Toast.makeText(context,"삭제가 완료되었습니다",Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    //답변 수정
    private fun patchComment(answerID: Int, content: String){
        val requestBody = RequestUpdateAnswer(answerID,content)
        RequestServer.qnaService.patchAnswer(requestBody).enqueue(object :
            Callback<ResponseUpdateAnswerBody>{
            override fun onResponse(
                call: Call<ResponseUpdateAnswerBody>,
                response: Response<ResponseUpdateAnswerBody>
            ) {
                if(response.isSuccessful){
                    Toast.makeText(context,"수정이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                    editDialog.dismiss()
                    getQnaDetail(questionUID)
                }
            }

            override fun onFailure(call: Call<ResponseUpdateAnswerBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
       })
    }

    override fun onEditComment(answerID: Int) {
        //dialog show
        editDialog = AlertEditCustomDialog()
        editDialog.setData(resources.getString(R.string.comment_update_dialog_title),resources.getString(R.string.comment_update_dialog_content))
        editDialog.setActionListener(object :onActionListener{
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
        removeDialog.setData(resources.getString(R.string.comment_remove_dialog_title),resources.getString(R.string.comment_remove_dialog_content))
        removeDialog.setActionListener(object :onActionListener{
            override fun onAction() {
                deleteComment(answerID)
            }
        })
        removeDialog.show(parentFragmentManager,"RemoveDialog")
    }
}