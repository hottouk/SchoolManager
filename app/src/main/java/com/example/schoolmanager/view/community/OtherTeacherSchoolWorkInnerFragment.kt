package com.example.schoolmanager.view.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.FragmentOtherTeacherSchoolWorkInnerBinding
import com.example.schoolmanager.databinding.FragmentOtherTeacherSubjectInnerBinding
import com.example.schoolmanager.model.network.SchoolWork
import com.example.schoolmanager.view.adapter.SchoolWorkRvAdapter
import com.example.schoolmanager.view.main.MainViewModel

class OtherTeacherSchoolWorkInnerFragment : Fragment() {

    //뷰모델
    private val viewModel: TeacherViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    //context
    private var mBinding: FragmentOtherTeacherSchoolWorkInnerBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentOtherTeacherSchoolWorkInnerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectedTeacher.observe(viewLifecycleOwner) { teacher ->
            viewModel.selectedSubject.observe(viewLifecycleOwner) { subjectName ->
                viewModel.fetchOtherSchoolWorkList(teacher.userId, subjectName)
                    .observe(viewLifecycleOwner) { schoolWorks ->
                        setOtherTeacherSchoolWorkAdapter(schoolWorks)
                    }
            }
        }
    }

    private fun setOtherTeacherSchoolWorkAdapter(schoolWorkList: MutableList<SchoolWork>) {
        val adapter = SchoolWorkRvAdapter()
        binding.schoolWorkListRecyclerview.adapter = adapter
        adapter.submitList(schoolWorkList)
        adapter.setOnItemClickListener {
            viewModel.selectSchoolWork(it)
            activity?.supportFragmentManager?.beginTransaction() //자식 프래그먼트에서 상위 프래그먼트 뷰 호출불가
                ?.replace(R.id.main_fragment_container_view, BringOtherSchoolWorkFragment())
                ?.commit()
        }
    }
}