package com.example.finamobileapp.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class CategoryDetailViewModelFactory(
    private val application: Application,
    private val categoryName: String
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {


        if (modelClass.isAssignableFrom(CategoryDetailViewModel::class.java)) {


            @Suppress("UNCHECKED_CAST")
            return CategoryDetailViewModel(application, categoryName) as T
        }


        throw IllegalArgumentException("Unknown ViewModel class")
    }
}