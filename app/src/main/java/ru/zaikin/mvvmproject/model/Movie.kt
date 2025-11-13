package ru.zaikin.mvvmproject.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.zaikin.mvvmproject.BR

@Entity(
    tableName = "movies_table",
    foreignKeys = [
        ForeignKey(
            entity = Genre::class,
            parentColumns = ["id"],
            childColumns = ["genre_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class Movie(
    movieId: Int = 0,
    name: String = "",
    movieDescription: String = "",
    genreId: Int = 0
) : BaseObservable() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "movie_id")
    @get:Bindable
    var movieId: Int = movieId
        set(value) {
            field = value
            notifyPropertyChanged(BR.movieId)
        }

    @ColumnInfo(name = "name")
    @get:Bindable
    var name: String = name
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @ColumnInfo(name = "movie_description")
    @get:Bindable
    var movieDescription: String = movieDescription
        set(value) {
            field = value
            notifyPropertyChanged(BR.movieDescription)
        }

    @ColumnInfo(name = "genre_id")
    @get:Bindable
    var genreId: Int = genreId
        set(value) {
            field = value
            notifyPropertyChanged(BR.genreId)
        }
}
