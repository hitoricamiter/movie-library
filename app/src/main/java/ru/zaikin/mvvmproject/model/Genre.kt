package ru.zaikin.mvvmproject.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.zaikin.mvvmproject.BR

@Entity(tableName = "genres_table")
class Genre(
    id: Long = 0L,
    genre: String = ""
) : BaseObservable() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @get:Bindable
    var id: Long = id
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    @ColumnInfo(name = "genre")
    @get:Bindable
    var genre: String = genre
        set(value) {
            field = value
            notifyPropertyChanged(BR.genre)
        }

    override fun toString(): String {
        return genre
    }


}
