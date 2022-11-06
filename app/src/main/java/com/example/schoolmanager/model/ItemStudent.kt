package com.example.schoolmanager.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class ItemStudent(
    @PrimaryKey val studentNumber : Int,
    @ColumnInfo val studentName: String,
    @ColumnInfo val studentLevel : Int,
    @ColumnInfo val studentDetailInfo : String
):Parcelable