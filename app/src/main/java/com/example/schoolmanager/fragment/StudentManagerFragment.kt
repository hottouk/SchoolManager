package com.example.schoolmanager.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.schoolmanager.*
import com.example.schoolmanager.KeyValue.Companion.APP_STUDENT_DB
import com.example.schoolmanager.KeyValue.Companion.LOG_TAG
import com.example.schoolmanager.model.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class StudentManagerFragment : Fragment() {

    //파이어베이스
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val userDB: DatabaseReference by lazy { Firebase.database.reference.child(KeyValue.DB_USERS) }
    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.forEach { student ->
                val model = student.getValue(Student::class.java)
                model ?: return
                studentList.add(model)
            }
            //todo 어뎁터 초기화, 학생 중복 생성됨
            inputStudentDataIntoAdapter(studentList)
        }

        override fun onCancelled(error: DatabaseError) {
        }
    }

    //context
    lateinit var mainActivity: MainActivity

    //학생 명단
    var studentList: MutableList<Student> = mutableListOf()

    //UI 관련
    private lateinit var studentListRecyclerView: RecyclerView

    //---------------------------------------------------------------------------------------생명주기
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(KeyValue.LOG_TAG, "Fragment: OnAttach")
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(KeyValue.LOG_TAG, "Fragment: OnCreateView")
        val rootView = inflater.inflate(R.layout.student_manager_fragment, container, false)
        studentListRecyclerView = rootView.findViewById(R.id.student_list_recyclerview)
        userDB.addValueEventListener(valueEventListener) //FirebaseDB 에서 받아서 Recycler에 뿌린다.
        return rootView
    }


    //--------------------------------------------------------------------------------------사용자함수

    //학생 어뎁터에 데이터 삽입
    private fun inputStudentDataIntoAdapter(studentList: MutableList<Student>) {
        studentListRecyclerView.adapter = StudentListRecyclerViewAdapter(
            studentList,
            itemClickListener = {
                val intent = Intent(activity, StudentDetailActivity::class.java)
                intent.putExtra("student", it)
                startActivity(intent)
            })
    }
}