package com.example.schoolmanager

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.schoolmanager.dao.StudentDao
import com.example.schoolmanager.model.ItemActivity
import com.example.schoolmanager.model.Student

@Database(entities = [Student::class, ItemActivity::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun studentDao(): StudentDao
}