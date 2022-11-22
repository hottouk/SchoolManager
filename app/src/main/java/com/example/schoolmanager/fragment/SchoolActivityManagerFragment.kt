package com.example.schoolmanager.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.ActivityDetailActivity
import com.example.schoolmanager.KeyValue.Companion.LOG_TAG
import com.example.schoolmanager.MainActivity
import com.example.schoolmanager.R
import com.example.schoolmanager.adapter.ActivityListRecyclerViewAdapter
import com.example.schoolmanager.model.SchoolActivity
import com.example.schoolmanager.schoolActivity.AddSchoolActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SchoolActivityManagerFragment : Fragment() {

    //context
    lateinit var mainActivity: MainActivity

    //활동 목록
    var schoolActivityList: MutableList<SchoolActivity> = mutableListOf()

    //UI 관련
    lateinit var schoolActivityListRecyclerView: RecyclerView

    //---------------------------------------------------------------------------------------생명주기
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTestData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.school_activity_manager_fragment, container, false)
        schoolActivityListRecyclerView = rootView.findViewById(R.id.activity_list_recyclerview)
        //버튼 클릭
        rootView.findViewById<FloatingActionButton>(R.id.plus_floating_btn).setOnClickListener {
            val intent = Intent(context,AddSchoolActivity::class.java)
            startActivity(intent)
        }

        inputSchoolActivityDataIntoAdapter(schoolActivityList)
        return rootView
    }
    //--------------------------------------------------------------------------------------사용자함수

    //임시 데이터 삽입 코드
    private fun initTestData() {
        for (i in 1..10) {
            schoolActivityList.add(
                SchoolActivity(
                    uid = i,
                    "활동명",
                    "간단설명을 입력하세요",
                    "생기부 문구를 입력하세요",
                    5,
                    5,
                    5,
                    5,
                    5
                )
            )
        }
    }

    private fun inputSchoolActivityDataIntoAdapter(activities: MutableList<SchoolActivity>) {
        schoolActivityListRecyclerView.adapter =
            ActivityListRecyclerViewAdapter(
                schoolActivityList,
                itemClickListener = {
                Log.d(LOG_TAG, it.uid.toString())
                val intent =
                    Intent(activity, ActivityDetailActivity::class.java)
                intent.putExtra(INTENT_EXTRA_ITEM, it)
                startActivity(intent)
            })
    }

    companion object {
        const val INTENT_EXTRA_ITEM = "itemActivity"
    }
}
