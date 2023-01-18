package com.example.schoolmanager.view.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.ActivityLogInBinding
import com.example.schoolmanager.model.kakao.KakaoUserInfo
import com.example.schoolmanager.util.KeyValue
import com.example.schoolmanager.view.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback

class LogInActivity : AppCompatActivity() {

    val viewModel: LoginViewModel by viewModels()

    private var mBinding: ActivityLogInBinding? = null
    private val binding get() = mBinding!!

    //---------------------------------------------------------------------------------------생명주기
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        naverInitialize()
        checkKaKaoLogin()
        binding.kakaoLoginBtn.setOnClickListener {
            kakaoLogin()
        }
        binding.naverLoginBtn.setOnClickListener {
            naverLogin()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    //--------------------------------------------------------------------------------------사용자함수
    //네이버 로그인 초기화
    fun naverInitialize() {
        NaverIdLoginSDK.initialize(
            this,
            getString(R.string.naver_client_id),
            getString(R.string.naver_client_secret),
            getString(
                R.string.naver_client_name
            )
        )
    }

    //카카오 로그인
    fun kakaoLogin() {
        val kakaoLogincallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) { //계정으로 로그인한 경우
                Log.e("카카오", "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i("카카오", "카카오계정으로 로그인 성공 ${token.accessToken}")
                checkKaKaoLogin()
            }
        } //콜백
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
                        callback = kakaoLogincallback
                    )
                } else if (token != null) { //카카오톡 어플로 로그인 한 경우
                    Log.i("카카오", "카카오톡으로 로그인 성공 ${token.accessToken}")
                    checkKaKaoLogin()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoLogincallback)
        }
    }

    //카카오 로그인 체크
    fun checkKaKaoLogin() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) { //토큰 정보 보기 실패
                Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
            } else if (tokenInfo != null) {
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Toast.makeText(this, "사용자 정보 보기 실패", Toast.LENGTH_SHORT).show()
                    } else if (user != null) {// 사용자정보 요청 성공 -> 정보 전달
                        val kakaoUserInfo = KakaoUserInfo(
                            userId = "kakao${user.id.toString()}",
                            userEmail = user.kakaoAccount?.email,
                            userNickName = user.kakaoAccount?.profile?.nickname,
                            userProfileImageUrl = user.kakaoAccount?.profile?.thumbnailImageUrl
                        )
                        viewModel.checkCurrentMember()
                        viewModel.isCurrentMember.observe(this) {
                            if (it) {
                                Toast.makeText(this, "기존유저 로그인", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                intent.putExtra(KeyValue.INTENT_EXTRA_USER_INFO, kakaoUserInfo)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "신규유저 로그인", Toast.LENGTH_SHORT).show()
                                viewModel.setUpFirstFlag() //신규 -> 기존
                                viewModel.kakaoUserToFirebase(kakaoUserInfo) //유저 정보 DB 저장
                                val intent = Intent(this, MainActivity::class.java)
                                intent.putExtra(KeyValue.INTENT_EXTRA_USER_INFO, kakaoUserInfo)
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
    }

    //네이버 로그인
    private fun naverLogin() {
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                Log.d("testt", "로그인 성공")
                Log.d("testt", "${NaverIdLoginSDK.getAccessToken()}")
                Log.d("testt", "${NaverIdLoginSDK.getRefreshToken()}")
                Log.d("testt", "${NaverIdLoginSDK.getExpiresAt()}")
                Log.d("testt", "${NaverIdLoginSDK.getTokenType()}")
                Log.d("testt", "${NaverIdLoginSDK.getState()}")
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(
                    this@LogInActivity,
                    "errorCode:$errorCode, errorDesc:$errorDescription",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }
        NaverIdLoginSDK.authenticate(this@LogInActivity, oauthLoginCallback)
    }
}