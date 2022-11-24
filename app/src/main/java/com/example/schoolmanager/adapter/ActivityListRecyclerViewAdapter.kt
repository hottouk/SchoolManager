package com.example.schoolmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.model.SchoolActivity
import com.example.schoolmanager.R

class ActivityListRecyclerViewAdapter(
    private val activityList: MutableList<SchoolActivity> = mutableListOf(),
    val itemClickListener: (SchoolActivity) -> Unit ={}
) :
    RecyclerView.Adapter<ActivityListRecyclerViewAdapter.ActivitiesViewHolder>() {

    inner class ActivitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //활동제목
        private val activityUidTextView: TextView =
            itemView.findViewById(R.id.id_activity_number_textview)
        private val activityTitleTextView: TextView =
            itemView.findViewById(R.id.activity_title_textview)

        //점수
        private val leadershipScoreTextView: TextView =
            itemView.findViewById(R.id.leadership_textview)
        private val academicScoreTextView: TextView = itemView.findViewById(R.id.academic_textview)
        private val cooperationScoreTextView: TextView =
            itemView.findViewById(R.id.cooperation_textview)
        private val sincerityScoreTextView: TextView =
            itemView.findViewById(R.id.sincerity_textview)
        private val careerScoreTextView: TextView = itemView.findViewById(R.id.career_textview)

        //버튼
        private val inputDetailInfoBtn: AppCompatButton =
            itemView.findViewById(R.id.input_detail_Info_btn)

        fun bind(activity: SchoolActivity) {
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
        val activityItemView = inflater.inflate(R.layout.activity_row_item_layout, parent, false)
        return ActivitiesViewHolder(activityItemView)
    }

    override fun onBindViewHolder(holder: ActivitiesViewHolder, position: Int) {
        val activity = activityList[position]
        holder.bind(activity)
    }

    override fun getItemCount(): Int {
        return activityList.size
    }
}

