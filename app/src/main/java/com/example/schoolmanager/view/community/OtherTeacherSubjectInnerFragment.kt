package com.example.schoolmanager.view.community

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.FragmentOtherTeacherSchoolWorkInnerBinding
import com.example.schoolmanager.databinding.FragmentOtherTeacherSubjectInnerBinding
import com.example.schoolmanager.util.KeyValue
import com.example.schoolmanager.view.adapter.SubjectRvAdapter
import com.example.schoolmanager.view.main.MainViewModel
import com.example.schoolmanager.view.schoolwork.SchoolWorkViewModel
import com.example.schoolmanager.view.schoolwork.SchoolWorkViewModelFactory


class OtherTeacherSubjectFragment : Fragment() {

    //뷰모델
    private val viewModel: TeacherViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    //context
    private var mBinding: FragmentOtherTeacherSubjectInnerBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentOtherTeacherSubjectInnerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectedTeacher.observe(viewLifecycleOwner) { otherTeacher ->
            viewModel.fetchOtherSubjectList(otherTeacher.userId)
                .observe(viewLifecycleOwner) { subjects ->
                    setOtherSubjectAdapter(subjects)
                }
        }
    }

    //어뎁터 세팅
    private fun setOtherSubjectAdapter(subjectList: MutableList<String>) {
        val adapter = SubjectRvAdapter()
        binding.subjectOuterRecyclerview.adapter = adapter
        adapter.setOnItemClickListener {
            viewModel.selectSubject(it)
            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.other_teacher_school_work_container,
                    OtherTeacherSchoolWorkInnerFragment()
                )
        }
        adapter.submitList(subjectList)
    }
}