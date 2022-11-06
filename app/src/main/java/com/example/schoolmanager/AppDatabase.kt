package com.example.schoolmanager

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.schoolmanager.Dao.StudentDao
import com.example.schoolmanager.model.ItemActivity
import com.example.schoolmanager.model.ItemStudent

@Database(entities = [ItemStudent::class, ItemActivity::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun studentDao(): StudentDao
}