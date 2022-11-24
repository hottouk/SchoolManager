package com.example.schoolmanager.fragment

import android.content.AbstractThreadedSyncAdapter
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
import com.example.schoolmanager.KeyValue
import com.example.schoolmanager.KeyValue.Companion.LOG_TAG
import com.example.schoolmanager.MainActivity
import com.example.schoolmanager.R
import com.example.schoolmanager.adapter.ActivityListRecyclerViewAdapter
import com.example.schoolmanager.model.SchoolActivity
import com.example.schoolmanager.schoolActivity.AddSchoolActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SchoolActivityManagerFragment : Fragment() {

    //파이어베이스
    private val schoolActDB: DatabaseReference by lazy { Firebase.database.reference.child(KeyValue.DB_SCHOOL_ACTIVITIES) }
    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.forEach { schoolActiviity ->
                val model = schoolActiviity.getValue(SchoolActivity::class.java)
                model ?: return
                schoolActivityList.add(model)
            }
            schoolActivityRecyclerVIewAdapter = ActivityListRecyclerViewAdapter()
            inputSchoolActivityDataIntoAdapter(schoolActivityList)
        }

        override fun onCancelled(error: DatabaseError) {
        }
    }

    //context
    lateinit var mainActivity: MainActivity

    //활동 목록
    var schoolActivityList: MutableList<SchoolActivity> = mutableListOf()

    //UI 관련
    private lateinit var schoolActivityListRecyclerView: RecyclerView
    private lateinit var schoolActivityRecyclerVIewAdapter: ActivityListRecyclerViewAdapter

    //---------------------------------------------------------------------------------------생명주기
    override fun onAttach(context: Context) {
        Log.d(KeyValue.LOG_TAG, "활동 Fragment: OnAttach")
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(KeyValue.LOG_TAG, "활동 Fragment: OnCreateView")
        val rootView = inflater.inflate(R.layout.school_activity_manager_fragment, container, false)
        schoolActivityListRecyclerView = rootView.findViewById(R.id.activity_list_recyclerview)
        initView()
        schoolActDB.addValueEventListener(valueEventListener)
        clkPlusActivityBtn(rootView)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        Log.d(KeyValue.LOG_TAG, "활동 Fragment: OnResume")
        schoolActivityRecyclerVIewAdapter = ActivityListRecyclerViewAdapter()
        schoolActivityRecyclerVIewAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(KeyValue.LOG_TAG, "활동 Fragment: OnDestroy")
        schoolActDB.removeEventListener(valueEventListener)
    }

    //--------------------------------------------------------------------------------------사용자함수

    //임시 데이터 삽입 코드
    private fun initView() {
        schoolActivityRecyclerVIewAdapter = ActivityListRecyclerViewAdapter()
        schoolActivityList.clear()
    }

    //활동 추가 버튼 클릭
    private fun clkPlusActivityBtn(rootView: View) {
        rootView.findViewById<FloatingActionButton>(R.id.plus_floating_btn).setOnClickListener {
            val intent = Intent(context, AddSchoolActivity::class.java)
            startActivity(intent)
        }
    }

    //활동 어뎁터에 데이터 삽입
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
