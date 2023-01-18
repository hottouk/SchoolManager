package com.example.schoolmanager.view.student

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.schoolmanager.databinding.ActivityStudentDetailsBinding
import com.example.schoolmanager.util.KeyValue.Companion.INTENT_EXTRA_STUDENT
import com.example.schoolmanager.model.network.Student
import com.example.schoolmanager.util.KeyValue
import com.example.schoolmanager.view.main.MainViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class StudentDetailActivity : AppCompatActivity() {

    val viewModel: StudentViewModel by viewModels()

    private var mbinding: ActivityStudentDetailsBinding? = null
    private val binding get() = mbinding!!

    private var teacherId: String? = null
    private var student: Student? = null
    private var schoolClass: String? = null

    //---------------------------------------------------------------------------------------생명주기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivityStudentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews() //뷰 그리기
        bindViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        mbinding = null
    }

    //--------------------------------------------------------------------------------------사용자함수
    //뷰 초기화
    private fun initViews() {
        //intent 받는 부분
        teacherId = intent.getStringExtra(KeyValue.INTENT_EXTRA_USER_INFO)
        student = intent.getParcelableExtra<Student>(INTENT_EXTRA_STUDENT)
        schoolClass = intent.getStringExtra(KeyValue.INTENT_EXTRA_SCHOOL_CLASS)

        //뷰 그리기
        student?.let {
            binding.studentDetailNumberTextview.text = it.studentNumber
            binding.studentDetailNameTextview.text = it.userName
            binding.studentDetailInfoEdittext.setText(it.studentDetailInfo)
            binding.studentAchievedQuestTextview.text = it.studentSimpleInfo
            binding.leadershipStudentScoreTextview.text = it.leadership.toString()
            binding.academicStudentScoreTextview.text = it.academicAbility.toString()
            binding.cooperationStudentScoreTextview.text = it.cooperation.toString()
            binding.careerStudentScoreTextview.text = it.career.toString()
            binding.sincerityStudentScoreTextview.text = it.sincerity.toString()
            binding.sumStudentScoreTextview.text = it.studentExp.toString()
            if(it.userProfileImageUrl != ""){
                Glide.with(this)
                    .load(it.userProfileImageUrl)
                    .into(binding.studentDetailCharacterImageview)
            }
        }
    }

    private fun bindViews() {
        binding.editStudentInfoBtn.setOnClickListener {
            teacherId?.let { tId ->
                schoolClass?.let { sClass ->
                    student?.let { student ->
                        updateStudent(tId, sClass, student)
                    }
                }
            }
        }
    }

    private fun updateStudent(teacherId: String, schoolClass: String, student: Student) {
        val dialogue = AlertDialog.Builder(this)
            .setTitle("생기부 수정")
            .setMessage("생기부를 수정하시겠습니까?")
            .setPositiveButton("수정") { _, _ ->
                student?.let { student ->
                    schoolClass?.let { schoolClass ->
                        teacherId?.let { teacherId ->
                            student.studentDetailInfo =
                                binding.studentDetailInfoEdittext.text.toString()
                            viewModel.postOneStudentData(teacherId, schoolClass, student)
                        }
                    }
                }
            }
            .setNegativeButton("취소") { _, _ ->
                return@setNegativeButton
            }
            .create()
        dialogue.show()
    }

}