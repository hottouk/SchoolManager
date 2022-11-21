package com.example.schoolmanager.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.schoolmanager.model.Student

@Dao
interface StudentDao {

    @Insert
    fun insertStudent(student: Student)

    @Query("SELECT * From Student")
    fun getAll(): List<Student>

    @Query("SELECT * From Student WHERE studentNumber ==:studentNumber ")
    fun getOneStudent(studentNumber: Int): Student
}