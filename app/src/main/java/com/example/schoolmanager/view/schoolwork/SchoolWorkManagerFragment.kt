package com.example.schoolmanager.view.schoolwork

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.schoolmanager.view.adapter.SchoolWorkRecyclerViewAdapter
import com.example.schoolmanager.databinding.FragmentSchoolWorkManagerBinding
import com.example.schoolmanager.model.network.SchoolWork
import com.example.schoolmanager.util.KeyValue

class SchoolWorkManagerFragment : Fragment() {
    //뷰모델
    private val viewModel: SchoolWorkViewModel by activityViewModels()

    //context
    private var mBinding: FragmentSchoolWorkManagerBinding? = null
    private val binding get() = mBinding!!

    //리사이클러뷰 어댑터
    private var schoolWorkList = mutableListOf<SchoolWork>()

    //---------------------------------------------------------------------------------------생명주기
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSchoolWorkManagerBinding.inflate(layoutInflater, container, false)
        bindViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchSchoolWorkList().observe(viewLifecycleOwner) {
            setSchoolWorkAdapter(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        schoolWorkList.clear()
        mBinding = null
    }

    //--------------------------------------------------------------------------------------사용자함수
    //뷰 초기화 코드
    private fun bindViews() {
        clkPlusActivityBtn()
    }

    //활동 추가 버튼 클릭
    private fun clkPlusActivityBtn() {
        binding.plusFloatingBtn.setOnClickListener {
            val intent = Intent(context, AddSchoolWork::class.java)
            startActivity(intent)
        }
    }

    //어뎁터 세팅
    private fun setSchoolWorkAdapter(schoolWorkList: MutableList<SchoolWork>) {
        val adapter = SchoolWorkRecyclerViewAdapter()
        binding.schoolWorkListRecyclerview.adapter = adapter
        adapter.setOnItemClickListener {
            val intent = Intent(context, SchoolWorkDetailActivity::class.java)
            intent.putExtra(KeyValue.INTENT_EXTRA_SCHOOL_WORK,it)
            startActivity(intent)
        }
        adapter.submitList(schoolWorkList)
    }
}
