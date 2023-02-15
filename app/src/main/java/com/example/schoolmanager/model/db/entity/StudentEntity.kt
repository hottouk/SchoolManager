package com.example.schoolmanager.model.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_table")
data class StudentEntity(
    //회원 정보
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo val id : Int,
    @ColumnInfo val studentUid : String,
    @ColumnInfo val studentNumber: Int,
    @ColumnInfo val studentName: String,
    @ColumnInfo val studentNickname: String,
    @ColumnInfo val studentEmail: String,
    @ColumnInfo val studentPassword: String,
    //능력치
    @ColumnInfo var studentDetailInfo: String,
    @ColumnInfo var studentLevel: Int,
    @ColumnInfo var studentExp: Long,
    @ColumnInfo var leadership: Int,
    @ColumnInfo var academicAbility: Int,
    @ColumnInfo var cooperation: Int,
    @ColumnInfo var sincerity: Int,
    @ColumnInfo var career: Int
)
