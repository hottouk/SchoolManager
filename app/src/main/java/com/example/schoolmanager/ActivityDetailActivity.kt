package com.example.schoolmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.schoolmanager.model.ItemActivity

class ActivityDetailActivity : AppCompatActivity() {
    private val titleTextView: EditText by lazy { findViewById(R.id.activity_title_edittext) }
    private val simpleInfoEditText: EditText by lazy { findViewById(R.id.student_achieved_quest_textview) }
    private val detailInfoEditText: EditText by lazy { findViewById(R.id.activity_detail_info_edittext) }

    private val leadershipScoreEditText: EditText by lazy { findViewById(R.id.leadership_student_score_edittext) }
    private val academicScoreEditText: EditText by lazy { findViewById(R.id.academic_score_edittext) }
    private val cooperationScoreEditText: EditText by lazy { findViewById(R.id.cooperation_score_edittext) }
    private val careerScoreEditText: EditText by lazy { findViewById(R.id.career_score_edittext) }
    private val sincerityScoreEditText: EditText by lazy { findViewById(R.id.sincerity_score_edittext) }

    private val editInfoBtn: Button by lazy { findViewById(R.id.edit_student_info_btn) }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //intent 받는 부분
        val itemActivity = intent.getParcelableExtra<ItemActivity?>(INTENT_EXTRA_ITEM)
        itemActivity?.let {
            titleTextView.setText(it.activityTitle)
            simpleInfoEditText.setText(it.activitySimpleInfo)
            detailInfoEditText.setText(it.activityDetailInfo)
        }
    }

    companion object {
        const val LOG_TAG = "studentM"
        const val INTENT_EXTRA_ITEM = "itemActivity"
    }
}