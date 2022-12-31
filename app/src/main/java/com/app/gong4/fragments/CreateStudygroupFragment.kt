package com.app.gong4.fragments

import android.Manifest
import android.R
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gong4.MainActivity
import com.app.gong4.databinding.FragmentCreateStudygroupBinding
import com.app.gong4.model.req.RequestCreateStudyGroup
import com.app.gong4.onActionListener
import com.app.gong4.utils.CommonService
import com.app.gong4.utils.NetworkResult
import com.app.gong4.utils.TimePickerCustomDialog
import com.app.gong4.viewmodel.CategoryViewModel
import com.app.gong4.viewmodel.StudyGroupViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

@AndroidEntryPoint
class CreateStudygroupFragment : BaseFragment<FragmentCreateStudygroupBinding>(FragmentCreateStudygroupBinding::inflate) {

    val imageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == RESULT_OK){
            val imageUri = result.data?.data
            imageUri?.let {
                imageFile = File(CommonService.getRealPathFromURI(requireActivity(),it)) // 이미지 -> 파일형태로 변환
                binding.imageSelectButton.text = imageFile?.name
            }
        }
    }

    private val categoryViewModel : CategoryViewModel by activityViewModels()
    private val studyViewModel : StudyGroupViewModel by viewModels()

    private var imageFile: File?=null
    private var imm : InputMethodManager?=null

    private var studyTime : Int = 0

    companion object{
        const val REQ_GALLERY = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    }

    override fun initView() {
        val mainActivity = activity as MainActivity
        mainActivity.hideToolbar(true)

        showCategories()
        showDatePicker()
        showTimePicker()
        createStudyGroup()
        selectGallery()

    }

    private fun createStudyGroup(){
        var checkedCamera = true
        var checkedOpened = true
        var checkedInput = true
        var checkedPanelty = true

        binding.cameraRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when(checkedId){
                binding.cameraTrue.id -> { checkedCamera = true}
                binding.cameraFalse.id -> { checkedCamera = false }
            }
        }//카메라 여부

        binding.openRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when(checkedId){
                binding.openTrue.id -> { checkedOpened = true }
                binding.openFalse.id -> { checkedOpened = false }
            }
        }//공개여부

        binding.paneltyEdittext.isFocusable = false
        binding.penaltyRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when(checkedId){
                binding.paneltyTrue.id -> {
                    checkedPanelty = true
                    binding.paneltyEdittext.isFocusable = true
                }
                binding.paneltyFalse.id -> {
                    checkedPanelty = false
                    binding.paneltyEdittext.text.clear()
                    binding.paneltyEdittext.isFocusable = false
                    view?.let { hideKeyboard(it) }
                }
            }
        }//벌점

        binding.inputEdittext.isFocusable = false
        binding.inputRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when(checkedId){
                binding.inputTrue.id -> {
                    checkedInput = true
                    binding.inputEdittext.isFocusable = true
                }
                binding.inputFalse.id -> {
                    checkedInput = false
                    binding.inputEdittext.text.clear()
                    binding.inputEdittext.isFocusable = false
                    view?.let { hideKeyboard(it) }
                }
            }
        }//스터디 재진입

        //배경이미지
        var checkedImage = false
        binding.imageSelectButton.isClickable = false
        binding.backgroundRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when(checkedId){
                binding.imageTrue.id -> {
                    checkedImage = true
                    binding.imageSelectButton.isClickable = true
                }
                binding.imageFalse.id -> {
                    checkedImage = false
                    binding.imageSelectButton.isClickable = false
                }
            }
        }

        binding.createButton.setOnClickListener { view ->
            val roomName = binding.roomEdittext.text.toString()//방제목
            val isCam = checkedCamera // 캠 필수여부
            val maxMember = binding.peopleSegmented.position+1 //최대 인원 수
            val isPrivate = checkedOpened // 방공개여부
            val checkedChipList =
                binding.categoriyChipGroup.children?.filter { (it as Chip).isChecked }?.map { it.id }?.toList()!!//카테고리 선택
            val isPenalty = checkedPanelty // 벌점유무
            val maxPenalty = if(binding.paneltyEdittext.text.toString().isEmpty()) 0 else binding.paneltyEdittext.text.toString().toInt() //최대 가능 벌점 횟수
            val maxTodayStudy = if(binding.inputEdittext.text.toString().isEmpty()) 0 else binding.inputEdittext.text.toString().toInt()//스터디 재진입 횟수
            val expiredDate = binding.endDate.text.toString()//만료날짜
            val minStudyHour = studyTime//스터디목표시간

            val requestBody : RequestCreateStudyGroup = RequestCreateStudyGroup(
                checkedChipList,expiredDate,isCam,isPenalty,isPrivate,maxPenalty,
                maxMember,maxTodayStudy,minStudyHour,roomName)

            var image : MultipartBody.Part? = null

            if(imageFile!!.length().toInt() != 0){
                val requestFile = RequestBody.create(MediaType.parse("image/jpeg"),imageFile)
                image = MultipartBody.Part.createFormData("image",imageFile?.name,requestFile)
            }

            goServerCreateStudy(image!!,requestBody)

        }

    }

    private fun goServerCreateStudy(image:MultipartBody.Part,requestBody:RequestCreateStudyGroup){
        studyViewModel.createStudyGroupLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is NetworkResult.Success -> {
                    val action = CreateStudygroupFragmentDirections.actionCreateStudyFragmentToCompleteStudyFragment()
                    findNavController().navigate(action)
                }
                is NetworkResult.Error -> {
                    showToastMessage(it.msg.toString())
                }
                else -> TODO()
            }
        })
        studyViewModel.createStudygroup(image,requestBody)
    }

    private fun selectGallery(){
        binding.imageSelectButton.setOnClickListener {

            val writePermission = ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val readPermission = ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)

            if(writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQ_GALLERY
                )
            }else{
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")

                imageResult.launch(intent)
            }
        }

    }

    private fun showDatePicker(){
        val today = Calendar.getInstance()

        val endMonth = if(today.get(Calendar.MONTH)+1<=9) "0${today.get(Calendar.MONTH)+1}" else today.get(Calendar.MONTH)+1
        val endDay =  if(today.get(Calendar.DATE)<=9) "0${today.get(Calendar.DATE)}" else today.get(Calendar.DATE)

        binding.startDate.text ="${today.get(Calendar.YEAR)}-${today.get(Calendar.MONTH)+1}-${today.get(Calendar.DATE)}"
        binding.endDate.text ="${today.get(Calendar.YEAR)}-${endMonth}-${endDay}"

        binding.endDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                val endMonth = if(month+1<=9) "0${month+1}" else month+1
                val endDay =  if(day<=9) "0${day}" else day

                binding.endDate.text = "${year}-${endMonth}-${endDay}"

            }
            DatePickerDialog(requireContext(), data, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                .apply { datePicker.minDate = System.currentTimeMillis() }
                .show()
        }
    }

    private fun showTimePicker(){
        binding.timeTextView.text = "1:00"
        binding.timeTextView.setOnClickListener {
            val picker = TimePickerCustomDialog()
            picker.setActionListener(object : onActionListener{
                override fun onAction() {
                    val hour = picker.getHour()
                    studyTime = hour
                    binding.timeTextView.text = if(hour<=9) "0${hour}:00" else "${hour}:00"
                }
            })
            picker.show(parentFragmentManager,"TimePickerDialog")
        }
    }

    private fun showCategories(){
        Log.d("categories",categoryViewModel.categoryLiveData.value!!.data!!.toString())
        val categories = categoryViewModel.categoryLiveData.value!!.data!!

        for (c in categories){
            binding.categoriyChipGroup.addView(Chip(context).apply {
                text = c.name
                id = c.categoryUID
                isCheckable = true
                isCheckedIconVisible = false
                width = 48
                height = 22
                chipBackgroundColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-R.attr.state_checked), intArrayOf(R.attr.state_checked)),
                    intArrayOf(Color.parseColor("#E8E8E8"), Color.parseColor("#2DB57B"))
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
    }

}

