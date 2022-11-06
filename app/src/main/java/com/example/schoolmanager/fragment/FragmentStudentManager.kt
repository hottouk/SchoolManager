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
import com.example.schoolmanager.model.ItemStudent

class FragmentStudentManager : Fragment() {

    var studentList: MutableList<ItemStudent> = mutableListOf()
    lateinit var studentListRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTestData()
    }

    //임시 데이터 삽입 코드
    private fun initTestData(){
        for (i in 1..10) {
            studentList.add(ItemStudent("301$i".toInt(), "$i 번째 학생", 1,"아무말아무말"))
        }
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
}