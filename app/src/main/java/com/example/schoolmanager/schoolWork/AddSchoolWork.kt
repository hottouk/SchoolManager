package com.example.schoolmanager.schoolWork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import com.example.schoolmanager.util.KeyValue
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.ActivityAddSchoolBinding
import com.example.schoolmanager.model.SchoolWork
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddSchoolWork : AppCompatActivity() {
    private lateinit var binding: ActivityAddSchoolBinding

    //파이어베이스
    private val schoolActDB: DatabaseReference by lazy { Firebase.database.reference.child(KeyValue.DB_SCHOOL_ACTIVITIES) }

    //UI관련
    private val schoolActTitleEditText: EditText by lazy { findViewById(R.id.school_activity_title_edittext) }
    private val schoolActSimpleEditText: EditText by lazy { findViewById(R.id.school_activity_simpleInfo_edittext) }
    private val schoolActDetailsEditText: EditText by lazy { findViewById(R.id.school_activity_detail_edittext) }

    //---------------------------------------------------------------------------------------생명주기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSchoolBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindViews()
    }

    //--------------------------------------------------------------------------------------사용자함수

    private fun bindViews() {
        with(binding) {
            //넘버피커 초기화
            val numberPickerList: List<NumberPicker> = mutableListOf(
                leadershipNumberpicker,
                academicNumberpicker,
                cooperationNumberpicker,
                sincerityNumberpicker,
                careerNumberpicker
            )
            numberPickerList.forEach {
                with(it) {
                    maxValue = 10
                    minValue = 0
                }
            }
            //버튼 초기화
            schoolActivitySaveBtn.setOnClickListener {
                showPopUp()
            }
        }
    }

    //전역변수 바인딩의 초기화 여부 체크
    private fun checkBinding(){
        if(::binding.isInitialized.not()){
            binding = ActivityAddSchoolBinding.inflate(layoutInflater)
        }
    }

    //저장 확인 팝업창 띄우기
    private fun showPopUp() {
        AlertDialog.Builder(this)
            .setTitle("저장 확인")
            .setMessage("입력한 활동을 저장하시겠습니까?")
            .setPositiveButton("저장") { _, _ ->
                saveSchoolAct()
                finish()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }

    //활동 정보 파이어베스 실시간 DB 저장하기
    private fun saveSchoolAct() {
        checkBinding()
        val model = SchoolWork(
            schoolWorkTitle = schoolActTitleEditText.text.toString(),
            schoolWorkSimpleInfo = schoolActSimpleEditText.text.toString(),
            schoolWorkDetailInfo = schoolActDetailsEditText.text.toString(),
            leadership = binding.leadershipNumberpicker.value,
            academicAbility = binding.academicNumberpicker.value,
            cooperation =binding.cooperationNumberpicker.value,
            sincerity = binding.sincerityNumberpicker.value,
            career = binding.careerNumberpicker.value
        )
        schoolActDB?.push()?.setValue(model)
    }
}