package com.example.schoolmanager.view.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.schoolmanager.*
import com.example.schoolmanager.view.adapter.SchoolWorkPaletteRecyclerViewAdapter
import com.example.schoolmanager.databinding.FragmentStudentManagerBinding
import com.example.schoolmanager.model.network.SchoolWork
import com.example.schoolmanager.model.network.Student
import com.example.schoolmanager.util.KeyValue
import com.example.schoolmanager.view.student.StudentDetailActivity
import com.example.schoolmanager.view.student.StudentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StudentManagerFragment : Fragment() {

    //뷰모델
    private val viewModel: StudentViewModel by activityViewModels()

    //binding
    private var mBinding: FragmentStudentManagerBinding? = null
    private val binding get() = mBinding!!

    //리사이클러뷰 어댑터
    private val studentRvAdapter = StudentRecyclerViewAdapter()
    private val schoolWorkRvAdapter: SchoolWorkPaletteRecyclerViewAdapter by lazy {
        SchoolWorkPaletteRecyclerViewAdapter()
    }

    //팔레트 클릭 변수들
    private var isPaletteOn = false
    private var isSchoolWorkSelected = false
        set(value) {
            studentRvAdapter.isSchoolWorkSelected = value
            studentRvAdapter.notifyDataSetChanged() //학생 선택 창 뜨도록 새로고침.
            field = value
        }

    //---------------------------------------------------------------------------------------생명주기
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentStudentManagerBinding.inflate(inflater, container, false)
        initView() //뷰 초기화
        bindViews() //뷰 장착
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchStudentList().observe(viewLifecycleOwner) {
            setStudentAdapter(it) //RV에 데이터 뿌리기
        }
        viewModel.fetchSchoolWorkList().observe(viewLifecycleOwner) {
            setPaletteAdapter(it) //RV에 데이터 뿌리기
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
        isSchoolWorkSelected = false
    }

    //--------------------------------------------------------------------------------------사용자함수
    private fun initView() {
        isSchoolWorkSelected = false
    }

    private fun bindViews() {
        binding.giveExpBtn.setOnClickListener {
            paletteOnOff()
        }
        binding.selectionWorkCompleteBtn.setOnClickListener { //활동 선택 완료
            paletteOnOff()
            isSchoolWorkSelected = true //이 변수는 StudentAdapter 변수와 연동되어 있음.
            showStudentSelectionBox(binding)
        }
    }

    private fun showStudentSelectionBox(binding: FragmentStudentManagerBinding) {
        binding.selectedStudentsDialogueLayout.isVisible = true
        binding.selectionStudentCompleteBtn.setOnClickListener { //학생 선택 완료
            viewModel.putSchoolWorkInfoIntoStudent(
                studentRvAdapter,
                schoolWorkRvAdapter
            ) //선택된 활동들 점수, 생기부 문구 -> 학생 삽입
            studentRvAdapter.releaseSelection() //선택 학생 해제
            schoolWorkRvAdapter.releaseSelection() //선택 활동 해제
            binding.selectedStudentsDialogueLayout.isVisible = false
            isSchoolWorkSelected = false
        }
    }

    private fun paletteOnOff() { //팔레트 온오프
        isPaletteOn = !isPaletteOn
        binding.selectionWorkCompleteBtn.isVisible = isPaletteOn
        binding.schoolworkPalette.isVisible = isPaletteOn
    }

    //학생 어뎁터 데이터 세팅
    private fun setStudentAdapter(studentList: MutableList<Student>) {
        val adapter = studentRvAdapter
        binding.studentListRecyclerview.adapter = adapter
        adapter.submitList(studentList)
        adapter.setOnItemClickListener {
            if (!isSchoolWorkSelected) {
                val intent = Intent(context, StudentDetailActivity::class.java)
                intent.putExtra(KeyValue.INTENT_EXTRA_STUDENT, it)
                startActivity(intent)
            }
        }
    }

    //팔레트 어뎁터 데이터 세팅
    private fun setPaletteAdapter(schoolWorkList: MutableList<SchoolWork>) {
        val adapter = schoolWorkRvAdapter
        binding.schoolworkPalette.adapter = adapter
        adapter.submitList(schoolWorkList)
        adapter.setOnItemClickListener {
            binding.selectionWorkCompleteBtn.isEnabled =
                adapter.getNumberOfSelectedSchoolWorks() > 0 //선택된 활동이 있다면 선택 완료 버튼 활성화
        }
    }
}
