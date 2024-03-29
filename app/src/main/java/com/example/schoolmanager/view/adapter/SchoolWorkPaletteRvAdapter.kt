package com.example.schoolmanager.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.ItemSchoolWorkPaletteBinding
import com.example.schoolmanager.model.network.SchoolWork
import com.example.schoolmanager.view.adapter.SchoolWorkRvAdapter.Companion.differCallBack
import com.example.schoolmanager.view.student.StudentViewModel

class SchoolWorkPaletteRvAdapter(val viewModel: StudentViewModel) :
    ListAdapter<SchoolWork, SchoolWorkPaletteRvAdapter.SchoolWorkPaletteHolder>(
        differCallBack
    ) {
    private var onItemClickListener: ((SchoolWork) -> Unit)? = null //리스너
    private var selectedSchoolWorks: MutableList<SchoolWork> = mutableListOf() //클릭된 아이템 넣는 변수

    inner class SchoolWorkPaletteHolder(private val binding: ItemSchoolWorkPaletteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindViews(schoolWork: SchoolWork) {
            changeBackground(binding, R.color.white)
            binding.schoolworkTitlePaletteTextview.text = schoolWork.schoolWorkTitle
            binding.schoolWorkInfoPaletteTextview.text = schoolWork.schoolWorkSimpleInfo
            binding.schoolworkScorePaletteTextview.text = schoolWork.getTotalScore().toString()
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


    //다중 클릭 관련 부분
    private fun applySelection(binding: ItemSchoolWorkPaletteBinding, schoolWork: SchoolWork) {
        if (selectedSchoolWorks.contains(schoolWork)) {
            selectedSchoolWorks.remove(schoolWork)
            viewModel.getSelectedSchoolWork(selectedSchoolWorks)
            changeBackground(binding, R.color.white)
        } else {
            selectedSchoolWorks.add(schoolWork)
            viewModel.getSelectedSchoolWork(selectedSchoolWorks)
            changeBackground(binding, R.color.palette_selection) //색변경
        }
    }

    private fun changeBackground(binding: ItemSchoolWorkPaletteBinding, colorId: Int) {
        binding.cardViewInnerCover.setBackgroundColor(
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
    fun releaseSelection(){
        selectedSchoolWorks.clear()
    }

    companion object {
        val differCallBack = object : DiffUtil.ItemCallback<SchoolWork>() {
            override fun areItemsTheSame(oldItem: SchoolWork, newItem: SchoolWork): Boolean {
                return oldItem.schoolWorkTitle == newItem.schoolWorkTitle
            }

            override fun areContentsTheSame(oldItem: SchoolWork, newItem: SchoolWork): Boolean {
                return oldItem == newItem
            }
        }
    }
}