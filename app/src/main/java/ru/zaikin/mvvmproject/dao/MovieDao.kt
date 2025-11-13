package ru.zaikin.mvvmproject.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.zaikin.mvvmproject.model.Movie

@Dao
interface MovieDao {
    @Insert
    suspend fun insert(movie: Movie)
    @Update
    suspend fun update(movie: Movie)
    @Delete
    suspend fun delete(movie: Movie)
    @Query("SELECT * FROM movies_table")
    fun getAllMovies() : Flow<List<Movie>>
    @Query("SELECT * FROM movies_table where genre_id=:genreId")
    fun getMoviesByGenre(genreId: Int) : Flow<List<Movie>>
}