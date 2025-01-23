package com.example.uaspam

import android.app.Application
import com.example.uaspam.repository.AppContainer
import com.example.uaspam.repository.AppsContainer

class RunApp : Application(){
    lateinit var appsContainer : AppContainer
    override fun onCreate() {
        super.onCreate()
        appsContainer = AppsContainer(
        )
    }
}