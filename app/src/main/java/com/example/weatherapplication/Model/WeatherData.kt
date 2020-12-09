package com.example.weatherapplication

data class WeatherData(

    val main: Main
)

data class Main(
    val temp: Double
)