package ru.zaikin.mvvmproject.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.zaikin.mvvmproject.model.Genre

@Dao
interface GenreDao {
    @Insert
    suspend fun insert(genre: Genre)
    @Update
    suspend  fun update(genre: Genre)
    @Delete
    suspend fun delete(genre: Genre)
    @Query("SELECT * FROM genres_table")
    fun getAllGenres() : Flow<List<Genre>>
}