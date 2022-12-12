package com.example.schoolmanager.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.*
import com.example.schoolmanager.adapter.SchoolWorkRecyclerViewAdapter
import com.example.schoolmanager.model.SchoolWork
import com.example.schoolmanager.schoolActivity.AddSchoolActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class SchoolWorkManagerFragment : Fragment(R.layout.fragment_school_work_manager) {

    //파이어베이스
    private val auth: FirebaseAuth by lazy { Firebase.auth } //인증

    //활동DB
    private val schoolWorkDB: DatabaseReference by lazy { Firebase.database.reference.child(KeyValue.DB_SCHOOL_ACTIVITIES) }
    private val schoolWorkValueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {//데이터 변경시 DB에서 받아오기
            snapshot.children.forEach { schoolWork ->
                val model = schoolWork.getValue(SchoolWork::class.java)
                model ?: return
                schoolWorkList.add(model)
            }
            inputSchoolActivityDataIntoAdapter(schoolWorkList)
        }

        override fun onCancelled(error: DatabaseError) {
        }
    }

    //context
    lateinit var mainActivity: MainActivity

    //활동 목록
    var schoolWorkList: MutableList<SchoolWork> = mutableListOf()

    //UI 관련
    private lateinit var schoolWorkRecyclerView: RecyclerView
    private lateinit var schoolWorkRecyclerViewAdapter: SchoolWorkRecyclerViewAdapter
    //드래그앤드랍
    private val itemTouchSimpleCallback = ItemTouchSimpleCallback()
    private val itemTouchHelper = ItemTouchHelper(itemTouchSimpleCallback)

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
        val rootView = inflater.inflate(R.layout.fragment_school_work_manager, container, false)
        //UI 관련
        schoolWorkRecyclerView = rootView.findViewById(R.id.activity_list_recyclerview)
        initView() //뷰 초기화
        //데이터 받아오기
        schoolWorkDB.addValueEventListener(schoolWorkValueEventListener)
        //활동 추가 버튼
        clkPlusActivityBtn(rootView)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        Log.d(KeyValue.LOG_TAG, "활동 Fragment: OnResume")
        schoolWorkRecyclerViewAdapter = SchoolWorkRecyclerViewAdapter()
        schoolWorkRecyclerViewAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(KeyValue.LOG_TAG, "활동 Fragment: OnDestroy")
        schoolWorkDB.removeEventListener(schoolWorkValueEventListener)
    }

    //--------------------------------------------------------------------------------------사용자함수
    //뷰 초기화 코드
    private fun initView() {
        schoolWorkRecyclerViewAdapter = SchoolWorkRecyclerViewAdapter()
        schoolWorkList.clear()
    }

    //활동 추가 버튼 클릭
    private fun clkPlusActivityBtn(rootView: View) {
        rootView.findViewById<FloatingActionButton>(R.id.plus_floating_btn).setOnClickListener {
            val intent = Intent(context, AddSchoolActivity::class.java)
            startActivity(intent)
        }
    }
    //어뎁터에 데이터 삽입
    private fun inputSchoolActivityDataIntoAdapter(schoolWorkList: MutableList<SchoolWork>) {
        val adapter =
            SchoolWorkRecyclerViewAdapter(
                itemClickListener = {
                    val intent =
                        Intent(activity, ActivityDetailActivity::class.java)
                    intent.putExtra(INTENT_EXTRA_ITEM, it)
                    startActivity(intent)
                })
        enableDragAndDrop(adapter)
        schoolWorkRecyclerView.adapter = adapter
        adapter.submitList(schoolWorkList)
    }

    //드래그 앤 드랍 코드
    private fun enableDragAndDrop(adapter : SchoolWorkRecyclerViewAdapter){
        itemTouchSimpleCallback.setOnItemMoveListener(object : ItemTouchSimpleCallback.OnItemMoveListener {
            override fun onItemMove(from: Int, to: Int) {
                Log.d(KeyValue.LOG_TAG, "from Position : $from, to Position : $to")

                // userList에도 값이 변하는 걸 원한다면 Collections.swap으로 변경
                Collections.swap(schoolWorkList, from, to)

                // userList != adapter.differ.currentList
                // adapter.differ.currentList는 계속 값을 변경했지만 userList는 변경 전 값(왜냐면 우리는 변경한적이 없다.)
                Log.d(KeyValue.LOG_TAG, "userList: $schoolWorkList")
            }
        })
        itemTouchHelper.attachToRecyclerView(schoolWorkRecyclerView)
    }

    companion object {
        const val INTENT_EXTRA_ITEM = "itemActivity"
    }
}
