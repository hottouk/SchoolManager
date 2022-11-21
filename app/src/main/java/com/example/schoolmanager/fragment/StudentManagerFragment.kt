package com.example.schoolmanager.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.KeyValue
import com.example.schoolmanager.R
import com.example.schoolmanager.StudentDetailActivity
import com.example.schoolmanager.StudentListRecyclerViewAdapter
import com.example.schoolmanager.model.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class StudentManagerFragment : Fragment() {

    //파이어베이스
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val userDB: DatabaseReference by lazy { Firebase.database.reference.child(KeyValue.DB_USERS) }

    //학생 명단
    var studentList: MutableList<Student> = mutableListOf()

    //UI 관련
    private lateinit var studentListRecyclerView: RecyclerView

    //---------------------------------------------------------------------------------------생명주기
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTestData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.student_manager_fragment, container, false)
        studentListRecyclerView = rootView.findViewById(R.id.student_list_recyclerview)
        studentListRecyclerView.adapter = StudentListRecyclerViewAdapter(studentList,
            itemClickListener = {
                val intent = Intent(activity, StudentDetailActivity::class.java)
                intent.putExtra("student", it)
                startActivity(intent)
            })
        return rootView
    }

    //--------------------------------------------------------------------------------------사용자함수
    //임시 데이터 삽입
    private fun initTestData() {
        for (i in 1..10) {
            studentList.add(Student("", 0, "", "", 1, "", "", ""))
        }
    }

    private fun inputStudentDataIntoAdapter(studentList: MutableList<Student>) {
        studentListRecyclerView.adapter = StudentListRecyclerViewAdapter(
            studentList,
            itemClickListener = {})
    }
}