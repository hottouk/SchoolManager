package com.example.schoolmanager.view.schoolwork

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.example.schoolmanager.databinding.FragmentInnerSubjectBinding
import com.example.schoolmanager.util.KeyValue
import com.example.schoolmanager.view.adapter.SubjectRvAdapter
import com.example.schoolmanager.view.main.MainViewModel

class SubjectInnerFragment : Fragment() {

    //뷰모델
    private lateinit var viewModel: SchoolWorkViewModel
    private lateinit var viewModelFactory: SchoolWorkViewModelFactory
    private val mainViewModel: MainViewModel by activityViewModels()

    //context
    private var mBinding: FragmentInnerSubjectBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelFactory = SchoolWorkViewModelFactory(mainViewModel.currentUser)
        viewModel = ViewModelProvider(this, viewModelFactory)[SchoolWorkViewModel::class.java]
        mBinding = FragmentInnerSubjectBinding.inflate(inflater, container, false)
        viewModel.fetchMySubjectList(mainViewModel.currentUser.userId).observe(viewLifecycleOwner){
            Log.d("선택",it.toString())
            setMySubjectAdapter(it)
        }
        return binding.root
    }

    //어뎁터 세팅
    private fun setMySubjectAdapter(subjectList: MutableList<String>) {
        val adapter = SubjectRvAdapter()
        binding.subjectOuterRecyclerview.adapter = adapter
        adapter.setOnItemClickListener {
           Log.d("선택과목", "$it 넘김")
            setFragmentResult(
                KeyValue.FRAG_TO_FRAG_SUBJECT_KEY,
                bundleOf(KeyValue.FRAG_BUNDLE_SUBJECT_KEY to it)
            )
        }
        adapter.submitList(subjectList)
    }
}