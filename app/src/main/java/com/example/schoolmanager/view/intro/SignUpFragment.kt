package com.example.schoolmanager.view.intro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.FragmentSignUpBinding
import com.example.schoolmanager.model.network.Teacher
import com.example.schoolmanager.util.KeyValue
import com.example.schoolmanager.view.main.MainActivity

class SignUpFragment : Fragment() {

    val viewModel: LoginViewModel by activityViewModels()

    private var mBinding: FragmentSignUpBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSignUpBinding.inflate(inflater, container, false)
        bindViews()
        return binding.root
    }

    fun bindViews() {
        binding.characterSelectionMaleImageview.setOnClickListener {
            viewModel.characterImage = "남"
            it.setBackgroundResource(R.drawable.draw_bold_outlines)
            binding.characterSelectionFemaleImageview.setBackgroundResource(R.drawable.draw_outlines)
        }
        binding.characterSelectionFemaleImageview.setOnClickListener {
            viewModel.characterImage = "여"
            it.setBackgroundResource(R.drawable.draw_bold_outlines)
            binding.characterSelectionMaleImageview.setBackgroundResource(R.drawable.draw_outlines)
        }

        binding.signUpBtn.setOnClickListener {
            viewModel.addInfoToTeacherUser(binding)
            viewModel.saveUserPref()
            viewModel.userToFirebase() //DB에 저장
            moveToMainPage(viewModel.fetchTeacherInfo())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    //페이지 이동
    private fun moveToMainPage(teacherUser: Teacher) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(KeyValue.INTENT_EXTRA_USER_INFO, teacherUser)
        startActivity(intent)
    }
}