package com.example.schoolmanager.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.R
import com.example.schoolmanager.StudentDetailActivity
import com.example.schoolmanager.StudentListRecyclerViewAdapter
import com.example.schoolmanager.model.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StudentManagerFragment : Fragment() {

    //파이어베이스
    private val auth: FirebaseAuth = Firebase.auth

    //컨텐츠
    var studentList: MutableList<Student> = mutableListOf()
    //UI 관련
    lateinit var studentListRecyclerView: RecyclerView

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
            val intent = Intent(activity,StudentDetailActivity::class.java)
            intent.putExtra("student",it)
            startActivity(intent)
        })
        return rootView
    }

    //임시 데이터 삽입
    private fun initTestData(){
        for (i in 1..10) {
            studentList.add(Student("",0,"","",1,"","",""))
        }
    }
}