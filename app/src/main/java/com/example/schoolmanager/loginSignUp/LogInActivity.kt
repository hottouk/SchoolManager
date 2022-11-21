package com.example.schoolmanager.loginSignUp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.schoolmanager.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogInActivity : AppCompatActivity() {

    //파이어베이스
    val firebaseAuth : FirebaseAuth by lazy{
        Firebase.auth
    }

    //UI관련
    val loginBtn : Button by lazy { findViewById(R.id.login_btn) }
    val moveSingUpPageBtn : Button by lazy { findViewById(R.id.move_sign_up_page_btn) }
    val emailEditTextView : EditText by lazy { findViewById(R.id.email_edittext) }
    val passwordEditTextView : EditText by lazy { findViewById(R.id.password_edittext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

    }
}