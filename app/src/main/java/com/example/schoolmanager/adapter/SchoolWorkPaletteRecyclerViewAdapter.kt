package com.example.schoolmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.ItemSchoolWorkPaletteBinding
import com.example.schoolmanager.model.SchoolWork

class SchoolWorkPaletteRecyclerViewAdapter :
    ListAdapter<SchoolWork, SchoolWorkPaletteRecyclerViewAdapter.SchoolWorkPaletteHolder>(
        differCallBack
    ) {
    private var onItemClickListener: ((SchoolWork) -> Unit)? = null //리스너
    private var selectedSchoolWorks: MutableList<SchoolWork> = mutableListOf() //클릭된 아이템 넣는 변수

    inner class SchoolWorkPaletteHolder(private val binding: ItemSchoolWorkPaletteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val schoolWorkTitleTextView = binding.schoolworkTitlePaletteTextview
        private val schoolWorkSimpleInfo: TextView = binding.schoolWorkInfoPaletteTextview

        fun bindViews(schoolWork: SchoolWork) {
            schoolWorkTitleTextView.text = schoolWork.schoolWorkTitle
            schoolWorkSimpleInfo.text = schoolWork.schoolWorkSimpleInfo

            binding.root.setOnClickListener {
                applySelection(binding, schoolWork)
                onItemClickListener?.let { it(schoolWork) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolWorkPaletteHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemSchoolWorkPaletteBinding.inflate(inflater, parent, false)
        return SchoolWorkPaletteHolder(itemView)
    }

    override fun onBindViewHolder(holder: SchoolWorkPaletteHolder, position: Int) {
        val schoolWork = currentList[position]
        holder.bindViews(schoolWork)
    }

    companion object {
        val differCallBack = object : DiffUtil.ItemCallback<SchoolWork>() {
            override fun areItemsTheSame(oldItem: SchoolWork, newItem: SchoolWork): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: SchoolWork, newItem: SchoolWork): Boolean {
                return oldItem == newItem
            }
        }
    }

    //다중 클릭 관련 부분
    private fun applySelection(binding: ItemSchoolWorkPaletteBinding, schoolWork: SchoolWork) {
        if (selectedSchoolWorks.contains(schoolWork)) {
            selectedSchoolWorks.remove(schoolWork)
            changeBackground(binding, R.color.white)
        } else {
            selectedSchoolWorks.add(schoolWork)
            changeBackground(binding, R.color.teal_200) //색변경
        }
    }

    private fun changeBackground(binding: ItemSchoolWorkPaletteBinding, colorId: Int) {
        binding.schoolworkPaletteContainer.setBackgroundColor(
            ContextCompat.getColor(
                binding.root.context,
                colorId
            )
        )
    }

    //외부 참조 함수
    fun setOnItemClickListener(listener: (SchoolWork) -> Unit) {
        onItemClickListener = listener
    }

    fun getNumberOfSelectedSchoolWorks() = selectedSchoolWorks.size
    fun getSelectedSchoolWorks() = selectedSchoolWorks
}