package com.openclassrooms.realestatemanager.di

import android.app.Application
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RealEstateViewModelFactory(
    private val application: Application,
    private val lifeCycleScope:LifecycleCoroutineScope
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Application::class.java,LifecycleCoroutineScope::class.java).newInstance(application,lifeCycleScope)
    }
}