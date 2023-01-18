package com.example.schoolmanager.model.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SchoolWork(
    //기본사항
    val schoolWorkTitle: String,
    val schoolWorkSimpleInfo: String,
    val schoolWorkDetailInfo: String,
    //계발능력
    val leadership: Int,
    val academicAbility: Int,
    val cooperation: Int,
    val sincerity: Int,
    val career: Int
) : Parcelable {

    constructor() : this("", "", "", 0, 0, 0, 0, 0)
    fun getTotalScore() : Long {
        return (leadership + academicAbility + cooperation + sincerity + career).toLong()
    }
}
