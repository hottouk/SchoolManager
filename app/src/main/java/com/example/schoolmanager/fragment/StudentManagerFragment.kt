package com.example.schoolmanager.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.*
import com.example.schoolmanager.adapter.SchoolWorkPaletteRecyclerViewAdapter
import com.example.schoolmanager.model.SchoolWork
import com.example.schoolmanager.model.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class StudentManagerFragment : Fragment(R.layout.fragment_student_manager) {

    //파이어베이스
    private val auth: FirebaseAuth by lazy { Firebase.auth } //인증

    //유저DB
    private val userDB: DatabaseReference by lazy { Firebase.database.reference.child(KeyValue.DB_USERS) }
    private val userDbValueEventListener = object : ValueEventListener {
        //데이터 변경시 DB에서 받아오기
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.forEach { student ->
                val model = student.getValue(Student::class.java)
                model ?: return
                studentList.add(model)
            }
            inputStudentDataIntoAdapter(studentList)
        }

        override fun onCancelled(error: DatabaseError) {
        }
    }

    //활동DB
    private val schoolActDB: DatabaseReference by lazy { Firebase.database.reference.child(KeyValue.DB_SCHOOL_ACTIVITIES) }
    private val schoolWorkDbValueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.forEach { schoolWork ->
                val model = schoolWork.getValue(SchoolWork::class.java)
                model ?: return
                schoolWorkList.add(model)
            }
            inputSchoolWorkDataIntoAdapter(schoolWorkList)
        }

        override fun onCancelled(error: DatabaseError) {
        }
    }

    //context
    lateinit var mainActivity: MainActivity

    //학생, 활동 목록
    var studentList: MutableList<Student> = mutableListOf()
    val schoolWorkList: MutableList<SchoolWork> = mutableListOf()

    //UI 관련
    private lateinit var studentRecyclerView: RecyclerView
    private val studentRecyclerViewAdapter: StudentRecyclerViewAdapter by lazy {
        StudentRecyclerViewAdapter()
    }
    private lateinit var schoolWorkPalette: RecyclerView
    private val schoolWorkPaletteRecyclerViewAdapter: SchoolWorkPaletteRecyclerViewAdapter by lazy {
        SchoolWorkPaletteRecyclerViewAdapter()
    }


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
        val rootView = inflater.inflate(R.layout.fragment_student_manager, container, false)

        //UI 관련
        studentRecyclerView = rootView.findViewById(R.id.student_list_recyclerview)
        schoolWorkPalette = rootView.findViewById(R.id.schoolwork_palette_in_students_recyclerview)
        initView() //뷰 초기화
        //데이터 받아오기
        userDB.addValueEventListener(userDbValueEventListener) //FirebaseDB 에서 받아서 Recycler에 뿌린다.
        schoolActDB.addValueEventListener(schoolWorkDbValueEventListener)
        return rootView
    }

    //--------------------------------------------------------------------------------------사용자함수
    private fun initView() {
        studentRecyclerViewAdapter
        studentList.clear()
    }

    //학생 어뎁터에 데이터 삽입
    private fun inputStudentDataIntoAdapter(studentList: MutableList<Student>) {
        val adapter = StudentRecyclerViewAdapter(
            itemClickListener = {
                val intent = Intent(activity, StudentDetailActivity::class.java)
                intent.putExtra("student", it)
                startActivity(intent)
            })
        studentRecyclerView.adapter = adapter
        adapter.submitList(studentList)
    }

    //뷰페이져에 데이터 삽입
    private fun inputSchoolWorkDataIntoAdapter(schoolWorkList: MutableList<SchoolWork>) {
        Log.d(KeyValue.LOG_TAG, "schoolWorkList: ${schoolWorkList.toString()}")
        val adapter = SchoolWorkPaletteRecyclerViewAdapter(
            schoolWorkList
        )
        schoolWorkPalette.adapter = adapter
    }
}