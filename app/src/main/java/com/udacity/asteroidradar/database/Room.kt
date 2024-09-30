package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Dao
interface AsteroidDao {
    @Query("select * from databaseasteroid WHERE closeApproachDate >= :startDate ORDER BY closeApproachDate ASC") fun getAsteroidsByStartDate(startDate: String): LiveData<List<DatabaseAsteroid>>

    @Query("select * from databaseasteroid WHERE closeApproachDate = :targetDate ORDER BY closeApproachDate ASC") fun getAsteroidsByTargetDate(targetDate: String): LiveData<List<DatabaseAsteroid>>

    @Query("select * from databaseasteroid ORDER BY closeApproachDate ASC") fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("DELETE FROM databaseasteroid")
    suspend fun clear()
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids").build()
        }
    }
    return INSTANCE
}