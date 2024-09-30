package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.DEFAULT_END_DATE_DAYS
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.NetworkAsteroidContainer
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    var asteroids: LiveData<List<Asteroid>> = database.asteroidDao.getAsteroidsByStartDate(getTodayDate()).map {
        it.asDomainModel()
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroidList = Network.asteroidradar.getAsteroidList(getTodayDate(), getWeekAheadDate(), API_KEY).await()
            database.asteroidDao.insertAll(*NetworkAsteroidContainer(JSONObject(asteroidList)).asDatabaseModel())
        }
    }

    suspend fun getPictureOfDay(): PictureOfDay {
        return Network.asteroidradar.getPictureOfDay(API_KEY).await()
    }

    fun viewWeekAsteroids() {
        asteroids = database.asteroidDao.getAsteroidsByStartDate(getTodayDate()).map {
            it.asDomainModel()
        }
    }

    fun viewTodayAsteroids() {
        asteroids = database.asteroidDao.getAsteroidsByTargetDate(getTodayDate()).map {
            it.asDomainModel()
        }
    }

    fun viewSavedAsteroids() {
        asteroids = database.asteroidDao.getAsteroids().map {
            it.asDomainModel()
        }
    }

    private fun getTodayDate(): String {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    private fun getWeekAheadDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, DEFAULT_END_DATE_DAYS)
        val yesterdayTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(yesterdayTime)
    }
}