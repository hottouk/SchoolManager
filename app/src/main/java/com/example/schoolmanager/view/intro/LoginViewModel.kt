package com.example.schoolmanager.view.intro

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanager.databinding.ActivityLogInBinding
import com.example.schoolmanager.databinding.FragmentSignUpBinding
import com.example.schoolmanager.model.kakao.KakaoUserInfo
import com.example.schoolmanager.model.naver.NaverUserInfo
import com.example.schoolmanager.model.network.Teacher
import com.example.schoolmanager.repository.MyDataStore
import com.example.schoolmanager.repository.Repository
import com.example.schoolmanager.util.KeyValue
import com.google.gson.Gson
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    //저장소
    private val repo = Repository()
    private val myDataStore = MyDataStore()

    //기존 유저 관련 변수
    private val mIsCurrentUser = MutableLiveData<Boolean>()
    val isCurrentUser: LiveData<Boolean> get() = mIsCurrentUser

    private val mUserFlag = MutableLiveData<String>()
    val userFlag: LiveData<String> get() = mUserFlag

    //카카오 유저
    var savedKakaoUserInfo: KakaoUserInfo? = null
    private lateinit var mSavedTeacherUser: Teacher

    //케릭터 정하기
    var characterImage: String? = null

    //로그인 정보 pref에 저장하기
    fun saveUserPref() = viewModelScope.launch {
        repo.prefEditor
        mSavedTeacherUser?.let {
            val json: String = Gson().toJson(it)
            repo.prefEditor.putString(KeyValue.SHARED_PREF_USER_INFO, json)
                .commit()
        }
    }

    //pref에 저장된 로그인 정보 불러오기
    fun getUserInfoFromPref(binding: ActivityLogInBinding): Teacher? {
        var currentUser: Teacher? = null
        viewModelScope.launch {
            try {
                val jsonString = repo.pref.getString(KeyValue.SHARED_PREF_USER_INFO, "정보 없음")
                currentUser = Gson().fromJson(jsonString, Teacher::class.java)
            } catch (e: Exception) {
                Toast.makeText(binding.root.context, "등록 사용자가 아닙니다.", Toast.LENGTH_SHORT).show()
                binding.signUpFragmentContainer.visibility = View.VISIBLE
            }
        }
        return currentUser
    }

    //뷰모델에 교사 유저로 저장
    fun kakaoUserToTeacherUser() = viewModelScope.launch {
        val teacher = savedKakaoUserInfo?.let { kakaoUser ->
            Teacher(
                userId = "kakao${kakaoUser.userId}T",
                userEmail = kakaoUser.userEmail,
                userName = kakaoUser.userName,
                userNickName = kakaoUser.userNickName,
                userProfileImageUrl = kakaoUser.userProfileImageUrl
            )
        }
        teacher?.let { mSavedTeacherUser = it }
    }

    //신규 유저 정보 받아서 추가하기
    fun addInfoToTeacherUser(binding: FragmentSignUpBinding) = viewModelScope.launch {
        mSavedTeacherUser.school = binding.teacherSchoolEdittext.text.toString()
        mSavedTeacherUser.userName = binding.teacherNameEdittext.text.toString()
        mSavedTeacherUser.subject = binding.teacherSubjectEdittext.text.toString()
        mSavedTeacherUser.teacherCharacterImg = characterImage ?: "남자"
    }

    //신규 유저 파이어베이스 DB에 저장하기
    fun userToFirebase() = viewModelScope.launch {
        repo.teacherDB.child("${mSavedTeacherUser.userId}").setValue(mSavedTeacherUser)
    }

    fun naverUserToTeacherUser(naverUser: NaverUserInfo): Teacher {
        return Teacher(
            userId = "naver${naverUser.id}T",
            userProfileImageUrl = naverUser.profileImage,
            userNickName = naverUser.name,
            userEmail = naverUser.email
        )
    }

    fun setUpFirstFlag() = viewModelScope.launch {
        myDataStore.setupFirstData()
    }

    fun checkCurrentMember() = viewModelScope.launch {
        val jsonString = repo.pref.getString(KeyValue.SHARED_PREF_USER_INFO, "정보없음")
        mIsCurrentUser.value = jsonString != "정보없음"
    }

    fun setUpNaverFlag() = viewModelScope.launch {
        myDataStore.setUpNaverFlag()
    }

    fun setUpKakaoFlag() = viewModelScope.launch {
        myDataStore.setUpKakaoFlag()
    }

    //교사 정보 내보내기
    fun fetchTeacherInfo() = mSavedTeacherUser

}