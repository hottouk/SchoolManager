package com.example.schoolmanager.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.schoolmanager.App

class MyDataStore {
    private val context = App.context()
    private val mDataStore: DataStore<Preferences> = context.dataStore //확장함수

    private val oldFlag = booleanPreferencesKey("OLD_FLAG")
    private val userKindFlag = stringPreferencesKey("USER_FROM_WHERE")

    //---------------------------------------------------------------------------------------기존유저
    suspend fun setupFirstData() {
        mDataStore.edit { preferences -> //처음 접속하는 유저가 아니라면 True 아니라면 False
            preferences[oldFlag] = true
        }
    }

    suspend fun isCurrentUser(): Boolean {
        var currentValue = false
        mDataStore.edit { preferences -> // 기존 유저라면 True 아니라면 False
            currentValue = preferences[oldFlag] ?: false
        }
        return currentValue
    }

    //-----------------------------------------------------------------------------------유저종류확인

    suspend fun setUpKakaoFlag() {
        mDataStore.edit { preferences -> //카카오 유저 플래그 세우기
            preferences[userKindFlag] = "KAKAO"
        }
    }

    suspend fun setUpNaverFlag() {
        mDataStore.edit { preferences -> //네이버 유저 플래그 세우기
            preferences[userKindFlag] = "NAVER"
        }
    }

    suspend fun checkUserFlag(): String {
        var userFlag = ""
        mDataStore.edit { preferences -> // 기존 유저라면 True 아니라면 False
            userFlag = preferences[userKindFlag] ?: ""
        }
        return userFlag
    }

    companion object { //확장함수
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_name")
    }
}