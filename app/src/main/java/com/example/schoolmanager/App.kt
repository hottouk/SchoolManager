package com.example.schoolmanager

import android.app.Application
import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK

class App : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.kakao_native_app_key))
    }

    companion object {
        private var instance: App? = null
        fun context(): Context {
            return instance!!.applicationContext
        }
    }
}