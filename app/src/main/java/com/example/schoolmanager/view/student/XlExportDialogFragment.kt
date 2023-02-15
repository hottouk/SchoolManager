package com.example.schoolmanager.view.student

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.FragmentStudentManagerBinding
import com.example.schoolmanager.databinding.FragmentXlExportDialogBinding
import com.example.schoolmanager.model.network.Pet
import com.example.schoolmanager.model.network.SchoolClass
import com.example.schoolmanager.view.main.MainViewModel


class XlExportDialogFragment : Fragment() {

    //뷰모델
    private val viewModel: StudentViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    //binding
    private var mBinding: FragmentXlExportDialogBinding? = null
    private val binding get() = mBinding!!

    //선택 반 학생들

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentXlExportDialogBinding.inflate(inflater, container, false)
        binding.exportBtn.setOnClickListener {
            viewModel.myStudentMutablePetList?.let {
                viewModel.createExcelFile(it, viewModel.selectedMutableClass?.className?:"Sheet1")
                viewModel.storeExcelInStorage(requireContext(), "GameInSchoolExcelFile")
                viewModel.shareFile(requireContext(), "GameInSchoolExcelFile")
            }
            viewModel.xlExportMode.value = false
        }

        binding.cancelBtn.setOnClickListener { //취소 버튼
            viewModel.xlExportMode.value = false
        }
        return binding.root
    }
}