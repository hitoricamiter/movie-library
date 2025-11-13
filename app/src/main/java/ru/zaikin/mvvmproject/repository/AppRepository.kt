package ru.zaikin.mvvmproject.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import ru.zaikin.mvvmproject.dao.GenreDao
import ru.zaikin.mvvmproject.dao.MovieDao
import ru.zaikin.mvvmproject.model.Genre
import ru.zaikin.mvvmproject.model.Movie

class AppRepository(application: Application) {

    private val genreDao: GenreDao
    private val movieDao: MovieDao

    init {
        val database = MovieDatabase.getInstance(application)
        genreDao = database.getGenreDao()
        movieDao = database.getMovieDao()
    }

    fun getGenres(): LiveData<List<Genre>> = genreDao.getAllGenres().asLiveData()

    fun getMoviesByGenre(id: Int): LiveData<List<Movie>> =
        movieDao.getMoviesByGenre(id).asLiveData()

    suspend fun insertGenre(genre: Genre) = genreDao.insert(genre)
    suspend fun insertMovie(movie: Movie) = movieDao.insert(movie)
    suspend fun updateGenre(genre: Genre) = genreDao.update(genre)
    suspend fun updateMovie(movie: Movie) = movieDao.update(movie)
    suspend fun deleteGenre(genre: Genre) = genreDao.delete(genre)
    suspend fun deleteMovie(movie: Movie) = movieDao.delete(movie)
}
