package com.example.schoolmanager.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.model.network.SchoolWork
import com.example.schoolmanager.databinding.ItemSchoolWorkBinding
import com.example.schoolmanager.util.KeyValue

class SchoolWorkRvAdapter :
    ListAdapter<SchoolWork, SchoolWorkRvAdapter.SchoolWorkItemViewHolder>(differCallBack) {

    var itemClickListener: ((SchoolWork) -> Unit)? = null

    inner class SchoolWorkItemViewHolder(private val binding: ItemSchoolWorkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindViews(item: SchoolWork) {
            with(binding) {
                schoolWorkTitleTextview.text = item.schoolWorkTitle
                schoolWorkSimpleInfoTextview.text = item.schoolWorkSimpleInfo
                difficultyRating.rating = item.difficulty?.toFloat() ?: 0f
                schoolWorkExp.text = item.getTotalScore().toString()
                schoolWorkMoney.text = item.money.toString()
                schoolWorkMonsterTextview.text = item.subject
                when(item.subject){
                    KeyValue.spinnerItems[0] -> {
                        schoolWorkMonsterTextview.setBackgroundColor(Color.RED)
                        schoolWorkMonsterTextview.text = KeyValue.spinnerItems[0]
                    }
                    KeyValue.spinnerItems[1] -> {
                        schoolWorkMonsterTextview.setBackgroundColor(Color.BLUE)
                        schoolWorkMonsterTextview.text = KeyValue.spinnerItems[1]
                    }
                    KeyValue.spinnerItems[2] -> {
                        schoolWorkMonsterTextview.setBackgroundColor(Color.GREEN)
                        schoolWorkMonsterTextview.text = KeyValue.spinnerItems[2]
                    }
                    KeyValue.spinnerItems[3] -> {
                        schoolWorkMonsterTextview.setBackgroundColor(Color.MAGENTA)
                        schoolWorkMonsterTextview.text = KeyValue.spinnerItems[3]
                    }
                    KeyValue.spinnerItems[4] -> {
                        schoolWorkMonsterTextview.setBackgroundColor(Color.YELLOW)
                        schoolWorkMonsterTextview.text = KeyValue.spinnerItems[4]
                    }
                }
                root.setOnClickListener {
                    itemClickListener?.let { it(item) }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolWorkItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemSchoolWorkBinding.inflate(inflater, parent, false)
        return SchoolWorkItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SchoolWorkItemViewHolder, position: Int) {
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

    //외부 참조 함수
    fun setOnItemClickListener(listener: (SchoolWork) -> Unit) {
        itemClickListener = listener
    }
}

