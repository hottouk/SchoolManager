package com.example.schoolmanager.view.schoolwork

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.FragmentInnerSchoolWorkBinding
import com.example.schoolmanager.model.network.SchoolWork
import com.example.schoolmanager.util.KeyValue
import com.example.schoolmanager.view.adapter.SchoolWorkRvAdapter
import com.example.schoolmanager.view.community.BringOtherSchoolWorkFragment
import com.example.schoolmanager.view.main.MainViewModel

class SchoolWorkInnerFragment : Fragment() {
    //뷰모델
    private lateinit var viewModel: SchoolWorkViewModel
    private lateinit var viewModelFactory: SchoolWorkViewModelFactory
    private val mainViewModel: MainViewModel by activityViewModels()

    //context
    private var mBinding: FragmentInnerSchoolWorkBinding? = null
    private val binding get() = mBinding!!

    //리사이클러뷰 어댑터
    private var schoolWorkList = mutableListOf<SchoolWork>()

    //---------------------------------------------------------------------------------------생명주기
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelFactory = SchoolWorkViewModelFactory(mainViewModel.currentUser)
        viewModel = ViewModelProvider(this, viewModelFactory)[SchoolWorkViewModel::class.java]
        mBinding = FragmentInnerSchoolWorkBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener(KeyValue.FRAG_TO_FRAG_SUBJECT_KEY) { _, bundle ->
            val selectedSubject = bundle.getString(KeyValue.FRAG_BUNDLE_SUBJECT_KEY)
            if (selectedSubject != null) {
                viewModel.fetchSchoolWorkList(mainViewModel.currentUser.userId, selectedSubject)
                    .observe(viewLifecycleOwner) {
                        setSchoolWorkAdapter(it)
                    }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        schoolWorkList.clear()
        mBinding = null
    }

    //--------------------------------------------------------------------------------------사용자함수

    //어뎁터 세팅
    private fun setSchoolWorkAdapter(schoolWorkList: MutableList<SchoolWork>) {
        val adapter = SchoolWorkRvAdapter()
        binding.schoolWorkListRecyclerview.adapter = adapter
        adapter.setOnItemClickListener {
            val intent = Intent(context, SchoolWorkDetailActivity::class.java)
            intent.putExtra(KeyValue.INTENT_EXTRA_USER_INFO, mainViewModel.currentUser) //현재 사용자 정보
            intent.putExtra(KeyValue.INTENT_EXTRA_SCHOOL_WORK, it) // 활동 정보
            startActivity(intent)
        }
        adapter.submitList(schoolWorkList)
    }
}