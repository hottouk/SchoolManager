package com.example.schoolmanager.view.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.schoolmanager.databinding.ActivityLogInBinding
import com.example.schoolmanager.view.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class LogInActivity : AppCompatActivity() {

    //파이어베이스
    val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private var mBinding: ActivityLogInBinding? = null
    private val binding get() = mBinding!!

    val kakaoLogIncallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("카카오", "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i("카카오", "카카오계정으로 로그인 성공 ${token.accessToken}")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    //---------------------------------------------------------------------------------------생명주기
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        checkKaKaoLogin()
//        clkMoveSingUpPageBtn()
//        clkLogInBtn()
        binding.kakaoLoginBtn.setOnClickListener {
            kakaoLogin()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    //--------------------------------------------------------------------------------------사용자함수
    //회원가입 버튼 클릭
    private fun clkMoveSingUpPageBtn() {
        binding.moveSignUpPageBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    //로그인 버튼 클릭
    private fun clkLogInBtn() {
        binding.loginBtn.setOnClickListener {
            val email = binding.emailEdittext.text.toString()
            val password = binding.passwordEdittext.text.toString()
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

    //카카오 로그인
    fun kakaoLogin() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e("카카오", "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(
                        this,
                        callback = kakaoLogIncallback
                    )
                } else if (token != null) {
                    Log.i("카카오", "카카오톡으로 로그인 성공 ${token.accessToken}")
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoLogIncallback)
        }
    }

    //카카오 로그인 체크
    fun checkKaKaoLogin() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) { //토큰 정보 보기 실패
                Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
            } else if (tokenInfo != null) {
                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }


}