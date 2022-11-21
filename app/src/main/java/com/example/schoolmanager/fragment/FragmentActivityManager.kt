package com.example.schoolmanager.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.ActivityDetailActivity
import com.example.schoolmanager.R
import com.example.schoolmanager.adapter.ActivityListRecyclerViewAdapter
import com.example.schoolmanager.model.ItemActivity

class FragmentActivityManager : Fragment() {

    var activityList: MutableList<ItemActivity> = mutableListOf()
    lateinit var activityListRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTestData()
    }

    //임시 데이터 삽입 코드
    private fun initTestData(){
        for (i in 1..10) {
            activityList.add(
                ItemActivity(
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_manager_fragment, container, false)
        activityListRecyclerView = rootView.findViewById(R.id.activity_list_recyclerview)
        //어뎁터 부착
        activityListRecyclerView.adapter =
            ActivityListRecyclerViewAdapter(activityList, itemClickListener = {
                Log.d(LOG_TAG, it.uid.toString())
                val intent =
                    Intent(activity, ActivityDetailActivity::class.java)
                intent.putExtra(INTENT_EXTRA_ITEM, it)
                startActivity(intent)
            })
        return rootView
    }

    companion object {
        const val LOG_TAG = "studentM"
        const val INTENT_EXTRA_ITEM = "itemActivity"
    }
}
