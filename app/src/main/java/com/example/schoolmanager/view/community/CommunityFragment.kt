package com.example.schoolmanager.view.community

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.FragmentCommunityBinding
import com.example.schoolmanager.model.network.Teacher
import com.example.schoolmanager.view.adapter.TeacherRvAdapter
import com.example.schoolmanager.view.intro.LogInActivity
import com.example.schoolmanager.view.main.HomeFragment
import com.example.schoolmanager.view.main.MainViewModel
import com.kakao.sdk.user.UserApiClient
import kotlin.system.exitProcess

class CommunityFragment : Fragment() {

    val mainViewModel: MainViewModel by activityViewModels()
    val viewModel: TeacherViewModel by activityViewModels()

    private var mBinding: FragmentCommunityBinding? = null
    val binding get() = mBinding!!

    private lateinit var callback: OnBackPressedCallback
    private var waitCloseTime = 0L

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - waitCloseTime >= 1500) {
                    waitCloseTime = System.currentTimeMillis()
                    Toast.makeText(context, "back 버튼을 한번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    exitProcess(0)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCommunityBinding.inflate(inflater, container, false)
        binding.logoutBtn.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("로그 아웃")
                .setMessage("로그아웃하고 앱이 종료됩니다.계속 하시겠습니까?")
                .setPositiveButton("로그아웃") { _, _ -> //로그아웃 실행
                    mainViewModel.checkUserFlag()
                    mainViewModel.userFlag.observe(viewLifecycleOwner) { userFlag ->
                        if (userFlag == "KAKAO") { //카카오 로그아웃
                            UserApiClient.instance.logout { error ->
                                if (error != null) {
                                    Log.e("logout", "카카오 로그아웃 실패. SDK에서 토큰 삭제됨", error)
                                } else {
                                    Log.i("logout", "카카오 로그아웃 성공. SDK에서 토큰 삭제됨")
                                    Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT)
                                        .show()
                                    activity?.finish()
                                }
                            }
                        } else { //네이버 로그아웃
                            LogInActivity().naverIdLoginSDK.logout()
                            activity?.finish()
                        }
                    }
                }
                .setNegativeButton("취소") { dialog, _ ->
                    dialog.cancel()
                }
                .create()
                .show()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchTeacherList().observe(viewLifecycleOwner) {
            //todo 본인 목록에서 지우기
            setTeacherAdapter(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        callback.remove()
    }

    //교사 어뎁터 데이터 세팅
    private fun setTeacherAdapter(teacherList: MutableList<Teacher>) {
        val adapter = TeacherRvAdapter()
        binding.teacherRecyclerview.adapter = adapter
        adapter.submitList(teacherList)
        binding.teacherRecyclerview.layoutManager = LinearLayoutManager(context)
        adapter.setOnItemClickListener {
            viewModel.selectTeacher(it)
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container_view, OtherTeacherSchoolWorkManagerFragment())
                .commit()
        }
    }
}