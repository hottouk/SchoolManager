package com.example.schoolmanager.view.student

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import com.example.schoolmanager.*
import com.example.schoolmanager.view.adapter.SchoolWorkPaletteRvAdapter
import com.example.schoolmanager.databinding.FragmentStudentManagerBinding
import com.example.schoolmanager.model.network.Pet
import com.example.schoolmanager.model.network.SchoolWork
import com.example.schoolmanager.util.KeyValue
import com.example.schoolmanager.util.KeyValue.Companion.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION
import com.example.schoolmanager.view.main.MainViewModel

class StudentManagerFragment : Fragment() {

    //뷰모델
    private val viewModel: StudentViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    //binding
    private var mBinding: FragmentStudentManagerBinding? = null
    private val binding get() = mBinding!!

    //리사이클러뷰 어댑터
    private val studentPetRvAdapter: StudentPetRvAdapter by lazy {
        StudentPetRvAdapter(viewModel)
    }
    private val schoolWorkRvAdapter: SchoolWorkPaletteRvAdapter by lazy {
        SchoolWorkPaletteRvAdapter(viewModel)
    }

    //팔레트 클릭 변수들
    private var isPaletteOn = false
    private var isSchoolWorkSelected = false
        set(value) {
            studentPetRvAdapter.isSchoolWorkSelected = value
            studentPetRvAdapter.notifyDataSetChanged() //학생Rv '선택하기' 버튼 뜨도록 새로고침.
            binding.giveExpBtn.isEnabled = !value
            field = value
        }


    //뒤로가기
    private lateinit var callback: OnBackPressedCallback

    //---------------------------------------------------------------------------------------생명주기
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) { //뒤로가기 버튼 클릭 시
            override fun handleOnBackPressed() {
                parentFragmentManager.beginTransaction() //이전 프래그먼트로 돌아간다.
                    .replace(R.id.main_fragment_container_view, ClassManagerFragment())
                    .commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentStudentManagerBinding.inflate(inflater, container, false)
        isSchoolWorkSelected = false
        bindViews() //뷰 장착
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //선택 반 학생들, 학급활동 데이터 구독
        viewModel.selectedClass.observe(viewLifecycleOwner) { schoolClass -> //선택 반
            val classId = schoolClass.className
            viewModel.fetchMyStudentPetList(mainViewModel.currentUser.userId, classId)
                .observe(viewLifecycleOwner) { classStudentPets -> //학생들
                    setStudentPetAdapter(classStudentPets) //어댑터 전달
                }
            viewModel.fetchSchoolWorkList(mainViewModel.currentUser.userId, schoolClass.subject)
                .observe(viewLifecycleOwner) { schoolWorks -> //학급활동들
                    setPaletteAdapter(schoolWorks) //팔레트 어댑터 전달
                }
            binding.actionBar.text = "${classId}반"
        }
        //엑셀 내보내기 모드 구독
        viewModel.xlExportMode.observe(viewLifecycleOwner) {
            if (it) {
                binding.exportExcelDialogContainer.visibility = View.VISIBLE
                binding.dialogCoverLayout.visibility = View.VISIBLE
                binding.giveExpBtn.isEnabled = false
                binding.exportExcelBtn.isEnabled = false
            } else {
                binding.exportExcelDialogContainer.visibility = View.GONE
                binding.dialogCoverLayout.visibility = View.GONE
                binding.giveExpBtn.isEnabled = true
                binding.exportExcelBtn.isEnabled = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isSchoolWorkSelected = false
        mBinding = null
    }

    override fun onDetach() { //뒤로가기
        super.onDetach()
        callback.remove()
        viewModel.xlExportMode.value = false
    }

    //--------------------------------------------------------------------------------------사용자함수
    private fun bindViews() {
        binding.giveExpBtn.setOnClickListener { //경험치 주기 버튼 클릭
            schoolWorkRvAdapter.releaseSelection() //활동 선택 해제
            paletteOnOff()
        }

        binding.selectionWorkCompleteBtn.setOnClickListener { //활동 선택 완료 버튼 클릭
            paletteOnOff()
            studentPetRvAdapter.releaseSelection() //학생 선택 해제
            isSchoolWorkSelected = true //이 변수는 StudentAdapter 변수와 연동되어 있음.
            showStudentSelectionBox()
            binding.coverLayout.visibility = View.INVISIBLE
        }

        binding.exportExcelBtn.setOnClickListener { //엑셀 내보내기 버튼
            viewModel.xlExportMode.value = true
        }

        showSelectedStudentsToText()
    }

    //학생 선택 박스 보여주기
    private fun showStudentSelectionBox() {
        binding.selectedStudentsDialogueLayout.isVisible = true
        binding.selectionStudentCompleteBtn.setOnClickListener { //학생 선택 완료
            viewModel.selectedClass.observe(viewLifecycleOwner) {
                viewModel.putExpIntoStudent(
                    studentPetRvAdapter.getSelectedStudents(),
                    schoolWorkRvAdapter.getSelectedSchoolWorks(),
                    teacherId = mainViewModel.currentUser.userId,
                    classId = it.className
                )
            }
            Toast.makeText(
                context, "${studentPetRvAdapter.getNumberOfSelectedStudents()}명의 학생이 경험치 획득!!",
                Toast.LENGTH_SHORT
            ).show()
            studentPetRvAdapter.releaseSelection() //선택 학생 해제
            schoolWorkRvAdapter.releaseSelection() //선택 활동 해제
            binding.selectedStudentsDialogueLayout.isVisible = false
            isSchoolWorkSelected = false
        }
    }

    //팔레트 온오프
    private fun paletteOnOff() {
        schoolWorkRvAdapter.notifyDataSetChanged()
        isPaletteOn = !isPaletteOn
        binding.selectionWorkCompleteBtn.isVisible = isPaletteOn
        binding.schoolworkPalette.isVisible = isPaletteOn
        binding.coverLayout.isVisible = !binding.coverLayout.isVisible
    }

    //선택된 학생 실시간 텍스트창
    private fun showSelectedStudentsToText() {
        viewModel.selectedStudentPetList.observe(viewLifecycleOwner) { pets ->
            val petUserNameList = mutableListOf<String>()
            for (pet in pets) {
                petUserNameList.add(pet.userName)
            }
            if (petUserNameList.isEmpty()) {
                binding.selectedStudentsTextview.text = "선택된 학생이 없습니다."
            } else {
                binding.selectedStudentsTextview.text = "${petUserNameList} 학생을 선택하셨습니다."
            }
        }
    }

    //학생 어뎁터 데이터 세팅
    private fun setStudentPetAdapter(studentPetList: MutableList<Pet>) {
        val adapter = studentPetRvAdapter
        binding.studentListRecyclerview.adapter = adapter
        adapter.submitList(studentPetList)
        adapter.setOnItemClickListener {
            if (!isSchoolWorkSelected && !isPaletteOn) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container_view, StudentPetDetailFragment())
                    .commit()
                viewModel.selectStudentPet(it)
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
