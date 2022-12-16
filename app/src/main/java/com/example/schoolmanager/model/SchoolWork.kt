package com.example.schoolmanager.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class SchoolWork(
    //기본사항
    @PrimaryKey val schoolWorkTitle: String,
    val schoolWorkSimpleInfo: String,
    val schoolWorkDetailInfo: String,
    val leadership: Int,
    val academicAbility: Int,
    val cooperation: Int,
    val sincerity: Int,
    val career: Int
) : Parcelable {
    constructor() : this("", "", "", 0, 0, 0, 0, 0)
}
