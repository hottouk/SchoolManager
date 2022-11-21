package com.example.schoolmanager.loginSignUp

import android.content.Intent
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
import com.google.firebase.ktx.Firebase

class LogInActivity : AppCompatActivity() {

    //파이어베이스
    val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    var userDB: DatabaseReference? = null

    //UI관련
    private val loginBtn: Button by lazy { findViewById(R.id.login_btn) }
    private val moveSingUpPageBtn: Button by lazy { findViewById(R.id.move_sign_up_page_btn) }
    private val emailEditTextView: EditText by lazy { findViewById(R.id.email_edittext) }
    private val passwordEditTextView: EditText by lazy { findViewById(R.id.password_edittext) }

    //---------------------------------------------------------------------------------------생명주기
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        clkMoveSingUpPageBtn()
        clkLogInBtn()
    }

    //--------------------------------------------------------------------------------------사용자함수
    //회원가입 버튼 클릭
    private fun clkMoveSingUpPageBtn() {
        moveSingUpPageBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
    //로그인 버튼 클릭
    private fun clkLogInBtn() {
        loginBtn.setOnClickListener {
            val email = emailEditTextView.text.toString()
            val password = passwordEditTextView.text.toString()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "로그인 실패, 아이디, 비밀번호 확인", Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }
}