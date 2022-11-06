package com.example.schoolmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.schoolmanager.model.ItemStudent

class StudentDetailActivity : AppCompatActivity() {
    private val studentNumber: TextView by lazy { findViewById(R.id.student_detail_number_textview) }
    private val studentName: TextView by lazy { findViewById(R.id.student_detail_name_textview) }
    private val studentDetailInfoEditText: EditText by lazy { findViewById(R.id.student_detail_info_edittext) }
    private val studentDetailInfoEditBtn: AppCompatButton by lazy { findViewById(R.id.edit_student_info_btn) }
    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)

        //DB 빌드
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "studentInfoDB"
        ).build()

        //intent 받는 부분
        val student = intent.getParcelableExtra<ItemStudent>(INTENT_EXTRA_ITEM)

        //뷰 그리기
        student?.let {
            studentNumber.text = student.studentNumber.toString()
            studentName.text = student.studentName
            studentDetailInfoEditText.setText(it.studentDetailInfo)
        }
        Thread {//DB에서 받아온 정보 그리기
            val savedStudentDetails =
                db.studentDao().getOneStudent(student?.studentNumber ?: 0).studentDetailInfo
            runOnUiThread {
                studentDetailInfoEditText.setText(savedStudentDetails)
            }
        }.start()

        //todo 정보 수정 버튼 클릭시 오류남 수정할것
        studentDetailInfoEditBtn.setOnClickListener {
            Thread {
                db.studentDao().insertStudent(
                    ItemStudent(
                        studentNumber = student?.studentNumber ?: 0,
                        studentName = student?.studentName ?: "이름",
                        studentLevel = 0,
                        studentDetailInfo = studentDetailInfoEditText.text.toString()
                    )
                )
            }.start()
        }
    }


    companion object {
        const val LOG_TAG = "studentM"
        const val INTENT_EXTRA_ITEM = "student"
    }
}