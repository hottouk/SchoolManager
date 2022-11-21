package com.example.schoolmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.schoolmanager.adapter.FragmentAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private val tabLayout: TabLayout by lazy {
        findViewById(R.id.tab_layout)
    }
    val viewPager: ViewPager2 by lazy {
        findViewById(R.id.view_pager)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        tabLayout.addTab(tabLayout.newTab().setText("1번째"))
        tabLayout.addTab(tabLayout.newTab().setText("활동 관리"))
        tabLayout.addTab(tabLayout.newTab().setText("학생 관리"))
        tabLayout.addTab(tabLayout.newTab().setText("4번째"))

        viewPager.adapter = FragmentAdapter(this,4)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
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
}