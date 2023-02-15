package com.example.schoolmanager.view.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.ActivityMainBinding
import com.example.schoolmanager.model.network.Teacher
import com.example.schoolmanager.util.KeyValue
import com.example.schoolmanager.util.KeyValue.Companion.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION
import com.example.schoolmanager.view.community.CommunityFragment
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
        requestWriteStoragePermission()
        val user = intent.getParcelableExtra<Teacher>(KeyValue.INTENT_EXTRA_USER_INFO)
        if (user != null) {
            viewModelFactory = MainViewModelFactory(user) //main에서 viewModel로 값 전달
            viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        }
        initViews()
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

    //외부저장소 권한 요청
    private fun requestWriteStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { //구버젼일 경우
            Toast.makeText(this, "권한 요청 성공", Toast.LENGTH_SHORT).show()
        } else { //28이상의 버젼일 경우
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION
            )
        }
    }

    //요청 결과
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val writeExternalStoragePermissionGranted =
            requestCode == REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED

        if (writeExternalStoragePermissionGranted) {
            //권한 요청 성공시
            Toast.makeText(this, "권한 요청 성공", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "권한이 거부되어 앱이 작동하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
