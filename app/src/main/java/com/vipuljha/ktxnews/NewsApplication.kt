package com.vipuljha.ktxnews

import android.app.Application
import com.google.android.material.color.DynamicColors

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Apply dynamic color
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}