package com.example.schoolmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.model.ItemActivity
import com.example.schoolmanager.model.ItemStudent

class StudentListRecyclerViewAdapter(
    val studentList: List<ItemStudent>,
    val itemClickListener: (ItemStudent) -> Unit
) :
    RecyclerView.Adapter<StudentListRecyclerViewAdapter.studentInfoViewHolder>() {

    inner class studentInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val studentNameTextView: TextView
        private val studentNumberTextView: TextView
        private val studentDetailsBtn: AppCompatButton

        init {
            studentNameTextView = itemView.findViewById(R.id.student_name_content_textview)
            studentNumberTextView = itemView.findViewById(R.id.student_number_content_textview)
            studentDetailsBtn = itemView.findViewById(R.id.student_details_btn)
        }

        fun bindView(student: ItemStudent) {
            studentNumberTextView.text = student.studentNumber.toString()
            studentNameTextView.text = student.studentName
            studentDetailsBtn.setOnClickListener {
                itemClickListener(student)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): studentInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.student_row_item_layout, parent, false)
        return studentInfoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: studentInfoViewHolder, position: Int) {
        val student = studentList[position]
        holder.bindView(student)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }
}

