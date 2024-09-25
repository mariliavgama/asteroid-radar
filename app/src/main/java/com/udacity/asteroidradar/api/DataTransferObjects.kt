package com.udacity.asteroidradar.api

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabaseAsteroid
import org.json.JSONObject

@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val asteroids: JSONObject)

/**
 * Videos represent a devbyte that can be played.
 */
/*@JsonClass(generateAdapter = true)
data class NetworkAsteroid(
    val id: String,
    val name: String,
    val close_approach_data: String,
    val updated: String,
    val thumbnail: String,
    val closedCaptions: String?)*/

/*
val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
 */

/**
 * Convert Network results to database objects
 */
/*fun NetworkVideoContainer.asDomainModel(): List<Video> {
    return videos.map {
        Video(
            title = it.title,
            description = it.description,
            url = it.url,
            updated = it.updated,
            thumbnail = it.thumbnail)
    }
}*/

fun NetworkAsteroidContainer.asDatabaseModel(): Array<DatabaseAsteroid> {
    return parseAsteroidsJsonResult(asteroids).map {
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous)
    }.toTypedArray()
    /*asteroids.map {
        DatabaseAsteroid(
            title = it.title,
            description = it.description,
            url = it.url,
            updated = it.updated,
            thumbnail = it.thumbnail)
    }.toTypedArray()*/
}