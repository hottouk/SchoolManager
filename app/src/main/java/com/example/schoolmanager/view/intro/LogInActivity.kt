package com.example.schoolmanager.view.intro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.ActivityLogInBinding
import com.example.schoolmanager.model.kakao.KakaoUserInfo
import com.example.schoolmanager.model.naver.NaverUserInfo
import com.example.schoolmanager.model.network.Teacher
import com.example.schoolmanager.util.KeyValue
import com.example.schoolmanager.view.main.MainActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.NidOAuthLoginState
import com.navercorp.nid.oauth.NidOAuthPreferencesManager.errorDescription
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class LogInActivity : AppCompatActivity() {

    val viewModel: LoginViewModel by viewModels()

    private var mBinding: ActivityLogInBinding? = null
    private val binding get() = mBinding!!

    lateinit var naverIdLoginSDK: NaverIdLoginSDK

    //---------------------------------------------------------------------------------------생명주기
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        naverInitialize()
        binding.kakaoLoginBtn.setOnClickListener {
            checkKaKaoLogin()
        }
        binding.naverLoginBtn.setOnClickListener {
            naverInitLogin()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    //-------------------------------------------------------------------------------------사용자함수
    //네이버 로그인 초기화
    fun naverInitialize() {
        naverIdLoginSDK = NaverIdLoginSDK
        naverIdLoginSDK.initialize(
            this,
            getString(R.string.naver_client_id),
            getString(R.string.naver_client_secret),
            getString(R.string.naver_client_name)
        )
    }

    //-------------------------------------------------------------------------------------로그인체크

    //카카오 로그인 체크
    fun checkKaKaoLogin() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error -> //쓰레드
            if (error != null) { //토큰 정보 보기 실패
                Log.d("로그인", "저장된 카카오 로그인 정보가 없습니다.")
                kakaoInitLogin()
            } else if (tokenInfo != null) { //카카오 토큰 정보가 있는 경우
                fetchKakaoUserInfo()
                Toast.makeText(this, "저장된 카카오톡으로 로그인합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //네이버 토큰 확인
    private fun checkNaverLogin(): Boolean {
        return !(NidOAuthLoginState.NEED_LOGIN == naverIdLoginSDK.getState() ||
                NidOAuthLoginState.NEED_INIT == naverIdLoginSDK.getState())
    }

    //-------------------------------------------------------------------------------------초기로그인
    //카카오 초기 로그인
    fun kakaoInitLogin() { //계정 로그인 콜백
        val kakaoLogincallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) { //계정으로 로그인한 경우
                Log.e("로그인", "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                fetchKakaoUserInfo()
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e("로그인", "카카오톡으로 로그인 실패", error)
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
                } else if (token != null) { //카카오톡 어플로 로그인 성공한 경우
                    fetchKakaoUserInfo()
                }
            }
        } else { //카카오톡이 없어 계정으로 로그인 시도
            UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoLogincallback)
        }
    }

    //네이버 초기 로그인
    private fun naverInitLogin() {
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {// 네이버 로그인 인증 성공
                Log.e("로그인", "네이버 로그인 성공")
                fetchNaverUserInfo()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.e("로그인", "네이버 로그인 실패; 에러코드:$errorDescription")
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }
        NaverIdLoginSDK.authenticate(this@LogInActivity, oauthLoginCallback)
    }

    //-------------------------------------------------------------------------------------사용자정보
    //인증 성공하여 카카오 사용자 정보 불러오기
    fun fetchKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Toast.makeText(this, "카카오 사용자 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            } else if (user != null) { // 사용자정보 요청 성공
                val kakaoUserInfo = KakaoUserInfo(
                    userId = user.id.toString(),
                    userEmail = user.kakaoAccount?.email,
                    userNickName = user.kakaoAccount?.profile?.nickname,
                    userProfileImageUrl = user.kakaoAccount?.profile?.thumbnailImageUrl
                )
                val teacherUser = viewModel.kakaoUserToTeacherUser(kakaoUserInfo)
                viewModel.checkCurrentMember()
                viewModel.isCurrentUser.observe(this@LogInActivity) {
                    if (it) { //기존유저
                        Log.d("로그인", "카카오 기존")
                        viewModel.setUpKakaoFlag() //카카오 아이디로 접속
                        moveToMainPage(teacherUser)
                    } else { //신규유저
                        Log.d("로그인", "카카오 신규")
                        viewModel.userToFirebase(teacherUser)
                        viewModel.setUpFirstFlag() //처음 접속
                        viewModel.setUpKakaoFlag() //카카오 아이디로 접속
                        moveToMainPage(teacherUser)
                    }
                }
            }
        }
    }

    //인증 성공하여 네이버 사용자 정보 불러오기
    fun fetchNaverUserInfo() {
        val nidProfileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onError(errorCode: Int, message: String) {
                Log.e("로그인", "네이버 프로필 정보 가져오기 실패 : $errorCode: $message")
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Log.e("로그인", "네이버 프로필 정보 가져오기 실패 : $httpStatus, $message")
            }

            override fun onSuccess(result: NidProfileResponse) {
                val naverUserInfo = NaverUserInfo(
                    id = result.profile?.id.toString(),
                    name = result.profile?.name.toString(),
                    email = result.profile?.email.toString(),
                    gender = result.profile?.gender.toString(),
                    profileImage = result.profile?.profileImage.toString(),
                    age = result.profile?.age.toString(),
                    tel = result.profile?.mobile.toString()
                )
                val teacherUser = viewModel.naverUserToTeacherUser(naverUserInfo)
                viewModel.checkCurrentMember()
                viewModel.isCurrentUser.observe(this@LogInActivity) {
                    if (it) { //기존유저
                        Log.d("로그인", "네이버 기존")
                        viewModel.setUpNaverFlag() //네이버 아이디로 접속
                        moveToMainPage(teacherUser)
                    } else { //신규유저
                        Log.d("로그인", "네이버 신규")
                        viewModel.userToFirebase(teacherUser)
                        viewModel.setUpFirstFlag() //처음 접속
                        viewModel.setUpNaverFlag() //네이버 아이디로 접속
                        moveToMainPage(teacherUser)
                    }
                }
            }
        }
        NidOAuthLogin().callProfileApi(nidProfileCallback)
    }

    //페이지 이동
    fun moveToMainPage(teacherUser: Teacher) {
        val intent = Intent(this@LogInActivity, MainActivity::class.java)
        intent.putExtra(KeyValue.INTENT_EXTRA_USER_INFO, teacherUser)
        startActivity(intent)
    }
}




