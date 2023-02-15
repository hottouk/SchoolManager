package com.example.schoolmanager.view.community

import android.content.Context
import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.FragmentBringOtherSchoolWorkBinding
import com.example.schoolmanager.view.main.MainViewModel

class BringOtherSchoolWorkFragment : Fragment() {

    private val viewModel: TeacherViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private var mBinding: FragmentBringOtherSchoolWorkBinding? = null
    val binding get() = mBinding!!

    lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_fragment_container_view,
                        OtherTeacherSchoolWorkManagerFragment()
                    ).commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentBringOtherSchoolWorkBinding.inflate(inflater, container, false)
        bindViews()
        viewModel.selectedTeacher.observe(viewLifecycleOwner) {
            binding.actionBar.text = "${it.userName} 선생님의 활동"
        }
        viewModel.selectedSchoolWork.observe(viewLifecycleOwner) {
            binding.schoolWorkTitleEditTextview.text = it.schoolWorkTitle
            binding.schoolWorkSimpleInfoTextview.text = it.schoolWorkSimpleInfo
            binding.schoolWorkDetailEditTextview.text = it.schoolWorkDetailInfo
            binding.expectedDifficultyRating.rating = it.difficulty?.toFloat() ?: 0F
            binding.subjectTextview.text = it.subject

            binding.leadershipNumberpicker.text = it.leadership.toString()
            binding.academicNumberpicker.text = it.academicAbility.toString()
            binding.careerNumberpicker.text = it.career.toString()
            binding.cooperationNumberpicker.text = it.cooperation.toString()
            binding.sincerityNumberpicker.text = it.sincerity.toString()
            binding.moneyNumberpicker.text = it.money.toString()
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        callback.remove()
    }

    fun bindViews() {
        binding.schoolWorkDetailEditTextview.movementMethod = ScrollingMovementMethod()
        binding.bringSchoolWorkBtn.setOnClickListener {
            bringSchoolWork()
        }
    }

    fun bringSchoolWork() {
        viewModel.selectedTeacher.observe(viewLifecycleOwner) { teacher ->
            viewModel.selectedSchoolWork.observe(viewLifecycleOwner) { schoolWork ->
                mainViewModel.postBringSchoolWork(schoolWork, teacher)
            }
        }
    }

}