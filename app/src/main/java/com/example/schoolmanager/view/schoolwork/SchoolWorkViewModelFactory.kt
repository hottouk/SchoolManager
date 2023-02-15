package com.example.schoolmanager.view.schoolwork

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.schoolmanager.model.network.Teacher

class SchoolWorkViewModelFactory(private val user:Teacher) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SchoolWorkViewModel::class.java)) {
            return SchoolWorkViewModel(user) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}