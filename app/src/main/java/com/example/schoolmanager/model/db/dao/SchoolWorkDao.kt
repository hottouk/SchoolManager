package com.example.schoolmanager.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.schoolmanager.model.db.entity.SchoolWorkEntity
import com.example.schoolmanager.model.db.entity.StudentEntity
import com.example.schoolmanager.model.network.Student

@Dao
interface SchoolWorkDao {

    @Insert
    fun insertSchoolWork(schoolWork: SchoolWorkEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg schoolWork: SchoolWorkEntity)

    @Query("SELECT * From school_work_table")
    fun getAll(): MutableList<SchoolWorkEntity>

}