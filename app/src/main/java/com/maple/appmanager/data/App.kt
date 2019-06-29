package com.maple.appmanager.data

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.maple.appmanager.ManagerApp
import com.maple.appmanager.R


data class App(
        val logo: Drawable?,
        val appName: String,
        val appVersion: String,
        val packageName: String
) {

    fun showLogo(): Drawable {
        val defLogo = ContextCompat.getDrawable(ManagerApp.getApp(), R.drawable.ic_launcher)!!
        return logo ?: defLogo
    }

}
