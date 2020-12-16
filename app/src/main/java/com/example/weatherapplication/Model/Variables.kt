package com.example.weatherapplication.Model

import android.util.Log
import kotlin.properties.Delegates

object Variables {

    var isNetworkConnected: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
        Log.i("Network connectivity", "$newValue")
    }
}