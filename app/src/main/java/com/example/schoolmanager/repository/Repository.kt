package com.example.schoolmanager.repository

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.example.schoolmanager.App
import com.example.schoolmanager.model.network.*
import com.example.schoolmanager.util.KeyValue
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Repository {

    //로컬 Pref
    val pref: SharedPreferences =
        App.context().getSharedPreferences(KeyValue.SHARED_PREFERENCES, MODE_PRIVATE)
    val prefEditor: SharedPreferences.Editor = pref.edit()

    //원격
    private val database = Firebase.database
    val schoolWorkDB = database.reference.child(KeyValue.DB_SCHOOL_ACTIVITIES)
    val classDB = database.reference.child(KeyValue.DB_SCHOOL_CLASSES)
    val studentDB = database.reference.child(KeyValue.DB_STUDENTS)
    val teacherDB = database.reference.child(KeyValue.DB_TEACHER_USERS)

    //전체 교사 데이터 받아오기 from 파이어베이스
    fun getEntireTeacherList(): MutableLiveData<MutableList<Teacher>> {
        val mutableTeacherList = MutableLiveData<MutableList<Teacher>>()
        teacherDB.addValueEventListener(object : ValueEventListener {
            val listData: MutableList<Teacher> = mutableListOf()
            override fun onDataChange(snapshot: DataSnapshot) {
                listData.clear()
                if (snapshot.exists()) {
                    for (teacher in snapshot.children) {
                        val data = teacher.getValue(Teacher::class.java)
                        data?.let { listData.add(it) }
                    }
                    mutableTeacherList.value = listData
                } else {
                    mutableTeacherList.value = listData
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        return mutableTeacherList
    }

    //교사 과목 데이터 받아오기 from 파이어베이스
    fun getSubjectList(teacherId: String): MutableLiveData<MutableList<String>> {
        val mutableSchoolWorkData = MutableLiveData<MutableList<String>>()
        schoolWorkDB.child(teacherId).addValueEventListener(object : ValueEventListener {
                val listData: MutableList<String> = mutableListOf()

                override fun onDataChange(snapshot: DataSnapshot) {
                    listData.clear() //실시간 데이터 업데이트 시 리사이클러뷰 데이터 중복 방지
                    if (snapshot.exists()) {
                        for (subject in snapshot.children) {
                            val data = subject.key
                            data?.let { listData.add(it) }
                        }
                        mutableSchoolWorkData.value = listData
                    } else {
                        mutableSchoolWorkData.value = listData
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        return mutableSchoolWorkData
    }

    //교사의 과목 활동 데이터 받아오기 from 파이어베이스
    fun getSchoolWorkList(
        teacherId: String,
        subject: String
    ): MutableLiveData<MutableList<SchoolWork>> {
        val mutableSchoolWorkData = MutableLiveData<MutableList<SchoolWork>>()
        schoolWorkDB.child(teacherId).child(subject)
            .addValueEventListener(object : ValueEventListener {
                val listData: MutableList<SchoolWork> = mutableListOf()

                override fun onDataChange(snapshot: DataSnapshot) {
                    listData.clear() //실시간 데이터 업데이트 시 리사이클러뷰 데이터 중복 방지
                    if (snapshot.exists()) {
                        for (schoolWork in snapshot.children) {
                            val data = schoolWork.getValue(SchoolWork::class.java)
                            data?.let { listData.add(it) }
                        }
                        mutableSchoolWorkData.value = listData
                    } else {
                        mutableSchoolWorkData.value = listData
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        return mutableSchoolWorkData
    }


    //나의 클래스에 소속되어있는 학생 펫 데이터 불러오기 from FireBase
    fun getMyStudentPetInClassList(
        teacherId: String,
        classId: String
    ): MutableLiveData<MutableList<Pet>> {
        val mutableStudentPetData = MutableLiveData<MutableList<Pet>>()
        val studentDB = classDB.child(teacherId).child(classId).child(KeyValue.DB_STUDENTS)
        studentDB.addValueEventListener(object : ValueEventListener {

            val listData: MutableList<Pet> = mutableListOf()
            override fun onDataChange(snapshot: DataSnapshot) {
                listData.clear()
                if (snapshot.exists()) {
                    for (pet in snapshot.children) {
                        val data = pet.getValue(Pet::class.java)
                        data?.let { listData.add(it) }
                    }
                    mutableStudentPetData.value = listData
                } else {
                    mutableStudentPetData.value = listData
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        return mutableStudentPetData
    }

    //반 데이터 받아오기 from FireBase
    fun getClassList(user: String): MutableLiveData<MutableList<SchoolClass>> {
        val mutableClassData = MutableLiveData<MutableList<SchoolClass>>()
        val classDB = classDB.child(user)
        classDB.addValueEventListener(object : ValueEventListener {
            val listData: MutableList<SchoolClass> = mutableListOf()

            override fun onDataChange(snapshot: DataSnapshot) {
                listData.clear()
                if (snapshot.exists()) {
                    for (schoolClass in snapshot.children) {
                        val data = schoolClass.getValue(SchoolClass::class.java)
                        data?.let { listData.add(it) }
                    }
                    mutableClassData.value = listData
                } else {
                    mutableClassData.value = listData
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        return mutableClassData
    }
}