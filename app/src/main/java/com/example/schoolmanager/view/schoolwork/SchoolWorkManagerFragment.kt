package com.example.schoolmanager.view.schoolwork

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.schoolmanager.databinding.FragmentSchoolWorkManagerBinding
import com.example.schoolmanager.util.KeyValue
import com.example.schoolmanager.view.main.MainViewModel

class SchoolWorkManagerFragment : Fragment() {
    //뷰모델
    private lateinit var viewModel: SchoolWorkViewModel
    private lateinit var viewModelFactory: SchoolWorkViewModelFactory
    private val mainViewModel: MainViewModel by activityViewModels()

    //context
    private var mBinding: FragmentSchoolWorkManagerBinding? = null
    private val binding get() = mBinding!!

    //---------------------------------------------------------------------------------------생명주기
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelFactory = SchoolWorkViewModelFactory(mainViewModel.currentUser)
        viewModel = ViewModelProvider(this, viewModelFactory)[SchoolWorkViewModel::class.java]
        mBinding = FragmentSchoolWorkManagerBinding.inflate(layoutInflater, container, false)
        clkPlusSchoolWorkBtn()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    //--------------------------------------------------------------------------------------사용자함수

    //활동 추가 버튼 클릭
    private fun clkPlusSchoolWorkBtn() {
        binding.plusFloatingBtn.setOnClickListener {
            val intent = Intent(context, AddSchoolWorkActivity::class.java)
            intent.putExtra(KeyValue.INTENT_EXTRA_USER_INFO, mainViewModel.currentUser) //현재 사용자 정보
            startActivity(intent)
        }
    }

}
