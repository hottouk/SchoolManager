package com.example.schoolmanager.model.network

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Student(
    @PrimaryKey val userId: String,
    val userName: String? = "",
    val userEmail: String? = "",
    val userProfileImageUrl: String? = "",
    val userClass: String = "",
    //회원정보
    var studentNumber: String = "",
    var studentNickname: String = "",
    var studentLevel: Int = 1,
    var studentDetailInfo: String = "",
    var studentSimpleInfo: String = "",
    var studentExp: Long = 0,
    //능력치
    var leadership: Int = 0,
    var academicAbility: Int = 0,
    var cooperation: Int = 0,
    var sincerity: Int = 0,
    var career: Int = 0,
    //재화
    var money : Int = 0
) : Parcelable {
    constructor() : this(
        "", "", "", "", "",
        "", "", 1, "", "",
        0, 0, 0, 0, 0, 0
    )

    fun getLevel(): Int {
        val currentExp = this.studentExp
        var currentLevel = 1
        when (currentExp) {
            in 0 until 25 -> {
                currentLevel = 1
            }
            in 25 until 80 -> {
                currentLevel = 2
            }
            in 50 until 200 -> {
                currentLevel = 3
            }
            in 200 until 500 -> {
                currentLevel = 4
            }
            else -> {
                currentLevel = 5
            }
        }
        return currentLevel
    }
}