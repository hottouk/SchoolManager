package com.example.schoolmanager.view.schoolwork

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanager.databinding.ActivityAddSchoolBinding
import com.example.schoolmanager.databinding.ActivitySchoolWorkDetailBinding
import com.example.schoolmanager.model.network.SchoolWork
import com.example.schoolmanager.model.network.Teacher
import com.example.schoolmanager.repository.Repository
import com.example.schoolmanager.util.KeyValue
import kotlinx.coroutines.launch

class SchoolWorkViewModel(user: Teacher) : ViewModel() {

    private val mSchoolWorkList = MutableLiveData<MutableList<SchoolWork>>()
    private val mSubjectList = MutableLiveData<MutableList<String>>()
    private val repo = Repository()

    //사용자 정보
    private var mCurrentUser: Teacher
    val currentUser get() = mCurrentUser //외부 참조용

    init {
        mCurrentUser = user // Main에서 전달된 데이터
    }

    //나의 과목 데이터 받아오기 from 파이어베이스
    fun fetchMySubjectList(teacherId: String): LiveData<MutableList<String>> {
        viewModelScope.launch {
            repo.getSubjectList(teacherId).observeForever {
                mSubjectList.value = it
            }
        }
        return mSubjectList
    }

    //교사의 과목 활동 데이터 받아오기 from 파이어베이스 #네트워크
    fun fetchSchoolWorkList(teacherId: String, subject: String): LiveData<MutableList<SchoolWork>> {
        viewModelScope.launch {
            repo.getSchoolWorkList(teacherId, subject).observeForever {
                mSchoolWorkList.value = it
            }
        }
        return mSchoolWorkList
    }

    //----------------------------------------------------------------------------------활동정보 CRUD
    //활동 정보 파이어베스 실시간 DB 저장하기
    fun postSaveSchoolWork(binding: ActivityAddSchoolBinding) = viewModelScope.launch {
        val model = SchoolWork(
            binding.subjectSpinner.selectedItem.toString(),
            binding.schoolWorkTitleEdittext.text.toString(),
            binding.schoolWorkSimpleInfoEdittext.text.toString(),
            binding.schoolWorkDetailEdittext.text.toString(),
            binding.leadershipNumberpicker.value,
            binding.academicNumberpicker.value,
            binding.academicNumberpicker.value,
            binding.sincerityNumberpicker.value,
            binding.careerNumberpicker.value,
            "",
            binding.expectedDifficultyRating.rating.toInt(),
            binding.moneyNumberpicker.value,
            mCurrentUser.userId
        )
        repo.schoolWorkDB.child(mCurrentUser.userId).child(model.subject)
            .child(model.schoolWorkTitle)
            .setValue(model) //전체활동란에 저장
        repo.teacherDB.child(mCurrentUser.userId).child(KeyValue.DB_SCHOOL_ACTIVITIES)
            .child(model.subject).child(model.schoolWorkTitle).setValue(model) //교사 아래 활동란에 저장
    }

    //활동 정보 파이어베스 실시간 DB 수정하기
    fun postEditSchoolWorkData(binding: ActivitySchoolWorkDetailBinding) = viewModelScope.launch {
        val model = SchoolWork(
            binding.subjectTextview.text.toString(),
            binding.schoolWorkTitleEditTextview.text.toString(),
            binding.schoolWorkSimpleInfoEdittext.text.toString(),
            binding.schoolWorkDetailEditEdittext.text.toString(),
            binding.leadershipNumberpicker.value,
            binding.academicNumberpicker.value,
            binding.academicNumberpicker.value,
            binding.sincerityNumberpicker.value,
            binding.careerNumberpicker.value,
            "",
            binding.expectedDifficultyRating.rating.toInt(),
            binding.moneyNumberpicker.value,
            mCurrentUser.userId
        )
        repo.schoolWorkDB.child(mCurrentUser.userId).child(model.subject)
            .child(model.schoolWorkTitle)
            .setValue(model) //전체활동란에 저장
        repo.teacherDB.child(mCurrentUser.userId).child(KeyValue.DB_SCHOOL_ACTIVITIES)
            .child(model.subject).child(model.schoolWorkTitle).setValue(model) //교사 아래 활동란에 저장
    }

    //활동 정보 삭제하기
    fun postDeleteSchoolWorkData(schoolWork: SchoolWork) {
        repo.schoolWorkDB.child(mCurrentUser.userId).child(schoolWork.subject)
            .child(schoolWork.schoolWorkTitle).removeValue() //현 주소의 아이템 삭제
        repo.teacherDB.child(mCurrentUser.userId).child(KeyValue.DB_SCHOOL_ACTIVITIES)
            .child(schoolWork.subject).child(schoolWork.schoolWorkTitle)
            .removeValue() //현 주소의 아이템 삭제
    }
}