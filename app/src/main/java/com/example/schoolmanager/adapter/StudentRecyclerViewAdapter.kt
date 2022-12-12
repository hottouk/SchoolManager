package com.example.schoolmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.model.Student
import java.util.*

class StudentRecyclerViewAdapter(
    val itemClickListener: (Student) -> Unit = {}
) :
    ListAdapter<Student,StudentRecyclerViewAdapter.studentInfoViewHolder>(differCallback) {

    inner class studentInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val studentNameTextView: TextView = itemView.findViewById(R.id.student_name_content_textview)
        private val studentNumberTextView: TextView = itemView.findViewById(R.id.student_number_content_textview)

        fun bindViews(student: Student) {
            studentNumberTextView.text = student.studentNumber.toString()
            studentNameTextView.text = student.studentName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): studentInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_student, parent, false)
        return studentInfoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: studentInfoViewHolder, position: Int) {
        val student = currentList[position]
        holder.bindViews(student)
    }

    companion object{
        val differCallback = object : DiffUtil.ItemCallback<Student>(){
            override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
                return oldItem.studentUid == newItem.studentUid
            }

            override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
                return oldItem == newItem
            }
        }
    }

}

