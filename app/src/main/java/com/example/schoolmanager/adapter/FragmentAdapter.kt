package com.example.schoolmanager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.schoolmanager.fragment.SchoolActivityManagerFragment
import com.example.schoolmanager.fragment.StudentManagerFragment

class FragmentAdapter(fragmentActivity: FragmentActivity, val tabCount : Int) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return tabCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StudentManagerFragment()
            1 -> SchoolActivityManagerFragment()
            else -> StudentManagerFragment()
        }
    }
}