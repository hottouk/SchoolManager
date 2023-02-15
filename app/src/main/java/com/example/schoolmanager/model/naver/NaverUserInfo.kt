package com.example.schoolmanager.model.naver

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NaverUserInfo(
    val id: String,
    val name: String?,
    val email: String?,
    val gender: String?,
    val profileImage : String?,
    val age: String?,
    val tel: String?
) : Parcelable
