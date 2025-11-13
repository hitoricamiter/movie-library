package ru.zaikin.mvvmproject.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.zaikin.mvvmproject.dao.GenreDao
import ru.zaikin.mvvmproject.dao.MovieDao
import ru.zaikin.mvvmproject.model.Genre
import ru.zaikin.mvvmproject.model.Movie

@Database(entities = [Movie::class, Genre::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao
    abstract fun getGenreDao(): GenreDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(MovieDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class MovieDatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.getGenreDao(), database.getMovieDao())
                } ?: run {
                    println("Base isn't initialised for some reason")
                }
            }
        }

        private suspend fun populateDatabase(genreDao: GenreDao, movieDao: MovieDao) {
            val genres = listOf(
                Genre(genre = "Драма"),
                Genre(genre = "Фантастика"),
                Genre(genre = "Комедия"),
                Genre(genre = "Ужасы")
            )
            genres.forEach { genreDao.insert(it) }

            val movies = listOf(
                Movie(name = "Интерстеллар", movieDescription = "Про космос", genreId = 2),
                Movie(name = "Начало", movieDescription = "Про сны", genreId = 2),
                Movie(name = "Шрэк", movieDescription = "Весёлый мультфильм", genreId = 3),
                Movie(name = "Сияние", movieDescription = "Классический хоррор", genreId = 4)
            )
            movies.forEach { movieDao.insert(it) }
        }
    }
}
