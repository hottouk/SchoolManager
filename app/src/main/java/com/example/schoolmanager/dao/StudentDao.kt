package com.example.schoolmanager.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.schoolmanager.model.ItemStudent

@Dao
interface StudentDao {

    @Insert
    fun insertStudent(student: ItemStudent)

    @Query("SELECT * From ItemStudent")
    fun getAll(): List<ItemStudent>

    @Query("SELECT * From ItemStudent WHERE studentNumber ==:studentNumber ")
    fun getOneStudent(studentNumber: Int): ItemStudent
}