package com.example.schoolmanager.view.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanager.model.network.SchoolWork
import com.example.schoolmanager.model.network.Teacher
import com.example.schoolmanager.repository.Repository
import kotlinx.coroutines.launch

class TeacherViewModel : ViewModel() {


    private val mEntireTeacherList = MutableLiveData<MutableList<Teacher>>()

    //선택된 타교사
    private val mSelectedTeacher = MutableLiveData<Teacher>()
    val selectedTeacher: LiveData<Teacher> get() = mSelectedTeacher

    //선택된 타교사 과목
    private val mSelectedSubject = MutableLiveData<String>()
    val selectedSubject: LiveData<String> get() = mSelectedSubject

    //선택된 타교사 활동
    private val mSelectedSchoolWork = MutableLiveData<SchoolWork>()
    val selectedSchoolWork: LiveData<SchoolWork> get() = mSelectedSchoolWork

    //타교사 과목
    private val mSubjectList = MutableLiveData<MutableList<String>>()

    //타교사 활동
    private val mSchoolWorkList = MutableLiveData<MutableList<SchoolWork>>()
    private val repo = Repository()

    fun fetchTeacherList(): LiveData<MutableList<Teacher>> {
        viewModelScope.launch {
            repo.getEntireTeacherList().observeForever {
                mEntireTeacherList.value = it
            }
        }
        return mEntireTeacherList
    }

    //타 교사의의 과목 데이터 받아오기 from 파이어베이스
    fun fetchOtherSubjectList(teacherId: String): LiveData<MutableList<String>> {
        viewModelScope.launch {
            repo.getSubjectList(teacherId).observeForever {
                mSubjectList.value = it
            }
        }
        return mSubjectList
    }

    //타 교사의의 활동 데이터 받아오기 from 파이어베이스
    fun fetchOtherSchoolWorkList(
        teacherId: String,
        subject: String
    ): LiveData<MutableList<SchoolWork>> {
        viewModelScope.launch {
            repo.getSchoolWorkList(teacherId, subject).observeForever {
                mSchoolWorkList.value = it
            }
        }
        return mSchoolWorkList
    }

    //교사 선택
    fun selectTeacher(teacher: Teacher) = viewModelScope.launch {
        mSelectedTeacher.value = teacher
    }

    //과목 선택
    fun selectSubject(subject: String) = viewModelScope.launch {
        mSelectedSubject.value = subject
    }

    //활동 선택
    fun selectSchoolWork(schoolWork: SchoolWork) = viewModelScope.launch {
        mSelectedSchoolWork.value = schoolWork
    }
}