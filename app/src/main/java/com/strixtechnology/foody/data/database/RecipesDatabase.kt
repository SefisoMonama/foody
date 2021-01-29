package com.strixtechnology.foody.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.strixtechnology.foody.data.database.RecipesEntity
import com.strixtechnology.foody.data.database.RecipesTypeConverter
import com.strixtechnology.foody.data.database.RecipesDao

@Database(
    entities = [RecipesEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase: RoomDatabase() {

    abstract fun recipesDao(): RecipesDao

}
