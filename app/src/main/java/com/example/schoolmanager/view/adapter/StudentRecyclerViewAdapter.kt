package com.example.schoolmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.databinding.ItemStudentBinding
import com.example.schoolmanager.model.network.Student

class StudentRecyclerViewAdapter() :
    ListAdapter<Student, StudentRecyclerViewAdapter.StudentInfoViewHolder>(differCallback) {

    private var itemClickListener: ((Student) -> Unit)? = null //리스너
    private var selectedStudents: MutableList<Student> = mutableListOf() //클릭된 아이템 넣는 변수
    var isSchoolWorkSelected: Boolean = false

    inner class StudentInfoViewHolder(private val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindViews(student: Student) {
            with(binding) {
                studentNumberContentTextview.text = student.studentNumber.toString()
                studentNameContentTextview.text = student.studentName
                studentLevelContentTextview.text = student.getExp().toString()

                if (isSchoolWorkSelected) {
                    getExpBtn.isEnabled = true
                    getExpBtn.visibility = View.VISIBLE
                    checkBox.visibility = View.VISIBLE
                    getExpBtn.setOnClickListener {
                        applySelection(binding, student)
                    }
                } else {
                    getExpBtn.isEnabled = false
                    getExpBtn.visibility = View.GONE
                    checkBox.visibility = View.GONE
                    root.setOnClickListener {
                        itemClickListener?.let { it(student) } //외부 리스너 설정
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentRecyclerViewAdapter.StudentInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemStudentBinding.inflate(inflater, parent, false)
        return StudentInfoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StudentInfoViewHolder, position: Int) {
        val student = currentList[position]
        holder.bindViews(student)
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<Student>() {
            override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
                return oldItem.studentUid == newItem.studentUid
            }

            override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
                return oldItem == newItem
            }
        }
    }

    //다중 클릭 관련 부분
    private fun applySelection(binding: ItemStudentBinding, student: Student) {
        if (selectedStudents.contains(student)) {
            selectedStudents.remove(student)
            binding.checkBox.isChecked = false
        } else {
            selectedStudents.add(student)
            binding.checkBox.isChecked = true
        }
    }

    //외부 참조 함수
    fun setOnItemClickListener(listener: (Student) -> Unit) {
        itemClickListener = listener
    }

    fun getNumberOfSelectedStudents() = selectedStudents.size
    fun getSelectedStudents() = selectedStudents
    fun releaseSelection() = selectedStudents.clear()
}

