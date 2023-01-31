package com.example.schoolmanager.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.ActivityMainBinding
import com.example.schoolmanager.model.network.Teacher
import com.example.schoolmanager.util.KeyValue
import com.example.schoolmanager.view.schoolwork.SchoolWorkManagerFragment
import com.example.schoolmanager.view.student.ClassManagerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    //뷰모델
    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModelFactory

    //뷰
    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!

    //프래그먼트
    private val classManagerFragment = ClassManagerFragment()
    private val schoolWorkManagerFragment = SchoolWorkManagerFragment()
    private val homeFragment = HomeFragment()
    private val communityFragment = CommunityFragment()

    //UI관련
    private val bottomNavigationView: BottomNavigationView by lazy {
        findViewById(R.id.bottom_navigation_view)
    }

    //---------------------------------------------------------------------------------------생명주기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        val user = intent.getParcelableExtra<Teacher>(KeyValue.INTENT_EXTRA_USER_INFO)
        if (user != null) {
            viewModelFactory = MainViewModelFactory(user) //main에서 viewModel로 값 전달
            viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        }
    }


    //--------------------------------------------------------------------------------------사용자함수
    //뷰 초기화
    private fun initViews() {
        replaceFragment(homeFragment)

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home_menu -> replaceFragment(homeFragment)
                R.id.class_manager_menu -> replaceFragment(classManagerFragment)
                R.id.schoolwork_manager_menu -> replaceFragment(schoolWorkManagerFragment)
                R.id.community_menu -> replaceFragment(communityFragment)
            }
            true
        }
    }

    //프래그먼트 교체
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.main_fragment_container_view, fragment)
                commit()
            }
    }
}
