package com.example.schoolmanager.view.intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.schoolmanager.util.KeyValue
import com.example.schoolmanager.databinding.ActivitySignUpBinding
import com.example.schoolmanager.model.network.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    //파이어베이스
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val userDB: DatabaseReference by lazy { Firebase.database.reference.child(KeyValue.DB_USERS) }

    //UI 관련
    private var mBinding: ActivitySignUpBinding? = null
    private val binding get() = mBinding!!
    //---------------------------------------------------------------------------------------생명주기
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //DB 초기화
        clkSignUpBtn()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    //--------------------------------------------------------------------------------------사용자함수
    //회원가입 버튼 클릭
    private fun clkSignUpBtn() {
        binding.signUpBtn.setOnClickListener {
            val email = binding.emailSignUpEdittext.text.toString()
            val password = binding.passwordSignUpEdittext.text.toString()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        auth.signInWithEmailAndPassword(email, password) //자동 로그인
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    saveUserInfo()
                                    finish()
                                }
                            }
                    } else {
                        Toast.makeText(this, "이미 가입한 이메일입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    //회원가입 시 입력한 정보 DB에 저장
    private fun saveUserInfo() {
        val model = Student(
            auth.uid ?: "",
            studentNumber = binding.studentNumberEdittext.text.toString().toInt(),
            studentName = binding.studentNameEdittext.text.toString(),
            studentNickname = binding.studentNicknameEdittext.text.toString(),
            studentLevel = 1,
            studentExp = 0,
            studentDetailInfo = "",
            //회원정보
            studentEmail = "",
            studentPassword = "",
            //능력치
            leadership = 0,
            academicAbility = 0,
            cooperation = 0,
            sincerity = 0,
            career = 0
        )
        userDB.child(auth.uid ?: "").setValue(model)
    }
}
