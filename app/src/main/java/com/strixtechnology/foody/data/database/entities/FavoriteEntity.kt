package com.strixtechnology.foody.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.strixtechnology.foody.models.Result
import com.strixtechnology.foody.util.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoriteEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)