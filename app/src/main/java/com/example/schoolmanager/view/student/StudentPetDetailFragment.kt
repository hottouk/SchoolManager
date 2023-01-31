package com.example.schoolmanager.view.student

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.FragmentStudentPetDetailsBinding
import com.example.schoolmanager.model.network.Pet
import com.example.schoolmanager.util.KeyValue
import com.example.schoolmanager.view.main.MainViewModel


class StudentPetDetailFragment : Fragment() {

    val viewModel: StudentViewModel by activityViewModels()
    val mainViewModel: MainViewModel by activityViewModels()

    private var mBinding: FragmentStudentPetDetailsBinding? = null
    private val binding get() = mBinding!!

    private val radarChartFragment = RadarChartFragment()

    //뒤로가기
    private lateinit var callback: OnBackPressedCallback

    //---------------------------------------------------------------------------------------생명주기
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            //뒤로가기 버튼 클릭 시
            override fun handleOnBackPressed() {
                parentFragmentManager.beginTransaction() //이전 프래그먼트로 돌아간다.
                    .replace(R.id.main_fragment_container_view, StudentManagerFragment())
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
        mBinding = FragmentStudentPetDetailsBinding.inflate(inflater, container, false)
        bindViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectedStudentPet.observe(viewLifecycleOwner) { pet ->
            binding.studentDetailNameTextview.text = pet.userName
            binding.studentDetailNumberTextview.text = pet.userStudentNumber

            binding.studentPetDetailInfoEdittext.setText(pet.petDetailInfo)
            binding.studentAchievedQuestTextview.text = pet.petSimpleInfo

            binding.levelTextview.text = "Lv.${pet.petLevel}"
            binding.moneyTextview.text = pet.money.toString()
            binding.currentExpTextview.text = pet.petExp.toString()
            binding.leadershipStudentScoreTextview.text = pet.leadership.toString()
            binding.academicStudentScoreTextview.text = pet.academicAbility.toString()
            binding.cooperationStudentScoreTextview.text = pet.cooperation.toString()
            binding.careerStudentScoreTextview.text = pet.career.toString()
            binding.sincerityStudentScoreTextview.text = pet.sincerity.toString()

            pet.setPetImage(requireContext(), pet.petUrlImage, binding.studentDetailPetImageview)
            pet.setDetailProgressBar(binding)

            binding.showRadarChartBtn.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.chart_fragment_container, radarChartFragment)
                    .commit()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    //--------------------------------------------------------------------------------------사용자함수
    private fun bindViews() {
        binding.editStudentInfoBtn.setOnClickListener {
            viewModel.selectedClass.observe(viewLifecycleOwner) { schoolClass ->
                viewModel.selectedStudentPet.observe(viewLifecycleOwner) { pet ->
                    val classId = schoolClass.className
                    updateStudentPet(classId, pet)
                }
            }
        }


    }

    private fun updateStudentPet(classId: String, pet: Pet) {
        val dialogue = AlertDialog.Builder(requireContext())
            .setTitle("생기부 수정")
            .setMessage("생기부를 수정하시겠습니까?")
            .setPositiveButton("수정") { _, _ ->
                pet.petDetailInfo =
                    binding.studentPetDetailInfoEdittext.text.toString()
                mainViewModel.postOneStudentPetData(classId, pet)
            }
            .setNegativeButton("취소") { _, _ ->
                return@setNegativeButton
            }
            .create()
        dialogue.show()
    }
}