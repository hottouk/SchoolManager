package com.example.schoolmanager.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanager.model.network.Pet
import com.example.schoolmanager.model.network.SchoolClass
import com.example.schoolmanager.model.network.SchoolWork
import com.example.schoolmanager.model.network.Teacher
import com.example.schoolmanager.repository.MyDataStore
import com.example.schoolmanager.repository.Repository
import com.example.schoolmanager.util.KeyValue
import kotlinx.coroutines.launch

class MainViewModel(user: Teacher) : ViewModel() {

    private val repo = Repository()
    private val myDataStore = MyDataStore()
    private var classList = MutableLiveData<MutableList<SchoolClass>>()

    private var mCurrentUser: Teacher
    val currentUser get() = mCurrentUser //외부 참조용

    private var mUserFlag = MutableLiveData<String>()
    val userFlag: LiveData<String> get() = mUserFlag

    //클래스 개설 모드
    var classCreateMode = MutableLiveData(false)

    init {
        mCurrentUser = user //MainActivity 에서 전달된 데이터
    }

    fun getUser() = currentUser

    //전체 클래스 불러오기
    fun fetchClassesList(): LiveData<MutableList<SchoolClass>> {
        viewModelScope.launch {
            repo.getClassList(mCurrentUser.userId).observeForever {
                classList.value = it
            }
        }
        return classList
    }

    //클래스 만들기
    fun postCreateClass(className: String, subject: String) = viewModelScope.launch {
        val schoolClassTitle = "$subject-$className"
        val schoolClass = SchoolClass(
            "", schoolClassTitle, subject, ""
        )
        repo.classDB.child(mCurrentUser.userId).child(schoolClassTitle).setValue(schoolClass)
    }

    //클래스 삭제하기
    fun postDeleteClass(selectedClasses: MutableList<SchoolClass>) = viewModelScope.launch {
        selectedClasses.forEach {
            repo.classDB.child(mCurrentUser.userId)
                .child(it.className).removeValue()
        }
    }

    //학생 한명 펫 데이터 업데이트
    fun postOneStudentPetData(classId: String, pet: Pet) =
        viewModelScope.launch {
            //교실 펫 정보 업데이트
            repo.classDB.child(getUser().userId)
                .child(classId)
                .child(KeyValue.DB_STUDENTS)
                .child(pet.belongToUser).setValue(pet)
            //학생 펫 정보 업데이트
            repo.studentDB.child(pet.belongToUser).child(KeyValue.DB_PETS).child(pet.petId)
                .setValue(pet)
        }

    //교사 활동 복붙하기
    fun postBringSchoolWork(schoolWork: SchoolWork, teacher: Teacher) {
        repo.schoolWorkDB.child(mCurrentUser.userId).child(schoolWork.subject)
            .child("${schoolWork.schoolWorkTitle} from ${teacher.userNickName}T")
            .setValue(schoolWork) //활동 DB 저장
    }

    //로그아웃용 유저 Flag 체크
    fun checkUserFlag() = viewModelScope.launch {
        val getData = myDataStore.checkUserFlag()
        mUserFlag.value = getData
    }
}