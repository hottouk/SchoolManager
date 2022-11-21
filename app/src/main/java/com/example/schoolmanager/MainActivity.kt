package com.example.schoolmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.example.schoolmanager.adapter.FragmentAdapter
import com.example.schoolmanager.loginSignUp.LogInActivity
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    //파이어베이스
    val auth: FirebaseAuth by lazy { Firebase.auth }

    //UI관련
    private val tabLayout: TabLayout by lazy {
        findViewById(R.id.tab_layout)
    }
    val viewPager: ViewPager2 by lazy {
        findViewById(R.id.view_pager)
    }

    //---------------------------------------------------------------------------------------생명주기
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        auth.signOut()
    }

    override fun onStart() {
        super.onStart()
        checkLogIn()
    }

    //--------------------------------------------------------------------------------------사용자함수
    //뷰 초기화
    private fun initViews() {
        tabLayout.addTab(tabLayout.newTab().setText("1번째"))
        tabLayout.addTab(tabLayout.newTab().setText("활동 관리"))
        tabLayout.addTab(tabLayout.newTab().setText("학생 관리"))
        tabLayout.addTab(tabLayout.newTab().setText("4번째"))

        viewPager.adapter = FragmentAdapter(this, 4)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { viewPager.setCurrentItem(it.position) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.let { viewPager.setCurrentItem(it.position) }
            }
        })
    }

    //로그인 체크
    private fun checkLogIn() {
        Log.d(ConstKey.LOG_TAG,"로그인 유저 아이디 : ${auth.currentUser}")
        if (auth.currentUser == null) {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }
}