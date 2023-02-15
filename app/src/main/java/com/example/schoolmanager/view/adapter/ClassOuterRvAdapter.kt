package com.example.schoolmanager.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.ItemOuterClassBinding
import com.example.schoolmanager.model.network.SchoolClass

class ClassOuterRvAdapter(private val itemList : MutableList<SchoolClass>) :
    RecyclerView.Adapter<ClassOuterRvAdapter.SchoolClassItemViewHolder>()
{

    var itemClickListener: ((SchoolClass) -> Unit)? = null
    private val selectedSchoolClasses = mutableListOf<SchoolClass>()
    var deleteMode = false
        set(value) {
            selectedSchoolClasses.clear()
            field = value
        }

    inner class SchoolClassItemViewHolder(private val binding: ItemOuterClassBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindViews(item: SchoolClass) {
            binding.classNameTextview.text = item.className
            if (deleteMode) {
                binding.root.setOnClickListener {
                    applySelection(binding, item)
                }
            } else {
                changeBackground(binding, R.color.beige_white)
                binding.root.setOnClickListener {
                    itemClickListener?.let { it(item) }
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassOuterRvAdapter.SchoolClassItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemOuterClassBinding.inflate(inflater, parent, false)
        return SchoolClassItemViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ClassOuterRvAdapter.SchoolClassItemViewHolder,
        position: Int
    ) {
        val item = itemList[position]
        holder.bindViews(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    //다중 클릭 관련 부분
    private fun applySelection(binding: ItemOuterClassBinding, schoolClass: SchoolClass) {
        if (selectedSchoolClasses.contains(schoolClass)) {
            selectedSchoolClasses.remove(schoolClass)
            changeBackground(binding, R.color.beige_white)
        } else {
            selectedSchoolClasses.add(schoolClass)
            changeBackground(binding, R.color.palette_selection) //색변경
        }
    }

    private fun changeBackground(binding: ItemOuterClassBinding, colorId: Int) {
        binding.cardViewInnerCover.setBackgroundColor(
            ContextCompat.getColor(
                binding.root.context,
                colorId
            )
        )
    }

    fun setOnItemClickListener(listener: (SchoolClass) -> Unit) {
        itemClickListener = listener
    }

    fun getSelectedClasses(): MutableList<SchoolClass> {
        return selectedSchoolClasses
    }

    fun getNumberOfSelectedClasses() = selectedSchoolClasses.size

}