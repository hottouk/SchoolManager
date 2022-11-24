package com.example.schoolmanager.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class SchoolActivity(
    //기본사항
    @PrimaryKey val uid: Int,
    val activityTitle: String,
    val activitySimpleInfo: String,
    val activityDetailInfo: String,
    //점수
    val leadership: Int,
    val academicAbility: Int,
    val cooperation: Int,
    val sincerity: Int,
    val career: Int
) : Parcelable{
    constructor():this(1,"","","",0,0,0,0,0)
}
