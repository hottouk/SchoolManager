package com.example.schoolmanager.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var mBinding: FragmentHomeBinding? = null
    private val binding get() = mBinding!!
    //---------------------------------------------------------------------------------------생명주기
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.forWhomTextview.text =
            "${viewModel.currentUser.userNickName} ${getString(R.string.welcome_textview)}"
        viewModel.fetchClassesInfo().observe(viewLifecycleOwner) {
            binding.numberOfClassTextview.text = "${it.size}개의 클래스가 등록되어 있습니다."
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}