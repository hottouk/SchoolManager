package com.example.schoolmanager.view.student

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.schoolmanager.databinding.ActivityStudentDetailsBinding
import com.example.schoolmanager.util.KeyValue.Companion.INTENT_EXTRA_STUDENT
import com.example.schoolmanager.model.network.Student
import com.example.schoolmanager.util.KeyValue
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class StudentDetailActivity : AppCompatActivity() {

    private var mbinding: ActivityStudentDetailsBinding? = null
    private val binding get() = mbinding!!
    private val userDB: DatabaseReference by lazy { Firebase.database.reference.child(KeyValue.DB_USERS) }

    //---------------------------------------------------------------------------------------생명주기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivityStudentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews() //뷰 그리기
    }

    override fun onDestroy() {
        super.onDestroy()
        mbinding = null
    }

    //--------------------------------------------------------------------------------------사용자함수
    //뷰 초기화
    private fun initViews() {
        //intent 받는 부분
        val student = intent.getParcelableExtra<Student>(INTENT_EXTRA_STUDENT)

        //뷰 그리기
        student?.let {
            binding.studentDetailNumberTextview.text = it.studentNumber.toString()
            binding.studentDetailNameTextview.text = it.studentName
            binding.studentDetailInfoEdittext.setText(it.studentDetailInfo)
            binding.leadershipStudentScoreTextview.text = it.leadership.toString()
            binding.academicStudentScoreTextview.text = it.academicAbility.toString()
            binding.cooperationStudentScoreTextview.text = it.cooperation.toString()
            binding.careerStudentScoreTextview.text = it.career.toString()
            binding.sumStudentScoreTextview.text = it.getExp().toString()
        }

        binding.editStudentInfoBtn.setOnClickListener {
            val newText = binding.studentDetailInfoEdittext.text
            val dialogue = AlertDialog.Builder(this)
                .setTitle("생기부 수정")
                .setMessage("생기부를 수정하시겠습니까?")
                .setPositiveButton("수정") { _, _ ->
                    student?.let {
                        //todo 에러발생
                        userDB.child(it.studentUid).child("studentDetailInfo").setValue(newText.toString())
                        Toast.makeText(this, "생기부가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("취소") { _, _ ->
                    return@setNegativeButton
                }
                .create()
            dialogue.show()
        }
    }
}