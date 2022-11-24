package com.example.schoolmanager.schoolActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import com.example.schoolmanager.KeyValue
import com.example.schoolmanager.R
import com.example.schoolmanager.model.SchoolActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddSchoolActivity : AppCompatActivity() {

    //파이어베이스
    private val schoolActDB: DatabaseReference by lazy { Firebase.database.reference.child(KeyValue.DB_SCHOOL_ACTIVITIES) }

    //UI관련
    private val schoolActTitleEditText: EditText by lazy { findViewById(R.id.school_activity_title_edittext) }
    private val schoolActSimpleEditText: EditText by lazy { findViewById(R.id.school_activity_simpleInfo_edittext) }
    private val schoolActDetailsEditText: EditText by lazy { findViewById(R.id.school_activity_detail_edittext) }
    private val addSchoolActBtn: Button by lazy { findViewById(R.id.school_activity_save_btn) }

    private val leadershipNumberPicker: NumberPicker by lazy { findViewById(R.id.leadership_numberpicker) }
    private val academicNumberPicker: NumberPicker by lazy { findViewById(R.id.academic_numberpicker) }
    private val cooperationNumberPicker: NumberPicker by lazy { findViewById(R.id.cooperation_numberpicker) }
    private val sincerityNumberPicker: NumberPicker by lazy { findViewById(R.id.sincerity_numberpicker) }
    private val careerNumberPicker: NumberPicker by lazy { findViewById(R.id.career_numberpicker) }

    //---------------------------------------------------------------------------------------생명주기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_school)
        initView()
        clkAddSchoolActBtn()
    }

    //--------------------------------------------------------------------------------------사용자함수
    //초기화
    private fun initView() {
        val numberPickerList: List<NumberPicker> = mutableListOf(
            leadershipNumberPicker,
            academicNumberPicker,
            cooperationNumberPicker,
            sincerityNumberPicker,
            careerNumberPicker
        )
        numberPickerList.forEach {
            with(it) {
                maxValue = 10
                minValue = 0
            }
        }
    }

    //저장 버튼
    private fun clkAddSchoolActBtn() {
        addSchoolActBtn.setOnClickListener {
            showPopUp()
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

    //활동 정보 파베 DB 저장하기
    private fun saveSchoolAct() {
        val model = SchoolActivity(
            uid = 1,
            activityTitle = schoolActTitleEditText.text.toString(),
            activitySimpleInfo = schoolActSimpleEditText.text.toString(),
            activityDetailInfo = schoolActDetailsEditText.text.toString(),
            leadership = leadershipNumberPicker.value,
            academicAbility = academicNumberPicker.value,
            cooperation = cooperationNumberPicker.value,
            sincerity = sincerityNumberPicker.value,
            career = careerNumberPicker.value
        )
        schoolActDB?.push()?.setValue(model)
    }
}