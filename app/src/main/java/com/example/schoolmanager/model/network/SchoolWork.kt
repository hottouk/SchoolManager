package com.example.schoolmanager.model.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SchoolWork(
    //기본사항(필수)
    val subject: String,
    val schoolWorkTitle: String,
    //기본사항(옵션)
    val schoolWorkSimpleInfo: String?,
    val schoolWorkDetailInfo: String?,

    //계발능력
    val leadership: Int,
    val academicAbility: Int,
    val cooperation: Int,
    val sincerity: Int,
    val career: Int,

    //동기부여요소
    val monsterImage: String?,
    val difficulty: Int?,
    val money: Int = 0,

    //만든 사람
    val madeBy: String
) : Parcelable {
    constructor() : this(
        "", "", "", "",
        0, 0, 0, 0, 0, "",
        0, 0, ""
    )

    fun getTotalScore(): Long {
        return (leadership + academicAbility + cooperation + sincerity + career).toLong()
    }
}
