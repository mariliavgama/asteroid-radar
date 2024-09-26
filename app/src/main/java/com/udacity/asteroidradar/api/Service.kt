package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * A retrofit service to fetch an asteroid list.
 */
interface AsteroidService {
    @GET("neo/rest/v1/feed")
    fun getAsteroidList(@Query("start_date") startDate: String, @Query("end_date") endDate: String,
                        @Query("api_key") apiKey: String): Deferred<String>
}

/**
 * Main entry point for network access. Call like `Network.asteroidradar.getAsteroidList()`
 */
object Network {
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val asteroidradar = retrofit.create(AsteroidService::class.java)
}