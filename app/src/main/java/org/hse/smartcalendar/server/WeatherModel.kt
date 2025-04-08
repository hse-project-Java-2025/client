package org.hse.smartcalendar.server;

data class WeatherModel (
    val location: Location,
    val current: Current
)

data class Current (
    val lastUpdatedEpoch: String,
    val lastUpdated: String,
    val tempC: String,
    val tempF: String,
    val isDay: String,
    val condition: Condition,
    val windMph: String,
    val windKph: String,
    val windDegree: String,
    val windDir: String,
    val pressureMB: String,
    val pressureIn: String,
    val precipMm: String,
    val precipIn: String,
    val humidity: String,
    val cloud: String,
    val feelslikeC: String,
    val feelslikeF: String,
    val windchillC: String,
    val windchillF: String,
    val heatindexC: String,
    val heatindexF: String,
    val dewpointC: String,
    val dewpointF: String,
    val visKM: String,
    val visMiles: String,
    val uv: String,
    val gustMph: String,
    val gustKph: String
)

data class Condition (
    val text: String,
    val icon: String,
    val code: String
)

data class Location (
    val name: String,
    val region: String,
    val country: String,
    val lat: String,
    val lon: String,
    val tzID: String,
    val localtimeEpoch: String,
    val localtime: String
)
