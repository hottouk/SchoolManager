package com.example.schoolmanager.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.*
import com.example.schoolmanager.adapter.SchoolWorkPaletteRecyclerViewAdapter
import com.example.schoolmanager.adapter.SchoolWorkRecyclerViewAdapter
import com.example.schoolmanager.databinding.FragmentStudentManagerBinding
import com.example.schoolmanager.model.SchoolWork
import com.example.schoolmanager.model.Student
import com.example.schoolmanager.util.KeyValue
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
            setStudentAdapter(studentList)
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
            setPaletteAdapter(schoolWorkList)
        }

        override fun onCancelled(error: DatabaseError) {
        }
    }

    //context
    lateinit var mainActivity: MainActivity
    private var mBinding: FragmentStudentManagerBinding? = null
    private val binding get() = mBinding!!

    //학생, 활동 목록
    var studentList: MutableList<Student> = mutableListOf()
    val schoolWorkList: MutableList<SchoolWork> = mutableListOf()

    //리사이클러뷰 어댑터
    private lateinit var studentRecyclerView: RecyclerView
    private val  studentRecyclerViewAdapter = StudentRecyclerViewAdapter()

    private lateinit var schoolWorkPalette: RecyclerView
    private val schoolWorkPaletteRecyclerViewAdapter: SchoolWorkPaletteRecyclerViewAdapter by lazy {
        SchoolWorkPaletteRecyclerViewAdapter()
    }
    //팔레트 클릭 변수들
    private var isPaletteOn = false
    private var isSchoolWorkSelected = false
        set(value) {
            studentRecyclerViewAdapter.isSchoolWorkSelected = value
            studentRecyclerViewAdapter.notifyDataSetChanged()
            field = value
        }


    //---------------------------------------------------------------------------------------생명주기
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(KeyValue.LOG_TAG, "학생 관리 Fragment: OnAttach")
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(KeyValue.LOG_TAG, "학생 관리 Fragment: OnCreateView")
        mBinding = FragmentStudentManagerBinding.inflate(inflater, container, false)
        initView() //뷰 초기화
        bindViews()

        userDB.addValueEventListener(userDbValueEventListener) //FirebaseDB 데이터 받아서 RecyclerV에 뿌린다.
        schoolActDB.addValueEventListener(schoolWorkDbValueEventListener)
        return binding.root
    }

    override fun onDestroy() {
        Log.d(KeyValue.LOG_TAG, "학생 관리 Fragment: OnDestroy")
        super.onDestroy()
        mBinding = null
    }

    //--------------------------------------------------------------------------------------사용자함수
    private fun initView() {
        studentRecyclerViewAdapter
        schoolWorkPaletteRecyclerViewAdapter
        studentList.clear()
        schoolWorkList.clear()
        isSchoolWorkSelected = false
    }

    private fun bindViews() {
        studentRecyclerView = binding.studentListRecyclerview
        schoolWorkPalette = binding.schoolworkPalette
        binding.giveExpBtn.setOnClickListener {
            paletteOnOff()
        }
        binding.selectionWorkCompleteBtn.setOnClickListener { //활동 선택 완료
            paletteOnOff()
            isSchoolWorkSelected = true
            showStudentSelectionBox(binding)
        }
    }

    private fun showStudentSelectionBox(binding: FragmentStudentManagerBinding) {
        binding.selectedStudentsDialogueLayout.isVisible = true
        binding.selectionStudentCompleteBtn.setOnClickListener {
            putWorkInfoIntoStudent()
        }
    }

    private fun putWorkInfoIntoStudent() {
        val selectedStudents = studentRecyclerViewAdapter.getSelectedStudents()
        val selectedSchoolWorks = schoolWorkPaletteRecyclerViewAdapter.getSelectedSchoolWorks()
        Log.d(KeyValue.LOG_TAG, "선택된 활동: $selectedSchoolWorks")
        Log.d(KeyValue.LOG_TAG, "선택된 학생: $selectedStudents")
    }

    private fun paletteOnOff() {
        isPaletteOn = !isPaletteOn
        binding.selectionWorkCompleteBtn.isVisible = isPaletteOn
        schoolWorkPalette.isVisible = isPaletteOn
    }

    //학생 어뎁터 데이터 세팅
    private fun setStudentAdapter(studentList: MutableList<Student>) {
        val adapter = studentRecyclerViewAdapter
        studentRecyclerView.adapter = adapter
        adapter.submitList(studentList)
    }

    //팔레트 어뎁터 데이터 세팅
    private fun setPaletteAdapter(schoolWorkList: MutableList<SchoolWork>) {
        val adapter = schoolWorkPaletteRecyclerViewAdapter
        adapter.setOnItemClickListener {
            binding.selectionWorkCompleteBtn.isEnabled =
                adapter.getNumberOfSelectedSchoolWorks() > 0
        }
        schoolWorkPalette.adapter = adapter
        adapter.submitList(schoolWorkList)
    }
}