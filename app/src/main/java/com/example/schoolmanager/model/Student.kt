package com.example.schoolmanager.model

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
    @ColumnInfo val studentDetailInfo: String,
    @ColumnInfo val studentEmail: String,
    @ColumnInfo val studentPassword: String
) : Parcelable {
    constructor() : this("", 0, "", "", 1, "", "", "")
}