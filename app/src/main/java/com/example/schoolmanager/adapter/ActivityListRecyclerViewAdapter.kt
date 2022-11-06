package com.example.schoolmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.model.ItemActivity
import com.example.schoolmanager.R

class ActivityListRecyclerViewAdapter(
    private val activityList: List<ItemActivity>,
    val itemClickListener: (ItemActivity) -> Unit
) :
    RecyclerView.Adapter<ActivityListRecyclerViewAdapter.ActivitiesViewHolder>() {

    inner class ActivitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //활동제목
        private val activityUidTextView: TextView
        private val activityTitleTextView: TextView
        //점수
        private val leadershipScoreTextView: TextView
        private val academicScoreTextView: TextView
        private val cooperationScoreTextView: TextView
        private val sincerityScoreTextView: TextView
        private val careerScoreTextView: TextView
        //버튼
        private val inputDetailInfoBtn: AppCompatButton

        init {
            //활동제목
            activityUidTextView = itemView.findViewById(R.id.id_activity_number_textview)
            activityTitleTextView = itemView.findViewById(R.id.activity_title_textview)
            //점수
            leadershipScoreTextView = itemView.findViewById(R.id.leadership_textview)
            academicScoreTextView = itemView.findViewById(R.id.academic_textview)
            cooperationScoreTextView = itemView.findViewById(R.id.cooperation_textview)
            sincerityScoreTextView = itemView.findViewById(R.id.sincerity_textview)
            careerScoreTextView = itemView.findViewById(R.id.career_textview)
            //활동 정보 추가 버튼
            inputDetailInfoBtn = itemView.findViewById(R.id.input_detail_Info_btn)

        }

        fun bind(activity: ItemActivity) {
            activityUidTextView.text = activity.uid.toString()
            activityTitleTextView.text = activity.activityTitle

            leadershipScoreTextView.text = activity.leadership.toString()
            academicScoreTextView.text = activity.academicAbility.toString()
            cooperationScoreTextView.text = activity.cooperation.toString()
            sincerityScoreTextView.text = activity.sincerity.toString()
            careerScoreTextView.text = activity.career.toString()
            //버튼 Listener
            inputDetailInfoBtn.setOnClickListener {
                itemClickListener(activity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivitiesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.activity_row_item_layout, parent, false)
        return ActivitiesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ActivitiesViewHolder, position: Int) {
        val activity = activityList[position]
        holder.bind(activity)
    }

    override fun getItemCount(): Int {
        return activityList.size
    }
}

