package com.example.weatherapplication

import android.app.Application

class Callback : Application(){
    override fun onCreate() {
        super.onCreate()
        //Start network callback
        NetworkMonitor(this).startNetworkCallback()
    }

    override fun onTerminate(){
        super.onTerminate()
        //Stop network callback
        NetworkMonitor(this).stopNetworkCallback()
    }
}