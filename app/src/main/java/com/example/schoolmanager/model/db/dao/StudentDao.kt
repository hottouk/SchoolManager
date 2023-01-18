package com.example.schoolmanager.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.schoolmanager.model.db.entity.StudentEntity
import com.example.schoolmanager.model.network.Student

@Dao
interface StudentDao {

    @Insert
    fun insertStudent(student: StudentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg student: StudentEntity)

    @Query("SELECT * From student_table")
    fun getAll(): List<StudentEntity>

    @Query("SELECT * From student_table WHERE studentNumber =:studentNumber ")
    fun getOneStudent(studentNumber: Int): StudentEntity
}