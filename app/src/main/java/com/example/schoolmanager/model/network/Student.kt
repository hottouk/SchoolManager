package com.example.schoolmanager.model.network

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Student(
    @PrimaryKey val studentUid: String,
    @ColumnInfo val studentNumber: Int,
    @ColumnInfo val studentName: String,
    @ColumnInfo val studentNickname: String,
    @ColumnInfo val studentLevel: Int,
    @ColumnInfo var studentExp: Long,
    @ColumnInfo var studentDetailInfo: String,
    //회원정보
    @ColumnInfo val studentEmail: String,
    @ColumnInfo val studentPassword: String,
    //능력치
    @ColumnInfo var leadership: Int,
    @ColumnInfo var academicAbility: Int,
    @ColumnInfo var cooperation: Int,
    @ColumnInfo var sincerity: Int,
    @ColumnInfo var career: Int
) : Parcelable {
    constructor() : this(
        "", 0, "", "",
        1, 0, "",
        "", "",
        0, 0, 0, 0, 0
    )
    fun getExp() : Long {
        return (leadership + academicAbility + cooperation + sincerity + career).toLong()
    }
}