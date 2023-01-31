package com.example.schoolmanager.view.student

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.schoolmanager.*
import com.example.schoolmanager.view.adapter.SchoolWorkPaletteRvAdapter
import com.example.schoolmanager.databinding.FragmentStudentManagerBinding
import com.example.schoolmanager.model.network.Pet
import com.example.schoolmanager.model.network.SchoolWork
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
    private var teacherId: String? = null
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
        teacherId = mainViewModel.currentUser.userId


        bindViews() //뷰 장착
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val teacherId = mainViewModel.getUser().userId
        viewModel.selectedClass.observe(viewLifecycleOwner) { schoolClass ->
            val classId = schoolClass.className
            viewModel.fetchMyStudentPetList(mainViewModel.currentUser.userId, classId)
                .observe(viewLifecycleOwner) { classStudentPets ->
                    setStudentPetAdapter(classStudentPets)
                }
            binding.actionBar.text = "${classId}반 학생 관리"
        }

        viewModel.selectedClass.observe(viewLifecycleOwner) {
            viewModel.fetchSchoolWorkList(teacherId, it.subject)
                .observe(viewLifecycleOwner) { schoolWorks ->
                    setPaletteAdapter(schoolWorks)
                }
        }
        yieldSelectedStudentsToText()
    }


    override fun onDestroy() {
        super.onDestroy()
        isSchoolWorkSelected = false
        mBinding = null
    }

    override fun onDetach() { //뒤로가기
        super.onDetach()
        callback.remove()
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
    }

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

    private fun paletteOnOff() { //팔레트 온오프
        schoolWorkRvAdapter.notifyDataSetChanged()
        isPaletteOn = !isPaletteOn
        binding.selectionWorkCompleteBtn.isVisible = isPaletteOn
        binding.schoolworkPalette.isVisible = isPaletteOn
        binding.coverLayout.isVisible = !binding.coverLayout.isVisible
    }

    //선택된 학생 실시간으로 텍스트창에 띄우기
    private fun yieldSelectedStudentsToText() {
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
                Log.d("선택", "선택 펫 ${it.petId}")
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
