package com.example.schoolmanager.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.schoolmanager.db.dao.StudentDao
import com.example.schoolmanager.db.entity.StudentEntity
import com.example.schoolmanager.util.KeyValue

@Database(entities = [StudentEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase(){
    abstract fun studentDao(): StudentDao

    companion object{
        @Volatile
        private var INSTANCE : AppDatabase? = null

        @Synchronized
        fun getInstance(context : Context) : AppDatabase {
            var instance = INSTANCE
            if(instance == null){
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    KeyValue.LOCAL_DB_STUDENT
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }
}