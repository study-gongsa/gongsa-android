package com.app.gong4.fragments

import android.Manifest
import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.gong4.MainActivity
import com.app.gong4.databinding.FragmentSettingBinding
import com.app.gong4.model.req.RequestSaveUserCateogry
import com.app.gong4.model.req.RequestUserInfo
import com.app.gong4.model.res.ResponseUserBody
import com.app.gong4.utils.CommonService
import com.app.gong4.utils.CommonTextWatcher
import com.app.gong4.utils.GlideApp
import com.app.gong4.utils.NetworkResult
import com.app.gong4.viewmodel.CategoryViewModel
import com.app.gong4.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.regex.Pattern

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {

    val imageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){
            val imageUri = result.data?.data
            imageUri?.let {
                imageFile = File(CommonService.getRealPathFromURI(requireActivity(),it)) // 이미지 -> 파일형태로 변환
                GlideApp.with(requireContext()).load(imageFile).into(binding.settingProfileImageView)
            }
            changeImage = true
        }
    }

    private val categoryViewModel : CategoryViewModel by activityViewModels()
    private val userViewModel : UserViewModel by viewModels()
    private var imageFile: File?=null
    private var changeImage: Boolean = false
    private lateinit var currentCategory : List<Int>

    override fun onResume() {
        super.onResume()
        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(false)
        serverUserInfo()
    }

    override fun onStop() {
        super.onStop()
        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(true)
    }

    override fun initView() {
        showCategories()
        checkNickname()
        checkPassword()
        checkPasswordConfirm()
        patchUserInfo()
        selectGallery()
    }


    fun patchUserInfo(){
        binding.settingConfirmButton.setOnClickListener {
            val nickname = binding.nicknameEditText.text.toString()

            var requestBody: RequestUserInfo
            var image : MultipartBody.Part? = null

            if(changeImage) {
                val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageFile)
                image = MultipartBody.Part.createFormData("image", imageFile?.name, requestFile)
            }

            if(binding.passwordChangeEditText.text.isNotEmpty()){
                val password = binding.passwordChangeEditText.text.toString()
                requestBody = RequestUserInfo(nickname,password,changeImage)
            }else{
                requestBody = RequestUserInfo(nickname = nickname,changeImage = changeImage)
            }

            userViewModel.patchUserSettingInfoRes.observe(viewLifecycleOwner, Observer {
                when(it){
                    is NetworkResult.Success -> {
                        showToastMessage("수정이 완료되었습니다.")
                    }
                    else -> {
                        showErrorMsg(it.location.toString(), it.msg.toString())
                    }
                }
            })
            userViewModel.patchUserInfo(image=image,requestUserInfo=requestBody)
            saveCategory()
        }

    }

    private fun selectGallery(){
        binding.imageSelectTextview.setOnClickListener {

            val writePermission = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val readPermission = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)

            if(writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE),
                    CreateStudygroupFragment.REQ_GALLERY
                )
            }else{
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")

                imageResult.launch(intent)
            }
        }

    }

    // 로그인 에러메시지 보여주는 함수
    fun showErrorMsg(location : String?, msg:String?){
        if(location == "nickname"){
            binding.validNicknameTextView.text = msg
        }
        binding.settingConfirmButton.isEnabled = false
    }

    fun checkNickname(){
        binding.nicknameEditText.addTextChangedListener(CommonTextWatcher(
            afterChanged = { text->
                binding.settingConfirmButton.isEnabled = text!!.isNotEmpty()
            }
        ))
    }

    fun checkPassword(){
        binding.passwordChangeEditText.addTextChangedListener(CommonTextWatcher(
            onChanged = { text,_,_,_ ->
                if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,16}$", text.toString()))
                {
                    binding.validPasswordChangeTextView.text = String.format(resources.getString(com.app.gong4.R.string.login_fragment_wrong_answer),"비밀번호")
                    binding.passwordChangeEditText.background = requireContext().resources.getDrawable(
                        com.app.gong4.R.drawable.custom_error_input
                    )
                }
                else
                {
                    binding.validPasswordChangeTextView.text = ""
                    binding.passwordChangeEditText.background = requireContext().resources.getDrawable(
                        com.app.gong4.R.drawable.custom_input
                    )

                }
            }
        ))
    }

    fun checkPasswordConfirm(){
        binding.passwordChangeConfirmEditText.addTextChangedListener(CommonTextWatcher(
            onChanged = { text,_,_,_ ->
                if(text.toString() != binding.validPasswordChangeTextView.text.toString())
                {
                    binding.validPasswordChangeConfirmTextView.text = resources.getString(com.app.gong4.R.string.signup_wrong_password)
                    binding.passwordChangeConfirmEditText.background = requireContext().resources.getDrawable(
                        com.app.gong4.R.drawable.custom_error_input
                    )
                }
                else
                {
                    binding.validPasswordChangeConfirmTextView.text = ""
                    binding.passwordChangeConfirmEditText.background = requireContext().resources.getDrawable(
                        com.app.gong4.R.drawable.custom_input
                    )

                }
            }
        ))
    }

    fun serverUserInfo(){
        userViewModel.userSettingInfoRes.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    initUserInfo(it.data!!)
                }
                is NetworkResult.Error -> {
                    showToastMessage(it.msg.toString())
                }
                else -> TODO()
            }
        })
        userViewModel.getSettingUserInfo()
    }

    fun saveCategory(){
        val checkedChipList =
            binding.categoryChipGroup.children?.filter { (it as Chip).isChecked }?.map { it.id }?.toList()!!

        if(checkedChipList != currentCategory){
            val requestSaveUserCateogry = RequestSaveUserCateogry(checkedChipList)
            categoryViewModel.putCategoryList(requestSaveUserCateogry)
        }

        categoryViewModel.putCategoryLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> TODO()
                is NetworkResult.Error -> showErrorMsg(it?.location.toString(), it?.msg.toString())
                else -> {
                    showToastMessage(it.msg.toString())
                }
            }
        })

    }

    fun initUserInfo(user: ResponseUserBody.UserData){
        binding.nicknameEditText.setText(user.nickname)
        val url = CommonService.getImageGlide(user.imgPath)
        GlideApp.with(requireContext()).load(url).into(binding.settingProfileImageView)
    }

    fun showCategories(){
        val categories = categoryViewModel.categoryLiveData.value!!.data!!
        val userCategory = categoryViewModel.myCategoryLiveData.value!!.data!!

        for (c in categories){
            binding.categoryChipGroup.addView(Chip(context).apply {
                text = c.name
                id = c.categoryUID
                isCheckable = true
                for(u in userCategory){
                    if(u.categoryUID == c.categoryUID){
                        isChecked = true
                        break
                    }
                }
                isCheckedIconVisible = false
                width = 56
                height = 28
                chipStrokeWidth = 2f
                //  setTextSize(TypedValue.COMPLEX_UNIT_DIP,10f)
                chipStrokeColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-R.attr.state_checked), intArrayOf(R.attr.state_checked)),
                    intArrayOf(Color.parseColor("#2DB57B"), Color.parseColor("#2DB57B"))
                )
                chipBackgroundColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-R.attr.state_checked), intArrayOf(R.attr.state_checked)),
                    intArrayOf(Color.WHITE, Color.parseColor("#2DB57B"))
                )

                setTextColor(
                    ColorStateList(
                        arrayOf(
                            intArrayOf(-R.attr.state_checked), intArrayOf(R.attr.state_checked)),
                        intArrayOf(Color.BLACK, Color.WHITE)
                    )

                )
            })
        }
        currentCategory = binding.categoryChipGroup.children?.filter { (it as Chip).isChecked }?.map { it.id }?.toList()!!
        Log.d("currentCategory",currentCategory.toString())
    }
}