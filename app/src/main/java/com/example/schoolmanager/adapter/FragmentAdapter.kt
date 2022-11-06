package com.example.schoolmanager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.schoolmanager.fragment.FragmentActivityManager
import com.example.schoolmanager.fragment.FragmentStudentManager

class FragmentAdapter(fragmentActivity: FragmentActivity, val tabCount : Int) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return tabCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentStudentManager()
            1 -> FragmentActivityManager()
            else -> FragmentStudentManager()
        }
    }
}