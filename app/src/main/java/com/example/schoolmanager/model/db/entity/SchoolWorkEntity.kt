package com.example.schoolmanager.model.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "school_work_table")
data class SchoolWorkEntity(

    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @ColumnInfo var schoolWorkTitle: String,
    @ColumnInfo var schoolWorkSimpleInfo: String,
    @ColumnInfo var schoolWorkDetailInfo: String,

    @ColumnInfo var leadership: Int,
    @ColumnInfo val academicAbility: Int,
    @ColumnInfo val cooperation: Int,
    @ColumnInfo var sincerity: Int,
    @ColumnInfo var career:  Int
)