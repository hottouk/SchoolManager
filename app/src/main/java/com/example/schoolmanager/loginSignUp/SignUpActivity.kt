package com.example.schoolmanager.loginSignUp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.schoolmanager.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    //파이어베이스
    val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }
    val userDB: DatabaseReference by lazy { Firebase.database.reference.child("Users") }

    //UI 관련
    val emailSignUpEditText: EditText by lazy { findViewById(R.id.email_sign_up_edittext) }
    val passwordSignUpEditText: EditText by lazy { findViewById(R.id.password_sign_up_edittext) }
    val studentNumberEditText: EditText by lazy { findViewById(R.id.student_number_edittext) }
    val studentNameEditText: EditText by lazy { findViewById(R.id.student_name_edittext) }
    val studentNicknameEditText: EditText by lazy { findViewById(R.id.student_nickname_edittext) }
    val signUpBtn: Button by lazy { findViewById(R.id.sign_up_btn) }
    //---------------------------------------------------------------------------------------생명주기
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        clkSignUpBtn()
    }
    //--------------------------------------------------------------------------------------사용자함수
    private fun clkSignUpBtn() {
        signUpBtn.setOnClickListener {
            val email = emailSignUpEditText.text.toString()
            val password = passwordSignUpEditText.text.toString()
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        //todo 회원가입 성공 시 유저정보 DB에 저장

                    } else {
                        Toast.makeText(this, "이미 가입한 이메일입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}