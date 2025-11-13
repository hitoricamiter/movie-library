package ru.zaikin.mvvmproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.zaikin.mvvmproject.model.Genre
import ru.zaikin.mvvmproject.model.Movie
import ru.zaikin.mvvmproject.repository.AppRepository

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository = AppRepository(application)

    val genres: LiveData<List<Genre>> = appRepository.getGenres()

    fun getMoviesByGenre(id: Int): LiveData<List<Movie>> =
        appRepository.getMoviesByGenre(id)

    fun addNewMovie(movie: Movie) = viewModelScope.launch {
        appRepository.insertMovie(movie)
    }

    fun updateMovie(movie: Movie) = viewModelScope.launch {
        appRepository.updateMovie(movie)
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch {
        appRepository.deleteMovie(movie)
    }
}
