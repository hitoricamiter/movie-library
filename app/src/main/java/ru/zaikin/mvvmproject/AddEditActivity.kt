package ru.zaikin.mvvmproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ru.zaikin.mvvmproject.databinding.ActivityAddEditBinding
import ru.zaikin.mvvmproject.model.Movie

class AddEditActivity : AppCompatActivity() {

    private lateinit var movie: Movie
    private lateinit var binding: ActivityAddEditBinding
    private lateinit var handler: AddEditActivityClickHandlers

    companion object {
        const val MOVIE_ID = "movieId"
        const val MOVIE_NAME = "movieName"
        const val MOVIE_DESCRIPTION = "movieDescription"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit)
        movie = Movie()
        binding.movie = movie

        handler = AddEditActivityClickHandlers(this)
        binding.clickHandlers = handler

        val intent = intent
        if (intent.hasExtra(MOVIE_ID)) {
            setTitle("Edit movie")
            movie.name = intent.getStringExtra(MOVIE_NAME).orEmpty()
            movie.movieDescription = intent.getStringExtra(MOVIE_DESCRIPTION).orEmpty()
        } else {
            setTitle("Add movie")
        }
    }

    inner class AddEditActivityClickHandlers(private val context: Context) {
        fun onOkButtonClicked(view: View) {
            if (movie.name.isBlank()) {
                Toast.makeText(context, "Please input the name", Toast.LENGTH_SHORT).show()
            } else {
                val resultIntent = Intent().apply {
                    putExtra(MOVIE_NAME, movie.name)
                    putExtra(MOVIE_DESCRIPTION, movie.movieDescription)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}
