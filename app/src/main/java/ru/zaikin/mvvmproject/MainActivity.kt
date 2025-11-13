package ru.zaikin.mvvmproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zaikin.mvvmproject.databinding.ActivityMainBinding
import ru.zaikin.mvvmproject.model.Genre
import ru.zaikin.mvvmproject.model.Movie
import ru.zaikin.mvvmproject.util.MovieAdapter
import ru.zaikin.mvvmproject.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var clickHandler: MainActivityClickHandlers
    private lateinit var selectedGenre: Genre
    private lateinit var genreList: ArrayList<Genre>
    private lateinit var movieList: ArrayList<Movie>
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private var selectedMovieId: Int = 0

    companion object {
        const val ADD_MOVIE_REQUEST_CODE: Int = 255
        const val EDIT_MOVIE_REQUEST_CODE: Int = 355
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        clickHandler = MainActivityClickHandlers()
        binding.clickHandler = clickHandler

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        viewModel.genres.observe(this, Observer { value ->
            genreList = ArrayList(value ?: emptyList())

            for (genre in genreList) {
                Log.d("WATCH_HERE", "Genre id=${genre.id}, name=${genre.genre}")
            }

            showInSpinner()
        })
    }

    private fun showInSpinner() {
        val adapter = ArrayAdapter(this, R.layout.spinner_item, genreList)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerAdapter = adapter
    }

    inner class MainActivityClickHandlers {
        fun onFabClicked(view: View) {
            val intent = Intent(this@MainActivity, AddEditActivity::class.java)
            startActivityForResult(intent, ADD_MOVIE_REQUEST_CODE)
        }

        fun onSelectedItem(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            selectedGenre = parent.getItemAtPosition(position) as Genre
            val msg = "id is ${selectedGenre.id}\nname is ${selectedGenre.genre}"
            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
            loadGenreMovieInArrayList(selectedGenre.id.toInt())
        }
    }

    private fun loadGenreMovieInArrayList(genreId: Int) {
        viewModel.getMoviesByGenre(genreId).observe(this, Observer { value ->
            movieList = ArrayList(value ?: emptyList())
            loadRecyclerView()
        })
    }

    private fun loadRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        movieAdapter = MovieAdapter()
        movieAdapter.setMovieList(movieList)
        recyclerView.adapter = movieAdapter

        movieAdapter.setOnItemClickListener(object : MovieAdapter.OnItemClickListener {
            override fun onItemClicked(movie: Movie) {
                selectedMovieId = movie.movieId
                val intent = Intent(this@MainActivity, AddEditActivity::class.java).apply {
                    putExtra(AddEditActivity.MOVIE_ID, selectedMovieId)
                    putExtra(AddEditActivity.MOVIE_NAME, movie.name)
                    putExtra(AddEditActivity.MOVIE_DESCRIPTION, movie.movieDescription)
                }
                startActivityForResult(intent, EDIT_MOVIE_REQUEST_CODE)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val selectedGenreId = selectedGenre.id

        if (requestCode == ADD_MOVIE_REQUEST_CODE && resultCode == RESULT_OK) {
            val movie = Movie().apply {
                genreId = selectedGenreId.toInt()
                name = data?.getStringExtra(AddEditActivity.MOVIE_NAME).orEmpty()
                movieDescription = data?.getStringExtra(AddEditActivity.MOVIE_DESCRIPTION).orEmpty()
            }
            viewModel.addNewMovie(movie)

            loadGenreMovieInArrayList(selectedGenreId.toInt())

        } else if (requestCode == EDIT_MOVIE_REQUEST_CODE && resultCode == RESULT_OK) {
            val movie = Movie().apply {
                movieId = selectedMovieId
                genreId = selectedGenreId.toInt()
                name = data?.getStringExtra(AddEditActivity.MOVIE_NAME).orEmpty()
                movieDescription = data?.getStringExtra(AddEditActivity.MOVIE_DESCRIPTION).orEmpty()
            }
            viewModel.updateMovie(movie)
        }
    }
}
