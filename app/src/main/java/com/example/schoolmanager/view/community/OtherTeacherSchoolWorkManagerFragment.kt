package com.example.schoolmanager.view.community

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.FragmentTeacherSchoolWorkBinding
import com.example.schoolmanager.view.main.MainViewModel

class OtherTeacherSchoolWorkManagerFragment : Fragment() {

    val mainViewModel: MainViewModel by activityViewModels()
    val viewModel: TeacherViewModel by activityViewModels()

    private var mBinding: FragmentTeacherSchoolWorkBinding? = null
    val binding get() = mBinding!!

    private lateinit var callback: OnBackPressedCallback

    //---------------------------------------------------------------------------------------생명주기

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container_view, CommunityFragment())
                    .commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentTeacherSchoolWorkBinding.inflate(inflater, container, false)
        viewModel.selectedTeacher.observe(viewLifecycleOwner) {
            binding.actionBar.text = "${it.userName.toString()} 선생님의 활동"
            viewModel.fetchOtherSubjectList(it.userId).observe(viewLifecycleOwner) { subjects->
                if (subjects.isEmpty()) {
                    binding.noSchoolworkTextview.visibility = View.VISIBLE
                } else {
                    binding.noSchoolworkTextview.visibility = View.GONE
                }
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        callback.remove()
    }
}