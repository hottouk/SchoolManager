package com.example.schoolmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.schoolmanager.fragment.HomeFragment
import com.example.schoolmanager.fragment.SchoolWorkManagerFragment
import com.example.schoolmanager.fragment.StudentManagerFragment
import com.example.schoolmanager.loginSignUp.LogInActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    //파이어베이스
    val auth: FirebaseAuth by lazy { Firebase.auth }

    //UI관련
    private val bottomNavigationView: BottomNavigationView by lazy {
        findViewById(R.id.bottom_navigation_view)
    }
    //---------------------------------------------------------------------------------------생명주기
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    override fun onStart() {
        super.onStart()
        checkLogIn()
    }

    //--------------------------------------------------------------------------------------사용자함수
    //뷰 초기화
    private fun initViews() {
        val studentManagerFragment = StudentManagerFragment()
        val schoolWorkManagerFragment = SchoolWorkManagerFragment()
        val homeFragment = HomeFragment()
        replaceFragment(homeFragment)

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home_menu -> replaceFragment(homeFragment)
                R.id.student_manager_menu->  replaceFragment(studentManagerFragment)
                R.id.schoolwork_manager_menu->  replaceFragment(schoolWorkManagerFragment)
                R.id.my_page_menu-> replaceFragment(schoolWorkManagerFragment)
            }
            true
        }

    }

    //프래그먼트 교체
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragment_container,fragment)
                commit()
            }
    }

    //로그인 체크
    fun checkLogIn() {
        Log.d(KeyValue.LOG_TAG,"로그인 유저 아이디 : ${auth.currentUser}")
        if (auth.currentUser == null) {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }
}