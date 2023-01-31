package com.example.schoolmanager.view.schoolwork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.schoolmanager.databinding.ActivityAddSchoolBinding
import com.example.schoolmanager.model.network.Teacher
import com.example.schoolmanager.util.KeyValue

class AddSchoolWorkActivity : AppCompatActivity() {
    //뷰모델
    private lateinit var viewModel: SchoolWorkViewModel
    private lateinit var viewModelFactory: SchoolWorkViewModelFactory

    private var mBinding: ActivityAddSchoolBinding? = null
    private val binding get() = mBinding!!

    //스피너 관련
    val spinnerItems = KeyValue.spinnerItems
    private var mSelectedSubjectItem = ""
    private val spinnerItemListener = object : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position) {
                0 -> {
                    mSelectedSubjectItem = spinnerItems[0]
                }
                1 -> {
                    mSelectedSubjectItem = spinnerItems[1]
                }
                2 -> {
                    mSelectedSubjectItem = spinnerItems[2]
                }
                3 -> {
                    mSelectedSubjectItem = spinnerItems[3]
                }
                4 -> {
                    mSelectedSubjectItem = spinnerItems[4]
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    //별점 관련
    private var mSelectedRating: Float = 0f
    private val ratingListener =
        RatingBar.OnRatingBarChangeListener { _, rating, _ -> mSelectedRating = rating }

    private val schoolWorkTitleList = mutableListOf<String>()
    //---------------------------------------------------------------------------------------생명주기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = intent.getParcelableExtra<Teacher>(KeyValue.INTENT_EXTRA_USER_INFO)
        viewModelFactory = user?.let { SchoolWorkViewModelFactory(it) }!!
        viewModel = ViewModelProvider(this, viewModelFactory)[SchoolWorkViewModel::class.java]

        mBinding = ActivityAddSchoolBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        bindViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    //---------------------------------------------------------------------------------------뷰그리기
    private fun initViews() { //뷰 초기화
        val mySpinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems)

        with(binding) {
            val numberPickerList: List<NumberPicker> = mutableListOf(
                leadershipNumberpicker,
                academicNumberpicker,
                cooperationNumberpicker,
                sincerityNumberpicker,
                careerNumberpicker
            )
            numberPickerList.forEach {
                with(it) {
                    maxValue = 3
                    minValue = 0
                }
            }
            moneyNumberpicker.maxValue = 10
            moneyNumberpicker.minValue = 0
            expectedDifficultyRating.onRatingBarChangeListener = ratingListener
            subjectSpinner.adapter = mySpinnerAdapter
            subjectSpinner.onItemSelectedListener = spinnerItemListener
        }
    }

    private fun bindViews() {
        binding.schoolWorkSaveBtn.setOnClickListener {
            val schoolWorkTitle = binding.schoolWorkTitleEdittext.text.toString()
            when {
                (schoolWorkTitle.isEmpty()) -> {
                    Toast.makeText(this, "활동 제목을 입력해주세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                (schoolWorkTitleList.contains(schoolWorkTitle)) -> {
                    Toast.makeText(this, "동일한 활동 제목이 존재합니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else -> {
                    showPopUp()
                }
            }
        }
        binding.imagePicker.setOnClickListener {
            //todo 이미지피커
        }
    }

    //-------------------------------------------------------------------------------------사용자함수

    //저장 확인 팝업창 띄우기
    private fun showPopUp() {
        AlertDialog.Builder(this)
            .setTitle("저장 확인")
            .setMessage("입력한 활동을 저장하시겠습니까?")
            .setPositiveButton("저장") { _, _ ->
                viewModel.postSaveSchoolWork(binding)
                Toast.makeText(this, "활동이 저장되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }
}