package com.strixtechnology.foody.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.strixtechnology.foody.data.database.entities.FavoriteEntity
import com.strixtechnology.foody.data.database.entities.RecipesEntity

@Database(
    entities = [RecipesEntity::class, FavoriteEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase: RoomDatabase() {

    abstract fun recipesDao(): RecipesDao

}
