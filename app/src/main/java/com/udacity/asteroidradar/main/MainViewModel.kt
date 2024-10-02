package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class ApiStatus { LOADING, ERROR, DONE }
enum class SortType { WEEK, TODAY, SAVED }

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    private val _sortType = MutableLiveData<SortType>()

    val asteroidList: LiveData<List<Asteroid>> = _sortType.switchMap { sortType ->
        // Fetch the corresponding asteroid list from the data source
        when (sortType) {
            SortType.WEEK -> asteroidsRepository.asteroidsThisWeek
            SortType.TODAY -> asteroidsRepository.asteroidsToday
            SortType.SAVED -> asteroidsRepository.asteroidsSaved
        }
    }

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()

    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<ApiStatus>()
    // The external immutable LiveData for the request status String
    val status: LiveData<ApiStatus>
        get() = _status

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()

    val navigateToSelectedAsteroid: LiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    init {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                asteroidsRepository.refreshAsteroids()
                _pictureOfDay.value = asteroidsRepository.getPictureOfDay()
                _status.value = ApiStatus.DONE
                _sortType.value = SortType.WEEK
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() { _navigateToSelectedAsteroid.value = null }

    fun doneStatus() {
        _status.value = ApiStatus.DONE
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.clear()
        }
    }

    /**
     * Executes when the View week asteroids menu item is clicked.
     */
    fun onSortByWeek() {
        viewModelScope.launch {
            _sortType.value = SortType.WEEK
        }
    }

    /**
     * Executes when the View today asteroids menu item is clicked.
     */
    fun onSortByToday() {
        viewModelScope.launch {
            _sortType.value = SortType.TODAY
        }
    }

    /**
     * Executes when the View saved asteroids menu item is clicked.
     */
    fun onSortBySaved() {
        viewModelScope.launch {
            _sortType.value = SortType.SAVED
        }
    }

    /**
     * Executes when the Delete asteroid menu item is clicked.
     */
    fun onClear() {
        viewModelScope.launch {
            // Clear the database table.
            clear()
        }
    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}